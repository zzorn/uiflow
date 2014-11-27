package org.uiflow.propertyeditor.model.bean.dynamic;

import org.uiflow.propertyeditor.model.bean.*;
import org.uiflow.propertyeditor.ui.editors.EditorConfiguration;
import org.uiflow.propertyeditor.ui.editors.bean.BeanEditorConfiguration;
import org.uiflow.propertyeditor.ui.editors.bean.LabelLocation;
import org.uiflow.propertyeditor.ui.editors.number.NumberEditorConfiguration;
import org.uiflow.propertyeditor.ui.editors.text.TextEditorConfiguration;
import org.uiflow.utils.colorfunction.ColorFunction;

import java.util.*;

/**
 * Simple implementation of Bean that allows properties to be added and removed on the fly.
 */
public class DynamicBean extends BeanBase implements MutableBean {

    private String name;
    private final List<Property> properties = new ArrayList<Property>();

    private transient List<Property> readOnlyProperties;

    public DynamicBean() {
        this(null);
    }

    /**
     * @param name user readable name for the bean.
     * @param initialProperties zero or more initial properties of the bean.
     */
    public DynamicBean(String name, Property ... initialProperties) {
        this(name, initialProperties.length == 0 ? null : Arrays.asList(initialProperties));
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
            notifyBeanNameChanged();
        }
    }

    @Override public List<Property> getProperties() {
        if (readOnlyProperties == null) {
            readOnlyProperties = Collections.unmodifiableList(properties);
        }

        return readOnlyProperties;
    }

    /**
     * Adds a new bean type property to this bean.
     *
     * @param name name of the property
     * @return the added property
     */
    public Property addBean(String name) {
        return addBean(name, null);
    }

    /**
     * Adds a new bean type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @return the added property
     */
    public Property addBean(String name, Bean value) {
        return addProperty(name, value, new BeanEditorConfiguration(), PropertyDirection.IN);
    }

    /**
     * Adds a new bean type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param  labelLocation where the label for bean properties should be in relation to the bean editor.
     * @return the added property
     */
    public Property addBean(String name, Bean value, LabelLocation labelLocation) {
        return addProperty(name, value, new BeanEditorConfiguration(labelLocation), PropertyDirection.IN);
    }

    /**
     * Adds a new bean type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param  labelLocation where the label for bean properties should be in relation to the bean editor.
     * @param direction whether this is an input or output property.
     * @return the added property
     */
    public Property addBean(String name, Bean value, LabelLocation labelLocation, final PropertyDirection direction) {
        return addProperty(name, value, new BeanEditorConfiguration(labelLocation), direction);
    }

    /**
     * Adds a new string type property to this bean.
     *
     * @param name name of the property
     * @return the added property
     */
    public Property addString(String name) {
        return addString(name, "");
    }

    /**
     * Adds a new string type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @return the added property
     */
    public Property addString(String name, String value) {
        return addProperty(name, value, new TextEditorConfiguration(), PropertyDirection.INOUT);
    }

    /**
     * Adds a new string type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param rows number of rows in the editor.
     * @return the added property
     */
    public Property addString(String name, String value, int rows) {
        return addProperty(name, value, new TextEditorConfiguration(rows), PropertyDirection.INOUT);
    }

    /**
     * Adds a new string type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param rows number of rows in the editor.
     * @param direction whether this is an input or output property.
     * @return the added property
     */
    public Property addString(String name, String value, int rows, final PropertyDirection direction) {
        return addProperty(name, value, new TextEditorConfiguration(rows), direction);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @return the added property
     */
    public Property addDouble(String name) {
        return addDouble(name, 0, PropertyDirection.INOUT);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @return the added property
     */
    public Property addDouble(String name, double value) {
        return addProperty(name, value, new NumberEditorConfiguration(Double.class), PropertyDirection.INOUT);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @return the added property
     */
    public Property addDouble(String name, double value, final PropertyDirection direction) {
        return addProperty(name, value, new NumberEditorConfiguration(Double.class), direction);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              double min,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class, min, max, enforceRange, logarithmic),
                           PropertyDirection.INOUT);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              final PropertyDirection direction,
                              double min,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class, min, max, enforceRange, logarithmic),
                           direction);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @param colorFunction used to color the slider bar with depending on the value.  -1 = min value, 1 = max value.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              final PropertyDirection direction,
                              double min,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic,
                              ColorFunction colorFunction) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class, min, max, enforceRange, logarithmic, true, true, colorFunction),
                           direction);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @param colorFunction used to color the slider bar with depending on the value.  -1 = min value, 1 = max value.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              double min,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic,
                              ColorFunction colorFunction) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class, min, max, enforceRange, logarithmic, true, true, colorFunction),
                           PropertyDirection.INOUT);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              final PropertyDirection direction,
                              double min,
                              double origin,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class, min, origin, max, enforceRange, logarithmic),
                           direction);
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              double min,
                              double origin,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class, min, origin, max, enforceRange, logarithmic),
                           PropertyDirection.INOUT);
    }


    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param origin value at center of range
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @param colorFunction used to color the slider bar with depending on the value.  -1 = min value, 0 = origin, 1 = max value.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              double min,
                              double origin,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic,
                              ColorFunction colorFunction) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class,
                                                         min,
                                                         origin,
                                                         max,
                                                         enforceRange,
                                                         logarithmic,
                                                         true,
                                                         true,
                                                         true,
                                                         colorFunction),
                           PropertyDirection.INOUT
        );
    }

    /**
     * Adds a new double type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param origin value at center of range
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @param colorFunction used to color the slider bar with depending on the value.  -1 = min value, 0 = origin, 1 = max value.
     * @return the added property
     */
    public Property addDouble(String name,
                              double value,
                              final PropertyDirection direction,
                              double min,
                              double origin,
                              double max,
                              boolean enforceRange,
                              boolean logarithmic,
                              ColorFunction colorFunction) {
        return addProperty(name,
                           value,
                           new NumberEditorConfiguration(Double.class,
                                                         min,
                                                         origin,
                                                         max,
                                                         enforceRange,
                                                         logarithmic,
                                                         true,
                                                         true,
                                                         true,
                                                         colorFunction),
                           direction
        );
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @return the added property
     */
    public Property addFloat(String name) {
        return addFloat(name, 0);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @return the added property
     */
    public Property addFloat(String name, float value) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class), PropertyDirection.INOUT);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction
     * @return the added property
     */
    public Property addFloat(String name, float value, final PropertyDirection direction) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class), direction);
    }


    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addFloat(String name,
                             float value,
                             float min,
                             float max,
                             boolean enforceRange,
                             boolean logarithmic) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class, min, max, enforceRange, logarithmic),
                           PropertyDirection.INOUT);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addFloat(String name,
                             float value,
                             float min,
                             float origin,
                             float max,
                             boolean enforceRange,
                             boolean logarithmic) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class, min, origin, max, enforceRange, logarithmic),
                           PropertyDirection.INOUT);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addFloat(String name,
                             float value,
                             final PropertyDirection direction,
                             float min,
                             float max,
                             boolean enforceRange,
                             boolean logarithmic) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class, min, max, enforceRange, logarithmic),
                           direction);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addFloat(String name,
                             float value,
                             final PropertyDirection direction,
                             float min,
                             float origin,
                             float max,
                             boolean enforceRange,
                             boolean logarithmic) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class, min, origin, max, enforceRange, logarithmic),
                           direction);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addFloat(String name,
                             float value,
                             final PropertyDirection direction,
                             float min,
                             float max,
                             boolean enforceRange,
                             boolean logarithmic,
                             ColorFunction colorFunction) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class, min, max, enforceRange, logarithmic, true, true, colorFunction),
                           direction);
    }

    /**
     * Adds a new float type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param direction whether this is an input or output property.
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addFloat(String name,
                             float value,
                             final PropertyDirection direction,
                             float min,
                             float origin,
                             float max,
                             boolean enforceRange,
                             boolean logarithmic,
                             ColorFunction colorFunction) {
        return addProperty(name, value, new NumberEditorConfiguration(Float.class, min, origin, max, enforceRange, logarithmic, true, true, true, colorFunction),
                           direction);
    }

    /**
     * Adds a new integer type property to this bean.
     *
     * @param name name of the property
     * @return the added property
     */
    public Property addInt(String name) {
        return addInt(name, 0);
    }

    /**
     * Adds a new integer type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @return the added property
     */
    public Property addInt(String name, int value) {
        return addProperty(name, value, new NumberEditorConfiguration(Integer.class), PropertyDirection.INOUT);
    }

    /**
     * Adds a new integer type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @return the added property
     */
    public Property addInt(String name, int value, int min, int max) {
        return addProperty(name, value, new NumberEditorConfiguration(Integer.class, min, max), PropertyDirection.INOUT);
    }

    /**
     * Adds a new integer type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @return the added property
     */
    public Property addInt(String name, int value, int min, int max, boolean enforceRange, boolean logarithmic) {
        return addProperty(name, value, new NumberEditorConfiguration(Integer.class, min, max, enforceRange, logarithmic),
                           PropertyDirection.INOUT);
    }

    /**
     * Adds a new integer type property to this bean.
     *
     * @param name name of the property
     * @param value initial value for the property
     * @param min minimum value for the property (inclusive)
     * @param max maximum value for the property (inclusive)
     * @param enforceRange true if minimum and maximum value are enforced, if false they are only used to restrict the slider, but the number editor still allows inputting out of range values.
     * @param logarithmic if true the slider will use a logarithmic scale instead of linear scale.
     * @param direction whether this is an input or output property.
     * @return the added property
     */
    public Property addInt(String name,
                           int value,
                           int min,
                           int max,
                           boolean enforceRange,
                           boolean logarithmic,
                           final PropertyDirection direction) {
        return addProperty(name, value, new NumberEditorConfiguration(Integer.class, min, max, enforceRange, logarithmic),
                           direction);
    }

    /**
     * Adds a new property to this bean, with an initial value or null.
     * @param name name of the property
     * @param editorConfiguration editor configuration for the property
     * @param direction whether this is an input or output property.
     * @return the added property
     */
    public Property addProperty(String name, EditorConfiguration editorConfiguration, final PropertyDirection direction) {
        return addProperty(name, null, editorConfiguration, direction);
    }

    /**
     * Adds a new property to this bean.
     * @param name name of the property
     * @param value initial value for the property
     * @param editorConfiguration editor configuration for the property
     * @param direction whether this is an input or output property.
     * @return the added property
     */
    public Property addProperty(String name,
                                Object value,
                                EditorConfiguration editorConfiguration,
                                final PropertyDirection direction) {
        final DynamicProperty property = new DynamicProperty(name, editorConfiguration, value, direction);
        addProperty(property);
        return property;
    }

    /**
     * Adds a new property to this bean.
     */
    public Property addProperty(Property property) {
        if (property == null) throw new IllegalArgumentException("The property can not be null");
        if (properties.contains(property)) throw new IllegalArgumentException("The properties already contain the property" + property);

        property.setBean(this);

        property.addListener(getPropertyListener());

        properties.add(property);

        notifyPropertyAdded(property);

        return property;
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
