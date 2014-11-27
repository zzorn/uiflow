package org.uiflow.propertyeditor.model.bean;

import org.uiflow.propertyeditor.ui.editors.EditorConfiguration;

/**
 * A Bean where properties can be added and removed.
 */
public interface MutableBean extends Bean {

    /**
     * Change name of the bean.
     *
     * @param name new name.
     */
    void setName(String name);

    /**
     * Adds a new property to this bean.
     *
     * @param name name of the property
     * @param type type of the property value.
     * @param value initial value for the property
     * @param editorConfiguration editor configuration for the property
     * @param direction whether the property is read or write (or both).
     * @return the added property
     */
    Property addProperty(String name,
                         Class type,
                         Object value,
                         EditorConfiguration editorConfiguration,
                         final PropertyDirection direction);

    /**
     * Adds a new property to this bean.
     *
     * @param property proeprty to add
     * @return the added property
     */
    Property addProperty(Property property);

    /**
     * Removes a property from this bean.
     */
    void removeProperty(Property property);

}
