package org.uiflow.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import org.uiflow.UiContext;

/**
 *
 */
public interface FlowWidget  {

    Actor getUi(UiContext uiContext);

    boolean isUiCreated();

    void dispose();
}
