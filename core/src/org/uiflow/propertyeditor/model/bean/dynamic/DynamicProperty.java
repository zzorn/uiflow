package org.uiflow.propertyeditor.model.bean.dynamic;

import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.bean.Property;
import org.uiflow.propertyeditor.model.bean.PropertyBase;
import org.uiflow.propertyeditor.model.bean.PropertyDirection;
import org.uiflow.propertyeditor.ui.editors.EditorConfiguration;

/**
 * Simple implementation of Property.
 */
public class DynamicProperty extends PropertyBase {

    private Object value;
    private String name;


    /**
     * @param name user readable name of this property.
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     */
    public DynamicProperty(String name,
                           Class type,
                           EditorConfiguration editorConfiguration) {
        this(name, type, editorConfiguration, null, PropertyDirection.INOUT);
    }

    /**
     * @param name user readable name of this property.
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param value initial value of this property.
     */
    public DynamicProperty(String name,
                           Class type,
                           EditorConfiguration editorConfiguration,
                           Object value) {
        this(name, type, editorConfiguration, value, PropertyDirection.INOUT);
    }

    /**
     * @param name user readable name of this property.
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param value initial value of this property.
     * @param propertyDirection whether this property is an input or output or both property.
     */
    public DynamicProperty(String name,
                           Class type,
                           EditorConfiguration editorConfiguration,
                           Object value,
                           PropertyDirection propertyDirection) {
        this(name, type, editorConfiguration, value, propertyDirection, null);
    }

    /**
     * @param name user readable name of this property.
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param value initial value of this property.
     * @param propertyDirection whether this property is an input or output or both property.
     * @param bean the bean that this property belongs to, or null if not yet known.
     */
    public DynamicProperty(String name,
                           Class type,
                           EditorConfiguration editorConfiguration,
                           Object value,
                           PropertyDirection propertyDirection,
                           Bean bean) {
        this (name, type, editorConfiguration, value, propertyDirection, bean, null);
    }

    /**
     * @param name user readable name of this property.
     * @param editorConfiguration the type of editor to use to edit the value of this property, and the configuration for it.
     * @param value initial value of this property.
     * @param propertyDirection whether this property is an input or output or both property.
     * @param bean the bean that this property belongs to, or null if not yet known.
     * @param source the property to use as source for this property, or null to not use any source.
     */
    public DynamicProperty(String name,
                           Class type,
                           EditorConfiguration editorConfiguration,
                           Object value,
                           PropertyDirection propertyDirection,
                           Bean bean,
                           Property source) {
        super(type, editorConfiguration, propertyDirection, bean, source);
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

    @Override public void setPropertyDirection(PropertyDirection propertyDirection) {
        super.setPropertyDirection(propertyDirection);
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
