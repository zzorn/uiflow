package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private VerticalGroup propertyList;

    private final BeanListener beanListener = new BeanListener() {
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

    @Override protected Actor createActor(UiContext uiContext) {
        propertyList = new VerticalGroup();

        updateUi();

        return propertyList;
    }

    private void updateUi() {
        if (isUiCreated()) {
            // Update name
            // TODO

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
                    propertyList.removeActor(editor.getActor(getUiContext()));
                }

                // Dispose removed editor
                editor.dispose();
            }
        }
    }

    private void addPropertyUi(Property property) {
        if (isUiCreated()) {
            // Create editor
            final PropertyEditor propertyEditor = new PropertyEditor(property);

            // Add to lookup map
            propertyEditors.put(property, propertyEditor);

            // Add to ui
            propertyList.addActor(propertyEditor.getActor(getUiContext()));
        }
    }

}
