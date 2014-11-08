package org.uiflow.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import org.uiflow.UiContext;

/**
 *
 */
public abstract class FlowWidgetBase implements FlowWidget {

    private Actor actor;
    private boolean uiCreated = false;
    private UiContext uiContext;

    @Override public final Actor getActor(UiContext uiContext) {
        if (actor == null) {
            uiCreated = true;
            this.uiContext = uiContext;
            actor = createActor(this.uiContext);

            if (actor == null) throw new IllegalStateException("Could not create an UI for the widget " + this.getClass().getName() + ", null returned from createActor()");
        }

        return actor;
    }

    public final boolean isUiCreated() {
        return uiCreated;
    }

    /**
     * @return the user interface context used by this FlowWidget.
     * Throws an exception if this is called before the ui has been created using a call to getActor (this can be checked with isUiCreated()).
     */
    public final UiContext getUiContext() {
        if (uiContext == null) throw new UnsupportedOperationException("getUiContext can not be called before getActor has been called and a uiContext provided through that call");

        return uiContext;
    }

    protected abstract Actor createActor(UiContext uiContext);

    @Override public void dispose() {
    }
}
