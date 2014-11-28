package org.uiflow.propertyeditor.model.bean;

import org.uiflow.propertyeditor.ui.editors.EditorConfiguration;

/**
 * Property with name, type and a value.  May also have another property as source, in which case it will retrieve its value from there.
 */
// TODO: Recursive references may be solved either by not allowing them, or by moving values to outputs during update steps.
// TODO  That is an implementation specific decision thou.  Other approaches might be possible as well.
public interface Property {

    /**
     * @return user readable name of the property.
     */
    String getName();

    /**
     * @return the type of the property value.
     */
    Class getType();

    /**
     * @return current value of this property, regardless of whether a source is specified.
     */
    <T> T getValue();

    /**
     * @param value new value for this property.
     */
    void setValue(Object value);

    /**
     * @return source property for the value of this property, or null if it has no source set.
     * If the property has a source set, it will use the value of the source as its value instead of its own value.
     */
    Property getSource();

    /**
     * @param source source property for the value of this property, or null if it has no source set.
     * If the property has a source set, it will use the value of the source as its value instead of its own value.
     * If the source property uses this property as its source, an exception may be thrown.
     */
    void setSource(Property source);

    /**
     * @return current value of this property, or the value of the source property if it is set.
     * If the source property points back to this Property as its source, an exception may be thrown.
     */
    <T> T get();

    /**
     * Set the value or source of the property.
     *
     * @param value new value or source for this property.
     *              If this is a Property object, it attempts to set it as the source and the value to null.
     *              If this is not a Property object, it attempts to set source to null and the value to the given object.
     */
    <T> void set(T value);

    /**
     * @return the bean that this property is in.
     */
    Bean getBean();

    /**
     * @param bean the bean that this property is in.  Should only be set during property initialization,
     *             it is assumed that properties do not normally move from one bean to another.
     */
    void setBean(Bean bean);

    /**
     * @return whether the property can be used as input or output.
     */
    PropertyDirection getDirection();

    /**
     * Recursively checks whether the specified property is used as source by this property.
     */
    boolean usesSourceProperty(Property property);

    /**
     * @return the type of editor to use to edit this property and the configuration for it,
     * or null if it is not editable or viewable.
     */
    EditorConfiguration getEditorConfiguration();

    /**
     * @param editorConfiguration the type of editor to use to edit this property and the configuration for it,
     *                            or null if it is not editable or viewable.
     */
    void setEditorConfiguration(EditorConfiguration editorConfiguration);

    /**
     * Listen to changes in this property or its value.
     */
    void addListener(PropertyListener propertyListener);

    /**
     * Remove listener.
     */
    void removeListener(PropertyListener propertyListener);

    /**
     * @return true if this property can use the specified one as source.
     */
    boolean canUseSource(Property sourceProperty);
}


