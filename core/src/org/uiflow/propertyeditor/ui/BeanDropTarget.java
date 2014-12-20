package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.uiflow.propertyeditor.model.BeanContainer;
import org.uiflow.propertyeditor.model.bean.Bean;

/**
 * An UI component that can receive dropped beans.
 */
public interface BeanDropTarget {

    /**
     * @param sourceBean
     * @param sourceContainer
     * @param uiComponent
     * @param x
     * @param y
     * @return true if the bean was received.
     */
    boolean handleBeanDrop(Bean sourceBean, BeanContainer sourceContainer, Actor uiComponent, float x, float y);

}
