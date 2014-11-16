package org.uiflow.propertyeditor.ui.editors;

import org.uiflow.propertyeditor.model.EditorConfigurationBase;

/**
 *
 */
public class NumberEditorConfiguration extends EditorConfigurationBase {

    public static final NumberEditorConfiguration INTEGER_DEFAULT = new NumberEditorConfiguration(Integer.class);
    public static final NumberEditorConfiguration DOUBLE_DEFAULT = new NumberEditorConfiguration(Double.class);
    public static final NumberEditorConfiguration DOUBLE_ZERO_TO_HUNDRED = new NumberEditorConfiguration(Double.class, 0, 100, false, true, true);

    private static final double DEFAULT_DELAY_BEFORE_ARROW_BUTTON_REPEAT = 0.3;
    private static final double ARROW_BUTTON_REPEAT_DELAY = 0.15;
    private static final int DEFAULT_NUMBER_OF_DIGITS_TO_SHOW = 5;
    private static final double DEFAULT_SCROLL_ACCELERATION = 1.1;

    private Class<? extends Number> numberType;
    private double maxValue;
    private double minValue;
    private boolean logarithmic;
    private boolean showSlider;
    private boolean showArrows;
    private int numberOfDigitsToShow = DEFAULT_NUMBER_OF_DIGITS_TO_SHOW;
    private double initialDelayWhenArrowPressed_seconds = DEFAULT_DELAY_BEFORE_ARROW_BUTTON_REPEAT;
    private double timeBetweenValueUpdatesWhenArrowPressed_seconds = ARROW_BUTTON_REPEAT_DELAY;
    private double scrollAcceleration_per_tick = DEFAULT_SCROLL_ACCELERATION;

    public NumberEditorConfiguration() {
        this(Double.class);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType) {
        this(numberType, Double.MAX_VALUE, Double.MIN_VALUE, false, true, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue) {
        this(numberType, minValue, maxValue, false, false, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue,
                                     boolean logarithmic) {
        this(numberType, minValue, maxValue, logarithmic, true, true);
    }

    public NumberEditorConfiguration(Class<? extends Number> numberType,
                                     double minValue,
                                     double maxValue,
                                     boolean logarithmic,
                                     boolean showSlider,
                                     boolean showArrows) {
        super(NumberEditor.class);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.logarithmic = logarithmic;
        this.showSlider = showSlider;
        this.showArrows = showArrows;
        this.numberType = numberType;
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

    public boolean isLogarithmic() {
        return logarithmic;
    }

    public void setLogarithmic(boolean logarithmic) {
        this.logarithmic = logarithmic;
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
}
