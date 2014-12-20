package org.uiflow.propertyeditor.model;

import org.uiflow.propertyeditor.model.bean.Bean;

import java.util.Collection;

/**
 * Something that contains zero or more Beans.
 */
public interface BeanContainer<L> {

    Collection<Bean> getBeans();

    boolean containsBean(Bean bean);

    Bean addBean(Bean bean);

    void removeBean(Bean bean);

    void addListener(L listener);

    void removeListener(L listener);

}

