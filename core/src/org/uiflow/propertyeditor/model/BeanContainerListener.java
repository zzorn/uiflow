package org.uiflow.propertyeditor.model;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;

/**
 * Listener for something that contains zero or more beans.
 */
public interface BeanContainerListener {

    void onBeanAdded(BeanContainer beanContainer, Bean bean, Vector2 position);

    void onBeanRemoved(BeanContainer beanContainer, Bean bean, Vector2 position);

}
