package org.uiflow.propertyeditor.model.beanlist;

import org.uiflow.propertyeditor.model.BeanContainer;
import org.uiflow.propertyeditor.model.BeanContainerListener;
import org.uiflow.propertyeditor.model.bean.Bean;

import java.util.List;

/**
 * Contains zero or more beans in some order.
 */
public interface BeanList extends BeanContainer<BeanContainerListener> {

    List<Bean> getBeans();

}
