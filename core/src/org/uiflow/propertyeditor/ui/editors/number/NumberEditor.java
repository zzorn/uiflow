package org.uiflow.propertyeditor.ui.editors.number;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import org.uiflow.UiContext;
import org.uiflow.utils.TextFieldChangeListener;
import org.uiflow.propertyeditor.ui.editors.EditorBase;
import org.uiflow.propertyeditor.ui.widgets.FlowSlider;
import org.uiflow.utils.MathUtils;

import java.text.DecimalFormat;

/**
 *
 */
public class NumberEditor extends EditorBase<Number, NumberEditorConfiguration> {

    private static final int MOUSE_BUTTON_FOR_ARROW_BUTTONS = Input.Buttons.LEFT;
    private static final float MIN_TICK_DELAY = 0.01f;
    private static final int SCALE_TO_N_SIGNIFICANT_NUMBERS = 2;
    private static final double SCALE_FACTOR = 1.5;

    /**
     * Accepts characters that appear in integers or floating point numbers.
     */
    private static final TextField.TextFieldFilter NUMBER_FILTER = new TextField.TextFieldFilter() {
        @Override public boolean acceptChar(TextField textField, char c) {
            return Character.isDigit(c) ||
                   c == '+' ||
                   c == '-' ||
                   c == '.' ||
                   c == 'E' ||
                   c == 'e';
        }
    };

    private static final boolean INVERT_SCALING_FOR_NEGATIVE_VALUES = false;

    private Table table;
    private TextField numberField;
    private boolean errorStyle = false;

    private Button decrementButton;
    private Button incrementButton;
    private Button tuneUpButton;
    private Button tuneDownButton;

    private FlowSlider slider;

    private DecimalFormat decimalFormat = new DecimalFormat("0.0########");

    private final InputListener scrollWheelListener = new InputListener() {

        @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            // Tell the stage that yes, we do want to receive scroll events (why can't it send them by default?)...
            getUiContext().getStage().setScrollFocus(event.getTarget());
        }

        @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            // Don't receive scroll events when cursor outside component
            getUiContext().getStage().setScrollFocus(null);
        }

        @Override public boolean scrolled(InputEvent event, float x, float y, int amount) {
            if (amount != 0) {
                changeValue(-amount);
            }
            return true;
        }
    };

    public NumberEditor() {
        this(NumberEditorConfiguration.DOUBLE_DEFAULT);
    }

    public NumberEditor(NumberEditorConfiguration configuration) {
        super(configuration);
    }

    @Override protected Actor createEditor(final NumberEditorConfiguration configuration, final UiContext uiContext) {
        table = new Table(uiContext.getSkin());

        // Create number field
        numberField = new TextField("", uiContext.getSkin());
        Container<TextField> numberFieldContainer = new Container<TextField>(numberField);
        numberField.setTextFieldFilter(NUMBER_FILTER);
        numberField.setRightAligned(true);
        numberField.addListener(scrollWheelListener);

        // Create increment, decrement, and adjust buttons
        incrementButton = createChangeButton(uiContext, "arrow_up", 1, false, 1, true);
        decrementButton = createChangeButton(uiContext, "arrow_down", -1, false, 1, true);
        tuneUpButton    = createChangeButton(uiContext, "arrow_up2", 0, true, SCALE_FACTOR, false);
        tuneDownButton  = createChangeButton(uiContext, "arrow_down2", 0, true, 1 / SCALE_FACTOR, false);

        final float buttonHeights = numberField.getHeight() / 2;
        final float buttonWidths = buttonHeights;
        Table incDec = new Table(uiContext.getSkin());
        incDec.add(incrementButton).size(buttonWidths, buttonHeights).row();
        incDec.add(decrementButton).size(buttonWidths, buttonHeights);

        Table mulDiv = new Table(uiContext.getSkin());
        mulDiv.add(tuneUpButton).size(buttonWidths, buttonHeights).row();
        mulDiv.add(tuneDownButton).size(buttonWidths, buttonHeights);

        // Set number field width
        final float digitWidth = numberField.getStyle().font.getBounds("0").width;
        final float width = digitWidth * (configuration.getNumberOfDigitsToShow() + 2);
        numberFieldContainer.width(width);

        // Create slider
        slider = new FlowSlider(configuration.getMinValue(),
                                configuration.getMaxValue(),
                                calculateStepSize(configuration),
                                false, 0, 0, uiContext.getSkin());
        slider.addListener(scrollWheelListener);
        slider.addListener(new FlowSlider.FlowSliderListener() {
            @Override public void onChanged(double newValue) {
                // Drop last digits, as the slider is not very precise
                newValue = MathUtils.roundToNDigits(newValue, SCALE_TO_N_SIGNIFICANT_NUMBERS);

                // Convert to correct type
                Number value = convertToCorrectNumberType(configuration.getNumberType(), newValue);

                // Update the rest of the UI
                updateEditedValue(value);

                // Notify listeners
                notifyValueEdited(value);
            }
        });

        // Arrange components in the ui
        table.add(numberFieldContainer);
        if (configuration.isShowArrows()) {
            table.add(incDec);//.padLeft(uiContext.getSmallGap());
            table.add(mulDiv);//.padLeft(uiContext.getSmallGap());
        }
        if (configuration.isShowSlider()) {
            table.add(slider).height(numberField.getHeight()).fill().expand();//.padLeft(uiContext.getSmallGap());
        }

        numberField.addListener(new TextFieldChangeListener() {
            @Override protected boolean onTextFieldChanged(InputEvent event, String oldValue, String newValue) {
                Number value = parseNumberFromText(configuration.getNumberType(), newValue);

                setErrorStyle(uiContext, value == null);

                if (value != null) notifyValueEdited(value);
                return false;
            }
        });

        return table;
    }

    private double calculateStepSize(NumberEditorConfiguration configuration) {
        // We just want about a hundred steps on the slider.

        double stepSize = (configuration.getMaxValue() - configuration.getMinValue()) * 0.01;

        // Account for maximums or minimums like MAX_DOUBLE or POSITIVE_INFINITY
        if (Double.isInfinite(stepSize) ||
            Double.isNaN(stepSize) ||
            stepSize == 0) stepSize = 0.01;

        return stepSize;
    }

    private Button createChangeButton(UiContext uiContext,
                                          String imageName,
                                          final double changeDelta,
                                          final boolean scale,
                                          final double scaleFactor,
                                          final boolean enableAcceleration) {

        // Create button
        final Button button = new Button(uiContext.getSkin());
        button.add(new Image(uiContext.getSkin().getDrawable(imageName), Scaling.stretch));

        // Listen to scrolling
        button.addListener(scrollWheelListener);

        // Action used to produce value changes while the button is held down
        final Action continuousPressAction = new Action() {
            private float slowdownFactorForScalingButton = scale ? 2 : 1;
            private float tickDelay =  (float) getConfiguration().getTimeBetweenValueUpdatesWhenArrowPressed_seconds() * slowdownFactorForScalingButton ;
            private float timeUntilNextTick = (float) getConfiguration().getInitialDelayWhenArrowPressed_seconds() * slowdownFactorForScalingButton ;

            @Override public void reset() {
                super.reset();

                // Reset initial time
                timeUntilNextTick = (float) getConfiguration().getInitialDelayWhenArrowPressed_seconds() * slowdownFactorForScalingButton;

                // Reset acceleration
                tickDelay =  (float) getConfiguration().getTimeBetweenValueUpdatesWhenArrowPressed_seconds() * slowdownFactorForScalingButton;
            }

            @Override public boolean act(float delta) {
                final boolean isPressed = button.isPressed();

                if (isPressed) {
                    timeUntilNextTick -= delta;

                    // Handle zero or more change ticks
                    double change = 0;
                    double scaling = 1;
                    while (timeUntilNextTick <= 0) {
                        // Determine time until next fire
                        timeUntilNextTick += tickDelay;

                        // Keep track of total change
                        change += changeDelta;
                        scaling *= scaleFactor;

                        // Accelerate scrolling
                        if (enableAcceleration) {
                            tickDelay /= getConfiguration().getScrollAcceleration_per_tick();
                            if (tickDelay < MIN_TICK_DELAY) tickDelay = MIN_TICK_DELAY;
                        }
                    }

                    // Trigger value change
                    if (scale) {
                        scaleValue(scaling, INVERT_SCALING_FOR_NEGATIVE_VALUES, SCALE_TO_N_SIGNIFICANT_NUMBERS, true);
                    }
                    else {
                        changeValue(change);
                    }

                    // Continue action
                    return false;
                }
                else {
                    // End action
                    return true;
                }
            }


        };

        // When clicked, increase or decrease the editor value
        button.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttonNum) {
                if (buttonNum == MOUSE_BUTTON_FOR_ARROW_BUTTONS) {

                    if (scale) {
                        scaleValue(scaleFactor, INVERT_SCALING_FOR_NEGATIVE_VALUES, SCALE_TO_N_SIGNIFICANT_NUMBERS, true);
                    }
                    else {
                        changeValue(changeDelta);
                    }

                    continuousPressAction.reset();
                    button.addAction(continuousPressAction);
                    return false;
                }
                else {
                    return false;
                }

            }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int buttonNum) {
                if (buttonNum == MOUSE_BUTTON_FOR_ARROW_BUTTONS) {
                    button.removeAction(continuousPressAction);
                }
            }

            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.removeAction(continuousPressAction);
            }


        });

        return button;
    }

    /**
     * Adds some delta to the edited value.
     */
    protected final void changeValue(Number delta) {
        if (delta.doubleValue() != 0) {
            final Number editedValue = getValue();
            if (editedValue != null) {
                Number result;

                final Class<? extends Number> numberType = getConfiguration().getNumberType();

                if (numberType.equals(Byte.class))         result = delta.byteValue()   + editedValue.byteValue();
                else if (numberType.equals(Short.class))   result = delta.shortValue()  + editedValue.shortValue();
                else if (numberType.equals(Integer.class)) result = delta.intValue()    + editedValue.intValue();
                else if (numberType.equals(Long.class))    result = delta.longValue()   + editedValue.longValue();
                else if (numberType.equals(Float.class))   result = delta.floatValue()  + editedValue.floatValue();
                else if (numberType.equals(Double.class))  result = delta.doubleValue() + editedValue.doubleValue();
                else throw new IllegalStateException("Unsupported number type in number field: " + numberType);

                // Update the rest of the UI
                updateEditedValue(result);

                // Notify listeners
                notifyValueEdited(result);
            }
        }
    }

    /**
     * Scale the edited value by some factor.
     *
     * @param scaling
     * @param invertScalingForNegativeValues
     * @param roundToNSignificantNumbers
     * @param forceChange if the change factor would be too small to create a change, force a change by one.
     */
    protected final void scaleValue(double scaling, boolean invertScalingForNegativeValues, int roundToNSignificantNumbers, boolean forceChange) {
        if (scaling != 1) {
            final Number editedValue = getValue();
            System.out.println("editedValue = " + editedValue);
            if (editedValue != null) {
                Number result;

                final Class<? extends Number> numberType = getConfiguration().getNumberType();

                // Invert scaling for negative values if requested
                double nonInvertedScaling = scaling;
                if (invertScalingForNegativeValues && editedValue.doubleValue() < 0 && scaling != 0) {
                    scaling = 1.0 / scaling;
                }

                // Round result to remove excessive decimals
                double resultAsDouble = MathUtils.roundToNDigits(scaling * editedValue.doubleValue(), roundToNSignificantNumbers);
                System.out.println("resultAsDouble = " + resultAsDouble);

                // Check if we should move at least one, to avoid getting stuck on a low integer value when scaling
                if (forceChange && isIntegerType(numberType) && editedValue.longValue() == (long)resultAsDouble) {
                    // Force a change by at least one
                    if (nonInvertedScaling >= 1) resultAsDouble += 1;
                    else resultAsDouble -= 1;
                }

                result = convertToCorrectNumberType(numberType, resultAsDouble);

                // Update the rest of the UI
                updateEditedValue(result);

                // Notify listeners
                notifyValueEdited(result);
            }
        }
    }

    private Number convertToCorrectNumberType(Class<? extends Number> numberType, double resultAsDouble) {
        Number result;// Convert to actual number type being edited
        if (numberType.equals(Byte.class))         result = (byte) resultAsDouble;
        else if (numberType.equals(Short.class))   result = (short) resultAsDouble;
        else if (numberType.equals(Integer.class)) result = (int) resultAsDouble;
        else if (numberType.equals(Long.class))    result = (long) resultAsDouble;
        else if (numberType.equals(Float.class))   result = (float)(resultAsDouble);
        else if (numberType.equals(Double.class))  result = resultAsDouble;
        else throw new IllegalStateException("Unsupported number type in number field: " + numberType);
        return result;
    }

    private boolean isIntegerType(Class<? extends Number> numberType) {
        return numberType == Byte.class ||
               numberType == Short.class ||
               numberType == Integer.class ||
               numberType == Long.class;
    }

    @Override protected void updateEditedValue(Number value) {
        final String valueAsText;
        if (value instanceof Double ||
            value instanceof Float ) {
            // Strip small rounding errors
            valueAsText = decimalFormat.format(value);
        }
        else {
            valueAsText = "" + value;
        }

        numberField.setText(valueAsText);

        if (value != null) slider.setValue(value.floatValue());
    }

    @Override protected void setDisabled(boolean disabled) {
        numberField.setDisabled(disabled);
    }

    private Number parseNumberFromText(Class<? extends Number> numberType, final String text) {
        try {
            if (numberType.equals(Byte.class)) return Byte.valueOf(text);
            else if (numberType.equals(Short.class)) return Short.valueOf(text);
            else if (numberType.equals(Integer.class)) return Integer.valueOf(text);
            else if (numberType.equals(Long.class)) return Long.valueOf(text);
            else if (numberType.equals(Float.class)) return Float.valueOf(text);
            else if (numberType.equals(Double.class)) return Double.valueOf(text);
            else throw new IllegalStateException("Unsupported number type in number field: " + numberType);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private void setErrorStyle(UiContext uiContext, boolean error) {
        if (errorStyle != error) {
            errorStyle = error;

            // Change style to indicate invalid or ok input
            String styleName = error ? "error" : "default";
            numberField.setStyle(uiContext.getSkin().get(styleName, TextField.TextFieldStyle.class));
            numberField.layout();
        }
    }

}
