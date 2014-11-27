package org.uiflow.propertyeditor.model.bean;

import org.uiflow.propertyeditor.ui.editors.EditorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of common property functionality.
 */
public abstract class PropertyBase implements Property {

    private Bean bean;
    private transient boolean loopCheckFlag = false;

    private final List<PropertyListener> listeners = new ArrayList<PropertyListener>(4);


    private PropertyDirection propertyDirection;
    private EditorConfiguration editorConfiguration;
    private Property source;

    /**
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     */
    protected PropertyBase(EditorConfiguration editorConfiguration) {
        this(editorConfiguration, PropertyDirection.INOUT);
    }

    /**
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param propertyDirection whether this property is an input or output or both property.
     */
    protected PropertyBase(EditorConfiguration editorConfiguration,
                           PropertyDirection propertyDirection) {
        this(editorConfiguration, propertyDirection, null);
    }

    /**
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param propertyDirection whether this property is an input or output or both property.
     * @param bean the bean that this property belongs to, or null if not yet known.
     */
    protected PropertyBase(EditorConfiguration editorConfiguration,
                           PropertyDirection propertyDirection,
                           Bean bean) {
        this(editorConfiguration, propertyDirection, bean, null);
    }


    /**
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param propertyDirection whether this property is an input or output or both property.
     * @param bean the bean that this property belongs to, or null if not yet known.
     * @param source the property to use as source for this property, or null to not use any source.
     */
    protected PropertyBase(EditorConfiguration editorConfiguration,
                           PropertyDirection propertyDirection,
                           Bean bean,
                           Property source) {
        this.bean = bean;
        this.editorConfiguration = editorConfiguration;

        setPropertyDirection(propertyDirection);
        if (source != null) setSource(source);
    }

    public final boolean usesSourceProperty(Property property) {
        // Check if we already visited this property
        if (loopCheckFlag) return false;

        final Property source = getSource();

        // Check if our source directly is the property
        if (source == property) return true;

        // Recursively look through sources
        if (source != null) {
            // Use loop check flag to not get caught in infinite loops if a source property has a loop.
            loopCheckFlag = true;
            boolean usedBySource = source.usesSourceProperty(property);
            loopCheckFlag = false;

            return usedBySource;
        }

        return false;
    }

    public final void addListener(PropertyListener listener) {
        if (listener == null) throw new IllegalArgumentException("The listener should not be null");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public final void removeListener(PropertyListener listener) {
        listeners.remove(listener);
    }

    @Override public final PropertyDirection getDirection() {
        return propertyDirection;
    }

    @Override public EditorConfiguration getEditorConfiguration() {
        return editorConfiguration;
    }

    public final Property getSource() {
        return source;
    }

    public final void setSource(Property source) {
        checkSource(source);

        this.source = source;

        notifyPropertyChanged();
    }


    @Override public void setEditorConfiguration(EditorConfiguration editorConfiguration) {
        this.editorConfiguration = editorConfiguration;
        notifyValueEditorChanged();
    }

    /**
     * Whether this is an input or output or inout property.
     */
    protected void setPropertyDirection(PropertyDirection propertyDirection) {
        if (propertyDirection == null) throw new IllegalArgumentException("PropertyDirection can not be null");

        this.propertyDirection = propertyDirection;
        notifyPropertyChanged();
    }


    @Override public final void setValue(Object value) {
        if (getValue() != value) {
            doSetValue(value);
            notifyValueChanged(value);
        }
    }

    @Override public <T> T get() {
        if (source != null) return source.get();
        else return getValue();
    }

    @Override public <T> void set(T value) {
        if (value instanceof Property) {
            setSource((Property) value);
        }
        else {
            setValue(value);
        }
    }

    /**
     * @param value new value to set this property to.
     */
    protected abstract void doSetValue(Object value);

    public final void setBean(Bean bean) {
        // Null values not allowed
        if (bean == null) throw new IllegalArgumentException("The bean can not be null");

        // Throw exception if trying to change a non-null value
        if (this.bean != null && this.bean != bean) {
            throw new IllegalStateException("The bean of this property has already been set to " + this.bean + ", can not change it to " + bean);
        }

        // Update bean if it wasn't already updated
        if (this.bean != bean) {
            this.bean = bean;
            notifyPropertyChanged();
        }
    }

    public Bean getBean() {
        return bean;
    }

    /**
     * Throws an illegal argument exception if the suggestedSource is not acceptable.
     * By default checks if setting the source to sourceToCheck would cause a loop.
     */
    protected final void checkSource(Property suggestedSource) {
        if (suggestedSource == this || suggestedSource.usesSourceProperty(this)) {
            throw new IllegalArgumentException("Setting source to " +
                                               suggestedSource + " would create an infinite loop, as " +
                                               suggestedSource + " or its sources uses this property as their source");
        }
    }

    /**
     * Notifies all listeners that something other than the value of this property has changed.
     */
    protected final void notifyPropertyChanged() {
        for (PropertyListener listener : listeners) {
            listener.onPropertyChanged(bean, this);
        }
    }

    protected final void notifyValueEditorChanged() {
        for (PropertyListener listener : listeners) {
            listener.onValueEditorChanged(bean, this);
        }
    }

    /**
     * Notifies all listeners that the value of this property has changed.
     */
    protected final void notifyValueChanged(Object newValue) {
        for (PropertyListener listener : listeners) {
            listener.onValueChanged(bean, this, newValue);
        }
    }

}
