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
import org.uiflow.propertyeditor.ui.widgets.ConnectorButton;
import org.uiflow.utils.Check;
import org.uiflow.utils.MathUtils;

/**
 *
 */
public class Connection extends Actor {

    private static final int MAX_SEGMENT_COUNT = 200;
    private static final int MIN_SEGMENT_COUNT = 20;
    private static final String SEGMENT_NAME = "connection_segment_soft";
    private static float segmentLength = 10f;
    private static final int BORDER_FUDGE_FACTOR = 7;

    private final TextureRegion segmentImage;

    private PropertyUi source;
    private PropertyUi target;

    private Vector2 start = new Vector2();
    private Vector2 end = new Vector2();
    private Vector2 pos = new Vector2();
    private Vector2 oldPos = new Vector2();
    private Vector2 t = new Vector2();
    private Color color = new Color();

    private final UiContext uiContext;

    public Connection(UiContext uiContext) {
        this(uiContext, null, null);
    }

    public Connection(UiContext uiContext, PropertyUi source, PropertyUi target) {
        Check.notNull(uiContext, "uiContext");

        this.uiContext = uiContext;
        this.segmentImage = uiContext.getSkin().getRegion(SEGMENT_NAME);
        this.source = source;
        this.target = target;

        segmentLength = segmentImage.getRegionWidth() * 0.4f;
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
        this.source = source;
    }

    public PropertyUi getTarget() {
        return target;
    }

    public void setTarget(PropertyUi target) {
        this.target = target;
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

        // Determine connection color based on type
        Color startColor = uiContext.getTypeColor(source.getProperty().getType());
        Color endColor = uiContext.getTypeColor(target.getProperty().getType());
        final Color oldColor = batch.getColor();

        final float segmentW = segmentImage.getRegionWidth();
        final float segmentH = segmentImage.getRegionHeight();

        // Determine number of segments to use
        final float distance = distance(start, end);
        int segmentCount = MathUtils.clamp((int)(distance / segmentLength), MIN_SEGMENT_COUNT, MAX_SEGMENT_COUNT);

        if (end.x < start.x) {
            segmentCount *= 2;
        }

        // Draw dots
        oldPos.set(start);
        for (int i = 0; i < segmentCount; i++) {
            float relPos = (float) i / (segmentCount - 1);

            // Calculate color
            color.set(startColor);
            color.lerp(endColor, relPos);
            color.a *= parentAlpha;
            batch.setColor(color);

            // Calculate dot position
            float swing = MathUtils.clamp(start.x - end.x, 0, segmentLength * 10);
            pos.x = interpolate(relPos, start.x, end.x, Interpolation.linear) + swing *(float) Math.sin(relPos * Math.PI*2);
            pos.y = interpolate(relPos, start.y, end.y, Interpolation.sine);

            // Calculate angle
            t.set(pos).sub(oldPos);
            float angle = t.angle();
            oldPos.set(pos);

            // Draw dot
            batch.draw(segmentImage,
                       pos.x, pos.y,
                       segmentW*0.5f,
                       segmentH*0.5f,
                       segmentW, segmentH,
                       1f, 1f,
                       angle);
        }

        batch.setColor(oldColor);
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
            // FUdge
            posOut.set(connectorButton.getOffsetX() - segmentImage.getRegionWidth() * 0.5f - BORDER_FUDGE_FACTOR,
                       0 - BORDER_FUDGE_FACTOR);

            connectorButton.localToStageCoordinates(posOut);
        }
    }


}
