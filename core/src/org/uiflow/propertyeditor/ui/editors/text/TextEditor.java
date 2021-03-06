package org.uiflow.propertyeditor.ui.editors.text;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.ui.editors.EditorBase;
import org.uiflow.utils.TextFieldChangeListener;

/**
 * String editor that provides a single line textfield.
 */
public class TextEditor extends EditorBase<String, TextEditorConfiguration> {

    private TextField textWidget;

    private final TextFieldChangeListener textFieldChangeListener = new TextFieldChangeListener(){
        @Override protected boolean onTextFieldChanged(InputEvent event, String oldValue, String newValue) {
            notifyValueEditedInUi(newValue);
            return false;
        }
    };

    public TextEditor() {
        this(TextEditorConfiguration.DEFAULT);
    }

    public TextEditor(TextEditorConfiguration configuration) {
        super(configuration);
    }

    @Override protected void updateValueInUi(String value) {
        String text = value == null ? "" : value;
        textWidget.setText(text);
        textFieldChangeListener.setPreviousText(text);
    }

    @Override protected void setDisabled(boolean disabled) {
        textWidget.setDisabled(disabled);
        textWidget.setStyle(getUiContext().getSkin().get(disabled ? "disabled" : "default", TextField.TextFieldStyle.class));
    }

    @Override protected Actor createEditor(TextEditorConfiguration configuration, UiContext uiContext) {
        Table table = new Table(uiContext.getSkin());

        // Create single-line text field or multi-line text area
        if (configuration.isMultiLine()) {
            final TextArea textArea = new TextArea("", uiContext.getSkin());
            textArea.setPrefRows(Math.max(0, configuration.getRows() - 0.8f)); // Fudge factor to get desired number of rows
            textWidget = textArea;
        }
        else {
            textWidget = new TextField("", uiContext.getSkin());
        }

        table.add(textWidget).expandX().fillX();

        // When textfield content is edited, update the value.
        textWidget.addListener(textFieldChangeListener);

        return table;
    }

}
