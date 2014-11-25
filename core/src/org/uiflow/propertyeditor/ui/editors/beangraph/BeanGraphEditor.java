package org.uiflow.propertyeditor.ui.editors.beangraph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;
import org.uiflow.propertyeditor.ui.editors.EditorBase;

/**
 *
 */
// TODO: Support closing and opening animations
// TODO: Support pan
// TODO: Support zoom?  Maybe keep beans fixed size, just zoom positions
// TODO: Support deleting beans
// TODO: Support bean palettes that can be used to add new beans to a graph?
// TODO: How to support undo/redo or version control?
public class BeanGraphEditor extends EditorBase<BeanGraph, BeanGraphConfiguration> {

    public BeanGraphEditor(BeanGraphConfiguration configuration) {
        super(configuration);
    }

    @Override protected Actor createEditor(BeanGraphConfiguration configuration, UiContext uiContext) {
        // TODO: Implement
        return null;
    }

    @Override protected void updateValueInUi(BeanGraph value) {
        // TODO: Implement

    }

    @Override protected void setDisabled(boolean disabled) {
        // TODO: Implement

    }
}
