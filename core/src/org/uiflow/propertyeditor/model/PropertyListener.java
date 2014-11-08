package org.uiflow.propertyeditor.model;

/**
 * Listens to a property.
 */
public interface PropertyListener {

    /**
     * Called when the value of the specified property changes to a different value object.
     */
    void onValueChanged(Bean bean, Property property, Object newValue);

    /**
     * Called if the value editor for the property changed.
     */
    void onValueEditorChanged(Bean bean, Property property);

    /**
     * Called if the name or other metadata of the property changes.
     */
    void onPropertyChanged(Bean bean, Property property);
}
