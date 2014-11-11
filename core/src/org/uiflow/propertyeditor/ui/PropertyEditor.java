package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.Bean;
import org.uiflow.propertyeditor.model.Property;
import org.uiflow.propertyeditor.model.PropertyListener;
import org.uiflow.widgets.FlowWidgetBase;

/**
 * Helper class for handling the edition of one property
 */
public class PropertyEditor extends FlowWidgetBase {

    private Property property;
    private Label nameLabel;
    private Table table;
    private ValueEditor valueEditor;

    private ValueEditorListener valueEditorListener = new ValueEditorListener() {
        @Override public void onValueEdited(ValueEditor valueEditor, Object currentValue) {
            if (property != null) {
                property.setValue(currentValue);
            }
        }
    };


    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Bean bean, Property property, Object newValue) {
            if (valueEditor != null) {
                valueEditor.setEditedValue(property.getValue());
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
    private Cell nameLabelCell;

    public PropertyEditor() {
        this(null);
    }

    public PropertyEditor(Property property) {
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

    @Override protected Actor createUi(UiContext uiContext) {
        table = new Table(uiContext.getSkin());

        nameLabel = new Label("", uiContext.getSkin());
        nameLabel.setAlignment(Align.right);
        nameLabelCell = table.add(nameLabel);
        nameLabelCell.expandX();
        nameLabelCell.space(getUiContext().getGap());

        buildValueEditor();

        updateUi();

        return table;
    }

    public void setNameLabelWidth(float width) {
        if (nameLabelCell == null) throw new IllegalStateException("Name label not yet initialized for this property, can not call setNameLabelWidth");

        nameLabelCell.width(width).right();
        nameLabel.layout();
    }

    public float getNameLabelWidth() {
        if (nameLabel == null) throw new IllegalStateException("Name label not yet initialized for this property, can not call getNameLabelWidth");

        return nameLabel.getPrefWidth();
    }

    private void buildValueEditor() {
        // Remove old if present
        if (valueEditor != null && valueEditor.isUiCreated()) {
            valueEditor.removeListener(valueEditorListener);
            table.removeActor(valueEditor.getUi(getUiContext()));
            valueEditor.dispose();
        }

        if (property == null) {
            valueEditor = null;
        }
        else {
            // Create instance
            try {
                valueEditor = property.getEditorType().newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("Could not create editor for property " + property, e);
            }

            valueEditor.addListener(valueEditorListener);
            table.add(valueEditor.getUi(getUiContext())).right();
        }

    }

    private void updateUi() {
        if (isUiCreated()) {
            // Update name
            final String name = property != null ? property.getName() : "";
            nameLabel.setText(name + ":");

            // Update edited value
            if (property != null && valueEditor != null) valueEditor.setEditedValue(property.getValue());
        }
    }
}
