package org.uiflow.propertyeditor.model.bean;

/**
 * Listens to changes in a bean and the properties in it.
 */
public interface BeanListener extends PropertyListener {
    /**
     * Called if the bean changed in a significant way.
     */
    void onChanged(Bean bean);

    /**
     * Called when a property is added.
     */
    void onPropertyAdded(Bean bean, Property property);

    /**
     * Called when a property is removed.
     */
    void onPropertyRemoved(Bean bean, Property property);

}
