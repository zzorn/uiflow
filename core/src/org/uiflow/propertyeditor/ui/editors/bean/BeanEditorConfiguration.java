package org.uiflow.propertyeditor.ui.editors.bean;

import org.uiflow.propertyeditor.ui.editors.EditorConfigurationBase;

/**
 * Configuration for a BeanEditor.
 */
public class BeanEditorConfiguration extends EditorConfigurationBase {

    private LabelLocation labelLocation;

    public static final BeanEditorConfiguration DEFAULT = new BeanEditorConfiguration();

    public BeanEditorConfiguration() {
        this(LabelLocation.LEFT);
    }

    /**
     * @param labelLocation location of labels for properties, relative to the property editors.
     */
    public BeanEditorConfiguration(LabelLocation labelLocation) {
        super(BeanEditor.class);
        this.labelLocation = labelLocation;
    }

    /**
     * @return location of labels for properties, relative to the property editors.
     */
    public LabelLocation getLabelLocation() {
        return labelLocation;
    }

    /**
     * @param labelLocation location of labels for properties, relative to the property editors.
     */
    public void setLabelLocation(LabelLocation labelLocation) {
        this.labelLocation = labelLocation;
    }
}
