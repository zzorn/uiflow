package org.uiflow.propertyeditor.ui.editors.beangraph;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
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

    private static final int MAX_SEGMENT_COUNT = 100;
    private static final int MIN_SEGMENT_COUNT = 8;
    private static float segmentLength = 10f;
    private static final int BORDER_FUDGE_FACTOR = 8;

    private final Drawable dotImage;

    private PropertyUi source;
    private PropertyUi target;

    private Vector2 start = new Vector2(100, 10);
    private Vector2 end = new Vector2(200, 300);
    private Vector2 pos = new Vector2();

    private final UiContext uiContext;

    public Connection(UiContext uiContext) {
        this(uiContext, null, null);
    }

    public Connection(UiContext uiContext, PropertyUi source, PropertyUi target) {
        Check.notNull(uiContext, "uiContext");

        this.uiContext = uiContext;
        this.dotImage = uiContext.getSkin().getDrawable("connector");
        this.source = source;
        this.target = target;

        segmentLength = dotImage.getMinWidth() * 0.5f;
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
        Color color = uiContext.getTypeColor(source.getProperty().getType());
        final Color oldColor = batch.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        final float dotW = dotImage.getMinWidth();
        final float dotH = dotImage.getMinHeight();

        // Determine number of segments to use
        int segmentCount = MathUtils.clamp((int)(distance(start, end) / segmentLength), MIN_SEGMENT_COUNT, MAX_SEGMENT_COUNT);

        // Draw dots
        for (int i = 0; i < segmentCount; i++) {
            // Calculate dot position
            float relPos = (float) i / (segmentCount - 1);
            pos.set(start);
            pos.interpolate(end, relPos, Interpolation.linear);

            // Draw dot
            // (Getting rid of occasional weird artifacts by rounding to integer coordinates).
            dotImage.draw(batch,
                          (int) pos.x,
                          (int) pos.y,
                          dotW,
                          dotH);
        }

        batch.setColor(oldColor);
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
            posOut.set(connectorButton.getOffsetX() - dotImage.getMinWidth() * 0.5f - BORDER_FUDGE_FACTOR,
                       0 - BORDER_FUDGE_FACTOR);

            connectorButton.localToStageCoordinates(posOut);
        }
    }


}
