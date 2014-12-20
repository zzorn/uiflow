package org.uiflow.propertyeditor.model.bean;

import com.badlogic.gdx.utils.Array;

/**
 * Base functionality for Bean implementations.
 */
public abstract class BeanBase implements Bean {

    private final Array<BeanListener> listeners = new Array<BeanListener>();

    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue) {
            for (BeanListener listener : listeners) {
                listener.onValueChanged(bean, property, oldValue, newValue);
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

        @Override public void onSourceChanged(Bean bean, Property property, Property oldSource, Property newSource) {
            for (BeanListener listener : listeners) {
                listener.onSourceChanged(bean, property, oldSource, newSource);
            }
        }
    };

    @Override public final void addListener(BeanListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(BeanListener listener) {
        listeners.removeValue(listener, true);
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

    @Override
    public Bean createCopy() {
        // Create empty copy
        Bean copy = createCopiedInstance();

        // Copy property values
        for (Property property : getProperties()) {
            Object value = property.getValue();

            // Deep copy bean values -- TODO: Unless they are meant to be references??  Introduce BeanReference type?
            if (value instanceof Bean) {
                value = ((Bean)value).createCopy();
            }

            copy.getProperty(property.getName()).set(value);
        }

        return copy;
    }

    protected abstract Bean createCopiedInstance();
}
