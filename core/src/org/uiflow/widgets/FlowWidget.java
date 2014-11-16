package org.uiflow.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import org.uiflow.UiContext;

/**
 *
 */
public interface FlowWidget  {

    /**
     * @param uiContext holds various UI related constants and information, such as UI style.
     * @return user interface of the widget.  If it is not yet created, it will be created when this function is called.
     */
    Actor getUi(UiContext uiContext);

    /* * NOTE: Actors already have an act method
     * Called each frame.  Allows the widget to do any animation it may need.
     * @param timeSinceLastCall_seconds number of seconds since the last call of this method, or zero if it has not been called before.
     * /
    void update(double timeSinceLastCall_seconds);
    */

    /**
     * @return true if the UI actor has been created.
     */
    boolean isUiCreated();

    /**
     * Destroy the UI and release any reserved resources.  Should be called when the FlowWidget is removed from use.
     * After dispose has been called, the FlowWidget should not be used anymore.
     */
    void dispose();
}
