package org.uiflow.propertyeditor.ui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

/**
 * Improved slider component
 */
// TODO: Create custom component that:
// TODO: * Has a drawable or color for positive selected area, negative selected area, positive unselected area, negative unselected area, and position indicator
// TODO:   * Should support updating the drawables depending on the current value, or just in update call
// TODO:   * Should support color functions to calculate the color for the drawables depending on the current value (and preferred default color)
// TODO: * Should cover whole widget area (or drawable area), and have a flexible width, and configurable / flexible height
// TODO: * Should support a logarithmic scale with arbitrary exponent and centered value and min/max visible values
// TODO: * Should support mouse wheel adjustment
// TODO: * Should support listening to changes in value

public class FlowSlider extends Slider {

    private double min;
    private double max;
    private double origin;
    private double value;

    private boolean disabled = false;

    private FlowSliderStyle flowSliderStyle;

    private boolean vertical;

    private double visualKnobPosition;

    private float prefWidth;
    private float prefHeight;

    private boolean notifyingAboutValueChange = false;

    private final Array<FlowSliderListener> listeners = new Array<FlowSliderListener>(3);

    private final InputListener inputListener = new InputListener() {
        @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            notifyValueChanged();
            return false;
        }

        @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            notifyValueChanged();
        }

        @Override public void touchDragged(InputEvent event, float x, float y, int pointer) {
            notifyValueChanged();
        }

        @Override public boolean mouseMoved(InputEvent event, float x, float y) {
            if (isDragging()) notifyValueChanged();
            return false;
        }

        @Override public boolean scrolled(InputEvent event, float x, float y, int amount) {
            // TODO: Scroll value
            notifyValueChanged();
            return false;
        }
    };

    public FlowSlider(double min,
                      double max,
                      double stepSize,
                      boolean vertical,
                      FlowSliderStyle flowSliderStyle) {

        this(min, max, stepSize, vertical, min, min, flowSliderStyle);
    }

    public FlowSlider(double min,
                      double max,
                      double stepSize,
                      boolean vertical,
                      double origin,
                      double initialValue,
                      Skin skin) {
        this(min, max, stepSize, vertical, origin, initialValue,
             skin.get("default-" + (vertical ? "vertical" : "horizontal"), FlowSliderStyle.class));
    }

    public FlowSlider(double min,
                      double max,
                      double stepSize,
                      boolean vertical,
                      double origin,
                      double initialValue,
                      FlowSliderStyle flowSliderStyle) {

        super((float) min, (float) max, (float) stepSize, vertical, flowSliderStyle);

        this.min = min;
        this.origin = origin;
        this.max = max;
        this.value = initialValue;
        this.flowSliderStyle = flowSliderStyle;
        this.vertical = vertical;

        //addListener(inputListener);
        setValue((float) initialValue);
    }


    @Override public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        FlowSliderStyle style = this.flowSliderStyle;
        boolean disabled = this.disabled;
        final Drawable knob = (disabled && style.disabledKnob != null) ? style.disabledKnob : style.knob;
        final Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
        final Drawable knobBefore = (disabled && style.disabledKnobBefore != null) ? style.disabledKnobBefore : style.knobBefore;
        final Drawable knobAfter = (disabled && style.disabledKnobAfter != null) ? style.disabledKnobAfter : style.knobAfter;

        Color color = getColor();
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        float knobHeight = knob == null ? 0 : height;
        float knobWidth = knob == null ? 0 : knob.getMinWidth();
        float value = getVisualValue();

        float bgHeight = height;

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if (vertical) {
            bg.draw(batch, x + (int)((width - bg.getMinWidth()) * 0.5f), y, bg.getMinWidth(), height);

            float positionHeight = height - (bg.getTopHeight() + bg.getBottomHeight());
            float knobHeightHalf = 0;
            if (min != max) {
                if (knob == null) {
                    knobHeightHalf = knobBefore == null ? 0 : knobBefore.getMinHeight() * 0.5f;
                    visualKnobPosition = (value - min) / (max - min) * (positionHeight - knobHeightHalf);
                    visualKnobPosition = Math.min(positionHeight - knobHeightHalf, visualKnobPosition);
                } else {
                    knobHeightHalf = knobHeight * 0.5f;
                    visualKnobPosition = (value - min) / (max - min) * (positionHeight - knobHeight);
                    visualKnobPosition = Math.min(positionHeight - knobHeight, visualKnobPosition) + bg.getBottomHeight();
                }
                visualKnobPosition = Math.max(0, visualKnobPosition);
            }

            if (knobBefore != null) {
                float offset = 0;
                if (bg != null) offset = bg.getTopHeight();
                knobBefore.draw(batch, x + (int)((width - knobBefore.getMinWidth()) * 0.5f), y + offset, knobBefore.getMinWidth(),
                                (int)(visualKnobPosition + knobHeightHalf));
            }
            if (knobAfter != null) {
                knobAfter.draw(batch, x + (int)((width - knobAfter.getMinWidth()) * 0.5f), y + (int)(visualKnobPosition + knobHeightHalf),
                               knobAfter.getMinWidth(), height - (int)(visualKnobPosition + knobHeightHalf));
            }
            if (knob != null) knob.draw(batch, x + (int)((width - knobWidth) * 0.5f), (int)(y + visualKnobPosition), knobWidth, knobHeight);
        } else {
            bg.draw(batch, x, y + (int)((height - bgHeight) * 0.5f), width, bgHeight);

            float positionWidth = width - (bg.getLeftWidth() + bg.getRightWidth());
            float knobWidthHalf = 0;
            if (min != max) {
                if (knob == null) {
                    knobWidthHalf = knobBefore == null ? 0 : knobBefore.getMinWidth() * 0.5f;
                    visualKnobPosition = (value - min) / (max - min) * (positionWidth - knobWidthHalf);
                    visualKnobPosition = Math.min(positionWidth - knobWidthHalf, visualKnobPosition);
                } else {
                    knobWidthHalf = knobWidth * 0.5f;
                    visualKnobPosition = (value - min) / (max - min) * (positionWidth - knobWidth);
                    visualKnobPosition = Math.min(positionWidth - knobWidth, visualKnobPosition) + bg.getLeftWidth();
                }
                visualKnobPosition = Math.max(0, visualKnobPosition);
            }

            if (knobBefore != null) {
                float offset = 0;
                if (bg != null) offset = bg.getLeftWidth();
                knobBefore.draw(batch, x + offset, y + (int)((height - knobBefore.getMinHeight()) * 0.5f),
                                (int)(visualKnobPosition + knobWidthHalf), knobBefore.getMinHeight());
            }
            if (knobAfter != null) {
                knobAfter.draw(batch, x + (int)(visualKnobPosition + knobWidthHalf), y + (int)((height - knobAfter.getMinHeight()) * 0.5f),
                               width - (int)(visualKnobPosition + knobWidthHalf), knobAfter.getMinHeight());
            }
            if (knob != null) knob.draw(batch, (int)(x + visualKnobPosition), (int)(y + (height - knobHeight) * 0.5f), knobWidth, knobHeight);
        }

    }

    public void setStyle(FlowSliderStyle style) {
        super.setStyle(style);
        flowSliderStyle = style;
    }

    @Override public FlowSliderStyle getStyle() {
        return flowSliderStyle;
    }

    @Override public float getPrefWidth() {
        if (prefWidth > 0) {
            return prefWidth;
        } else {
            return super.getPrefWidth();
        }
    }

    @Override public float getPrefHeight() {
        if (prefHeight > 0) {
            return prefHeight;
        } else {
            return super.getPrefHeight();
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

    @Override public float getValue() {
        return super.getValue();
    }

    @Override public boolean setValue(float value) {
        if (!notifyingAboutValueChange) {
            final boolean wasChanged = super.setValue(value);
            if (wasChanged) notifyValueChanged();
            return wasChanged;
        }
        else {
            return false;
        }
    }

    private void notifyValueChanged() {
        notifyingAboutValueChange = true;
        final float value = getValue();
        for (FlowSliderListener listener : listeners) {
            listener.onChanged(value);
        }
        notifyingAboutValueChange = false;
    }

    public static class FlowSliderStyle extends SliderStyle {

        public FlowSliderStyle() {
        }

        public FlowSliderStyle(Drawable background, Drawable knob) {
            super(background, knob);
        }

        public FlowSliderStyle(SliderStyle style) {
            super(style);
        }

        private Drawable uncoveredLeft;
        private Drawable uncoveredRight;


    }

    public static interface FlowSliderListener {
        /**
         * Called when the slider is moved.
         */
        void onChanged(double newValue);
    }
}
