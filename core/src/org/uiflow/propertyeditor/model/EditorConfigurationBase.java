package org.uiflow.propertyeditor.model;

import org.uiflow.propertyeditor.ui.ValueEditor;

/**
 * Common functionality for EditorConfigurations.
 */
public abstract class EditorConfigurationBase implements EditorConfiguration {

    private final Class<? extends ValueEditor> editorClass;

    public EditorConfigurationBase(Class<? extends ValueEditor> editorClass) {
        this.editorClass = editorClass;
    }

    @Override public final ValueEditor createEditor() {
        try {
            // Create instance using no-argument constructor.
            final ValueEditor valueEditor = editorClass.newInstance();

            // Configure created instance with this configuration class
            valueEditor.configure(this);

            return valueEditor;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create a value editor of type " + editorClass + " from ValueEditorInfo "+this+" : " + e.getMessage(), e);
        }
    }
}
