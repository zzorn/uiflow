package org.uiflow.propertyeditor.model.beangraph;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.BeanContainer;
import org.uiflow.propertyeditor.model.BeanContainerListener;
import org.uiflow.propertyeditor.model.bean.Bean;

/**
 * Notified about changes to a bean graph.
 */
public interface BeanGraphListener {

    /**
     * Called when the position of a bean in a graph is changed.
     */
    void onBeanMoved(BeanGraph beanGraph, Bean bean, Vector2 newPosition);

    void onBeanAdded(BeanGraph beanGraph, Bean bean, Vector2 position);

    void onBeanRemoved(BeanGraph beanGraph, Bean bean, Vector2 position);

}
