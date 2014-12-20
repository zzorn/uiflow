package org.uiflow.propertyeditor.model;

import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.utils.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public abstract class BeanContainerBase<L> implements BeanContainer<L> {

    private final List<Bean> beans = new ArrayList<Bean>();
    private transient List<Bean> readOnlyBeans;

    private final List<L> listeners = new ArrayList<L>(3);

    @Override public final Collection<Bean> getBeans() {
        if (readOnlyBeans == null) readOnlyBeans = Collections.unmodifiableList(beans);

        return readOnlyBeans;
    }

    @Override public final boolean containsBean(Bean bean) {
        return beans.contains(bean);
    }

    @Override public final Bean addBean(Bean bean) {
        Check.notNull(bean, "bean");
        Check.notContained(bean, beans, "beans");

        beans.add(bean);

        onBeanAdded(bean);

        return bean;
    }

    @Override public final void removeBean(Bean bean) {
        if (containsBean(bean)) {
            beans.remove(bean);

            onBeanRemoved(bean);
        }

    }

    @Override public final void addListener(L listener) {
        Check.notNull(listener, "listener");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(L listener) {
        listeners.remove(listener);
    }

    protected final List<L> getListeners() {
        return listeners;
    }

    protected abstract void onBeanRemoved(Bean bean);

    protected abstract void onBeanAdded(Bean bean);
}
