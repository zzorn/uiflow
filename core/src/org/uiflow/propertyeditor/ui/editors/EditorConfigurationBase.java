package org.uiflow.propertyeditor.ui.editors;

/**
 * Common functionality for EditorConfigurations.
 */
public abstract class EditorConfigurationBase implements EditorConfiguration {

    private final Class<? extends Editor> editorClass;

    public EditorConfigurationBase(Class<? extends Editor> editorClass) {
        this.editorClass = editorClass;
    }

    @Override public final Editor createEditor() {
        try {
            // Create instance using no-argument constructor.
            final Editor editor = editorClass.newInstance();

            // Configure created instance with this configuration class
            editor.setConfiguration(this);

            return editor;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create a value editor of type " + editorClass + " from ValueEditorInfo "+this+" : " + e.getMessage(), e);
        }
    }
}
