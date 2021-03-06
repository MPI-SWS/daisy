import daisy.lang._
import Real._

object Traincar2 {

  // y1, y2, y3:<1, 30, 24>    s.:<1, 30, 25>
  def out1(s0: Real, s1: Real, s2: Real, s3: Real, s4: Real) = {
    require(-2 <= s0 && s0 <= 9 && -2 <= s1 && s1 <= 9 && 0 <= s2 && s2 <= 10 && 0 <= s3 && s3 <= 10 && 0 <= s4 && s4 <= 10)
    (-2.9829300077345002E+00) * s0 + 2.8399180104656203E+01 * s1 + (-1.5201325206209998E+02) * s2 +
     3.5623840930436779E+02 * s3 + (-2.0636383424191501E+02) * s4 + 2.1660394157545902E+01
  }


  def state1(s0: Real, s1: Real, s2: Real, s3: Real, s4: Real, y0: Real, y1: Real, y2: Real) = {
    require(-2 <= s0 && s0 <= 9 && -2 <= s1 && s1 <= 9 && 0 <= s2 && s2 <= 10 && 0 <= s3 && s3 <= 10 && 0 <= s4 && s4 <= 10 &&
      0 <= y0 && y0 <= 10 && 0 <= y1 && y1 <= 10 && 0 <= y2 && y2 <= 10)
    (9.9999998913656896E-01)*s0+ (1.6994651E-08)*s1+ (1.7498543776970001E-03)*s2+ (-1.4024571647840000E-03)*s3+ (-6.5504777638499998E-04)*s4 +
     (-1.6499310260960000E-03)*y0 + (1.3026354031020000E-03)*y1+ (6.5494511318299996E-04)*y2+ (4.95505E-10)*2.1660394157545902E+01
  }

  def state2(s0: Real, s1: Real, s2: Real, s3: Real, s4: Real, y0: Real, y1: Real, y2: Real) = {
    require(-2 <= s0 && s0 <= 9 && -2 <= s1 && s1 <= 9 && 0 <= s2 && s2 <= 10 && 0 <= s3 && s3 <= 10 && 0 <= s4 && s4 <= 10 &&
      0 <= y0 && y0 <= 10 && 0 <= y1 && y1 <= 10 && 0 <= y2 && y2 <= 10)
    (2.094468E-09)*s0 +(9.9999999447142096E-01)*s1+ (5.8521627784900005E-04)*s2+ (9.9584190606899991E-04)*s3+ (-1.6218071590199999E-03)*s4 +
    (-5.8524111148300004E-04)*y0 +(-8.9578466384499997E-04)*y1+ (1.5217743914859999E-03)*y2+ (1.65715E-10)*2.1660394157545902E+01

  }

  def state3(s0: Real, s1: Real, s2: Real, s3: Real, s4: Real, y0: Real, y1: Real, y2: Real) = {
    require(-2 <= s0 && s0 <= 9 && -2 <= s1 && s1 <= 9 && 0 <= s2 && s2 <= 10 && 0 <= s3 && s3 <= 10 && 0 <= s4 && s4 <= 10 &&
      0 <= y0 && y0 <= 10 && 0 <= y1 && y1 <= 10 && 0 <= y2 && y2 <= 10)
    (-6.146088015E-06)*s0 +(1.6077937611999999E-05)*s1+ (9.9960914235631004E-01)*s2+ (2.4348362886800001E-04)*s3+ (-7.6912511450999999E-05)*s4 +
    (3.0415154725900001E-04)*y0 + (-4.1163170637999999E-05)*y1+ (-3.9928229421000001E-05)*y2+ (5.66185021E-07)*2.1660394157545902E+01

  }

  def state4(s0: Real, s1: Real, s2: Real, s3: Real, s4: Real, y0: Real, y1: Real, y2: Real) = {
    require(-2 <= s0 && s0 <= 9 && -2 <= s1 && s1 <= 9 && 0 <= s2 && s2 <= 10 && 0 <= s3 && s3 <= 10 && 0 <= s4 && s4 <= 10 &&
      0 <= y0 && y0 <= 10 && 0 <= y1 && y1 <= 10 && 0 <= y2 && y2 <= 10)
    (7.818052258E-06)*s0 +(-7.817588962E-06)*s1+ (5.7051393301999997E-05)*s2+ (9.9968914504599404E-01)*s3+ (4.600832536E-05)*s4 +
    (-5.596127001E-05)*y0 + (3.0867555741500002E-04)*y1+ (-4.4919087053000003E-05)*y2+ (1.6155E-11)*2.1660394157545902E+01

  }

  def state5(s0: Real, s1: Real, s2: Real, s3: Real, s4: Real, y0: Real, y1: Real, y2: Real) = {
    require(-2 <= s0 && s0 <= 9 && -2 <= s1 && s1 <= 9 && 0 <= s2 && s2 <= 10 && 0 <= s3 && s3 <= 10 && 0 <= s4 && s4 <= 10 &&
      0 <= y0 && y0 <= 10 && 0 <= y1 && y1 <= 10 && 0 <= y2 && y2 <= 10)
    s0 +(7.81854687E-06)*s1+ (4.9735362656000002E-05)*s2+ (4.7301196887999997E-05)*s3+ (9.9971578912639303E-01)*s4 +
     (-4.9737493605000002E-05)*y0 +(-4.6203604013000001E-05)*y1+ (2.8311538117500000E-04)*y2+ (1.4084E-11)*2.1660394157545902E+01

  }

}