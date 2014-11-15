package org.uiflow.propertyeditor.model;

import org.uiflow.propertyeditor.ui.ValueEditor;

/**
 * Information about the value editor to use for a Property.
 * Can contain things like allowed ranges for numbers.
 *
 * Also knows how to create a suitable ValueEditor instance for the UI.
 */
public interface EditorConfiguration {

    /**
     * @return new editor instance.
     */
    ValueEditor createEditor();

}
