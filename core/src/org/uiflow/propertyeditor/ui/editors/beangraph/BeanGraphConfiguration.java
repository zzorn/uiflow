package org.uiflow.propertyeditor.ui.editors.beangraph;

import org.uiflow.propertyeditor.ui.editors.Editor;
import org.uiflow.propertyeditor.ui.editors.EditorConfiguration;
import org.uiflow.propertyeditor.ui.editors.EditorConfigurationBase;

/**
 *
 */
public class BeanGraphConfiguration extends EditorConfigurationBase {

    private boolean hideEditorWhenSourceUsed;

    public BeanGraphConfiguration() {
        this(true);
    }

    /**
     * @param hideEditorWhenSourceUsed if true, the editor will be hidden if a value is provided to a property by a source.
     */
    public BeanGraphConfiguration(boolean hideEditorWhenSourceUsed) {
        super(BeanGraphEditor.class);
        this.hideEditorWhenSourceUsed = hideEditorWhenSourceUsed;
    }


    public boolean isHideEditorWhenSourceUsed() {
        return hideEditorWhenSourceUsed;
    }

    /**
     * @param hideEditorWhenSourceUsed if true, the editor will be hidden if a value is provided to a property by a source.
     */
    public void setHideEditorWhenSourceUsed(boolean hideEditorWhenSourceUsed) {
        this.hideEditorWhenSourceUsed = hideEditorWhenSourceUsed;
    }
}
