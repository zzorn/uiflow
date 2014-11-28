package org.uiflow.propertyeditor.ui.editors.bean;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * An ImageButton that actually honors and uses its color attribute.
 */
public class ConnectorButton extends ImageButton {

    private float offsetX;
    private float offsetY;

    private float prefW = -1;
    private float prefH = -1;

    private final PropertyUi propertyUi;
    private final boolean input;


    public ConnectorButton(Drawable image, PropertyUi propertyUi, boolean input) {
        this(image, propertyUi, input, 0, 0);
    }

    public ConnectorButton(Drawable image, PropertyUi propertyUi, boolean input, float offsetX, float offsetY) {
        super(image);
        this.input = input;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.propertyUi = propertyUi;
    }


    public void setOffset(float x, float y) {
        offsetX = x;
        offsetY = y;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public PropertyUi getPropertyUi() {
        return propertyUi;
    }

    public boolean isInput() {
        return input;
    }

    public void setPrefSize(float width, float height) {
        prefW = width;
        prefH = height;
        invalidate();
    }

    public void setPrefWidth(float prefW) {
        this.prefW = prefW;
        invalidate();
    }

    public void setPrefHeight(float prefH) {
        this.prefH = prefH;
        invalidate();
    }

    @Override public void draw(Batch batch, float parentAlpha) {
        final Color color = getColor();
        getImage().setColor(color);

        moveBy(offsetX, offsetY);

        super.draw(batch, parentAlpha);
    }

    @Override public float getPrefWidth() {
        return prefW < 0 ? super.getPrefWidth() : prefW;
    }

    @Override public float getPrefHeight() {
        return prefH < 0 ? super.getPrefHeight() : prefH;
    }
}
