package org.uiflow.propertyeditor.model.bean;

import java.util.List;

/**
 * An object with properties.
 */
public interface Bean {

    /**
     * @return user readable name of the bean.
     */
    String getName();

    /**
     * @return read only list of the properties in the bean.
     */
    List<Property> getProperties();

    /**
     * Listen to changes in the bean and the properties of it.
     */
    void addListener(BeanListener listener);

    /**
     * Remove listener
     */
    void removeListener(BeanListener listener);

    /**
     * The first property with the specified name, or null if not found.
     */
    Property getProperty(String propertyName);

    Bean createCopy();

//    Bean createReference();
}
