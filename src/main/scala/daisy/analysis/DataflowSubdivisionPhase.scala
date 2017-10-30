// Copyright 2017 MPI-SWS, Saarbruecken, Germany

package daisy
package analysis

import lang.Trees.{Expr, _}
import tools.Rational.{zero, abs, max}
import lang.Identifiers.Identifier
import lang.Types.RealType
import tools._
import Interval._
import lang.Constructors._
import solvers.{Solver, Z3Solver}
import tools.FinitePrecision._
import lang.TreeOps._
import smtlib.parser.Commands.{AttributeOption, SetOption}
import smtlib.parser.Terms.{Attribute, SKeyword, SNumeral, SSymbol}

import scala.collection.immutable.Map
import scala.collection.parallel.{ParSeq, ParSet}
import scala.util.control.Breaks._


/**
 * Compute relative errors through absolute, i.e. not through first computing
 * absolute errors.
 *
 * Uses the (1 + delta) abstraction for floating-point computations.
 *
 *
 * Prerequisites:
 * - SpecsProcessingPhase
 */
object DataflowSubdivisionPhase extends DaisyPhase with Subdivision with RoundoffEvaluators {
  override val name = "Forward Dataflow with Subdivision"
  override val shortName = "subdiv"
  override val description = "Forward dataflow with subdivision"
  override val definedOptions: Set[CmdLineOption[Any]] = Set(
    NumOption(
      "divLimit",
      3,
      "Max amount of interval divisions"),
    NumOption(
      "totalOpt",
      32,
      "Max total amount of analysis runs")
//    StringChoiceOption(
//      "subdiv",
//      Set("simple", "model"),
//      "simple",
//      "Method to subdivide intervals"),
  )

  implicit val debugSection = DebugSectionAnalysis

  override def runPhase(ctx: Context, prg: Program): (Context, Program) = {
    val rangeMethod = ctx.option[String]("rangeMethod")
    val errorMethod = ctx.option[String]("errorMethod")
    require(errorMethod == "affine", s"$name only supports error method 'affine'")

    val uniformPrecision = ctx.option[Precision]("precision")

    val trackInitialErrs = !ctx.hasFlag("noInitialErrors")
    val trackRoundoffErrs = !ctx.hasFlag("noRoundoff")

    val divLimit = ctx.option[Long]("divLimit").toInt
    //  val subdiv: String = ctx.option("subdiv")

    // for each function, returns (abs error, rel error, result interval)
    val res: Map[Identifier, (Rational, Option[Rational], Interval)] = functionsToConsider(ctx, prg).map(fnc => {

      ctx.reporter.info("analyzing fnc: " + fnc.id)
      val startTime = System.currentTimeMillis
      val inputValMap: Map[Identifier, Interval] = ctx.specInputRanges(fnc.id)

      val bodyReal = fnc.body.get

      //  Subdivide input ranges
      val subIntervals: Seq[Map[Identifier, Interval]] =
        getEqualSubintervals(inputValMap, divLimit)

      // Evaluate each input range
      val errors = subIntervals.par.map(subInt => {

        // If we track both input and roundoff errors, then we pre-compute
        // the roundoff errors for those variables that do not have a user-defined
        // error, in order to keep correlations.
        val inputErrorMap: Map[Identifier, Rational] = if (trackInitialErrs && trackRoundoffErrs){

          val inputErrs = ctx.specInputErrors(fnc.id)
          val allIDs = fnc.params.map(_.id).toSet
          val missingIDs = allIDs -- inputErrs.keySet
          inputErrs ++ missingIDs.map(id => (id -> uniformPrecision.absRoundoff(inputValMap(id))))

        } else if (trackInitialErrs) {

          val inputErrs = ctx.specInputErrors(fnc.id)
          val allIDs = fnc.params.map(_.id).toSet
          val missingIDs = allIDs -- inputErrs.keySet
          inputErrs ++ missingIDs.map(id => (id -> Rational.zero))

        } else if (trackRoundoffErrs) {

          val allIDs = fnc.params.map(_.id)
          allIDs.map(id => (id -> uniformPrecision.absRoundoff(inputValMap(id)))).toMap

        } else {

          val allIDs = fnc.params.map(_.id)
          allIDs.map(id => (id -> Rational.zero)).toMap

        }

        val (absError, realRange) = evalError(bodyReal, subInt, inputErrorMap,
          ctx.option[String]("rangeMethod"), ctx.option[String]("errorMethod"),
          ctx.option[Precision]("precision"))

        // var failIntervals: List[(Map[Identifier, Interval], Rational)] = List.empty

        val relError: Option[Rational] = if (realRange.includes(Rational.zero)) {
          // failIntervals = failIntervals :+ (x, absError)
          None
        } else {
          Some(absError / Interval.minAbs(realRange))
        }

        (absError, relError, realRange)
      }) // end subIntervals.map

      // Merge results
      val (absErrors, relErrors, ranges) = errors.toSeq.unzip3

      val totalAbsError = absErrors.tail.fold(absErrors.head)({
        case (x, y) => max(x, y)
        })
      val totalRelError = relErrors.tail.fold(relErrors.head)({
        case (None, _) | (_, None) => None
        case (Some(x), Some(y)) => Some(max(x, y))
        })

      val totalRange = ranges.tail.fold(ranges.head)({
        case (x, y) => x.union(y)
        })

        (fnc.id -> (totalAbsError, totalRelError, totalRange))
      // val relError = errors.max(optionAbsOrdering).getOrElse("NaN")

      // if (failIntervals.nonEmpty)
      //   ctx.reporter.info("For several sub-intervals it was not possible to compute relative error")
      // for(x <- failIntervals){
      //   val (m, er) = x
      //   ctx.reporter.info(s"absErr: $er on $m")
      // }
      // ctx.reporter.info(s"relError: $relError, time: " +
      //   (System.currentTimeMillis - startTime))
    }).toMap

    (ctx.copy(
      resultAbsoluteErrors = res.mapValues(_._1),
      resultRelativeErrors = res.mapValues(_._2),
      resultRealRanges = res.mapValues(_._3)),
    prg)
  }

  def evalError(expr: Expr, inputValMap: Map[Identifier, Interval],
    inputErrorMap: Map[Identifier, Rational], rangeMethod: String, errorMethod: String,
    precision: Precision): (Rational, Interval) = {

    (rangeMethod, errorMethod) match {
      case ("interval", "affine") =>
        uniformRoundoff_IA_AA(expr, inputValMap, inputErrorMap, precision,
          trackRoundoffErrors = true)

      case ("affine", "affine") =>
        uniformRoundoff_AA_AA(expr, inputValMap, inputErrorMap, precision,
          trackRoundoffErrors = true)

      case ("smt", "affine") =>
        uniformRoundoff_SMT_AA(expr, inputValMap, inputErrorMap, precision,
          trackRoundoffErrors = true)
    }
  }

}

