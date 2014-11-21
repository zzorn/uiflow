package org.uiflow.propertyeditor.ui.editors;

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
    Editor createEditor();

}
