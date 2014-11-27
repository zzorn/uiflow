package org.uiflow.propertyeditor.model.bean;

import org.uiflow.utils.Check;

import java.util.List;

/**
 * Delegates all calls to another bean.
 */
public class BeanDelegate implements Bean {

    private final Bean delegate;

    /**
     * @param delegate bean to delegate all calls to.
     */
    public BeanDelegate(Bean delegate) {
        Check.notNull(delegate, "delegate");

        this.delegate = delegate;
    }

    @Override public String getName() {
        return delegate.getName();
    }

    @Override public List<Property> getProperties() {
        return delegate.getProperties();
    }

    @Override public void addListener(BeanListener listener) {
        delegate.addListener(listener);
    }

    @Override public void removeListener(BeanListener listener) {
        delegate.removeListener(listener);
    }
}
