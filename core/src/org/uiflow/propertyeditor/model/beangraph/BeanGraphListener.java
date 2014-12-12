package org.uiflow.propertyeditor.model.beangraph;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.BeanContainerListener;
import org.uiflow.propertyeditor.model.bean.Bean;

/**
 * Notified about changes to a bean graph.
 */
public interface BeanGraphListener extends BeanContainerListener {

    /**
     * Called when the position of a bean in a graph is changed.
     */
    void onBeanMoved(BeanGraph beanGraph, Bean bean, Vector2 newPosition);

}
