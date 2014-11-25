package org.uiflow.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Input listener that also listens to scroll events (sets entered and exited targets as the scroll focus).
 */
public class ScrollInputListener extends InputListener {

    @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        // Tell the stage that yes, we do want to receive scroll events (why can't it send them by default?)...
        acquireScrollFocus(event);
    }

    @Override public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        // Don't receive scroll events when cursor outside component
        releaseScrollFocus(event);
    }

    @Override public boolean mouseMoved(InputEvent event, float x, float y) {
        // Somehow scroll focus is lost after mouse buttons are pressed, but re-acquiring it in a mouse press doesn't work, so as a workaround we do it when the mouse is moved.
        acquireScrollFocus(event);
        return false;
    }

    private void acquireScrollFocus(InputEvent event) {
        event.getStage().setScrollFocus(event.getTarget());
    }

    private void releaseScrollFocus(InputEvent event) {
        event.getStage().setScrollFocus(null);
    }
}
