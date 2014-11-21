package org.uiflow.propertyeditor.ui.editors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.uiflow.UiContext;
import org.uiflow.widgets.FlowWidgetBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Common functionality for ValueEditors.
 */
public abstract class EditorBase<T, C extends EditorConfiguration> extends FlowWidgetBase implements Editor<T, C> {

    private boolean enabled = true;
    private List<EditorListener<T>> listeners = new ArrayList<EditorListener<T>>(4);
    private T editedValue;

    private transient boolean sendingEditUpdate = false;
    private C configuration;

    /**
     * @param configuration a default configuration to use if none is provided by the user.  Should not be null.
     */
    protected EditorBase(C configuration) {
        setConfiguration(configuration);
    }

    @Override public final void setConfiguration(C configuration) {
        //if (this.configuration != null) throw new IllegalStateException("The value editor '"+this+"' has already been configured!");
        if (configuration == null) throw new IllegalStateException("The configuration can not be null");

        this.configuration = configuration;
    }

    public final C getConfiguration() {
        return configuration;
    }

    @Override public final T getValue() {
        return editedValue;
    }

    // NOTE: This is only called from outside the editor.
    @Override public final void setValue(T newValue) {
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
            setDisabled(!enabled);
        }
    }

    @Override protected final Actor createUi(UiContext uiContext) {
        // Check configuration
        if (configuration == null) {
            throw new IllegalStateException("No configuration has been provided for the value editor " + this);
        }

        // Create editor UI
        final Actor editor = createEditor(configuration, uiContext);

        // Update value and state
        updateEditedValue(editedValue);
        if (!enabled) setDisabled(true);

        return editor;
    }

    /**
     * @param configuration editor specific configuration class with things such as value ranges.
     * @param uiContext context class with things such as the skin used and other settings.
     * @return editor UI.
     */
    protected abstract Actor createEditor(C configuration, UiContext uiContext);

    /**
     * Should be called when the value is changed in the UI.
     */
    protected final void notifyValueEdited(T newValue) {
        if (enabled) {
            sendingEditUpdate = true;
            setValue(newValue);
            notifyValueEdited();
            sendingEditUpdate = false;
        }
    }

    /**
     * Called when the value to be edited is changed from outside the editor, and the UI should update.
     */
    protected abstract void updateEditedValue(T value);

    /**
     * Called when the disabled/enabled state of the editor is changed.
     */
    protected abstract void setDisabled(boolean disabled);

    @Override public final void addListener(EditorListener<T> listener) {
        if (listener == null) throw new IllegalArgumentException("Listener should not be null");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(EditorListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that the value has been edited.
     */
    private void notifyValueEdited() {
        for (EditorListener<T> listener : listeners) {
            listener.onValueEdited(this, editedValue);
        }
    }
}
