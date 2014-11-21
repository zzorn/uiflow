package org.uiflow.propertyeditor.ui.editors;

import org.uiflow.widgets.FlowWidget;

/**
 * Interface for various types of editors of property values.
 */
public interface Editor<T, C extends EditorConfiguration> extends FlowWidget {

    /**
     * @return the currently used configuration.  Note that it may be shared between many different editor instances.
     */
    C getConfiguration();

    /**
     * This should be called before the UI is created, to configure the ValueEditor.
     * If this is called after the UI is created, it will not necessarily have any effect.
     * @param editorConfiguration information class to be used for configuring the value editor, contains allowed value ranges, etc.
     */
    void setConfiguration(C editorConfiguration);

    /**
     * @return the current value in the editor.
     */
    T getValue();

    /**
     * Update the value edited in the editor.
     */
    void setValue(T newValue);

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
     * Changes to the value through the setValue method are not reported.
     */
    void addListener(EditorListener<T> listener);

    /**
     * Remove value editing listener.
     */
    void removeListener(EditorListener<T> listener);

}
