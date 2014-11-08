package org.uiflow.propertyeditor.model.dynamic;

import org.uiflow.propertyeditor.model.Bean;
import org.uiflow.propertyeditor.model.Property;
import org.uiflow.propertyeditor.model.PropertyBase;
import org.uiflow.propertyeditor.model.PropertyDirection;
import org.uiflow.propertyeditor.ui.ValueEditor;

/**
 * Simple implementation of Property.
 */
public class DynamicProperty extends PropertyBase {

    private Object value;
    private String name;


    /**
     */
    public DynamicProperty() {
        this(null);
    }

    /**
     * @param name user readable name of this property.
     */
    public DynamicProperty(String name) {
        this(name, null);
    }

    /**
     * @param name user readable name of this property.
     * @param value initial value of this property.
     */
    public DynamicProperty(String name,
                           Object value) {
        this(name, value, null);
    }

    /**
     * @param name user readable name of this property.
     * @param value initial value of this property.
     * @param editorType the type of editor to use to edit the value of this property
     */
    public DynamicProperty(String name,
                           Object value,
                           Class<? extends ValueEditor> editorType) {
        this(name, value, editorType, PropertyDirection.INOUT);
    }

    /**
     * @param name user readable name of this property.
     * @param value initial value of this property.
     * @param editorType the type of editor to use to edit the value of this property.
     * @param propertyDirection whether this property is an input or output or both property.
     */
    public DynamicProperty(String name,
                           Object value,
                           Class<? extends ValueEditor> editorType,
                           PropertyDirection propertyDirection) {
        this(name, value, editorType, propertyDirection, null);
    }

    /**
     * @param name user readable name of this property.
     * @param value initial value of this property.
     * @param editorType the type of editor to use to edit the value of this property.
     * @param propertyDirection whether this property is an input or output or both property.
     * @param bean the bean that this property belongs to, or null if not yet known.
     */
    public DynamicProperty(String name,
                           Object value,
                           Class<? extends ValueEditor> editorType,
                           PropertyDirection propertyDirection,
                           Bean bean) {
        this (name, value, editorType, propertyDirection, bean, null);
    }

    /**
     * @param name user readable name of this property.
     * @param value initial value of this property.
     * @param editorType the type of editor to use to edit the value of this property.
     * @param propertyDirection whether this property is an input or output or both property.
     * @param bean the bean that this property belongs to, or null if not yet known.
     * @param source the property to use as source for this property, or null to not use any source.
     */
    public DynamicProperty(String name,
                           Object value,
                           Class<? extends ValueEditor> editorType,
                           PropertyDirection propertyDirection,
                           Bean bean,
                           Property source) {
        super(editorType, propertyDirection, bean, source);
        setName(name);
        setValue(value);
    }

    @Override public String getName() {
        return name;
    }

    /**
     * Set the user readable name of this property.
     */
    public void setName(String name) {
        if (this.name != name) {
            this.name = name;
            notifyPropertyChanged();
        }
    }


    @Override protected void doSetValue(Object value) {
        this.value = value;
    }

    @Override public <T> T getValue() {
        return (T) value;
    }

    @Override public String toString() {
        return getName();
    }
}
