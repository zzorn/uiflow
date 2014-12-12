package org.uiflow.propertyeditor.model.beangraph;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.BeanContainer;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.bean.BeanDelegate;
import org.uiflow.propertyeditor.model.bean.MutableBean;
import org.uiflow.propertyeditor.model.bean.dynamic.DynamicBean;

import java.util.Collection;
import java.util.Map;

/**
 * A number of connected beans, providing some common input and output properties.
 */
// NOTE: Could also be called BeanModule, BeanGroup, BeanCollection, or similar.
public interface BeanGraph extends Bean, BeanContainer<BeanGraphListener> {

    /**
     * @param bean bean to add to the graph.
     * @param position position in the graph to add the bean at.
     *                 A copy of this vector should be made, so the vector passed as a parameter can be modified later.
     */
    Bean addBean(Bean bean, Vector2 position);

    /**
     * @param bean bean to add to the graph.
     * @param x x coordinate to place the bean at in the graph.
     * @param y y coordinate to place the bean at in the graph.
     */
    Bean addBean(Bean bean, float x, float y);

    /**
     * @param bean bean to remove from graph.
     */
    void removeBean(Bean bean);

    /**
     * @return read only collection of beans in the graph.
     */
    Collection<Bean> getBeans();

    /**
     * @return read only map with beans in the graph and their positions.
     */
    Map<Bean, Vector2> getBeansAndPositions();

    /**
     * Changes the position of the specified bean.
     *
     * @param bean bean to move
     * @param position a copy of the vector should be made, so that the vector object passed as a parameter can be changed later.
     */
    void setBeanPosition(Bean bean, Vector2 position);

    /**
     * Changes the position of the specified bean.
     */
    void setBeanPosition(Bean bean, float x, float y);

    /**
     * Returns the position of the specified bean
     * @param bean bean to get the position of.
     * @return the bean position vector.
     */
    Vector2 getBeanPosition(Bean bean);

    /**
     * Returns the position of the specified bean
     * @param bean bean to get the position of.
     * @param positionOut output vector to write bean position to, if not null.
     * @return the positionOut parameter with the bean position if it was not null, the bean position vector otherwise.
     */
    Vector2 getBeanPosition(Bean bean, Vector2 positionOut);

    /**
     * @return true if the specified bean is contained in this graph.
     */
    boolean containsBean(Bean bean);

    /**
     * @param listener listener to add to the BeanGraph.
     *                 Will be notified of beans added and removed to/from the BeanGraph, and beans moved in the BeanGraph.
     */
    void addListener(BeanGraphListener listener);

    /**
     * @param listener listener to remove from the BeanGraph.
     */
    void removeListener(BeanGraphListener listener);

    /**
     * @return a bean containing the input and output properties of this BeanGraph, functioning as an interface to this BeanGraph.
     */
    DynamicBean getInterfaceBean();

    /**
     * @return a bean containing internal inputs (external outputs).  Automatically added to the graph.
     */
    BeanDelegate getInternalInputBean();

    /**
     * @return a bean containing internal outputs (external inputs).  Automatically added to the graph.
     */
    BeanDelegate getInternalOutputBean();

}
