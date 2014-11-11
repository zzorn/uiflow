package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.Bean;
import org.uiflow.propertyeditor.model.BeanListener;
import org.uiflow.propertyeditor.model.Property;
import org.uiflow.widgets.FlowWidgetBase;

import java.util.*;

/**
 *
 */
public class BeanEditor extends FlowWidgetBase {

    private final Map<Property, PropertyEditor> propertyEditors = new HashMap<Property, PropertyEditor>();
    private Bean bean;
    private Table beanTable;
    private VerticalGroup propertyList;

    private float lastMaxLabelWidth = 0;

    private final BeanListener beanListener = new BeanListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
        }

        @Override public void onValueEditorChanged(Bean bean, Property property) {
        }

        @Override public void onPropertyChanged(Bean bean, Property property) {
            // Align labels if needed (in case the label of some property was changed)
            alignLabels();
        }

        @Override public void onChanged(Bean bean) {
            updateUi();
        }

        @Override public void onPropertyAdded(Bean bean, Property property) {
            addPropertyUi(property);
        }

        @Override public void onPropertyRemoved(Bean bean, Property property) {
            removePropertyUi(property);
        }
    };
    private Label nameLabel;

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        if (bean != this.bean) {
            if (this.bean != null) {
                this.bean.removeListener(beanListener);
            }

            this.bean = bean;

            if (this.bean != null) {
                this.bean.addListener(beanListener);
            }

            updateUi();
        }
    }

    @Override protected Actor createUi(UiContext uiContext) {
        beanTable = new Table(uiContext.getSkin());

        // Name label
        nameLabel = new Label("", uiContext.getSkin());
        beanTable.add(nameLabel).expandX();
        beanTable.row();

        // Property list
        propertyList = new VerticalGroup();
        beanTable.add(propertyList).expand().fill();

        updateUi();

        alignLabels();

        return beanTable;
    }

    private void updateUi() {
        if (isUiCreated()) {
            // Update name
            final Bean bean = getBean();
            String name = bean == null ? "No Bean" : bean.getName();
            nameLabel.setText(name);

            updateAvailablePropertyUis();
        }
    }

    /**
     * Add or remove missing or extra property UIs.
     */
    private void updateAvailablePropertyUis() {
        List<Property> propertiesToRemove = null;

        if (bean == null && !propertyEditors.isEmpty()) {
            // Bean is null, remove all property uis
            propertiesToRemove = new ArrayList<Property>(propertyEditors.keySet());
        }
        else if (bean != null) {
            final List<Property> beanProperties = bean.getProperties();

            // Add any not yet present
            for (Property propertyInBean : beanProperties) {
                if (!propertyEditors.containsKey(propertyInBean)) {
                    // Add missing property UI
                    addPropertyUi(propertyInBean);
                }
            }

            // Remove any removed and present
            for (Property propertyInUi : propertyEditors.keySet()) {
                // Check if the property is present in the UI but not in the bean anymore
                if (!beanProperties.contains(propertyInUi)) {
                    if (propertiesToRemove == null) {
                        propertiesToRemove = new ArrayList<Property>(3);
                    }
                    propertiesToRemove.add(propertyInUi);
                }
            }
        }

        // Remove properties tagged for removal
        // We do this in a separate step because we can't removed elements of a collection we are looping through
        if (propertiesToRemove != null) {
            for (Property property : propertiesToRemove) {
                removePropertyUi(property);
            }
            propertiesToRemove.clear();
        }
    }

    private void removePropertyUi(Property property) {
        if (isUiCreated()) {
            // Remove from lookup map
            final PropertyEditor editor = propertyEditors.remove(property);

            // Remove the widget from the ui if the ui has been created
            if (editor != null) {
                if (editor.isUiCreated()) {
                    propertyList.removeActor(editor.getUi(getUiContext()));
                }

                // Dispose removed editor
                editor.dispose();
            }

            // Align labels if needed
            alignLabels();
        }
    }

    private void addPropertyUi(Property property) {
        if (isUiCreated()) {
            // Create editor
            final PropertyEditor propertyEditor = new PropertyEditor(property);

            // Add to lookup map
            propertyEditors.put(property, propertyEditor);

            // Add to ui
            propertyList.addActor(propertyEditor.getUi(getUiContext()));

            // Update label width of all properties if needed, to align the labels
            alignLabels();
        }
    }

    private void alignLabels() {
        System.out.println("BeanEditor.alignLabels");
        System.out.println("lastMaxLabelWidth = " + lastMaxLabelWidth);

        // Calculate maximum label width
        float maxNameLabelWidth = -1;
        for (PropertyEditor propertyEditor : propertyEditors.values()) {
            final float nameLabelWidth = propertyEditor.getNameLabelWidth();
            if (nameLabelWidth > maxNameLabelWidth) {
                maxNameLabelWidth = nameLabelWidth;
            }
        }

        System.out.println("maxNameLabelWidth = " + maxNameLabelWidth);

        // If the maximum label width changed, update all property editor label widths to align them
        if (maxNameLabelWidth != lastMaxLabelWidth) {
            lastMaxLabelWidth = maxNameLabelWidth;

            System.out.println("realigning, max width " + lastMaxLabelWidth);

            for (PropertyEditor propertyEditor : propertyEditors.values()) {
                propertyEditor.setNameLabelWidth(maxNameLabelWidth);
            }

            // Relayout as needed
            beanTable.layout();
        }

    }

}
