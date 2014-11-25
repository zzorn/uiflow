package org.uiflow.propertyeditor.ui.editors.bean;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.bean.Property;
import org.uiflow.propertyeditor.model.bean.PropertyListener;
import org.uiflow.propertyeditor.ui.editors.Editor;
import org.uiflow.propertyeditor.ui.editors.EditorListener;
import org.uiflow.widgets.FlowWidgetBase;

/**
 * Helper class for handling the edition of one property
 */
public class PropertyUi extends FlowWidgetBase {

    private Property property;
    private Label nameLabel;
    private Table table;
    private Editor editor;
    private final LabelLocation labelLocation;
    private Container<Label> labelContainer;
    private Container<Actor> valueEditorContainer;


    private EditorListener editorListener = new EditorListener() {
        @Override public void onValueEdited(Editor editor, Object currentValue) {
            if (property != null) {
                property.setValue(currentValue);
            }
        }
    };
    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
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
    };

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
        this.labelLocation = labelLocation;
        setProperty(property);
    }

    public final Property getProperty() {
        return property;
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

    @Override protected Actor createUi(UiContext uiContext) {
        // Create root table for the property editor
        table = new Table(uiContext.getSkin());
        table.pad(getUiContext().getGap() / 2);

        // Create label and a container for it, to allow resizing
        nameLabel = new Label("", uiContext.getSkin());
        labelContainer = new Container<Label>(nameLabel);
        labelContainer.pad(getUiContext().getSmallGap());

        // Create container for the value editor UI
        valueEditorContainer = new Container<Actor>();
        //valueEditorContainer.pad(spacing);

        // Arrange label and value editor in correct configuration
        switch (labelLocation) {
            case ABOVE:
                table.add(labelContainer).expandX().left();
                table.row();
                table.add(valueEditorContainer).expand().fillX();
                nameLabel.setAlignment(Align.left);
                break;
            case LEFT:
                table.add(labelContainer).right().padRight(getUiContext().getGap());
                table.add(valueEditorContainer).expand().fillX();
                nameLabel.setAlignment(Align.right);
                break;
            case BELOW:
                table.add(valueEditorContainer).expand().fillX();
                table.row();
                table.add(labelContainer).expandX().left();
                nameLabel.setAlignment(Align.left);
                break;
            case NONE:
                table.add(valueEditorContainer);
                break;
        }

        // Create the value editor UI
        buildValueEditor();

        // Update the UI with the current value of the property
        updateUi();

        return table;
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
            if (property != null && editor != null) editor.setValue(property.getValue());
        }
    }

    public void setDisabled(boolean disabled) {
        // TODO: Implement
    }
}
