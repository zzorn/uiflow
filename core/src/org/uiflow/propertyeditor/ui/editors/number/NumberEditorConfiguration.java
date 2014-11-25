package org.uiflow.propertyeditor.ui.editors.number;

import org.uiflow.propertyeditor.ui.editors.EditorConfigurationBase;
import org.uiflow.utils.colorfunction.ColorFunction;

/**
 * Configuration for a NumberEditor.
 */
public class NumberEditorConfiguration extends EditorConfigurationBase {

    public static final NumberEditorConfiguration INTEGER_DEFAULT = new NumberEditorConfiguration(Integer.class);
    public static final NumberEditorConfiguration DOUBLE_DEFAULT = new NumberEditorConfiguration(Double.class);

    private static final double DEFAULT_DELAY_BEFORE_ARROW_BUTTON_REPEAT = 0.3;
    private static final double ARROW_BUTTON_REPEAT_DELAY = 0.15;
    private static final int DEFAULT_NUMBER_OF_DIGITS_TO_SHOW = 5;
    private static final double DEFAULT_SCROLL_ACCELERATION = 1.1;
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;

    private Class<? extends Number> numberType;
    private double maxValue;
    private double minValue;
    private double originValue;
    private boolean enforceRange;
    private boolean logarithmic;
    private boolean useOrigin;
    private boolean showSlider;
    private boolean showArrows;
    private int numberOfDigitsToShow = DEFAULT_NUMBER_OF_DIGITS_TO_SHOW;
    private double initialDelayWhenArrowPressed_seconds = DEFAULT_DELAY_BEFORE_ARROW_BUTTON_REPEAT;
    private double timeBetweenValueUpdatesWhenArrowPressed_seconds = ARROW_BUTTON_REPEAT_DELAY;
    private double scrollAcceleration_per_tick = DEFAULT_SCROLL_ACCELERATION;
    private ColorFunction colorFunction;

    public NumberEditorConfiguration() {
        this(Double.class);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType) {
        this(numberType, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, false);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue) {
        this(numberType, minValue, maxValue, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double originValue,
                                     double maxValue) {
        this(numberType, minValue, originValue, maxValue, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue,
                                     final boolean enforceRange) {
        this(numberType, minValue, maxValue, enforceRange, false);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double originValue,
                                     double maxValue,
                                     final boolean enforceRange) {
        this(numberType, minValue, originValue, maxValue, enforceRange, false);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue,
                                     final boolean enforceRange,
                                     boolean logarithmic) {
        this(numberType, minValue, maxValue, enforceRange, logarithmic, true, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double originValue,
                                     double maxValue,
                                     final boolean enforceRange,
                                     boolean logarithmic) {
        this(numberType, minValue, originValue, maxValue, enforceRange, logarithmic, true, true, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue,
                                     boolean enforceRange,
                                     boolean logarithmic,
                                     boolean showSlider,
                                     boolean showArrows) {
        this(numberType, minValue, minValue, maxValue, enforceRange, logarithmic, false, showSlider, showArrows);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue,
                                     boolean enforceRange,
                                     boolean logarithmic,
                                     boolean showSlider,
                                     boolean showArrows,
                                     ColorFunction colorFunction) {
        this(numberType, minValue, minValue, maxValue, enforceRange, logarithmic, false, showSlider, showArrows, colorFunction);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double originValue,
                                     double maxValue,
                                     boolean enforceRange,
                                     boolean logarithmic,
                                     boolean useOrigin,
                                     boolean showSlider,
                                     boolean showArrows) {
        this(numberType, minValue, originValue, maxValue, enforceRange, logarithmic, useOrigin, showSlider, showArrows, null);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double originValue,
                                     double maxValue,
                                     boolean enforceRange,
                                     boolean logarithmic,
                                     boolean useOrigin,
                                     boolean showSlider,
                                     boolean showArrows,
                                     ColorFunction colorFunction) {
        super(NumberEditor.class);
        this.originValue = originValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.enforceRange = enforceRange;
        this.logarithmic = logarithmic;
        this.useOrigin = useOrigin;
        this.showSlider = showSlider;
        this.showArrows = showArrows;
        this.numberType = numberType;
        this.colorFunction = colorFunction;
    }

    public Class<? extends Number> getNumberType() {
        return numberType;
    }

    public void setNumberType(Class<? extends Number> numberType) {
        this.numberType = numberType;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getOriginValue() {
        return originValue;
    }

    public void setOriginValue(double originValue) {
        this.originValue = originValue;
    }

    public boolean isEnforceRange() {
        return enforceRange;
    }

    public void setEnforceRange(boolean enforceRange) {
        this.enforceRange = enforceRange;
    }

    public boolean isLogarithmic() {
        return logarithmic;
    }

    public void setLogarithmic(boolean logarithmic) {
        this.logarithmic = logarithmic;
    }

    public boolean isUseOrigin() {
        return useOrigin;
    }

    public void setUseOrigin(boolean useOrigin) {
        this.useOrigin = useOrigin;
    }

    public boolean isShowSlider() {
        return showSlider;
    }

    public void setShowSlider(boolean showSlider) {
        this.showSlider = showSlider;
    }

    public boolean isShowArrows() {
        return showArrows;
    }

    public void setShowArrows(boolean showArrows) {
        this.showArrows = showArrows;
    }

    public int getNumberOfDigitsToShow() {
        return numberOfDigitsToShow;
    }

    public void setNumberOfDigitsToShow(int numberOfDigitsToShow) {
        this.numberOfDigitsToShow = numberOfDigitsToShow;
    }

    public double getInitialDelayWhenArrowPressed_seconds() {
        return initialDelayWhenArrowPressed_seconds;
    }

    public void setInitialDelayWhenArrowPressed_seconds(double initialDelayWhenArrowPressed_seconds) {
        this.initialDelayWhenArrowPressed_seconds = initialDelayWhenArrowPressed_seconds;
    }

    public double getTimeBetweenValueUpdatesWhenArrowPressed_seconds() {
        return timeBetweenValueUpdatesWhenArrowPressed_seconds;
    }

    public void setTimeBetweenValueUpdatesWhenArrowPressed_seconds(double timeBetweenValueUpdatesWhenArrowPressed_seconds) {
        this.timeBetweenValueUpdatesWhenArrowPressed_seconds = timeBetweenValueUpdatesWhenArrowPressed_seconds;
    }

    public double getScrollAcceleration_per_tick() {
        return scrollAcceleration_per_tick;
    }

    public void setScrollAcceleration_per_tick(double scrollAcceleration_per_tick) {
        this.scrollAcceleration_per_tick = scrollAcceleration_per_tick;
    }

    public ColorFunction getColorFunction() {
        return colorFunction;
    }

    public void setColorFunction(ColorFunction colorFunction) {
        this.colorFunction = colorFunction;
    }
}
