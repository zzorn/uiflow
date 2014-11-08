package org.uiflow.propertyeditor.model;

/**
 * Listens to a property.
 */
public interface PropertyListener {

    /**
     * Called when the value of the specified property changes to a different value object.
     */
    void onValueChanged(Property property);

    /**
     * Called if the name or other metadata of the property changes.
     */
    void onPropertyChanged(Property property);
}
