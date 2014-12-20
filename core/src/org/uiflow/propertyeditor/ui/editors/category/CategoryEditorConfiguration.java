package org.uiflow.propertyeditor.ui.editors.category;

import org.uiflow.propertyeditor.ui.editors.Editor;
import org.uiflow.propertyeditor.ui.editors.EditorConfigurationBase;

/**
 *
 */
public class CategoryEditorConfiguration extends EditorConfigurationBase {

    public static final CategoryEditorConfiguration DEFAULT = new CategoryEditorConfiguration();

    public CategoryEditorConfiguration() {
        super(CategoryEditor.class);
    }


}
