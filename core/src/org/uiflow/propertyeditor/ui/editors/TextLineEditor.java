package org.uiflow.propertyeditor.ui.editors;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.ui.ValueEditorBase;

/**
 * String editor that provides a single line textfield.
 */
public class TextLineEditor extends ValueEditorBase {

    private TextField textField;

    @Override protected void updateEditedValue(Object value) {
        String text = value == null ? "" : value.toString();
        textField.setText(text);
    }

    @Override protected void doSetEnabled(boolean enabled) {
        textField.setDisabled(!enabled);
    }

    @Override protected Actor createEditor(UiContext uiContext) {
        textField = new TextField("", uiContext.getSkin());
        textField.addListener(new InputListener(){
            @Override public boolean keyUp(InputEvent event, int keycode) {
                notifyValueEdited(textField.getText());
                return false;
            }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                notifyValueEdited(textField.getText());
            }

            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                notifyValueEdited(textField.getText());
                return false;
            }
        });

        return textField;
    }

}
