package org.uiflow.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

/**
 * Widget group that clips its children to be within its bounds when drawing.
 * @deprecated seems to duplicate contents for some reason?
 */
public class ClippedWidgetGroup extends WidgetGroup {

    private boolean clip = true;

    private final Rectangle bounds = new Rectangle();

    @Override public void draw(Batch batch, float parentAlpha) {
        bounds.set(0, 0, getWidth(), getHeight());

        if (clip) {
            // Draw any unfinished things
            batch.flush();

            // Completely skip drawing children that are outside the bounds
            setCullingArea(bounds);

            // Set clip area, check if it is non-empty
            boolean clippingAreaVisible = clipBegin(0, 0, getWidth(), getHeight());
            if (clippingAreaVisible) {
                // Draw contents
                drawChildren(batch, parentAlpha);

                // End clipping
                clipEnd();
            }
        }
        else {
            drawChildren(batch, parentAlpha);
        }

        super.draw(batch, parentAlpha);
    }

    /**
     * @return true if widgets in this group will be clipped to the group bounds when drawn.
     */
    public boolean isClip() {
        return clip;
    }

    /**
     * @param clip true if widgets in this group should be clipped to the group bounds when drawn.
     */
    public void setClip(boolean clip) {
        this.clip = clip;
    }
}
