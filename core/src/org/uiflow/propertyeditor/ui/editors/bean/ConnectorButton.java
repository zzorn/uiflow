package org.uiflow.propertyeditor.ui.editors.bean;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import org.uiflow.propertyeditor.ui.editors.beangraph.Connection;

/**
 * An ImageButton that actually honors and uses its color attribute.
 */
public class ConnectorButton extends ImageButton {

    private float offsetX;
    private float offsetY;

    private float prefW = -1;
    private float prefH = -1;

    private final PropertyUi propertyUi;
    private final Drawable connectedImage;
    private final Drawable unconnectedImage;
    private final boolean input;
    private final Array<Connection> connections = new Array<Connection>();


    public ConnectorButton(Drawable connectedImage, Drawable unconnectedImage, PropertyUi propertyUi, boolean input) {
        this(connectedImage, unconnectedImage, propertyUi, input, 0, 0);
    }

    public ConnectorButton(Drawable connectedImage, Drawable unconnectedImage, PropertyUi propertyUi, boolean input, float offsetX, float offsetY) {
        super(unconnectedImage);
        this.connectedImage = connectedImage;
        this.unconnectedImage = unconnectedImage;
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

    public void addConnection(Connection connection) {
        connections.add(connection);
        updateConnectorAppearance();
    }

    public void removeConnection(Connection connection) {
        connections.removeValue(connection, true);
        updateConnectorAppearance();
    }

    public boolean isConnected() {
        return connections.size > 0;
    }

    private void updateConnectorAppearance() {
        getStyle().imageUp = isConnected() ? connectedImage : unconnectedImage;
    }
}
