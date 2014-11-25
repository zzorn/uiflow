package org.uiflow.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Utility functions for checking parameter validity.
 * They throw a descriptive IllegalArgumentException if they fail.
 * <p/>
 * You may wish to use static import of this class for more concise code.
 */
public final class Check {

    private static final String CHECK_CLASS_NAME = Check.class.getName();

    /**
     * Checks that the specified condition is fulfilled.
     *
     * @param condition true if condition acceptable, false if an error should be thrown.
     * @param description description of the invariant, added to exception if invariant failed.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void invariant(boolean condition, String description) {
        if (!condition) {
            fail(description);
        }
    }


    /**
     * Checks that the specified parameter is not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void normalNumber(double parameter, String parameterName) {
        if (Double.isInfinite(parameter)) {
            fail(parameter, parameterName, "be a normal, non-infinite number");
        }
        if (Double.isNaN(parameter)) {
            fail(parameter, parameterName, "be a normal number");
        }
    }


    /**
     * Checks that the specified parameter is positive and not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void positiveOrZero(double parameter, String parameterName) {
        normalNumber(parameter, parameterName);

        if (parameter < 0) {
            fail(parameter, parameterName, "be a normal positive number");
        }
    }


    /**
     * Checks that the specified parameter is positive, not zero, not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void positive(double parameter, String parameterName) {
        normalNumber(parameter, parameterName);

        if (parameter <= 0) {
            fail(parameter, parameterName, "be a normal positive non-zero number");
        }
    }


    /**
     * Checks that the specified parameter is negative and not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void negativeOrZero(double parameter, String parameterName) {
        normalNumber(parameter, parameterName);

        if (parameter > 0) {
            fail(parameter, parameterName, "be a normal negative or zero number");
        }
    }


    /**
     * Checks that the specified parameter is negative, not zero, not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void negative(double parameter, String parameterName) {
        normalNumber(parameter, parameterName);

        if (parameter >= 0) {
            fail(parameter, parameterName, "be a normal negative non-zero number");
        }
    }


    /**
     * Checks that the specified parameter is not zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param epsilon +- range around zero that is regarded as zero.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notZero(double parameter, String parameterName, double epsilon) {
        normalNumber(parameter, parameterName);

        if (parameter >= -epsilon && parameter <= epsilon) {
            fail(parameter, parameterName, "be a normal non-zero number");
        }
    }

    /**
     * Checks that the specified parameter is positive and not zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void positive(int parameter, String parameterName) {
        if (parameter <= 0) {
            fail(parameter, parameterName, "be a positive non-zero number");
        }
    }


    /**
     * Checks that the specified parameter is positive or zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void positiveOrZero(int parameter, String parameterName) {
        if (parameter < 0) {
            fail(parameter, parameterName, "be a positive or zero number");
        }
    }

    /**
     * Checks that the specified parameter is negative and not zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void negative(int parameter, String parameterName) {
        if (parameter >= 0) {
            fail(parameter, parameterName, "be a negative non-zero number");
        }
    }


    /**
     * Checks that the specified parameter is negative or zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void negativeOrZero(int parameter, String parameterName) {
        if (parameter > 0) {
            fail(parameter, parameterName, "be a negative or zero number");
        }
    }

    /**
     * Checks that the specified parameter not zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notZero(int parameter, String parameterName) {
        if (parameter == 0) {
            fail(parameter, parameterName, "be a non-zero number");
        }
    }


    /**
     * Checks that the specified parameter is not null.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notNull(final Object parameter, final String parameterName) {
        if (parameter == null) {
            fail(parameter, parameterName, "not be null");
        }
    }

    /**
     * Checks that the specified parameter is not an empty string nor null nor a string with just whitespace.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void nonEmptyString(final String parameter, final String parameterName) {
        if (parameter == null || parameter.trim().isEmpty()) {
            fail(parameter, parameterName, "be a non-empty string value");
        }
    }

    /**
     * Checks that the parameter is in the range 0..1 (inclusive).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void inRangeZeroToOne(final double parameter, String parameterName) {
        normalNumber(parameter, parameterName);

        if (parameter < 0 || parameter > 1) {
            fail(parameter, parameterName, "be in the range 0 to 1 inclusive");
        }
    }


    /**
     * Checks that the parameter is in the specified range
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void inRange(final double parameter,
                               String parameterName,
                               double minimumValueInclusive,
                               double maximumValueExclusive) {
        normalNumber(parameter, parameterName);

        if (parameter < minimumValueInclusive || parameter >= maximumValueExclusive) {
            fail(parameter, parameterName,
                 "be in the range " + minimumValueInclusive + " (inclusive) " +
                 "to " + maximumValueExclusive + " (exclusive)");
        }
    }

    /**
     * Checks that the parameter is in the specified range
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void inRangeInclusive(final double parameter,
                               String parameterName,
                               double minimumValueInclusive,
                               double maximumValueInclusive) {
        normalNumber(parameter, parameterName);

        if (parameter < minimumValueInclusive || parameter > maximumValueInclusive) {
            fail(parameter, parameterName,
                 "be in the range " + minimumValueInclusive + " (inclusive) " +
                 "to " + maximumValueInclusive + " (inclusive)");
        }
    }


    /**
     * Checks that the parameter is in the specified range
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void inRange(final int parameter,
                               String parameterName,
                               int minimumValueInclusive,
                               int maximumValueExclusive) {
        if (parameter < minimumValueInclusive || parameter >= maximumValueExclusive) {
            fail(parameter, parameterName,
                 "be in the range " + minimumValueInclusive + " (inclusive) " +
                 "to " + maximumValueExclusive + " (exclusive)");
        }
    }

    /**
     * Checks that the parameter is in the specified range
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void inRangeInclusive(final int parameter,
                                        String parameterName,
                                        int minimumValueInclusive,
                                        int maximumValueInclusive) {
        if (parameter < minimumValueInclusive || parameter > maximumValueInclusive) {
            fail(parameter, parameterName,
                 "be in the range " + minimumValueInclusive + " (inclusive) " +
                 "to " + maximumValueInclusive + " (inclusive)");
        }
    }



    /**
     * Checks that the parameter is smaller than the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueExclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void less(final int parameter,
                            String parameterName,
                            int maximumValueExclusive,
                            String valueName) {
        if (parameter >= maximumValueExclusive) {
            fail(parameter, parameterName, "smaller than", maximumValueExclusive, valueName);
        }
    }

    /**
     * Checks that the parameter is smaller or equal to the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueInclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void lessOrEqual(final int parameter,
                                   String parameterName,
                                   int maximumValueInclusive,
                                   String valueName) {
        if (parameter > maximumValueInclusive) {
            fail(parameter, parameterName, "smaller or equal to", maximumValueInclusive, valueName);
        }
    }

    /**
     * Checks that the parameter is greater than the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueExclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void greater(final int parameter,
                               String parameterName,
                               int minimumValueExclusive,
                               String valueName) {
        if (parameter <= minimumValueExclusive) {
            fail(parameter, parameterName, "larger than", minimumValueExclusive, valueName);
        }
    }

    /**
     * Checks that the parameter is greater or equal to the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueInclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void greaterOrEqual(final int parameter,
                                      String parameterName,
                                      int minimumValueInclusive,
                                      String valueName) {
        if (parameter < minimumValueInclusive) {
            fail(parameter, parameterName, "larger or equal to", minimumValueInclusive, valueName);
        }
    }


    /**
     * Checks that the integer parameter is equal to the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param requiredValue the value that the parameter should be equal to
     * @param valueName description of the value to be equal to, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void equal(final int parameter,
                             String parameterName,
                             int requiredValue,
                             String valueName) {
        if (parameter != requiredValue) {
            fail(parameter, parameterName, "equal to", requiredValue, valueName);
        }
    }

    /**
     * Checks that the parameter is smaller than the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueExclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void less(final double parameter,
                            String parameterName,
                            double maximumValueExclusive,
                            String valueName) {
        if (parameter >= maximumValueExclusive) {
            fail(parameter, parameterName, "smaller than", maximumValueExclusive, valueName);
        }
    }

    /**
     * Checks that the parameter is smaller or equal to the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param maximumValueInclusive the value that the parameter must be under
     * @param valueName description of the maximum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void lessOrEqual(final double parameter,
                                   String parameterName,
                                   double maximumValueInclusive,
                                   String valueName) {
        if (parameter > maximumValueInclusive) {
            fail(parameter, parameterName, "smaller or equal to", maximumValueInclusive, valueName);
        }
    }

    /**
     * Checks that the parameter is greater than the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueExclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void greater(final double parameter,
                               String parameterName,
                               double minimumValueExclusive,
                               String valueName) {
        if (parameter <= minimumValueExclusive) {
            fail(parameter, parameterName, "larger than", minimumValueExclusive, valueName);
        }
    }

    /**
     * Checks that the parameter is greater or equal to the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param minimumValueInclusive the value that the parameter should be larger than.
     * @param valueName description of the minimum value, or null if no description is desired.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void greaterOrEqual(final double parameter,
                                      String parameterName,
                                      double minimumValueInclusive,
                                      String valueName) {
        if (parameter < minimumValueInclusive) {
            fail(parameter, parameterName, "larger or equal to", minimumValueInclusive, valueName);
        }
    }


    /**
     * Checks that the floating point parameter is equal to the specified value.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @param requiredValue the value that the parameter should be equal to
     * @param valueName description of the value to be equal to, or null if no description is desired.
     * @param maximumAllowedDifference the epsilon, or maximum allowed difference, between the parameter value and the required value.
     *                                 Needed for floating point values as they are not precise.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void equal(final double parameter,
                             String parameterName,
                             double requiredValue,
                             String valueName,
                             double maximumAllowedDifference) {
        if (parameter < requiredValue - maximumAllowedDifference ||
            parameter > requiredValue + maximumAllowedDifference) {
            fail(parameter, parameterName, "equal to (with a max difference of "+maximumAllowedDifference+")", requiredValue, valueName);
        }
    }


    /**
     * Checks that the parameter equals the specified value.
     * Uses the equals method.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void equals(final Object parameter,
                             String parameterName,
                             Object requiredValue,
                             String otherName) {
        if ((parameter != null && !parameter.equals(requiredValue)) ||
            (parameter == null && requiredValue != null)) {
            fail(parameter, parameterName,
                 "be equal to " + otherName + " which is '" + requiredValue + "'");
        }
    }

    /**
     * Checks that the parameter equals the specified value.
     * Uses reference equality ( == ).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     * @throws IllegalArgumentException if the check fails.
     */
    public static void equalRef(final Object parameter,
                                String parameterName,
                                Object requiredValue,
                                String otherName) {
        if (parameter != requiredValue) {
            fail(parameter, parameterName,
                 "be the same object as " + otherName + " which is '" + requiredValue + "'");
        }
    }


    /**
     * Checks that the specified element is contained in the specified collection.
     *
     * @param element element to check for.
     * @param collection collection to check in
     * @param collectionName  name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void contained(final Object element,
                                 final Collection collection,
                                 final String collectionName) {
        if (!collection.contains(element)) {
            fail("The " + collectionName + " does not contain " + describeElementType(element));
        }
    }

    /**
     * Checks that the specified element is not contained in the specified collection.
     *
     * @param element element to check for.
     * @param collection collection to check in
     * @param collectionName  name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notContained(final Object element, final Collection collection, final String collectionName) {
        if (collection.contains(element)) {
            fail("The " + collectionName + " already contains " + describeElementType(element));
        }
    }


    /**
     * Checks that the specified key is contained in the specified map.
     *
     * @param key key to check for.
     * @param map map to check in
     * @param mapName name to use for map in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void contained(final Object key, final Map map, final String mapName) {
        if (!map.containsKey(key)) {
            fail("The " + mapName + " does not contain the key '" + key + "'");
        }
    }


    /**
     * Checks that the specified key is not contained in the specified map.
     *
     * @param key key to check for.
     * @param map map to check in
     * @param mapName name to use for map in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notContained(final Object key, final Map map, final String mapName) {
        if (map.containsKey(key)) {
            fail("The " + mapName + " already contains the key '" + key + "'");
        }
    }

    /**
     * Checks that the specified collection is empty.
     *
     * @param collection collection to check.
     * @param collectionName name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void empty(final Collection collection, final String collectionName) {
        if (!collection.isEmpty()) {
            fail("The " + collectionName + " was empty");
        }
    }

    /**
     * Checks that the specified collection is not empty.
     *
     * @param collection collection to check.
     * @param collectionName name to use for collection in any error message.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notEmpty(final Collection collection, final String collectionName) {
        if (collection.isEmpty()) {
            fail("The " + collectionName + " was empty");
        }
    }


    /**
     * Checks that the parameter is an instance of the specified type.
     *
     * @param parameter parameter to check
     * @param parameterName name to use for parameter
     * @param expectedParameterType type to check for.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void instanceOf(final Object parameter,
                                  final String parameterName,
                                  final Class expectedParameterType) {
        notNull(parameter, parameterName);

        if (!expectedParameterType.isInstance(parameter)) {
            fail("of type '" + parameter.getClass() + "'", parameterName,
                 "be of type '" + expectedParameterType + "'");
        }
    }

    /**
     * Checks that the parameter is not an instance of the specified type.
     *
     * @param parameter parameter to check
     * @param parameterName name to use for parameter
     * @param expectedParameterType type to check for.
     * @throws IllegalArgumentException if the check fails.
     */
    public static void notInstanceOf(final Object parameter,
                                  final String parameterName,
                                  final Class expectedParameterType) {
        notNull(parameter, parameterName);

        if (expectedParameterType.isInstance(parameter)) {
            fail("of type '" + parameter.getClass() + "'", parameterName,
                 "be of type '" + expectedParameterType + "'");
        }
    }


    /**
     * Throws an IllegalArgumentException, using the specified parameters to compose the error message,
     * and including information on the method that it was thrown from.
     *
     * @throws IllegalArgumentException with message.
     */
    public static void fail(final Object parameter,
                            final String parameterName,
                            final String expectedCondition) {
        fail("The parameter '" + parameterName + "' " +
             "should " + expectedCondition + ", " +
             "but it was '" + parameter + "'");
    }

    /**
     * Throws an IllegalArgumentException, using the specified parameters to compose the error message,
     * and including information on the method that it was thrown from.
     *
     * @param parameter parameter value
     * @param parameterName name of parameter
     * @param failureType type of failure of the parameter
     * @param value value that the parameter failed to relate to
     * @param valueName description of the value that the parameter failed to relate to
     * @throws IllegalArgumentException with message.
     */
    private static void fail(Object parameter,
                             String parameterName,
                             String failureType,
                             Object value,
                             String valueName) {
        String valueDesc = valueName == null ? " "+value : " "+ valueName +", which is " + value;
        fail("The parameter '" + parameterName + "' " +
             "was '" + parameter + "', " +
             "but it should be " + failureType + valueDesc);
    }


    /**
     * Throws an IllegalArgumentException with the specified description, including information on the method that
     * it was thrown from.
     *
     * @throws IllegalArgumentException with message.
     */
    public static void fail(final String desc) {
        throw new IllegalArgumentException(desc + determineContext());
    }

    //======================================================================
    // Private Methods

    // Constructor disabled
    private Check() {
    }

    private static String describeElementType(final Object element) {
        final String elementDesc;
        if (element == null) {
            elementDesc = "a null element.";
        } else {
            elementDesc = "the " + element.getClass() + "  '" + element + "'.";
        }

        return elementDesc;
    }



    /**
     * @return method and class that this method was called from, excluding any calls from within this class.
     */
    private static String determineContext() {
        // Get call stack
        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        // Iterate to first method not in Check class (= the originator of the failed check)
        // Skip over the first entry, as it is the getStackTrace method call.
        for (int i = 1; i < trace.length; i++) {
            if (!trace[i].getClassName().equals(CHECK_CLASS_NAME)) {
                String methodName = trace[i].getMethodName();
                String className = trace[i].getClassName();
                return ", in method " + methodName + " of class " + className;
            }
        }

        // Normally we wouldn't get here
        return ".";
    }

}

