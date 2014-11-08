package org.uiflow.propertyeditor.model;

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


}
