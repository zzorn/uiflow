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
import org.uiflow.propertyeditor.ui.widgets.ConnectorButton;
import org.uiflow.widgets.FlowWidgetBase;

/**
 * Helper class for handling the edition of one property
 */
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

    private EditorListener editorListener = new EditorListener() {
        @Override public void onValueEdited(Editor editor, Object currentValue) {
            /*
            if (property != null) {
                property.setValue(currentValue);
            }
            */
        }
    };
    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object oldValue, Object newValue) {
            if (editor != null) {
                editor.setValue(property.getValue());
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
        this(property, labelLocation, false, false);
    }

    /**
     * @param property property to edit
     * @param labelLocation relative location of the label.
     * @param showConnectors if true, connectors for connecting a source or output property to the property will be shown.
     * @param mirrorDirections if true, an output property will be shown as an input property and vice versa.
     *                         Used for internal views of BeanGraph interfaces.
     */
    public PropertyUi(Property property, LabelLocation labelLocation, boolean showConnectors, boolean mirrorDirections) {
        this.labelLocation = labelLocation;
        this.showConnectors = showConnectors;
        this.mirrorDirections = mirrorDirections;
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

    private ConnectorButton createConnector(UiContext uiContext, boolean isInput) {
        // Get connector icon
        final Drawable connectorImage = uiContext.getSkin().getDrawable("connector");

        final float offset = connectorImage.getMinWidth() / 2 - 2;
        final float xOffset = isInput ? -offset : offset;
        ConnectorButton imageButton = new ConnectorButton(connectorImage, xOffset, 0);
        imageButton.getImage().setScaling(Scaling.fill);
        imageButton.setPrefWidth(2);

        // Color connector according to property type
        imageButton.setColor(uiContext.getTypeColor(property.getType()));

        return imageButton;
    }

    private void buildValueEditor() {
        // Remove old if present
        if (editor != null && editor.isUiCreated()) {
            editor.removeListener(editorListener);
            valueEditorContainer.removeActor(editor.getUi(getUiContext()));
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
            valueEditorContainer.setActor(editor.getUi(getUiContext()));
            valueEditorContainer.fillX();
        }

    }

    private void updateUi() {
        if (isUiCreated()) {
            // Update name
            String name = property != null ? property.getName() : "";

            if (labelLocation == LabelLocation.LEFT) name += ":";

            nameLabel.setText(name);

            // Update edited value
            if (property != null && editor != null) {
                editor.setValue(property.getValue());
                editor.setEnabled(!disabled);
            }

            // Update connectors
            if (showConnectors) {

                inputConnector.setVisible(false);
                outputConnector.setVisible(false);

                if (property != null) {
                    PropertyDirection direction = property.getDirection();
                    if (mirrorDirections) direction = direction.getReverse();

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

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        updateUi();
    }
}
