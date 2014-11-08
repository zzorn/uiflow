package org.uiflow.propertyeditor.model.dynamic;

import org.uiflow.propertyeditor.model.BeanBase;
import org.uiflow.propertyeditor.model.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Simple implementation of Bean that allows properties to be added and removed on the fly.
 */
public class DynamicBean extends BeanBase {

    private String name;
    private final List<Property> properties = new ArrayList<Property>();

    private transient List<Property> readOnlyProperties;

    public DynamicBean() {
        this(null);
    }

    /**
     * @param name user readable name for the bean.
     */
    public DynamicBean(String name) {
        this(name, null);
    }

    /**
     * @param name user readable name for the bean.
     * @param initialProperties initial properties of the bean.
     */
    public DynamicBean(String name, Collection<Property> initialProperties) {
        setName(name);

        if (initialProperties != null) {
            for (Property initialProperty : initialProperties) {
                addProperty(initialProperty);
            }
        }
    }

    @Override public String getName() {
        return name;
    }

    /**
     * Set the user readable name of this bean.
     */
    public void setName(String name) {
        if (this.name != name) {
            this.name = name;
            notifyBeanChanged();
        }
    }

    @Override public List<Property> getProperties() {
        if (readOnlyProperties == null) {
            readOnlyProperties = Collections.unmodifiableList(properties);
        }

        return readOnlyProperties;
    }

    /**
     * Adds a new property to this bean.
     */
    public void addProperty(Property property) {
        if (property == null) throw new IllegalArgumentException("The property can not be null");
        if (properties.contains(property)) throw new IllegalArgumentException("The properties already contain the property" + property);

        property.setBean(this);

        property.addListener(getPropertyListener());

        properties.add(property);

        notifyPropertyAdded(property);
    }

    /**
     * Removes a property from this bean.
     */
    public void removeProperty(Property property) {
        if (property == null) throw new IllegalArgumentException("The property can not be null");
        if (!properties.contains(property)) throw new IllegalArgumentException("The properties do not contain the property" + property);

        property.removeListener(getPropertyListener());

        properties.remove(property);

        notifyPropertyRemoved(property);
    }

    @Override public String toString() {
        return getName();
    }
}
