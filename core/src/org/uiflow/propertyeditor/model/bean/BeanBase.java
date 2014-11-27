package org.uiflow.propertyeditor.model.bean;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Base functionality for Bean implementations.
 */
public abstract class BeanBase implements Bean {

    private final Set<BeanListener> listeners = new LinkedHashSet<BeanListener>();

    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
            for (BeanListener listener : listeners) {
                listener.onValueChanged(bean, property, newValue);
            }
        }

        @Override public void onValueEditorChanged(Bean bean, Property property) {
            for (BeanListener listener : listeners) {
                listener.onValueEditorChanged(bean, property);
            }
        }

        @Override public void onPropertyChanged(Bean bean, Property property) {
            for (BeanListener listener : listeners) {
                listener.onPropertyChanged(bean, property);
            }
        }

        @Override public void onSourceChanged(Bean bean, Property property, Property newSource) {
            for (BeanListener listener : listeners) {
                listener.onSourceChanged(bean, property, newSource);
            }
        }
    };

    @Override public final void addListener(BeanListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(BeanListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return a listener that should be notified about changes to properties in this bean.
     */
    protected final PropertyListener getPropertyListener() {
        return propertyListener;
    }

    /**
     * Notifies all listeners that the name of this bean has changed.
     */
    protected final void notifyBeanNameChanged() {
        for (BeanListener listener : listeners) {
            listener.onBeanNameChanged(this);
        }
    }

    /**
     * Notifies all listeners that a property has been added to this bean.
     */
    protected final void notifyPropertyAdded(Property property) {
        for (BeanListener listener : listeners) {
            listener.onPropertyAdded(this, property);
        }
    }

    /**
     * Notifies all listeners that a property has been removed from this bean.
     */
    protected final void notifyPropertyRemoved(Property property) {
        for (BeanListener listener : listeners) {
            listener.onPropertyRemoved(this, property);
        }
    }
}
