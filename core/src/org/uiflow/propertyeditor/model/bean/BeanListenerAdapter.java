package org.uiflow.propertyeditor.model.bean;

/**
 *
 */
public class BeanListenerAdapter implements BeanListener {
    @Override public void onBeanNameChanged(Bean bean) {
    }

    @Override public void onPropertyAdded(Bean bean, Property property) {
    }

    @Override public void onPropertyRemoved(Bean bean, Property property) {
    }

    @Override public void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue) {
    }

    @Override public void onSourceChanged(Bean bean, Property property, Property oldSource, Property newSource) {
    }

    @Override public void onValueEditorChanged(Bean bean, Property property) {
    }

    @Override public void onPropertyChanged(Bean bean, Property property) {
    }
}
