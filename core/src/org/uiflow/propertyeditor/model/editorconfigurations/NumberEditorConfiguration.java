package org.uiflow.propertyeditor.model.editorconfigurations;

import org.uiflow.propertyeditor.model.EditorConfigurationBase;
import org.uiflow.propertyeditor.ui.editors.NumberEditor;

/**
 *
 */
public class NumberEditorConfiguration extends EditorConfigurationBase {

    public static final NumberEditorConfiguration INTEGER_DEFAULT = new NumberEditorConfiguration(Integer.class);
    public static final NumberEditorConfiguration DOUBLE_DEFAULT = new NumberEditorConfiguration(Double.class);
    public static final NumberEditorConfiguration DOUBLE_ZERO_TO_HUNDRED = new NumberEditorConfiguration(Double.class, 0, 100, false, true, true);

    private Class<? extends Number> numberType;
    private double maxValue;
    private double minValue;
    private boolean logarithmic;
    private boolean showSlider;
    private boolean showArrows;

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
}
