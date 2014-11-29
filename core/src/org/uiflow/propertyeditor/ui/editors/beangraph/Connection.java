package org.uiflow.propertyeditor.ui.editors.beangraph;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.Property;
import org.uiflow.propertyeditor.ui.editors.bean.PropertyUi;
import org.uiflow.propertyeditor.ui.editors.bean.ConnectorButton;
import org.uiflow.utils.Check;
import org.uiflow.utils.MathUtils;

/**
 *
 */
public class Connection extends Actor {

    private static final int MAX_SEGMENT_COUNT = 200;
    private static final int MIN_SEGMENT_COUNT = 20;
    private static final String SEGMENT_NAME = "connection_segment_soft";
    private static final String SEGMENT_NAME_UNCONNECTED = "connection_segment_soft_unconnected";
    private static final float DEFAULT_SEGMENT_SCALE = 0.8f;
    private static final float SEGMENT_DENSITY = 4f;
    private static final int BORDER_FUDGE_FACTOR = 8;

    private final TextureRegion unconnectedSegmentImage;
    private final TextureRegion connectedSegmentImage;
    private final Drawable outputConnectorImage;
    private final Drawable inputConnectorImage;
    private final Drawable unconnectedConnectorImage;

    private PropertyUi source;
    private PropertyUi target;

    private Vector2 start = new Vector2();
    private Vector2 end = new Vector2();
    private Vector2 pos = new Vector2();
    private Vector2 oldPos = new Vector2();
    private Vector2 t = new Vector2();
    private Color startColor = new Color();
    private Color endColor = new Color();
    private Color color = new Color();

    private final UiContext uiContext;
    private float segmentScale = DEFAULT_SEGMENT_SCALE;

    private ConnectionHighlight highlightState = ConnectionHighlight.DRAGGED;

    public Connection(UiContext uiContext) {
        this(uiContext, null, null);
    }

    public Connection(UiContext uiContext, PropertyUi source, PropertyUi target) {
        Check.notNull(uiContext, "uiContext");

        this.uiContext = uiContext;
        this.connectedSegmentImage = uiContext.getSkin().getRegion(SEGMENT_NAME);
        this.unconnectedSegmentImage = uiContext.getSkin().getRegion(SEGMENT_NAME_UNCONNECTED);

        this.inputConnectorImage = uiContext.getSkin().getDrawable("connector_in");
        this.outputConnectorImage = uiContext.getSkin().getDrawable("connector_out");
        this.unconnectedConnectorImage= uiContext.getSkin().getDrawable("connector_unconnected");

        setSource(source);
        setTarget(target);

        updateHighlightState();
    }

    public PropertyUi getSource() {
        return source;
    }

    public Property getSourceProperty() {
        if (source != null) {
            return source.getProperty();
        } else {
            return null;
        }
    }

    public Property getTargetProperty() {
        if (target != null) {
            return target.getProperty();
        } else {
            return null;
        }
    }

    public void setSource(PropertyUi source) {
        if (this.source != null) this.source.getOutputConnector().removeConnection(this);

        this.source = source;

        if (this.source != null) this.source.getOutputConnector().addConnection(this);

        updateHighlightState();
    }

    public PropertyUi getTarget() {
        return target;
    }

    public void setTarget(PropertyUi target) {
        if (this.target != null) this.target.getInputConnector().removeConnection(this);

        this.target = target;

        if (this.target != null) this.target.getInputConnector().addConnection(this);

        updateHighlightState();
    }

    public void setStartPos(float x, float y) {
        start.set(x - BORDER_FUDGE_FACTOR,
                  y - BORDER_FUDGE_FACTOR);
    }

    public void setEndPos(float x, float y) {
        end.set(x - BORDER_FUDGE_FACTOR,
                y - BORDER_FUDGE_FACTOR);
    }

    public ConnectionHighlight getHighlightState() {
        return highlightState;
    }

    public void setHighlightState(ConnectionHighlight highlightState) {
        this.highlightState = highlightState;
    }

    private void updateStartPos() {
        if (source != null) {
            getActorPos(source.getOutputConnector(), start);
        }
    }

    private void updateEndPos() {
        if (target != null) {
            getActorPos(target.getInputConnector(), end);
        }
    }

    @Override public void draw(Batch batch, float parentAlpha) {
        // Determine start and end positions
        updateStartPos();
        updateEndPos();

        final TextureRegion segmentImage = highlightState.getStrokeImage(getSegmentImage(),
                                                                         connectedSegmentImage,
                                                                         unconnectedSegmentImage);
        float segmentLength = getSegmentImage().getRegionWidth() * (1f / SEGMENT_DENSITY);

        // Determine connection color based on type
        Color sourcePropertyColor = getPropertyColor(source);
        Color targetPropertyColor = getPropertyColor(target);
        if (sourcePropertyColor == null) sourcePropertyColor = targetPropertyColor;
        if (targetPropertyColor == null) targetPropertyColor = sourcePropertyColor;
        startColor.set(sourcePropertyColor);
        endColor.set(targetPropertyColor);
        highlightState.updateColor(startColor);
        highlightState.updateColor(endColor);

        final Color oldColor = batch.getColor();

        final float segmentW = segmentImage.getRegionWidth() * segmentScale;
        final float segmentH = segmentImage.getRegionHeight() * segmentScale;

        // Determine number of segments to use
        final float distance = distance(start, end);
        int segmentCount = MathUtils.clamp((int)(distance / segmentLength), MIN_SEGMENT_COUNT, MAX_SEGMENT_COUNT);

        if (end.x < start.x) {
            segmentCount *= 2;
        }

        // Draw segments
        oldPos.set(start);
        for (int i = 0; i < segmentCount; i++) {
            float relPos = (float) i / (segmentCount - 1);

            // Calculate color
            color.set(startColor);
            color.lerp(endColor, relPos);
            color.a *= parentAlpha;
            batch.setColor(color);

            // Calculate dot position
            float swing = MathUtils.clamp(distance + (start.x - end.x), segmentLength * 2, segmentLength * 10);
            pos.x =  interpolate(relPos, start.x, end.x, Interpolation.sine) + swing *(float) Math.sin(relPos * Math.PI*2);
            pos.y = interpolate(relPos, start.y, end.y, Interpolation.fade);

            // Calculate angle
            t.set(pos).sub(oldPos);
            float angle = t.angle();
            oldPos.set(pos);

            // Draw dot
            batch.draw(segmentImage,
                       pos.x - segmentW * 0.5f,
                       pos.y - segmentH * 0.5f,
                       segmentW*0.5f,
                       segmentH*0.5f,
                       segmentW, segmentH,
                       1f, 1f,
                       angle);
        }

        // Draw start and end connectors
        drawEndpoint(batch, startColor, start, true, source != null);
        drawEndpoint(batch, endColor, end, false, target != null);

        batch.setColor(oldColor);
    }

    private Color getPropertyColor(final PropertyUi propertyUi) {
        if (propertyUi != null && propertyUi.getProperty() != null && propertyUi.getProperty().getType() != null) {
            return uiContext.getTypeColor(propertyUi.getProperty().getType());
        }
        else {
            return null;
        }
    }

    private void drawEndpoint(Batch batch, Color color, final Vector2 pos, boolean isOutput, boolean isConnected) {
        batch.setColor(color);

        Drawable endPointImage;
        if (!isConnected && highlightState != ConnectionHighlight.ACCEPTABLE) {
            endPointImage = unconnectedConnectorImage;
        }
        else {
            endPointImage = isOutput ? outputConnectorImage : inputConnectorImage;
        }

        final float w = endPointImage.getMinWidth();
        final float h = endPointImage.getMinHeight();
        endPointImage.draw(batch,
                           pos.x - w*0.5f,
                           pos.y - h*0.5f,
                           w,
                           h);
    }

    private float interpolate(final float relPos,
                              final float start,
                              final float end,
                              final Interpolation interpolation) {
        return MathUtils.mix(interpolation.apply(0, 1, relPos),
                             start,
                             end);
    }

    private float distance(final Vector2 a, final Vector2 b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        return (float) Math.sqrt(dx*dx + dy*dy);
    }

    private void getActorPos(final ConnectorButton connectorButton, final Vector2 posOut) {
        posOut.set(0, 0);

        if (connectorButton != null) {
            // Fudge
            final float offsetX = connectorButton.getOffsetX();
            final float offsetY = connectorButton.getOffsetY();
            posOut.set(connectorButton.getWidth()  * 0.5f + offsetX - BORDER_FUDGE_FACTOR,
                       connectorButton.getHeight() * 0.5f + offsetY - BORDER_FUDGE_FACTOR);

            connectorButton.localToStageCoordinates(posOut);
        }
    }


    /**
     * @return true if this connection would be ok to make.
     */
    public boolean isAcceptable() {
        return source != null &&
               target != null &&
               getSourceProperty() != null &&
               getTargetProperty() != null &&
               getTargetProperty().canUseSource(getSourceProperty());
    }

    /**
     * Adds the actual connection between the properties.
     */
    public void createConnection() {
        final Property targetProperty = getTargetProperty();
        if (targetProperty != null) {
            targetProperty.setSource(getSourceProperty());
        }
    }

    public boolean containsPropertyUi(PropertyUi propertyUi) {
        return propertyUi != null &&
               (source == propertyUi ||
                target == propertyUi);
    }

    private TextureRegion getSegmentImage() {
        if (source != null && target != null) {
            return connectedSegmentImage;
        }
        else {
            return unconnectedSegmentImage;
        }
    }

    public boolean canConnectTo(PropertyUi propertyUi, boolean providedUiIsTarget) {
        if (source != null && target != null) return false;
        if (source == null && target == null) return false;
        if (propertyUi == null || propertyUi.getProperty() == null) return false;

        if (providedUiIsTarget) {
            return source != null && propertyUi.getProperty().canUseSource(source.getProperty());
        }
        else {
            return target != null && target.getProperty().canUseSource(propertyUi.getProperty());
        }
    }

    public void connectTo(PropertyUi propertyUi, boolean providedUiIsTarget) {
        if (canConnectTo(propertyUi, providedUiIsTarget)) {
            if (providedUiIsTarget) {
                setTarget(propertyUi);
            }
            else {
                setSource(propertyUi);
            }

            createConnection();
            updateHighlightState();
        }
        else {
            throw new IllegalArgumentException("Can not connect to the specified property ui");
        }
    }

    private void updateHighlightState() {
        if (source != null && target != null) {
            highlightState = ConnectionHighlight.CONNECTED;
        }
        else {
            highlightState = ConnectionHighlight.DRAGGED;
        }
    }

    public void dispose() {
        setSource(null);
        setTarget(null);
    }

    public static enum ConnectionHighlight {
        CONNECTED,
        DRAGGED,
        ACCEPTABLE,
        INVALID
        ;

        Color updateColor(Color originalColor) {
            switch (this) {
                case CONNECTED:
                    return originalColor;
                case DRAGGED:
                    return originalColor.lerp(Color.BLACK, 0.25f);
                case ACCEPTABLE:
                    return originalColor;
                case INVALID:
                    return originalColor.lerp(Color.BLACK, 0.5f);
                default:
                    throw new IllegalArgumentException("Unknown case");
            }
        }

        TextureRegion getStrokeImage(TextureRegion normal, TextureRegion connected, TextureRegion unconnected) {
            switch (this) {
                case CONNECTED:
                    return normal;
                case DRAGGED:
                    return unconnected;
                case ACCEPTABLE:
                    return connected;
                case INVALID:
                    return unconnected;
                default:
                    throw new IllegalArgumentException("Unknown case");
            }
        }


    }

}
