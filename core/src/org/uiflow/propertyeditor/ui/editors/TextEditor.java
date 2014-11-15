package org.uiflow.propertyeditor.ui.editors;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.editorconfigurations.TextEditorConfiguration;
import org.uiflow.propertyeditor.ui.utils.TextFieldChangeListener;
import org.uiflow.propertyeditor.ui.ValueEditorBase;

/**
 * String editor that provides a single line textfield.
 */
public class TextEditor extends ValueEditorBase<TextEditorConfiguration> {

    private TextField textWidget;
    private Button button;

    @Override protected void updateEditedValue(Object value) {
        String text = value == null ? "" : value.toString();
        textWidget.setText(text);
    }

    @Override protected void setDisabled(boolean disabled) {
        textWidget.setDisabled(disabled);
    }

    @Override protected Actor createEditor(TextEditorConfiguration configuration, UiContext uiContext) {
        Table table = new Table(uiContext.getSkin());

        // Create single-line text field or multi-line text area
        if (configuration.isMultiLine()) {
            final TextArea textArea = new TextArea("", uiContext.getSkin());
            textArea.setPrefRows(Math.max(0, configuration.getRowsToShow() - 0.8f)); // Fudge factor to get desired number of rows
            textWidget = textArea;
        }
        else {
            textWidget = new TextField("", uiContext.getSkin());
        }

        table.add(textWidget);

        /* Not working too well with mouse selection
        // When textfield is selected, select all text so that it is easy to replace existing content.
        textField.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                textField.selectAll();
            }
        });
        */

        // When textfield content is edited, update the value.
        textWidget.addListener(new TextFieldChangeListener(){
            @Override protected boolean onTextFieldChanged(InputEvent event, String oldValue, String newValue) {
                notifyValueEdited(newValue);
                return false;
            }
        });

        return table;
    }

}
