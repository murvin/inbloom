package com.uikit.utils;

public class MathUtils {

    final double sq2p1 = 2.414213562373095048802e0;
    final double sq2m1 = .414213562373095048802e0;
    final double p4 = .161536412982230228262e2;
    final double p3 = .26842548195503973794141e3;
    final double p2 = .11530293515404850115428136e4;
    final double p1 = .178040631643319697105464587e4;
    final double p0 = .89678597403663861959987488e3;
    final double q4 = .5895697050844462222791e2;
    final double q3 = .536265374031215315104235e3;
    final double q2 = .16667838148816337184521798e4;
    final double q1 = .207933497444540981287275926e4;
    final double q0 = .89678597403663861962481162e3;
    final double PIO2 = 1.5707963267948966135E0;
    final double nan = (0.0 / 0.0);
    final double huge = 1.0e+300;
    private final int HI_SHIFT = 32;
    private final double one = 1.0;
    private final long LO_MASK = 0x00000000ffffffffL;
    final double P1 = 1.66666666666666019037e-01, /* 0x3FC55555, 0x5555553E */
            P2 = -2.77777777770155933842e-03, /* 0xBF66C16C, 0x16BEBD93 */
            P3 = 6.61375632143793436117e-05, /* 0x3F11566A, 0xAF25DE2C */
            P4 = -1.65339022054652515390e-06, /* 0xBEBBBD41, 0xC5D26BF1 */
            P5 = 4.13813679705723846039e-08; /* 0x3E663769, 0x72BEA4D0 */


    private double mxatan(double arg) {
        double argsq, value;

        argsq = arg * arg;
        value = ((((p4 * argsq + p3) * argsq + p2) * argsq + p1) * argsq + p0);
        value = value / (((((argsq + q4) * argsq + q3) * argsq + q2) * argsq + q1) * argsq + q0);
        return value * arg;
    }

    private double msatan(double arg) {
        if (arg < sq2m1) {
            return mxatan(arg);
        }
        if (arg > sq2p1) {
            return PIO2 - mxatan(1 / arg);
        }
        return PIO2 / 2 + mxatan((arg - 1) / (arg + 1));
    }

    public double atan(double arg) {
        if (arg > 0) {
            return msatan(arg);
        }
        return -msatan(-arg);
    }

    public double atan2(double arg1, double arg2) {
        if (arg1 + arg2 == arg1) {
            if (arg1 >= 0) {
                return PIO2;
            }
            return -PIO2;
        }
        arg1 = atan(arg1 / arg2);
        if (arg2 < 0) {
            if (arg1 <= 0) {
                return arg1 + Math.PI;
            }
            return arg1 - Math.PI;
        }
        return arg1;

    }

    public double asin(double arg) {
        double temp;
        int sign;

        sign = 0;
        if (arg < 0) {
            arg = -arg;
            sign++;
        }
        if (arg > 1) {
            return nan;
        }
        temp = Math.sqrt(1 - arg * arg);
        if (arg > 0.7) {
            temp = PIO2 - atan(temp / arg);
        } else {
            temp = atan(arg / temp);
        }
        if (sign > 0) {
            temp = -temp;
        }
        return temp;
    }

    public double acos(double arg) {
        if (arg > 1 || arg < -1) {
            return nan;
        }
        return PIO2 - asin(arg);
    }

    public final double exp(double a) {
        return ieee754_exp(a);
    }
    private final double twom1000 = 9.33263618503218878990e-302, /* 2**-1000=0x01700000,0*/
            o_threshold = 7.09782712893383973096e+02, /* 0x40862E42, 0xFEFA39EF */
            u_threshold = -7.45133219101941108420e+02, /* 0xc0874910, 0xD52D3051 */
            invln2 = 1.44269504088896338700e+00; /* 0x3ff71547, 0x652b82fe */

    private final double[] halF = new double[]{0.5, -0.5},
            ln2HI = new double[]{6.93147180369123816490e-01, /* 0x3fe62e42, 0xfee00000 */
        -6.93147180369123816490e-01}, /* 0xbfe62e42, 0xfee00000 */
            ln2LO = new double[]{1.90821492927058770002e-10, /* 0x3dea39ef, 0x35793c76 */
        -1.90821492927058770002e-10}; /* 0xbdea39ef, 0x35793c76 */


    private final double ieee754_exp(double x) {
        double y, c, t;
        double hi = 0, lo = 0;
        int k = 0;
        int xsb, hx, lx;
        long yl;
        long xl = Double.doubleToLongBits(x);

        hx = (int) ((long) xl >>> HI_SHIFT);	/* high word of x */
        xsb = (hx >> 31) & 1;		/* sign bit of x */
        hx &= 0x7fffffff;		/* high word of |x| */

        /* filter out non-finite argument */
        if (hx >= 0x40862E42) {			/* if |x|>=709.78... */
            if (hx >= 0x7ff00000) {
                lx = (int) ((long) xl & LO_MASK);	/* low word of x */
                if (((hx & 0xfffff) | lx) != 0) {
                    return x + x; 		/* NaN */
                } else {
                    return (xsb == 0) ? x : 0.0;	/* exp(+-inf)={inf,0} */
                }
            }
            if (x > o_threshold) {
                return huge * huge; /* overflow */
            }
            if (x < u_threshold) {
                return twom1000 * twom1000; /* underflow */
            }
        }

        /* argument reduction */
        if (hx > 0x3fd62e42) {		/* if  |x| > 0.5 ln2 */
            if (hx < 0x3FF0A2B2) {	/* and |x| < 1.5 ln2 */
                hi = x - ln2HI[xsb];
                lo = ln2LO[xsb];
                k = 1 - xsb - xsb;
            } else {
                k = (int) (invln2 * x + halF[xsb]);
                t = k;
                hi = x - t * ln2HI[0];	/* t*ln2HI is exact here */
                lo = t * ln2LO[0];
            }
            x = hi - lo;
        } else if (hx < 0x3e300000) {	/* when |x|<2**-28 */
            if (huge + x > one) {
                return one + x;/* trigger inexact */
            }
        }

        /* x is now in primary range */
        t = x * x;
        c = x - t * (P1 + t * (P2 + t * (P3 + t * (P4 + t * P5))));
        if (k == 0) {
            return one - ((x * c) / (c - 2.0) - x);
        } else {
            y = one - ((lo - (x * c) / (2.0 - c)) - hi);
        }
        yl = Double.doubleToLongBits(y);
        if (k >= -1021) {
            yl += ((long) k << (20 + HI_SHIFT));	/* add k to y's exponent */
            return Double.longBitsToDouble(yl);
        } else {
            yl += ((long) (k + 1000) << (20 + HI_SHIFT));/* add k to y's exponent */
            return Double.longBitsToDouble(yl) * twom1000;
        }
    }

    public MathUtils() {
    }
}
