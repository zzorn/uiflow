package org.uiflow.propertyeditor.model;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;

/**
 * Listener for something that contains zero or more beans.
 */
public interface BeanContainerListener {

    void onBeanAdded(BeanGraph beanGraph, Bean bean, Vector2 position);

    void onBeanRemoved(BeanGraph beanGraph, Bean bean, Vector2 position);

}
