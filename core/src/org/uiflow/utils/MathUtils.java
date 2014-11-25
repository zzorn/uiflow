package org.uiflow.utils;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

/**
 *
 */
// TODO: Update flowutils to work on android as well (split things requiring extra imports out into separate libs).
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

    /**
     * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
     * @throws IllegalArgumentException if maximum is smaller than minimum.
     */
    public static int clamp(int value, int min, int max) {
        if (max < min) throw new IllegalArgumentException("The specified maximum value ("+max+") was smaller than the minimum value ("+min+") when trying to clamp the value ("+value+")");

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
     * @throws IllegalArgumentException if maximum is smaller than minimum.
     */
    public static float clamp(float value, float min, float max) {
        if (max < min) throw new IllegalArgumentException("The specified maximum value ("+max+") was smaller than the minimum value ("+min+") when trying to clamp the value ("+value+")");

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
     * @throws IllegalArgumentException if maximum is smaller than minimum.
     */
    public static double clamp(double value, double min, double max) {
        if (max < min) throw new IllegalArgumentException("The specified maximum value ("+max+") was smaller than the minimum value ("+min+") when trying to clamp the value ("+value+")");

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Clamps a value to the range formed by a and b.
     * The order that the range is specified does not matter,
     * the smallest of a and b is used as minimum, and the largest as maximum.
     */
    public static int clampToRange(int value, int a, int b) {
        int min = a;
        int max = b;
        if (a > b) {
            min = b;
            max = a;
        }

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Clamps a value to the range formed by a and b.
     * The order that the range is specified does not matter,
     * the smallest of a and b is used as minimum, and the largest as maximum.
     */
    public static float clampToRange(float value, float a, float b) {
        float min = a;
        float max = b;
        if (a > b) {
            min = b;
            max = a;
        }

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * Clamps a value to the range formed by a and b.
     * The order that the range is specified does not matter,
     * the smallest of a and b is used as minimum, and the largest as maximum.
     */
    public static double clampToRange(double value, double a, double b) {
        double min = a;
        double max = b;
        if (a > b) {
            min = b;
            max = a;
        }

        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    /**
     * @return the range clamped to the 0..1 range (inclusive).
     */
    public static float clamp0To1(float value) {
        if (value < 0) return 0;
        if (value > 1) return 1;
        return value;
    }

    /**
     * @return the range clamped to the 0..1 range (inclusive).
     */
    public static double clamp0To1(double value) {
        if (value < 0) return 0;
        if (value > 1) return 1;
        return value;
    }

    /**
     * @return the range clamped to the -1..1 range (inclusive).
     */
    public static float clampMinus1To1(float value) {
        if (value < -1) return -1;
        if (value > 1) return 1;
        return value;
    }

    /**
     * @return the range clamped to the -1..1 range (inclusive).
     */
    public static double clampMinus1To1(double value) {
        if (value < -1) return -1;
        if (value > 1) return 1;
        return value;
    }

    /**
     * Interpolate between the start and end values (and beyond).
     * @param t 0 corresponds to start, 1 to end.
     * @return interpolated value
     */
    public static float mix(float t, float start, float end) {
        return start + t * (end - start);
    }

    /**
     * Interpolate between the start and end values (and beyond).
     * @param t 0 corresponds to start, 1 to end.
     * @return interpolated value
     */
    public static double mix(double t, double start, double end) {
        return start + t * (end - start);
    }

    /**
     * Smoothly interpolate between the start and end values, using cosine interpolation.
     * @param t 0 corresponds to start, 1 to end.
     *          Values smaller than zero return the start value, and values greater than one return the end value.
     * @return smoothly interpolated value
     */
    public static float mixSmooth(float t, float start, float end) {
        if (t <= 0) return start;
        else if (t >= 1) return end;
        else {
            float projectedT = (float) (0.5 - cos(t * PI) * 0.5);
            return start + projectedT * (end - start);
        }
    }

    /**
     * Smoothly interpolate between the start and end values, using cosine interpolation.
     * @param t 0 corresponds to start, 1 to end.
     *          Values smaller than zero return the start value, and values greater than one return the end value.
     * @return smoothly interpolated value
     */
    public static double mixSmooth(double t, double start, double end) {
        if (t <= 0) return start;
        else if (t >= 1) return end;
        else {
            double projectedT = (0.5 - cos(t * PI) * 0.5);
            return start + projectedT * (end - start);
        }
    }

    /**
     * Interpolate between the start and end values.
     * Clamps output to be within the range formed by start and end.
     *
     * @param t 0 corresponds to start, 1 to end.
     * @return interpolated value, clamped to range formed by start and end.
     */
    public static float mixAndClamp(float t, float start, float end) {
        return clampToRange(start + t * (end - start), start, end);
    }

    /**
     * Interpolate between the start and end values.
     * Clamps output to be within the range formed by start and end.
     *
     * @param t 0 corresponds to start, 1 to end.
     * @return interpolated value, clamped to range formed by start and end.
     */
    public static double mixAndClamp(double t, double start, double end) {
        return clampToRange(start + t * (end - start), start, end);
    }

    /**
     * @return relative position of t between start and end.
     *         if t == start, returns 0, if t == end, returns 1, etc.
     *         In case start equals end, returns 0.5.
     */
    public static float relPos(float t, float start, float end) {
        if (end == start) return 0.5f;
        else return (t - start) / (end - start);
    }

    /**
     * @return relative position of t between start and end.
     *         if t == start, returns 0, if t == end, returns 1, etc.
     *         In case start equals end, returns 0.5.
     */
    public static double relPos(double t, double start, double end) {
        if (end == start) return 0.5;
        else return (t - start) / (end - start);
    }

    /**
     * Maps a value within a source range to the corresponding position in a target range.
     */
    public static float map(float t, float sourceStart, float sourceEnd, float targetStart, float targetEnd) {
        float r = relPos(t, sourceStart, sourceEnd);
        return mix(r, targetStart, targetEnd);
    }

    /**
     * Maps a value within a source range to the corresponding position in a target range.
     */
    public static double map(double t, double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        double r = relPos(t, sourceStart, sourceEnd);
        return mix(r, targetStart, targetEnd);
    }

    /**
     * Smoothly maps a value within a source range to the corresponding position in a target range.
     * Uses cosine interpolation.  If t is smaller than 0, targetStart is returned.  If t is larger than one, targetEnd is returned.
     */
    public static float mapSmooth(float t, float sourceStart, float sourceEnd, float targetStart, float targetEnd) {
        float r = relPos(t, sourceStart, sourceEnd);
        return mixSmooth(r, targetStart, targetEnd);
    }

    /**
     * Smoothly maps a value within a source range to the corresponding position in a target range.
     * Uses cosine interpolation.  If t is smaller than 0, targetStart is returned.  If t is larger than one, targetEnd is returned.
     */
    public static double mapSmooth(double t, double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        double r = relPos(t, sourceStart, sourceEnd);
        return mixSmooth(r, targetStart, targetEnd);
    }

    /**
     * Maps a value within a source range to the corresponding position in a target range.
     * Clamps the result to the target range.
     */
    public static float mapAndClamp(float t, float sourceStart, float sourceEnd, float targetStart, float targetEnd) {
        float r = relPos(t, sourceStart, sourceEnd);
        return clampToRange(mix(r, targetStart, targetEnd), targetStart, targetEnd);
    }

    /**
     * Maps a value within a source range to the corresponding position in a target range.
     * Clamps the result to the target range.
     */
    public static double mapAndClamp(double t, double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        double r = relPos(t, sourceStart, sourceEnd);
        return clampToRange(mix(r, targetStart, targetEnd), targetStart, targetEnd);
    }



}
