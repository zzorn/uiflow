package org.uiflow.widgets;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import org.uiflow.utils.*;
import org.uiflow.utils.colorfunction.ColorFunction;

/**
 * Improved slider component
 */
public class FlowSlider extends Widget implements Disableable {

    private static final int DRAG_BUTTON = Input.Buttons.LEFT; // This is zero, which will also always be returned on Android when using fingers as inputs.
    private static final double ORIGIN_RELATIVE_POSITION = 0.5;
    private static final double SCROLL_STEP = 0.1;

    private static final double EPSILON = 0.00001;
    private static final int DEFAULT_PREFERRED_WIDTH = 160;

    private double min;
    private double max;
    private double origin;
    private double value;
    private boolean logarithmic;
    private boolean useOrigin;
    private boolean disabled = false;
    private boolean enforceRange = false;
    private boolean useActualValueForColorFunction = false;

    private FlowSliderStyle flowSliderStyle;
    private ColorFunction valueColorFunction;

    private float prefWidth;
    private float prefHeight;

    private transient boolean notifyingListenersAboutValueChange = false;
    private transient boolean dragging = false;

    private final Array<FlowSliderListener> listeners = new Array<FlowSliderListener>(3);
    private final Color valueBarColor = new Color(Color.WHITE);
    private final Color tempColor = new Color();

    private final InputListener inputListener = new ScrollInputListener() {
        @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);

            if (button == DRAG_BUTTON) {
                uiValueChanged(x, y);
                dragging = true;
                return true;
            }
            else {
                return false;
            }
        }

        @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);

            if (button == DRAG_BUTTON) {
                uiValueChanged(x, y);
                dragging = false;
            }
        }

        @Override public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);

            uiValueChanged(x, y);
        }

        @Override public boolean mouseMoved(InputEvent event, float x, float y) {
            super.mouseMoved(event, x, y);

            if (dragging) {
                notifyValueChanged();
                return true;
            }
            else {
                return false;
            }
        }

        @Override public boolean scrolled(InputEvent event, float x, float y, int amount) {
            super.scrolled(event, x, y, amount);

            changeValue(-amount);
            return true;
        }
    };


    /**
     * Creates a new linear slider with no origin and range 0 to 100, that enforces the range and uses a default style and color.
     */
    public FlowSlider(Skin skin) {
        this(getStyleFromSkin(skin));
    }

    /**
     * Creates a new linear slider with no origin and range 0 to 100, that enforces the range and uses a default color.
     *
     * @param flowSliderStyle UI style for the slider.
     */
    public FlowSlider(FlowSliderStyle flowSliderStyle) {
        this(flowSliderStyle, null);
    }

    /**
     * Creates a new linear slider with no origin and range 0 to 100, that enforces the range.
     *
     * @param skin skin to get slider the style from.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(Skin skin,
                      ColorFunction valueColorFunction) {
        this(getStyleFromSkin(skin), valueColorFunction, 0, 100);
    }

    /**
     * Creates a new linear slider with no origin and range 0 to 100, that enforces the range.
     *
     * @param flowSliderStyle UI style for the slider.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(FlowSliderStyle flowSliderStyle,
                      ColorFunction valueColorFunction) {
        this(flowSliderStyle, valueColorFunction, 0, 100);
    }

    /**
     * Creates a new linear slider with no origin that enforces the range.
     *
     * @param skin skin to get slider the style from.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(Skin skin,
                      ColorFunction valueColorFunction,
                      double min,
                      double max) {
        this(getStyleFromSkin(skin), valueColorFunction, min, max, true);
    }

    /**
     * Creates a new linear slider with no origin that enforces the range.
     *
     * @param flowSliderStyle UI style for the slider.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(FlowSliderStyle flowSliderStyle,
                      ColorFunction valueColorFunction,
                      double min,
                      double max) {
        this(flowSliderStyle, valueColorFunction, min, max, true);
    }

    /**
     * Creates a new linear slider with no origin.
     *
     * @param skin skin to get slider the style from.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param enforceRange true if value will be clamped to the minimum and maximum values shown.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(Skin skin,
                      ColorFunction valueColorFunction,
                      double min,
                      double max,
                      boolean enforceRange) {
        this(getStyleFromSkin(skin), valueColorFunction, min, max, enforceRange, false);
    }

    /**
     * Creates a new linear slider with no origin.
     *
     * @param flowSliderStyle UI style for the slider.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param enforceRange true if value will be clamped to the minimum and maximum values shown.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(FlowSliderStyle flowSliderStyle,
                      ColorFunction valueColorFunction,
                      double min,
                      double max,
                      boolean enforceRange) {
        this(flowSliderStyle, valueColorFunction, min, max, enforceRange, false);
    }

    /**
     * Creates a new slider with no origin.
     *
     * @param skin skin to get slider the style from.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param enforceRange true if value will be clamped to the minimum and maximum values shown.
     * @param logarithmic true if the slider should use a logarithmic scale instead of a linear scale.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(Skin skin,
                      ColorFunction valueColorFunction,
                      double min,
                      double max,
                      boolean enforceRange,
                      boolean logarithmic) {
        this(getStyleFromSkin(skin), valueColorFunction, min, max, enforceRange, logarithmic, false, min);
    }

    /**
     * Creates a new slider with no origin.
     *
     * @param flowSliderStyle UI style for the slider.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param enforceRange true if value will be clamped to the minimum and maximum values shown.
     * @param logarithmic true if the slider should use a logarithmic scale instead of a linear scale.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     */
    public FlowSlider(FlowSliderStyle flowSliderStyle,
                      ColorFunction valueColorFunction,
                      double min,
                      double max,
                      boolean enforceRange,
                      boolean logarithmic) {
        this(flowSliderStyle, valueColorFunction, min, max, enforceRange, logarithmic, false, min);
    }

    /**
     * Creates a new slider.
     * @param skin skin to get slider the style from.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param enforceRange true if value will be clamped to the minimum and maximum values shown.
     * @param logarithmic true if the slider should use a logarithmic scale instead of a linear scale.
     * @param useOrigin true if the slider should have a origin at the middle, instead of at the left end.
     * @param origin value at origin.  Must be between min and max shown values.
     */
    public FlowSlider(Skin skin,
                      ColorFunction valueColorFunction,
                      double min,
                      double max,
                      boolean enforceRange,
                      boolean logarithmic,
                      boolean useOrigin,
                      double origin) {
        this(getStyleFromSkin(skin), valueColorFunction, min, max, enforceRange, logarithmic, useOrigin, origin);
    }

    /**
     * Creates a new slider.
     * @param flowSliderStyle UI style for the slider.
     * @param valueColorFunction function used to color the value bar.  If null, the default color will be used.  -1 = min value, 0 = origin (if used), 1 = max value.
     * @param min minimum value shown.
     * @param max maximum value shown.
     * @param enforceRange true if value will be clamped to the minimum and maximum values shown.
     * @param logarithmic true if the slider should use a logarithmic scale instead of a linear scale.
     * @param useOrigin true if the slider should have a origin at the middle, instead of at the left end.
     * @param origin value at origin.  Must be between min and max shown values.
     */
    public FlowSlider(FlowSliderStyle flowSliderStyle,
                      ColorFunction valueColorFunction,
                      double min,
                      double max,
                      boolean enforceRange,
                      boolean logarithmic,
                      boolean useOrigin,
                      double origin) {
        this.min = min;
        this.max = max;
        this.enforceRange = enforceRange;
        this.logarithmic = logarithmic;
        this.useOrigin = useOrigin;
        this.origin = origin;

        Check.notNull(flowSliderStyle, "flowSliderStyle");
        this.flowSliderStyle = flowSliderStyle;

        setValueColorFunction(valueColorFunction);

        // Check consistency
        Check.greaterOrEqual(max, "max", min, "min");
        if (useOrigin) {
            Check.greaterOrEqual(origin, "origin", min, "min");
            Check.greaterOrEqual(max, "max", origin, "origin");
        }

        // Initialize value
        if (useOrigin) {
            value = origin;
        }
        else {
            value = min;
        }

        // Listen to input
        addListener(inputListener);
    }


    /**
     * @return current value in the slider.
     */
    public double getValue() {
        return value;
    }

    /**
     * Set the value of the slider.
     */
    public void setValue(double value) {
        // Don't update value if it is a result of a listener notified about a value change in this slider.
        if (!notifyingListenersAboutValueChange) {

            // Clamp to range if necessary
            if (enforceRange) {
                value = MathUtils.clamp(value, min, max);
            }

            this.value = value;
        }
    }

    @Override public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public ColorFunction getValueColorFunction() {
        return valueColorFunction;
    }

    public void setValueColorFunction(ColorFunction valueColorFunction) {
        this.valueColorFunction = valueColorFunction;
    }

    public void setStyle(FlowSliderStyle style) {
        Check.notNull(style, "style");

        flowSliderStyle = style;
    }

    public FlowSliderStyle getStyle() {
        return flowSliderStyle;
    }

    @Override public float getPrefWidth() {
        if (prefWidth > 0) {
            return prefWidth;
        } else {
            return Math.max(DEFAULT_PREFERRED_WIDTH, flowSliderStyle.background.getMinWidth());
        }
    }

    @Override public float getPrefHeight() {
        if (prefHeight > 0) {
            return prefHeight;
        } else {
            return Math.max(24, flowSliderStyle.background.getMinHeight());
        }
    }

    public void setPrefWidth(float prefWidth) {
        this.prefWidth = prefWidth;
    }

    public void setPrefHeight(float prefHeight) {
        this.prefHeight = prefHeight;
    }

    public void addListener(FlowSliderListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FlowSliderListener listener) {
        listeners.removeValue(listener, true);
    }

    /**
     * @return true if the actual value should be passed to the color function to calculate the slider bar color,
     *         false if a relative value where -1 = min (0 = origin if in use) and 1 = max should be passed to the color function.
     *         Defaults to false.
     */
    public boolean isUseActualValueForColorFunction() {
        return useActualValueForColorFunction;
    }

    /**
     * @param useActualValueForColorFunction true if the actual value should be passed to the color function to calculate the slider bar color,
     *                                       false if a relative value where -1 = min (0 = origin if in use) and 1 = max should be passed to the color function.
     */
    public void setUseActualValueForColorFunction(boolean useActualValueForColorFunction) {
        this.useActualValueForColorFunction = useActualValueForColorFunction;
    }

    private static FlowSliderStyle getStyleFromSkin(Skin skin) {
        return skin.get("default", FlowSliderStyle.class);
    }


    private void uiValueChanged(float x, float y) {
        final float width = getWidth();
        final float handleWidth = flowSliderStyle.getHandle().getMinWidth();
        final float leftWidth = flowSliderStyle.getBackground().getLeftWidth();
        final float rightWidth = flowSliderStyle.getBackground().getRightWidth();
        double relativePosition = MathUtils.mapAndClamp(x, leftWidth + handleWidth/2, width - rightWidth - handleWidth/2, 0, 1);
        double value = getValueAtRelativePosition(relativePosition);

        valueChangedInUi(value);
    }

    private void changeValue(int scrollAmount) {
        if (scrollAmount != 0) {
            double value = getValueAtRelativePosition(getRelativePosition(this.value) + scrollAmount * SCROLL_STEP);
            valueChangedInUi(value);
        }
    }

    private void valueChangedInUi(double value) {
        if (!disabled) {
            // Clamp to range if necessary
            if (enforceRange) {
                value = MathUtils.clamp(value, min, max);
            }

            // Update value if it changed
            if (value != this.value) {
                this.value = value;
                notifyValueChanged();
            }
        }
    }

    /**
     * @param relativePosition 0..1, where 0 is leftmost on the slider and 1 is rightmost.
     * @return the value at the specified relative location on the slider.
     */
    private double getValueAtRelativePosition(double relativePosition) {
        if (logarithmic) {
            return expWithSign(mixWithOrigin(relativePosition,
                                             logWithSign(min),
                                             logWithSign(max),
                                             useOrigin,
                                             logWithSign(origin)));
        }
        else {
            return mixWithOrigin(relativePosition, min, max, useOrigin, origin);
        }
    }

    /**
     * @return 0..1 depending on where on the slider the specified value falls. (Or outside that range if the value is outside the min,max bounds).
     */
    private double getRelativePosition(double value) {
        if (logarithmic) {
            return unMixWithOrigin(logWithSign(value),
                                   logWithSign(min),
                                   logWithSign(max),
                                   useOrigin,
                                   logWithSign(origin));
        }
        else {
            return unMixWithOrigin(value, min, max, useOrigin, origin);
        }
    }

    private double mixWithOrigin(double relativePosition,
                                 double minValue,
                                 double maxValue,
                                 final boolean useOrigin,
                                 double originValue) {
        if (useOrigin) {
            if (relativePosition >= ORIGIN_RELATIVE_POSITION) {
                return  MathUtils.map(relativePosition, ORIGIN_RELATIVE_POSITION, 1.0, originValue, maxValue);
            }
            else {
                return MathUtils.map(relativePosition, 0.0, ORIGIN_RELATIVE_POSITION, minValue, originValue);
            }
        }
        else {
            return MathUtils.map(relativePosition, 0.0, 1.0, minValue, maxValue);
        }
    }

    private double unMixWithOrigin(double value,
                                   double minValue,
                                   double maxValue,
                                   final boolean useOrigin,
                                   double originValue) {
        if (useOrigin) {
            if (value >= originValue) {
                return  MathUtils.map(value, originValue, maxValue, ORIGIN_RELATIVE_POSITION, 1.0);
            }
            else {
                return MathUtils.map(value, minValue, originValue, 0.0, ORIGIN_RELATIVE_POSITION);
            }
        }
        else {
            return MathUtils.map(value, minValue, maxValue, 0.0, 1.0);
        }
    }

    private double logWithSign(final double v) {
        if (v > 0) return Math.log(v+1);
        else if (v < 0) return -Math.log(-v+1);
        else return 0;
    }

    private double expWithSign(final double v) {
        if (v > 0) return Math.exp(v)-1;
        else if (v < 0) return -Math.exp(-v)+1;
        else return 0;
    }


    @Override public void draw(Batch batch, float parentAlpha) {
        // Make sure the component is laid out
        super.draw(batch, parentAlpha);

        // Determine style and color to use
        FlowSliderStyle style = this.flowSliderStyle;
        boolean disabled = this.disabled;
        getValueColor(valueBarColor);
        Color overallColor = getColor();

        // Determine drawables to use
        final Drawable handle     = style.getHandle(disabled);
        final Drawable background = style.getBackground(disabled);
        final Drawable rightValue = style.getValue(disabled);
        final Drawable leftValue  = style.getValueUnderOrigin(disabled);

        // Calculate positions
        final double valueRelativePosition = getRelativePosition(value);
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        final float areaXStart = x + background.getLeftWidth();
        final float areaXEnd = x + width - background.getRightWidth();
        float innerHeight = height - background.getBottomHeight() - background.getTopHeight();
        float handleHeight = height;
        float handleWidth = handle.getMinWidth();
        float trackLength = width - background.getLeftWidth() - background.getRightWidth() - handleWidth;
        float handleX = areaXStart + (float)MathUtils.clamp0To1(valueRelativePosition) * trackLength;
        float handleXCenter = handleX + handleWidth / 2;
        float handleY = y + background.getBottomHeight();

        float relativeOrigin = useOrigin ? (float)ORIGIN_RELATIVE_POSITION : 0f;
        float originX = areaXStart + relativeOrigin * trackLength;

        final boolean drawHandle = valueRelativePosition >= 0 - EPSILON && valueRelativePosition <= 1 + EPSILON;
        final boolean drawLeftBar =  value < max && (useOrigin && value < origin);
        final boolean drawRightBar = value > min && (!useOrigin || value > origin);
        float barY = handleY;
        float barX = Math.min(originX, handleXCenter) + (!drawHandle && drawLeftBar ? -handleWidth/2 : 0);
        float barWidth = Math.max(originX, handleXCenter) - barX + (!drawHandle & drawRightBar ? handleWidth/2 : 0);
        float barHeight = innerHeight;

        // Draw background
        Color oldColor = setBatchColor(batch, overallColor, parentAlpha);
        background.draw(batch, x, y, width, height);

        // Draw value bar
        setBatchColor(batch, overallColor, valueBarColor, parentAlpha);
        if (drawLeftBar) leftValue.draw(batch, barX, barY, barWidth, barHeight);
        if (drawRightBar) rightValue.draw(batch, barX, barY, barWidth, barHeight);
        setBatchColor(batch, overallColor, parentAlpha);

        // Draw handle if the value is within the slider visible range
        if (drawHandle) handle.draw(batch, handleX, handleY, handleWidth, handleHeight);

        // Restore color
        batch.setColor(oldColor);
    }

    /**
     * Calculates and returns the current color of the value bar, based on the current value.
     * @param colorOut color to return the value in.
     * @return colorOut, or a new color with the result if colorOut was null.
     */
    public Color getValueColor(Color colorOut) {
        if (colorOut == null) colorOut = new Color();

        if (valueColorFunction != null) {
            valueColorFunction.getColor(calculateColorLookupValue(), colorOut);
        } else {
            if (flowSliderStyle != null && flowSliderStyle.defaultValueColor != null) {
                colorOut.set(flowSliderStyle.defaultValueColor);
            } else {
                colorOut.set(Color.GRAY);
            }
        }

        return colorOut;
    }

    private double calculateColorLookupValue() {
        if (useActualValueForColorFunction) {
            return value;
        }
        else {
            double relativeValue = getRelativePosition(value);

            double colorLookupValue;
            if (useOrigin) {
                if (relativeValue < ORIGIN_RELATIVE_POSITION) colorLookupValue = MathUtils.map(relativeValue, 0, ORIGIN_RELATIVE_POSITION, -1, 0);
                else colorLookupValue = MathUtils.map(relativeValue, ORIGIN_RELATIVE_POSITION, 1, 0, 1);
            }
            else {
                colorLookupValue = MathUtils.map(relativeValue, 0, 1, -1, 1);
            }
            return colorLookupValue;
        }
    }

    private Color setBatchColor(Batch batch, Color color, float parentAlpha) {
        return setBatchColor(batch, color, Color.WHITE, parentAlpha);
    }

    private Color setBatchColor(Batch batch, Color color1, Color color2, float parentAlpha) {
        Color oldColor = batch.getColor();
        tempColor.set(color1);
        tempColor.mul(color2);
        tempColor.a *= parentAlpha;
        batch.setColor(tempColor);
        return oldColor;
    }

    private void notifyValueChanged() {
        notifyingListenersAboutValueChange = true;

        for (FlowSliderListener listener : listeners) {
            listener.onChanged(value);
        }

        notifyingListenersAboutValueChange = false;
    }

    /**
     * Describes the appearance of the slider.
     */
    public static class FlowSliderStyle {

        private Drawable background;
        private Drawable handle;
        private Drawable valueUnderOrigin;
        private Drawable value;

        private Drawable disabledBackground;
        private Drawable disabledHandle;
        private Drawable disabledValueUnderOrigin;
        private Drawable disabledValue;

        private Color defaultValueColor;

        public FlowSliderStyle() {
        }

        public FlowSliderStyle(Drawable background,
                               Drawable handle,
                               Drawable value) {
            this(background, handle, value, value);
        }

        public FlowSliderStyle(Drawable background,
                               Drawable handle,
                               Drawable valueUnderOrigin,
                               Drawable value) {
            this(background, handle, valueUnderOrigin, value, background, handle, valueUnderOrigin, value);
        }

        public FlowSliderStyle(Drawable background,
                               Drawable handle,
                               Drawable valueUnderOrigin,
                               Drawable value,
                               Drawable disabledBackground,
                               Drawable disabledHandle,
                               Drawable disabledValueUnderOrigin,
                               Drawable disabledValue) {
            this.background = background;
            this.handle = handle;
            this.valueUnderOrigin = valueUnderOrigin;
            this.value = value;
            this.disabledBackground = disabledBackground;
            this.disabledHandle = disabledHandle;
            this.disabledValueUnderOrigin = disabledValueUnderOrigin;
            this.disabledValue = disabledValue;
        }

        public Drawable getBackground() {
            return background;
        }

        public void setBackground(Drawable background) {
            this.background = background;
        }

        public Drawable getHandle() {
            return handle;
        }

        public void setHandle(Drawable handle) {
            this.handle = handle;
        }

        public Drawable getValueUnderOrigin() {
            return valueUnderOrigin;
        }

        public void setValueUnderOrigin(Drawable valueUnderOrigin) {
            this.valueUnderOrigin = valueUnderOrigin;
        }

        public Drawable getValue() {
            return value;
        }

        public void setValue(Drawable value) {
            this.value = value;
        }

        public Drawable getDisabledBackground() {
            return disabledBackground;
        }

        public void setDisabledBackground(Drawable disabledBackground) {
            this.disabledBackground = disabledBackground;
        }

        public Drawable getDisabledHandle() {
            return disabledHandle;
        }

        public void setDisabledHandle(Drawable disabledHandle) {
            this.disabledHandle = disabledHandle;
        }

        public Drawable getDisabledValueUnderOrigin() {
            return disabledValueUnderOrigin;
        }

        public void setDisabledValueUnderOrigin(Drawable disabledValueUnderOrigin) {
            this.disabledValueUnderOrigin = disabledValueUnderOrigin;
        }

        public Drawable getDisabledValue() {
            return disabledValue;
        }

        public void setDisabledValue(Drawable disabledValue) {
            this.disabledValue = disabledValue;
        }

        public Color getDefaultValueColor() {
            return defaultValueColor;
        }

        public void setDefaultValueColor(Color defaultValueColor) {
            this.defaultValueColor = defaultValueColor;
        }

        public Drawable getBackground(boolean disabled) {
            if (disabled && disabledBackground != null) return disabledBackground;
            else return background;
        }

        public Drawable getHandle(boolean disabled) {
            if (disabled && disabledHandle != null) return disabledHandle;
            else return handle;
        }

        public Drawable getValue(boolean disabled) {
            if (disabled && disabledValue != null) {
                return disabledValue;
            }
            else {
                return value;
            }
        }

        public Drawable getValueUnderOrigin(boolean disabled) {
            if (disabled && disabledValueUnderOrigin != null) {
                return disabledValueUnderOrigin;
            }
            else if (valueUnderOrigin != null) {
                return valueUnderOrigin;
            } else {
                return getValue(disabled);
            }
        }

    }

    public static interface FlowSliderListener {
        /**
         * Called when the slider is moved.
         */
        void onChanged(double newValue);
    }
}
