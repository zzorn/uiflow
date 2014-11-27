package org.uiflow.propertyeditor.model.bean;

/**
 * Listens to a property.
 */
public interface PropertyListener {

    /**
     * Called when the value of the specified property changes to a different value object.
     */
    void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue);

    /**
     * Called when the source property for a property changes.
     */
    void onSourceChanged(Bean bean, Property property, Property oldSource, Property newSource);

    /**
     * Called if the value editor configuration for the property changed.
     */
    void onValueEditorChanged(Bean bean, Property property);

    /**
     * Called if the name or other metadata of the property changes.
     */
    void onPropertyChanged(Bean bean, Property property);
}
