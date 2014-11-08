package org.uiflow.propertyeditor.model;

/**
 *
 */
public interface Property {

    /**
     * @return user readable name of the property.
     */
    String getName();

    /**
     * @return the bean that this property is in.
     */
    Bean getBean();

    /**
     * @return whether the property can be used as input or output.
     */
    PropertyDirection getDirection();

    /**
     * @return editable value or this property.
     */
    <T> T getValue();

    /**
     * @return source property for the value of this property.
     */
    Property getSource();

    /**
     * @param value new value for this property.
     */
    void setValue(Object value);


    /**
     * Listen to changes in this property or its value.
     */
    void addListener(PropertyListener propertyListener);

    /**
     * Remove listener.
     */
    void removeListener(PropertyListener propertyListener);

}


