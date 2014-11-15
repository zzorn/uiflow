package org.uiflow.propertyeditor.ui.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Listens to any input that might change a text field.
 */
public abstract class TextFieldChangeListener extends InputListener {

    private String previousText;

    @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return checkIfTextFieldChanged(event);
    }

    @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        checkIfTextFieldChanged(event);
    }

    @Override public boolean keyDown(InputEvent event, int keycode) {
        return checkIfTextFieldChanged(event);
    }

    @Override public boolean keyUp(InputEvent event, int keycode) {
        return checkIfTextFieldChanged(event);
    }

    @Override public boolean keyTyped(InputEvent event, char character) {
        return checkIfTextFieldChanged(event);
    }

    private boolean checkIfTextFieldChanged(InputEvent event) {
        final Actor source = event.getTarget();

        if (source == null) {
            // Skip any events with a null actor
            return false;
        }
        else if (!(source instanceof TextField)) {
            // Not a text field, can't listen to changes
            throw new IllegalStateException("TextFieldChangeListener must listen to a TextField, instead the related actor was " + source);
        } else {
            TextField textField = (TextField) source;

            // Check if there was any change
            final String currentText = textField.getText();
            if (!currentText.equals(previousText)) {
                String oldText = previousText;
                previousText = currentText;

                // Notify listener of change
                return onTextFieldChanged(event, oldText, currentText);
            }
            else {
                // No change to the content
                return false;
            }
        }
    }

    /**
     * Called whenever the text field contents changes, or the first time an event is received for a text field.
     * @param event the event that caused the change.
     * @param oldValue previous text field value, or null if this listener has not been triggered yet.
     * @param newValue current text field value
     * @return true if the event should be marked as processed, false if it should be passed on to other handlers.
     */
    protected abstract boolean onTextFieldChanged(InputEvent event, String oldValue, String newValue);
}
