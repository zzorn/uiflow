package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.Property;
import org.uiflow.propertyeditor.model.PropertyListener;
import org.uiflow.widgets.FlowWidgetBase;

/**
 *
 */
public class PropertyEditor extends FlowWidgetBase {

    private Property property;

    private Label nameLabel;

    private final PropertyListener propertyListener = new PropertyListener() {
        @Override public void onValueChanged(Property property) {
            // TODO: Implement

        }

        @Override public void onPropertyChanged(Property property) {
            updateUi();
        }
    };

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

    @Override protected Actor createActor(UiContext uiContext) {
        // TODO: Implement

        nameLabel = new Label("", uiContext.getSkin());

        return nameLabel;
    }

    private void updateUi() {
        if (isUiCreated()) {
            // TODO: Implement

            final String name = property != null ? property.getName() : "";
            nameLabel.setText(name);


        }
    }
}
