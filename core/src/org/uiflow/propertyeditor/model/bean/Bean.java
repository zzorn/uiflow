package org.uiflow.propertyeditor.model.bean;

import org.uiflow.propertyeditor.model.beangraph.BeanGraph;

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
     * @param graph the graph that this bean is contained in, or null for none
     */
    void setGraph(BeanGraph graph);

    /**
     * @return the graph that this bean is contained in, or null for none
     */
    BeanGraph getGraph();
}
