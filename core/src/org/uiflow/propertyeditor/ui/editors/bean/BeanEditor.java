package org.uiflow.propertyeditor.ui.editors.bean;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.bean.BeanListener;
import org.uiflow.propertyeditor.model.bean.Property;
import org.uiflow.propertyeditor.ui.editors.EditorBase;

import java.util.*;

/**
 * An editor UI for Bean instances.
 */
public class BeanEditor extends EditorBase<Bean, BeanEditorConfiguration> {

    private final LinkedHashMap<Property, PropertyUi> propertyEditors = new LinkedHashMap<Property, PropertyUi>();
    private Table propertyList;

    private final BeanListener beanListener = new BeanListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
            notifyValueEdited(bean);
        }

        @Override public void onValueEditorChanged(Bean bean, Property property) {
        }

        @Override public void onPropertyChanged(Bean bean, Property property) {
        }

        @Override public void onChanged(Bean bean) {
            notifyValueEdited(bean);
        }

        @Override public void onPropertyAdded(Bean bean, Property property) {
            addPropertyUi(property);
            rebuildPropertyList();
            notifyValueEdited(bean);
        }

        @Override public void onPropertyRemoved(Bean bean, Property property) {
            removePropertyUi(property);
            rebuildPropertyList();
            notifyValueEdited(bean);
        }
    };

    private Label nameLabel;


    /**
     * Creates a new BeanEditor using the default configuration.
     */
    public BeanEditor() {
        this(BeanEditorConfiguration.DEFAULT);
    }

    /**
     * Creates a new BeanEditor using the specified configuration.
     */
    public BeanEditor(BeanEditorConfiguration configuration) {
        super(configuration);
    }

    /**
     * Creates a new BeanEditor.
     * @param labelLocation location of the property labels relative the the property editors.
     */
    public BeanEditor(LabelLocation labelLocation) {
        super(new BeanEditorConfiguration(labelLocation));
    }

    @Override protected void onValueChanged(Bean oldValue, Bean newValue) {
        if (oldValue != null) {
            oldValue.removeListener(beanListener);
        }

        if (newValue != null) {
            newValue.addListener(beanListener);
        }
    }

    @Override protected void updateValueInUi(Bean value) {
        updateUi();
    }

    @Override protected Actor createEditor(BeanEditorConfiguration configuration, UiContext uiContext) {
        Table beanTable = new Table(uiContext.getSkin());
        beanTable.setBackground("window_titled");

        // Name label
        nameLabel = new Label("", uiContext.getSkin());
        nameLabel.setStyle(uiContext.getSkin().get("window_title", Label.LabelStyle.class));
        nameLabel.setAlignment(Align.center);
        beanTable.add(nameLabel).expandX().fillX();
        beanTable.row();

        // Property list
        propertyList = new Table(uiContext.getSkin());
        beanTable.add(propertyList).expand().fill().pad(uiContext.getGap());

        updateUi();

        return beanTable;
    }

    @Override protected void setDisabled(boolean disabled) {
        for (PropertyUi propertyUi : propertyEditors.values()) {
            propertyUi.setDisabled(disabled);
        }
    }

    private void updateUi() {
        if (isUiCreated()) {
            // Update name
            final Bean bean = getValue();
            String name = bean == null ? "" : bean.getName();
            nameLabel.setText(name);

            updateAvailablePropertyUis();
        }
    }

    /**
     * Add or remove missing or extra property UIs.
     */
    private void updateAvailablePropertyUis() {
        List<Property> propertiesToRemove = null;

        boolean propertiesAddedOrRemoved = false;

        Bean bean = getValue();
        if (bean == null && !propertyEditors.isEmpty()) {
            // Bean is null, remove all property uis
            propertiesToRemove = new ArrayList<Property>(propertyEditors.keySet());
            propertiesAddedOrRemoved = true;
        }
        else if (bean != null) {
            final List<Property> beanProperties = bean.getProperties();

            // Add any not yet present
            for (Property propertyInBean : beanProperties) {
                if (!propertyEditors.containsKey(propertyInBean)) {
                    // Add missing property UI
                    addPropertyUi(propertyInBean);
                    propertiesAddedOrRemoved = true;
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
                    propertiesAddedOrRemoved = true;
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

        // Recreate the UI
        if (propertiesAddedOrRemoved) {
            rebuildPropertyList();
        }
    }

    private void removePropertyUi(Property property) {
        if (isUiCreated()) {
            // Remove from lookup map
            final PropertyUi editor = propertyEditors.remove(property);

            // Remove the widget from the ui if the ui has been created
            if (editor != null) {
                // Dispose removed editor
                editor.dispose();
            }
        }
    }

    private void addPropertyUi(Property property) {
        if (isUiCreated()) {
            // Create editor
            final PropertyUi propertyUi = new PropertyUi(property, getConfiguration().getLabelLocation());

            // Add to lookup map
            propertyEditors.put(property, propertyUi);
        }
    }

    private void rebuildPropertyList() {
        if (isUiCreated()) {
            // Remove all property UIs
            propertyList.clear();

            // Add property UIs
            final LabelLocation labelLocation = getConfiguration().getLabelLocation();
            for (Map.Entry<Property, PropertyUi> entry : propertyEditors.entrySet()) {
                final PropertyUi propertyUi = entry.getValue();

                final Actor ui = propertyUi.getUi(getUiContext());
                final Actor label = propertyUi.getLabelUi();

                switch (labelLocation) {
                    case LEFT:
                        propertyList.add(label).right();
                        propertyList.add(ui).expandX().fillX().row();
                        break;
                    case ABOVE:
                        propertyList.add(label).left().expandX().fillX().row();
                        propertyList.add(ui).expandX().fillX().row();
                        break;
                    case BELOW:
                        propertyList.add(ui).expandX().fillX().row();
                        propertyList.add(label).left().expandX().fillX().row();
                        break;
                    case NONE:
                        propertyList.add(ui).expandX().fillX().row();
                        break;
                }

            }
        }
    }


}
