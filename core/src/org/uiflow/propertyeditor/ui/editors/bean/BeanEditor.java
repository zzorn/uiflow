package org.uiflow.propertyeditor.ui.editors.bean;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.*;
import org.uiflow.propertyeditor.ui.editors.EditorBase;

import java.util.*;

/**
 * An editor UI for Bean instances.
 */
public class BeanEditor extends EditorBase<Bean, BeanEditorConfiguration> {

    private final boolean showConnectors;
    private final boolean mirrorDirections;
    private final PropertyDirection directionsToShow;

    private final LinkedHashMap<Property, PropertyUi> propertyEditors = new LinkedHashMap<Property, PropertyUi>();

    private Table propertyList;
    private Table beanTable;
    private Label nameLabel;

    private final BeanListener beanListener = new BeanListenerAdapter() {
        @Override public void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue) {
            notifyValueEdited(bean);
        }

        @Override public void onBeanNameChanged(Bean bean) {
            if (nameLabel != null) {
                nameLabel.setText(bean.getName());
            }
        }

        @Override public void onPropertyAdded(Bean bean, Property property) {
            if (shouldShowProperty(property)) {
                addPropertyUi(property);
                rebuildPropertyList();
                notifyValueEdited(bean);
            }
        }

        @Override public void onPropertyRemoved(Bean bean, Property property) {
            final boolean removed = removePropertyUi(property);
            if (removed) {
                rebuildPropertyList();
            }
            notifyValueEdited(bean);
        }
    };



    /**
     * Creates a new BeanEditor using the default configuration.
     */
    public BeanEditor() {
        this(false, false);
    }


    /**
     * Creates a new BeanEditor using the default configuration.
     *
     * @param showConnectors true if connectors for connecting properties to each other should be shown.
     * @param mirrorDirections if true, output properties will be shown as input properties and vice versa.
     *                         Used for internal views of BeanGraph interfaces.
     */
    public BeanEditor(boolean showConnectors, boolean mirrorDirections) {
        this(BeanEditorConfiguration.DEFAULT, showConnectors, mirrorDirections);
    }

    /**
     * Creates a new BeanEditor using the specified configuration.
     */
    public BeanEditor(BeanEditorConfiguration configuration) {
        this(configuration, false, false);
    }

    /**
     * Creates a new BeanEditor using the specified configuration.
     *
     * @param showConnectors true if connectors for connecting properties to each other should be shown.
     * @param mirrorDirections if true, output properties will be shown as input properties and vice versa.
     *                         Used for internal views of BeanGraph interfaces.
     */
    public BeanEditor(BeanEditorConfiguration configuration, boolean showConnectors, boolean mirrorDirections) {
        this(configuration.getLabelLocation(), showConnectors, mirrorDirections, null);
    }

    /**
     * Creates a new BeanEditor.
     *
     * @param labelLocation location of the property labels relative the the property editors.
     */
    public BeanEditor(LabelLocation labelLocation) {
        this(labelLocation, false, false, null);
    }

    /**
     * Creates a new BeanEditor.
     *
     * @param labelLocation location of the property labels relative the the property editors.
     * @param showConnectors true if connectors for connecting properties to each other should be shown.
     * @param mirrorDirections if true, output properties will be shown as input properties and vice versa.
     *                         Used for internal views of BeanGraph interfaces.
     * @param directionsToShow the property directions to show, or null if all should be shown.
     */
    public BeanEditor(LabelLocation labelLocation, boolean showConnectors, boolean mirrorDirections, PropertyDirection directionsToShow) {
        super(new BeanEditorConfiguration(labelLocation));
        this.showConnectors = showConnectors;
        this.mirrorDirections = mirrorDirections;
        this.directionsToShow = directionsToShow;
    }

    /**
     * @return true if connectors for connecting properties to each other should be shown.
     */
    public final boolean isShowConnectors() {
        return showConnectors;
    }

    /**
     * @return if true, output properties will be shown as input properties and vice versa.
     *         Used for internal views of BeanGraph interfaces.
     */
    public final boolean isMirrorDirections() {
        return mirrorDirections;
    }

    /**
     * @return the property directions to show, or null if all should be shown.
     */
    public final PropertyDirection getDirectionsToShow() {
        return directionsToShow;
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
        beanTable = new Table(uiContext.getSkin());
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
                    if (shouldShowProperty(propertyInBean)) {
                        addPropertyUi(propertyInBean);
                        propertiesAddedOrRemoved = true;
                    }
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

    private boolean shouldShowProperty(Property propertyInBean) {
        return directionsToShow == null ||
               directionsToShow == propertyInBean.getDirection() ||
               directionsToShow.isInput() && propertyInBean.getDirection().isInput();
    }

    private boolean removePropertyUi(Property property) {
        if (isUiCreated()) {
            // Remove from lookup map
            final PropertyUi editor = propertyEditors.remove(property);

            // Remove the widget from the ui if the ui has been created
            if (editor != null) {
                // Dispose removed editor
                editor.dispose();
                return true;
            }
        }
        return false;
    }

    private void addPropertyUi(Property property) {
        if (isUiCreated()) {
            // Create editor
            final PropertyUi propertyUi = new PropertyUi(property, getConfiguration().getLabelLocation(), showConnectors, mirrorDirections);

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
                final Actor inputConnector = propertyUi.getInputConnector();
                final Actor outputConnector = propertyUi.getOutputConnector();

                switch (labelLocation) {
                    case LEFT:
                        if (inputConnector != null) propertyList.add(inputConnector);
                        propertyList.add(label).left();
                        propertyList.add(ui).expandX().fillX();
                        if (outputConnector != null) propertyList.add(outputConnector);
                        propertyList.row();
                        break;
                    case ABOVE:
                        Table t = new Table();
                        if (inputConnector != null) t.add(inputConnector);
                        t.add(label).left().expandX().fillX();
                        if (outputConnector != null) t.add(outputConnector);

                        propertyList.add(t).expandX().fillX();
                        propertyList.row();
                        propertyList.add(ui).expandX().fillX().row();
                        break;
                    case BELOW:
                        propertyList.add(ui).expandX().fillX().row();

                        Table t2 = new Table();
                        if (inputConnector != null) t2.add(inputConnector);
                        t2.add(label).left().expandX().fillX();
                        if (outputConnector != null) t2.add(outputConnector);
                        propertyList.add(t2).left().expandX().fillX();
                        propertyList.row();
                        break;
                    case NONE:
                        if (inputConnector != null) propertyList.add(inputConnector);
                        propertyList.add(ui).expandX().fillX();
                        if (outputConnector != null) propertyList.add(outputConnector);
                        propertyList.row();
                        break;
                }

            }

            /*
            // TODO: Trying to layout bean container..
            propertyList.layout();
            beanTable.invalidate();
            beanTable.layout();
            */
        }
    }


    /**
     * @return the PropertyUi for the specified property, if found, null otherwise.
     */
    public PropertyUi getPropertyUi(Property property) {
        return propertyEditors.get(property);
    }
}
