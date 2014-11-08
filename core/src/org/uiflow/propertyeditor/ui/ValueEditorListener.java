package org.uiflow.propertyeditor.ui;

/**
 * Listens to edits of the value.
 */
public interface ValueEditorListener {

    /**
     * Called when the value was edited by the user.
     */
    void onValueEdited(ValueEditor valueEditor, Object currentValue);

}
