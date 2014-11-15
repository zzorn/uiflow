package org.uiflow.propertyeditor.ui.editors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.editorconfigurations.NumberEditorConfiguration;
import org.uiflow.propertyeditor.ui.utils.TextFieldChangeListener;
import org.uiflow.propertyeditor.ui.ValueEditorBase;

/**
 *
 */
public class NumberEditor extends ValueEditorBase<NumberEditorConfiguration> {

    private TextField numberField;

    private boolean errorStyle = false;

    @Override protected Actor createEditor(final NumberEditorConfiguration configuration, final UiContext uiContext) {
        numberField = new TextField("", uiContext.getSkin());

        //numberField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        numberField.addListener(new TextFieldChangeListener() {
            @Override protected boolean onTextFieldChanged(InputEvent event, String oldValue, String newValue) {
                Number value = parseNumberFromText(configuration.getNumberType(), newValue);

                setErrorStyle(uiContext, value == null);

                if (value != null) notifyValueEdited(value);
                return false;
            }
        });

        return numberField;
    }

    @Override protected void updateEditedValue(Object value) {
        numberField.setText("" + value);
    }

    @Override protected void setDisabled(boolean disabled) {
        numberField.setDisabled(disabled);
    }

    private Number parseNumberFromText(Class<? extends Number> numberType, final String text) {
        try {
            if (numberType.equals(Byte.class)) return Byte.valueOf(text);
            else if (numberType.equals(Short.class)) return Short.valueOf(text);
            else if (numberType.equals(Integer.class)) return Integer.valueOf(text);
            else if (numberType.equals(Long.class)) return Long.valueOf(text);
            else if (numberType.equals(Float.class)) return Float.valueOf(text);
            else if (numberType.equals(Double.class)) return Double.valueOf(text);
            else throw new IllegalStateException("Unsupported number type in number field: " + numberType);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private void setErrorStyle(UiContext uiContext, boolean error) {
        if (errorStyle != error) {
            errorStyle = error;

            // Change style to indicate invalid or ok input
            String styleName = error ? "error" : "default";
            numberField.setStyle(uiContext.getSkin().get(styleName, TextField.TextFieldStyle.class));
            numberField.layout();
        }
    }
}
