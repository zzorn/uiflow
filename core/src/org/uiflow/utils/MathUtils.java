package org.uiflow.utils;

/**
 *
 */
public class MathUtils {


    /**
     * Rounds to specific number of significant digits
     */
    public static double roundToNDigits(double value, int numberOfSignificantDigits) {

        if (numberOfSignificantDigits < 1) {
            numberOfSignificantDigits = 1;
        }

        double digitsInValue = (int) Math.floor(Math.log10(Math.abs(value))) + 1;
        double digitsToRemove = digitsInValue - numberOfSignificantDigits;

        final double scale = Math.pow(10, digitsToRemove);

        value /= scale;
        value = Math.round(value);
        value *= scale;

        return value;
    }

    /**
     * Rounds to closest integer.
     */
    public static int round(double value) {
        value += 0.5;

        // Use the fast floor formula
        return value < 0.0f ? (int)(value - 1) : (int) value;
    }

}
