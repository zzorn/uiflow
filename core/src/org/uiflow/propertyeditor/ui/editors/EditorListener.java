package org.uiflow.propertyeditor.ui.editors;

/**
 * Listens to edits of the value.
 */
public interface EditorListener<T> {

    /**
     * Called when the value was edited by the user.
     */
    void onValueEdited(Editor<T, ?> editor, T currentValue);

}
