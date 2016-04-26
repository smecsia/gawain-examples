package me.smecsia.gawain.util

import static java.math.RoundingMode.CEILING

/**
 * @author Ilya Sadykov
 */
abstract class MathUtil {
    private MathUtil() {}

    /**
     * Nilakant approach of Pi approximation, which calculates single value of the set
     */
    public static BigDecimal piApproximate(Number v, int precision) {
        BigDecimal first = dec(4.0d).divide(
                dec(v).multiply(dec(v + 1)).multiply(dec(v + 2)), precision, CEILING
        )
        BigDecimal second = dec(4.0d).divide(
                dec(v + 2).multiply(dec(v + 3)).multiply(dec(v + 4)), precision, CEILING
        )
        dec(first).subtract(second)
    }

    /**
     * Returns BigDecimal representation of the provided value
     */
    public static BigDecimal dec(Number v) {
        BigDecimal.valueOf(v as double)
    }

    /**
     */
    public static BigInteger bigInt(Number v) {
        BigInteger.valueOf(v)
    }
}
