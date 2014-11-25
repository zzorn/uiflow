package org.uiflow.propertyeditor.model.beangraph;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.bean.Bean;

import java.util.Collection;

/**
 * A number of connected beans, providing some common input and output properties.
 */
// NOTE: Could also be called BeanModule, BeanGroup, BeanCollection, or similar.
public interface BeanGraph extends Bean {

    /**
     * @param bean bean to add to the graph.
     * @param position position in the graph to add the bean at.
     */
    void addBean(Bean bean, Vector2 position);

    /**
     * @param bean bean to remove from graph.
     */
    void removeBean(Bean bean);

    /**
     * @return read only collection of beans in the graph.
     */
    Collection<Bean> getBeans();

    /**
     * Changes the position of the specified bean.
     */
    void setBeanPosition(Bean bean, Vector2 position);

    /**
     * Returns the position of the specified bean
     */
    Vector2 getBeanPosition(Bean bean, Vector2 positionOut);

    /**
     * @return true if the specified bean is contained in this graph.
     */
    boolean containsBean(Bean bean);

    /**
     * @param listener listener to add.  Will be notified of beans added and removed, and beans moved.
     */
    void addListener(BeanGraphListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(BeanGraphListener listener);

}
