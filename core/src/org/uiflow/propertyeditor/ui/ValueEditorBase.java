package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.uiflow.UiContext;
import org.uiflow.widgets.FlowWidgetBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Common functionality for ValueEditors.
 */
public abstract class ValueEditorBase extends FlowWidgetBase implements ValueEditor {

    private boolean enabled = true;
    private List<ValueEditorListener> listeners = new ArrayList<ValueEditorListener>(4);
    private Object editedValue;

    private transient boolean sendingEditUpdate = false;

    @Override public final Object getEditedValue() {
        return editedValue;
    }

    // NOTE: This is only called from outside the editor.
    @Override public final void setEditedValue(Object newValue) {
        editedValue = newValue;

        if (isUiCreated() && !sendingEditUpdate) {
            updateEditedValue(newValue);
        }
    }

    @Override public final boolean isEnabled() {
        return enabled;
    }

    @Override public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (isUiCreated()) {
            doSetEnabled(enabled);
        }
    }

    @Override protected final Actor createUi(UiContext uiContext) {
        final Actor editor = createEditor(uiContext);

        // Update value and state
        updateEditedValue(editedValue);
        if (!enabled) doSetEnabled(false);

        return editor;
    }

    /**
     * @return editor UI.
     */
    protected abstract Actor createEditor(UiContext uiContext);

    /**
     * Should be called when the value is changed in the UI.
     */
    protected final void notifyValueEdited(Object newValue) {
        if (enabled) {
            sendingEditUpdate = true;
            setEditedValue(newValue);
            notifyValueEdited();
            sendingEditUpdate = false;
        }
    }

    /**
     * Called when the value to be edited is changed from outside the editor, and the UI should update.
     */
    protected abstract void updateEditedValue(Object value);

    /**
     * Called when the enabled state of the editor is changed.
     */
    protected abstract void doSetEnabled(boolean enabled);

    @Override public final void addListener(ValueEditorListener listener) {
        if (listener == null) throw new IllegalArgumentException("Listener should not be null");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(ValueEditorListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that the value has been edited.
     */
    private void notifyValueEdited() {
        for (ValueEditorListener listener : listeners) {
            listener.onValueEdited(this, editedValue);
        }
    }
}
