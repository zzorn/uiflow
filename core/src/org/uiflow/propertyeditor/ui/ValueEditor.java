package org.uiflow.propertyeditor.ui;

import org.uiflow.propertyeditor.model.EditorConfiguration;
import org.uiflow.widgets.FlowWidget;

/**
 * Interface for various types of editors of property values.
 */
public interface ValueEditor<T extends EditorConfiguration> extends FlowWidget {

    /**
     * This will be called before the UI is created, to configure the ValueEditor.
     * @param editorInfo information class to be used for configuring the value editor, contains allowed value ranges, etc.
     */
    void configure(T editorInfo);

    /**
     * @return the current value in the editor.
     */
    Object getEditedValue();

    /**
     * Update the value edited in the editor.
     */
    void setEditedValue(Object newValue);

    /**
     * @return true if the editor is enabled, false if not.
     */
    boolean isEnabled();

    /**
     * Change enabled state of the editor.
     * @param enabled true if the value can be edited, false if not.
     */
    void setEnabled(boolean enabled);

    /**
     * Listen to edits of the value by the user.
     * Changes to the value through the setEditedValue method are not reported.
     */
    void addListener(ValueEditorListener listener);

    /**
     * Remove value editing listener.
     */
    void removeListener(ValueEditorListener listener);

}
