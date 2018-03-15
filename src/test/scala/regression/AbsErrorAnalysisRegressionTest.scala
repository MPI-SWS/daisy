package regression

import daisy._
import org.scalatest.FunSuite


/**
  Regression test for the basic absolute error and range computations.
*/
class AbsErrorAnalysisRegressionTest extends FunSuite {
  val fileName = "src/test/resources/AbsErrorRegressionFunctions.scala"

  val _ctx : Context = Main.processOptions(List(fileName, "--silent")).get
  Main.ctx = _ctx
  val (ctx, prg) = frontend.ExtractionPhase.run(_ctx, null)


  def run_tests(errorMethod: String, precision: String, results: List[(String, (String, String))]): Unit = {
    if (results.isEmpty) {
      ignore(s"$errorMethod $precision"){}
    }
    for ((fnc,(error, range)) <- results) {
      test(s"$errorMethod $precision: $fnc") {
        val ctx2 = Main.processOptions(errorMethod.split(' ').toList ++ List(fileName, "--silent",
          "--precision="+precision, "--functions="+fnc)).get
        var (res, _) = (analysis.SpecsProcessingPhase >> analysis.DataflowPhase).run(ctx2, prg)
        assert(res.resultAbsoluteErrors.values.head.toString === error)
        assert(res.resultRealRanges.values.head.toString === range)
      
      }
    }
  }


  run_tests("--analysis=dataflow --rangeMethod=interval --errorMethod=interval", "Float32", List(
    ("doppler", ("0.00023411420708645047", "[-158.7191444098274, -0.02944244059231351]")),
    ("sine", ("6.064885588214318e-07", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("7.790389913381476e-07", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("0.0001744032010009281", "[-402.125, 68.5]")),
    ("bspline0", ("8.69234485871102e-08", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("4.867712804686639e-07", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("4.569689551203739e-07", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("5.712112027822517e-08", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("0.00017261505240639963", "[-705.0, 705.0]")),
    ("rigidBody2", ("0.01960706761337861", "[-58740.0, 58740.0]")),
    ("verhulst", ("3.380285219892807e-07", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("1.348152108010569e-07", "[0.03727705922396188, 0.35710168263424846]")),
    ("carbonGas", ("26.469834078859897", "[2097409.2, 3.434323E7]")),
    ("turbine1", ("5.241255542561334e-05", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("7.760691760032766e-05", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("3.943285708968101e-05", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("6.840229145268494e-05", "[-35.7792, 159.8176]")),
    ("kepler1", ("0.00030414086568821415", "[-490.320768, 282.739712]")),
    ("kepler2", ("0.0015650665986970133", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("0.0012483597719210608", "[-1630.0, 3050.0]")),
    ("pendulum1", ("2.4749234266182054e-07", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("5.034483223432932e-07", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("8.940698954518462e-07", "[-1.0000000000000004, 1.0000000000000004]")),
    ("analysis2", ("3.373237164130552e-05", "[0.12722192275862546, 8.006978350980198]")),
    ("logExp", ("0.0017879966609438234", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("7.033348822460408e-06", "[-19.99999999999947, 19.99999999999947]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=interval --errorMethod=interval", "Float16", List(
    ("sine", ("0.00641708211922182", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("0.0077712651513449195", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("1.980473400260345", "[-402.125, 68.5]")),
    ("bspline0", ("0.0007338540174834098", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("0.005054562261157932", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("0.0048106617452622685", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("0.0004888776846409629", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("2.2369611023856506", "[-705.0, 705.0]")),
    ("rigidBody2", ("285.4035722621079", "[-58740.0, 58740.0]")),
    ("verhulst", ("0.0031822371179050105", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("0.0013229351465293318", "[0.03727705922396188, 0.35710168263424846]")),
    ("turbine1", ("0.6223904039914918", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("0.8913284420390682", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("0.43285759671217655", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("0.7758963792654312", "[-35.7792, 159.8176]")),
    ("kepler1", ("3.704598956039099", "[-490.320768, 282.739712]")),
    ("kepler2", ("17.95420862110512", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("14.368246037925685", "[-1630.0, 3050.0]")),
    ("pendulum1", ("0.002077231684819698", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("0.005150356162269661", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("0.008471295823035168", "[-1.0000000000000004, 1.0000000000000004]")),
    ("analysis2", ("0.4069289722083363", "[0.12722192275862546, 8.006978350980198]")),
    ("logExp", ("16.04806345447047", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("0.07200409541512987", "[-19.99999999999947, 19.99999999999947]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=interval --errorMethod=affine", "Fixed32", List(
    // when reenabling after implementing canRepresent, also keep in mind that caching will further improve on previous
    // test results
    ("doppler", ("3.515832518976976e-06", "[-158.7191444098274, -0.02944244059231351]")),
    ("sine", ("1.0790055174239156e-08", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("1.4966451423072368e-08", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("8.031725901479908e-06", "[-402.125, 68.5]")),
    ("bspline0", ("2.2765662990299188e-09", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("8.951044755235432e-09", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("8.019722179343003e-09", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("1.3452437200776313e-09", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("3.1162053353572805e-06", "[-705.0, 705.0]")),
    ("rigidBody2", ("0.00031348504140114863", "[-58740.0, 58740.0]")),
    ("verhulst", ("6.958629080279612e-09", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("3.6240424612772103e-09", "[0.03727705922396188, 0.35710168263424846]")),
    ("carbonGas", ("35.38938710461317", "[2097409.2, 3.434323E7]")),
    ("turbine1", ("1.0993864885838593e-06", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("1.3260776374959875e-06", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("8.084435699194726e-07", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("8.758157494390128e-07", "[-35.7792, 159.8176]")),
    ("kepler1", ("4.043614867231782e-06", "[-490.320768, 282.739712]")),
    ("kepler2", ("2.0751917379394948e-05", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("2.0392239117997257e-05", "[-1630.0, 3050.0]"))
  ))


  run_tests("--analysis=dataflow --rangeMethod=interval --errorMethod=affine", "Float32", List(
    ("doppler", ("0.00022501381411965077", "[-158.7191444098274, -0.02944244059231351]")),
    ("sine", ("6.064885588214318e-07", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("7.790389913381473e-07", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("0.00016796589915202306", "[-402.125, 68.5]")),
    ("bspline0", ("8.69234485871102e-08", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("4.2716663302873797e-07", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("3.973643085686263e-07", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("5.712112027822517e-08", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("0.00017261505240639963", "[-705.0, 705.0]")),
    ("rigidBody2", ("0.01960706761337861", "[-58740.0, 58740.0]")),
    ("verhulst", ("3.199529347654797e-07", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("1.329111840179917e-07", "[0.03727705922396188, 0.35710168263424846]")),
    ("carbonGas", ("22.99523348247323", "[2097409.2, 3.434323E7]")),
    ("turbine1", ("5.095029323813888e-05", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("7.46823932469574e-05", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("3.797059491586537e-05", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("5.605220905522401e-05", "[-35.7792, 159.8176]")),
    ("kepler1", ("0.0002587913631247376", "[-490.320768, 282.739712]")),
    ("kepler2", ("0.0013281227815583414", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("0.0012483597698746962", "[-1630.0, 3050.0]")),
    ("pendulum1", ("2.474338904728519e-07", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("5.033314179653559e-07", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("8.940698954518462e-07", "[-1.0000000000000004, 1.0000000000000004]")),
    ("analysis2", ("3.263652228162847e-05", "[0.12722192275862546, 8.006978350980198]")),
    ("logExp", ("0.0017879966609438234", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("7.033348822460408e-06", "[-19.99999999999947, 19.99999999999947]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=interval --errorMethod=affine", "Float64", List(
    ("doppler", ("4.1911988101104756e-13", "[-158.7191444098274, -0.02944244059231351]")),
    ("sine", ("1.1296729607621835e-15", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("1.4510731312549944e-15", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("3.1286084833936916e-13", "[-402.125, 68.5]")),
    ("bspline0", ("1.6190752442450204e-16", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("7.956598343146956e-16", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("7.401486830834377e-16", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("1.0639637319324417e-16", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("3.2152058793144533e-13", "[-705.0, 705.0]")),
    ("rigidBody2", ("3.652100843964945e-11", "[-58740.0, 58740.0]")),
    ("verhulst", ("5.959587110472846e-16", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("2.4756633989832674e-16", "[0.03727705922396188, 0.35710168263424846]")),
    ("carbonGas", ("4.2831938128927477e-08", "[2097409.2, 3.434323E7]")),
    ("turbine1", ("9.490226544267962e-14", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("1.3910672706969435e-13", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("7.072570725135768e-14", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("1.0440537323574973e-13", "[-35.7792, 159.8176]")),
    ("kepler1", ("4.820364551960666e-13", "[-490.320768, 282.739712]")),
    ("kepler2", ("2.4738213255659507e-12", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("2.3252511027749283e-12", "[-1630.0, 3050.0]")),
    ("pendulum1", ("4.608815354096435e-16", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("9.37527820572305e-16", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("1.665334536937736e-15", "[-1.0000000000000004, 1.0000000000000004]")),
    ("analysis2", ("6.078990162129583e-14", "[0.12722192275862546, 8.006978350980198]")),
    ("logExp", ("3.3304033073481607e-12", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("1.199040866595141e-14", "[-19.99999999999947, 19.99999999999947]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=affine --errorMethod=affine", "Fixed32", List(
    // when reenabling after implementing canRepresent, also keep in mind that caching will further improve on previous
    // test results
    ("doppler", ("1.0781470875901314e-05", "[-282.4762120545625, 133.53913086064713]")),
    ("sine", ("7.6124024417491e-09", "[-1.6540002686363373, 1.6540002686363373]")),
    ("sineOrder3", ("1.3073958221780822e-08", "[-1.909859317102744, 1.909859317102744]")),
    ("sqroot", ("8.009839820976444e-06", "[-365.875, 195.04296875]")),
    ("bspline0", ("2.2765662990299188e-09", "[-0.0625, 0.16666666666666666]")),
    ("bspline1", ("8.485383467855412e-09", "[-0.08333333333333333, 0.9791666666666666]")),
    ("bspline2", ("7.360035355440199e-09", "[-0.020833333333333332, 0.9166666666666666]")),
    ("bspline3", ("1.3452437200776313e-09", "[-0.16666666666666666, 0.0625]")),
    ("rigidBody1", ("3.1162053353572805e-06", "[-705.0, 705.0]")),
    ("rigidBody2", ("0.00031348504140114863", "[-57390.0, 58740.0]")),
    ("verhulst", ("6.492967792971873e-09", "[0.35217660784145, 0.9891620430819574]")),
    ("predatorPrey", ("3.663727010181625e-09", "[-0.0021029110350001693, 0.34533842311741914]")),
    ("carbonGas", ("35.362253285235276", "[-5799891.216, 3.159230584E7]")),
    ("turbine1", ("1.1088248886871038e-06", "[-55.55054339020381, 38.36885388122605]")),
    ("turbine2", ("1.3431257902981087e-06", "[-80.18793076923077, 63.665174692307694]")),
    ("turbine3", ("8.177376221931251e-07", "[-27.227529265841437, 38.719203390203816]")),
    ("kepler0", ("7.603317501866935e-07", "[3.0664, 102.8708]")),
    ("kepler1", ("3.685986998579438e-06", "[-288.15184, 89.927712]")),
    ("kepler2", ("1.847147347261089e-05", "[-337.61856, 850.310096]")),
    ("himmilbeau", ("8.843839186381541e-06", "[-462.5, 890.0]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=affine --errorMethod=affine", "Float32", List(
    ("doppler", ("0.0006900171033357558", "[-282.4762120545625, 133.53913086064713]")),
    ("sine", ("4.031187839420683e-07", "[-1.6540002686363373, 1.6540002686363373]")),
    ("sineOrder3", ("6.579194264554884e-07", "[-1.909859317102744, 1.909859317102744]")),
    ("sqroot", ("0.00016656518999980138", "[-365.875, 195.04296875]")),
    ("bspline0", ("8.69234485871102e-08", "[-0.0625, 0.16666666666666666]")),
    ("bspline1", ("4.048148912379665e-07", "[-0.08333333333333333, 0.9791666666666666]")),
    ("bspline2", ("3.6507845931528976e-07", "[-0.020833333333333332, 0.9166666666666666]")),
    ("bspline3", ("5.712112027822517e-08", "[-0.16666666666666666, 0.0625]")),
    ("rigidBody1", ("0.00017261505240639963", "[-705.0, 705.0]")),
    ("rigidBody2", ("0.01960706761337861", "[-57390.0, 58740.0]")),
    ("verhulst", ("2.901506123777844e-07", "[0.35217660784145, 0.9891620430819574]")),
    ("predatorPrey", ("1.3429757061219672e-07", "[-0.0021029110350001693, 0.34533842311741914]")),
    ("carbonGas", ("21.25866904228768", "[-5799891.216, 3.159230584E7]")),
    ("turbine1", ("5.1545112652219064e-05", "[-55.55054339020381, 38.36885388122605]")),
    ("turbine2", ("7.577347502729098e-05", "[-80.18793076923077, 63.665174692307694]")),
    ("turbine3", ("3.8565414329523206e-05", "[-27.227529265841437, 38.719203390203816]")),
    ("kepler0", ("4.866123310307557e-05", "[3.0664, 102.8708]")),
    ("kepler1", ("0.00023590317953098758", "[-288.15184, 89.927712]")),
    ("kepler2", ("0.0011821743715241617", "[-337.61856, 850.310096]")),
    ("himmilbeau", ("0.0005369187050519044", "[-462.5, 890.0]")),
    ("pendulum1", ("2.474338904728519e-07", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("5.033314179653559e-07", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("3.6319765361649225e-06", "[-3.538965513764188, 4.586419093867825]")),
    //("analysis2", ("3.373237164130552e-05", "0")), //div by zero
    ("logExp", ("0.0017879966609438234", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("1.2445237529550655e-05", "[-31.415920000000536, 31.415920000000536]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=affine --errorMethod=affine", "Float64", List(
    ("doppler", ("1.2852513956990104e-12", "[-282.4762120545625, 133.53913086064713]")),
    ("sine", ("7.508672360829453e-16", "[-1.6540002686363373, 1.6540002686363373]")),
    ("sineOrder3", ("1.2254703612493457e-15", "[-1.909859317102744, 1.909859317102744]")),
    ("sqroot", ("3.1025182423150005e-13", "[-365.875, 195.04296875]")),
    ("bspline0", ("1.6190752442450204e-16", "[-0.0625, 0.16666666666666666]")),
    ("bspline1", ("7.540264708912522e-16", "[-0.08333333333333333, 0.9791666666666666]")),
    ("bspline2", ("6.800116025829084e-16", "[-0.020833333333333332, 0.9166666666666666]")),
    ("bspline3", ("1.0639637319324417e-16", "[-0.16666666666666666, 0.0625]")),
    ("rigidBody1", ("3.2152058793144533e-13", "[-705.0, 705.0]")),
    ("rigidBody2", ("3.652100843964945e-11", "[-57390.0, 58740.0]")),
    ("verhulst", ("5.404475598160268e-16", "[0.35217660784145, 0.9891620430819574]")),
    ("predatorPrey", ("2.5014868553833655e-16", "[-0.0021029110350001693, 0.34533842311741914]")),
    ("carbonGas", ("3.9597334798088896e-08", "[-5799891.216, 3.159230584E7]")),
    ("turbine1", ("9.601020280849505e-14", "[-55.55054339020381, 38.36885388122605]")),
    ("turbine2", ("1.4113902525335888e-13", "[-80.18793076923077, 63.665174692307694]")),
    ("turbine3", ("7.183364461717313e-14", "[-27.227529265841437, 38.719203390203816]")),
    ("kepler0", ("9.063860773039779e-14", "[3.0664, 102.8708]")),
    ("kepler1", ("4.394038910504606e-13", "[-288.15184, 89.927712]")),
    ("kepler2", ("2.201971227577815e-12", "[-337.61856, 850.310096]")),
    ("himmilbeau", ("1.0000889005823412e-12", "[-462.5, 890.0]")),
    ("pendulum1", ("4.608815354096435e-16", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("9.37527820572305e-16", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("6.765081950078297e-15", "[-3.538965513764188, 4.586419093867825]")),
    //("analysis2", ("3.373237164130552e-05", "0")),  //div by zero
    ("logExp", ("3.3304033073481607e-12", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("2.3181058850241532e-14", "[-31.415920000000536, 31.415920000000536]"))
  ))

  run_tests("--analysis=dataflow --rangeMethod=interval --errorMethod=affine", "Quad", List(
    ("doppler", ("5.466102194446132e-31", "[-158.7191444098274, -0.02944244059231351]")),
    ("sine", ("1.334066489932821e-33", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("1.5307127574169135e-33", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("4.222360661825966e-31", "[-402.125, 68.5]")),
    ("bspline0", ("2.0864241064195054e-34", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("1.2197556314452493e-33", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("1.1234591342258875e-33", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("1.1234591342258877e-34", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("4.83889898527293e-31", "[-705.0, 705.0]")),
    ("rigidBody2", ("5.694300770072521e-29", "[-58740.0, 58740.0]")),
    ("verhulst", ("6.885992101414198e-34", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("2.907893430255591e-34", "[0.03727705922396188, 0.35710168263424846]")),
    ("carbonGas", ("4.3494082821706447e-26", "[2097409.2, 3.434323E7]")),
    ("turbine1", ("1.3449662356392724e-31", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("1.8662390696736128e-31", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("9.343730156425649e-32", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("1.210323710530937e-31", "[-35.7792, 159.8176]")),
    ("kepler1", ("6.118442035489584e-31", "[-490.320768, 282.739712]")),
    ("kepler2", ("2.9204223589555936e-30", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("2.964006184411956e-30", "[-1630.0, 3050.0]")),
    ("pendulum1", ("4.094489823911789e-34", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("1.015140091149826e-33", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("1.6643102058726482e-33", "[-1.0000000000000004, 1.0000000000000004]")),
    ("analysis2", ("7.90796547575446e-32", "[0.12722192275862546, 8.006978350980198]")),
    ("logExp", ("3.158288368327019e-30", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("1.416751492724838e-32", "[-19.99999999999947, 19.99999999999947]"))
  ))

  run_tests("--analysis=dataflow --subdiv --rangeMethod=affine --errorMethod=affine", "Float32", List(
    ("doppler", ("0.0006900171033357558", "[-282.4762120545625, 133.53913086064713]")),
    ("sine", ("4.031187839420683e-07", "[-1.6540002686363373, 1.6540002686363373]")),
    ("sineOrder3", ("6.579194264554884e-07", "[-1.909859317102744, 1.909859317102744]")),
    ("sqroot", ("0.00016656518999980138", "[-365.875, 195.04296875]")),
    ("bspline0", ("8.69234485871102e-08", "[-0.0625, 0.16666666666666666]")),
    ("bspline1", ("4.048148912379665e-07", "[-0.08333333333333333, 0.9791666666666666]")),
    ("bspline2", ("3.6507845931528976e-07", "[-0.020833333333333332, 0.9166666666666666]")),
    ("bspline3", ("5.712112027822517e-08", "[-0.16666666666666666, 0.0625]")),
    ("rigidBody1", ("0.00017261505240639963", "[-705.0, 705.0]")),
    ("rigidBody2", ("0.01960706761337861", "[-57390.0, 58740.0]")),
    ("verhulst", ("2.901506123777844e-07", "[0.35217660784145, 0.9891620430819574]")),
    ("predatorPrey", ("1.3429757061219672e-07", "[-0.0021029110350001693, 0.34533842311741914]")),
    ("carbonGas", ("21.25866904228768", "[-5799891.216, 3.159230584E7]")),
    ("turbine1", ("5.1545112652219064e-05", "[-55.55054339020381, 38.36885388122605]")),
    ("turbine2", ("7.577347502729098e-05", "[-80.18793076923077, 63.665174692307694]")),
    ("turbine3", ("3.8565414329523206e-05", "[-27.227529265841437, 38.719203390203816]")),
    ("kepler0", ("4.866123310307557e-05", "[3.0664, 102.8708]")),
    ("kepler1", ("0.00023590317953098758", "[-288.15184, 89.927712]")),
    ("kepler2", ("0.0011821743715241617", "[-337.61856, 850.310096]")),
    ("himmilbeau", ("0.0005369187050519044", "[-462.5, 890.0]")),
    ("pendulum1", ("2.474338904728519e-07", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("5.033314179653559e-07", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("3.6319765361649225e-06", "[-3.538965513764188, 4.586419093867825]")),
    //("analysis2", ("3.373237164130552e-05", "0")),
    ("logExp", ("0.0017879966609438234", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("1.2445237529550655e-05", "[-31.415920000000536, 31.415920000000536]"))
  ))

  run_tests("--analysis=opt --rangeMethod=interval", "Float64", List(
    ("doppler", ("4.1911988101104756e-13", "[-158.7191444098274, -0.02944244059231351]")),
    ("sine", ("1.1296729607621835e-15", "[-2.3011348046703466, 2.3011348046703466]")),
    ("sineOrder3", ("1.4510731312549944e-15", "[-2.9419084189651277, 2.9419084189651277]")),
    ("sqroot", ("3.1286084833936916e-13", "[-402.125, 68.5]")),
    ("bspline0", ("1.6190752442450204e-16", "[0.0, 0.16666666666666666]")),
    ("bspline1", ("7.956598343146956e-16", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline2", ("7.401486830834377e-16", "[-0.3333333333333333, 1.1666666666666667]")),
    ("bspline3", ("1.0639637319324417e-16", "[-0.16666666666666666, 0.0]")),
    ("rigidBody1", ("3.2152058793144533e-13", "[-705.0, 705.0]")),
    ("rigidBody2", ("3.652100843964945e-11", "[-58740.0, 58740.0]")),
    ("verhulst", ("5.959587110472846e-16", "[0.3148936170212766, 1.1008264462809918]")),
    ("predatorPrey", ("2.4756633989832674e-16", "[0.03727705922396188, 0.35710168263424846]")),
    ("carbonGas", ("4.2831938128927477e-08", "[2097409.2, 3.434323E7]")),
    ("turbine1", ("9.490226544267962e-14", "[-58.32912689020381, -1.5505285721480735]")),
    ("turbine2", ("1.3910672706969435e-13", "[-29.43698909090909, 80.993]")),
    ("turbine3", ("7.072570725135768e-14", "[0.4660958448753463, 40.375126890203816]")),
    ("kepler0", ("1.0440537323574973e-13", "[-35.7792, 159.8176]")),
    ("kepler1", ("4.820364551960666e-13", "[-490.320768, 282.739712]")),
    ("kepler2", ("2.4738213255659507e-12", "[-871.597824, 1860.323072]")),
    ("himmilbeau", ("2.3252511027749283e-12", "[-1630.0, 3050.0]")),
    ("pendulum1", ("4.608815354096435e-16", "[-2.05024516625, 2.05024516625]")),
    ("pendulum2", ("9.37527820572305e-16", "[-5.04903325, 5.04903325]")),
    ("analysis1", ("1.665334536937736e-15", "[-1.0000000000000004, 1.0000000000000004]")),
    ("analysis2", ("6.078990162129583e-14", "[0.12722192275862546, 8.006978350980198]")),
    ("logExp", ("3.3304033073481607e-12", "[3.354063728956623E-4, 8.000335406372898]")),
    ("sphere", ("1.199040866595141e-14", "[-19.99999999999947, 19.99999999999947]"))
  ))


  val _jetCtx : Context = Main.processOptions(List("src/test/resources/AbsErrorRegressionJetEngine.scala", "--silent")).get
  val (jctx, jprg) = (frontend.ExtractionPhase >> analysis.SpecsProcessingPhase).run(_jetCtx, null)

  test("IA-AA Default: JetEngine (fails)") {
    // with intervals this should crash
    intercept[daisy.tools.DivisionByZeroException] {
      analysis.DataflowPhase.run(jctx.copy(options = jctx.options ++ List("rangeMethod" -> "interval")), jprg)
    }
  }

  test("AA-AA Default: JetEngine") {
    val (res, _) = analysis.DataflowPhase.run(jctx.copy(options = jctx.options ++ List("rangeMethod" -> "affine")), jprg)
    assert(res.resultAbsoluteErrors.head._2.toString === "4.033866794164623e-08")
    assert(res.resultRealRanges.head._2.toString === "[-2185684.5850342796, 2199860.8357988168]")
  }

  // ignore("SMT-AA Default: JetEngine") {
  //   val (res, _) = analysis.DataflowPhase(jctx.copyWith("--rangeMethod=smt")).run(jctx,jprg)
  //   assert(res.resultAbsoluteErrors.head._2.toString === "1.1589127503449215e-08")
  //   assert(res.resultRealRanges.head._2.toString === "[-1781.3056274865728, 4820.245415085443]")
  // }
}