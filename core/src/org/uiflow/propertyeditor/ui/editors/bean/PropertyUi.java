package org.uiflow.propertyeditor.ui.editors.bean;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.bean.Property;
import org.uiflow.propertyeditor.model.bean.PropertyDirection;
import org.uiflow.propertyeditor.model.bean.PropertyListener;
import org.uiflow.propertyeditor.ui.editors.Editor;
import org.uiflow.propertyeditor.ui.editors.EditorListener;
import org.uiflow.widgets.FlowWidgetBase;

/**
 * Helper class for handling the edition of one property
 */
// TODO: Keep track of output connections from a connector, so that we can change its type depending on if it is connected or not.
public class PropertyUi extends FlowWidgetBase {

    private Property property;
    private final boolean showConnectors;
    private final boolean mirrorDirections;
    private Label nameLabel;
    private Table table;
    private Editor editor;
    private final LabelLocation labelLocation;
    private Container<Label> labelContainer;
    private Container<Actor> valueEditorContainer;
    private boolean disabled = false;

    private final boolean hideEditorWhenSourceUsed;
    private final boolean hideEditorWhenNoInput = true;
    private boolean editorVisible = false;

    private EditorListener editorListener = new EditorListener() {
        @Override public void onValueEdited(Editor editor, Object currentValue) {
            if (property != null) {
                property.setValue(currentValue);
            }
        }
    };
    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue) {
            if (editor != null) {
                editor.setValue(property.get());
            }
        }

        @Override public void onValueEditorChanged(Bean bean, Property property) {
            buildValueEditor();
            updateUi();
        }

        @Override public void onPropertyChanged(Bean bean, Property property) {
            updateUi();
        }

        @Override public void onSourceChanged(Bean bean, Property property, Property oldSource, Property newSource) {
            updateUi();
        }
    };

    private ConnectorButton inputConnector;
    private ConnectorButton outputConnector;

    /**
     */
    public PropertyUi() {
        this(null);
    }

    /**
     * @param property property to edit
     */
    public PropertyUi(Property property) {
        this(property, LabelLocation.LEFT);
    }

    /**
     * @param property property to edit
     * @param labelLocation relative location of the label.
     */
    public PropertyUi(Property property, LabelLocation labelLocation) {
        this(property, labelLocation, false, false, true);
    }

    /**
     * @param property property to edit
     * @param labelLocation relative location of the label.
     * @param showConnectors if true, connectors for connecting a source or output property to the property will be shown.
     * @param mirrorDirections if true, an output property will be shown as an input property and vice versa.
*                         Used for internal views of BeanGraph interfaces.
     * @param hideEditorWhenSourceUsed if true, the editor will be hidden if a value is provided to a property by a source.
     */
    public PropertyUi(Property property,
                      LabelLocation labelLocation,
                      boolean showConnectors,
                      boolean mirrorDirections,
                      boolean hideEditorWhenSourceUsed) {
        this.labelLocation = labelLocation;
        this.showConnectors = showConnectors;
        this.mirrorDirections = mirrorDirections;
        this.hideEditorWhenSourceUsed = hideEditorWhenSourceUsed;
        setProperty(property);
    }

    public final Property getProperty() {
        return property;
    }

    public boolean isShowConnectors() {
        return showConnectors;
    }

    public final boolean isMirrorDirections() {
        return mirrorDirections;
    }

    public final void setProperty(Property property) {
        if (this.property != property) {
            if (this.property != null) {
                this.property.removeListener(propertyListener);
            }

            this.property = property;

            if (this.property != null) {
                this.property.addListener(propertyListener);
            }

            updateUi();
        }
    }

    Actor getLabelUi() {
        return labelContainer;
    }

    public ConnectorButton getInputConnector() {
        return inputConnector;
    }

    public ConnectorButton getOutputConnector() {
        return outputConnector;
    }

    @Override protected Actor createUi(UiContext uiContext) {
        // Create root table for the property editor
        table = new Table(uiContext.getSkin());
        table.pad(getUiContext().getGap() / 2);

        // Create input connector, if applicable
        if (showConnectors) {
            inputConnector = createConnector(uiContext, true);
        }

        // Create output connector, if applicable
        if (showConnectors) {
            outputConnector = createConnector(uiContext, false);
        }

        // Create label and a container for it, to allow resizing
        nameLabel = new Label("", uiContext.getSkin());
        labelContainer = new Container<Label>(nameLabel);
        labelContainer.pad(getUiContext().getSmallGap());

        // Create container for the value editor UI
        valueEditorContainer = new Container<Actor>();

        table.add(valueEditorContainer).fillX().expandX();

        // Create the value editor UI
        buildValueEditor();

        // Update the UI with the current value of the property
        updateUi();

        return table;
    }

    private ConnectorButton createConnector(UiContext uiContext, final boolean isInput) {
        // Get connector icon
        final Drawable connectedImage = uiContext.getSkin().getDrawable(isInput ? "connector_in" : "connector_out");
        final Drawable unconnectedImage = uiContext.getSkin().getDrawable("connector_unconnected");

        final ConnectorButton connector = new ConnectorButton(connectedImage, unconnectedImage, this, isInput);
        connector.getImage().setScaling(Scaling.fill);

        // Color connector according to property type
        connector.setColor(uiContext.getTypeColor(property.getType()));

        return connector;
    }

    private void buildValueEditor() {
        // Remove old if present
        boolean wasEditorVisible = editorVisible;
        if (editor != null && editor.isUiCreated()) {
            editor.removeListener(editorListener);
            setEditorVisible(false);
            editor.dispose();
        }

        if (property == null) {
            editor = null;
        }
        else {
            // Create instance
            try {
                editor = property.getEditorConfiguration().createEditor();
            } catch (Exception e) {
                throw new IllegalStateException("Could not create editor for property " + property, e);
            }

            editor.addListener(editorListener);
            setEditorVisible(wasEditorVisible);
        }

    }

    private void updateUi() {
        if (isUiCreated()) {
            final boolean editorVisible = shouldEditorBeVisible();

            // Update name
            String name = property != null ? property.getName() : "";

            if (labelLocation == LabelLocation.LEFT && editorVisible) name += ":";

            nameLabel.setText(name);

            // Update edited value
            if (property != null && editor != null) {
                editor.setValue(property.get());
                editor.setEnabled(!disabled && !hasSource());
            }

            // Hide editor if desired
            setEditorVisible(editorVisible);

            // Update connectors
            if (showConnectors) {

                inputConnector.setVisible(false);
                outputConnector.setVisible(false);

                if (property != null) {
                    PropertyDirection direction = getPropertyDirection();

                    if (direction.isInput()) {
                        inputConnector.setVisible(true);
                    }

                    if (direction.isOutput()) {
                        outputConnector.setVisible(true);
                    }
                }
            }
        }
    }

    private boolean shouldEditorBeVisible() {
        boolean hideEditor = (hideEditorWhenSourceUsed && hasSource()) ||
                             (hideEditorWhenNoInput && property != null && !isMirrorDirections() && !getProperty().getDirection().isInput());
        // (If mirrorDirections is true, this is one of the interface beans, and we want to be able to provide default values for the inputs, and a fixed value for the output if it is not connected to a source, so don't hide them).
        return !hideEditor;
    }

    private PropertyDirection getPropertyDirection() {
        if (property != null) {
            return property.getDirection().getReverse(mirrorDirections);
        } else {
            return PropertyDirection.INOUT;
        }
    }

    private void setEditorVisible(boolean editorVisible) {
        if (this.editorVisible != editorVisible) {
            this.editorVisible = editorVisible;

            if (editor != null && isUiCreated()) {
                if (editorVisible) {
                    valueEditorContainer.setActor(editor.getUi(getUiContext()));
                    valueEditorContainer.fillX();
                    table.layout();
                }
                else {
                    valueEditorContainer.removeActor(editor.getUi(getUiContext()));
                }
            }
        }
    }

    private boolean hasSource() {
        return property != null &&
               property.getSource() != null;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        updateUi();
    }
}
