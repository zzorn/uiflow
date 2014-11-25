package org.uiflow.propertyeditor.model.beangraph;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.bean.Bean;

/**
 * Notified about changes to a bean graph.
 */
public interface BeanGraphListener {

    void onBeanAdded(BeanGraph beanGraph, Bean bean, Vector2 position);

    void onBeanRemoved(BeanGraph beanGraph, Bean bean, Vector2 position);

    void onBeanMoved(BeanGraph beanGraph, Bean bean, Vector2 newPosition);

}
