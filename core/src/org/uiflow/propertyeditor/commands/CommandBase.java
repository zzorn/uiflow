package org.uiflow.propertyeditor.commands;

import org.uiflow.utils.Check;

import java.util.ArrayList;
import java.util.List;

/**
 * Common functionality for a command.
 */
public abstract class CommandBase<T> implements Command, UndoableCommand<T> {

    private final String id;
    private CommandConfiguration commandConfiguration;
    private boolean enabled;
    private List<CommandListener> listeners = new ArrayList<CommandListener>();
    private CommandQueue commandQueue;

    protected CommandBase(String id,
                          CommandConfiguration commandConfiguration) {
        this(id, commandConfiguration, null);
    }

    protected CommandBase(String id,
                          CommandConfiguration commandConfiguration,
                          CommandQueue commandQueue) {
        this(id, commandConfiguration, commandQueue, true);
    }

    protected CommandBase(String id,
                          CommandConfiguration commandConfiguration,
                          CommandQueue commandQueue,
                          boolean enabled) {
        Check.nonEmptyString(id, "id");

        this.id = id;

        setCommandConfiguration(commandConfiguration);
        setQueue(commandQueue);
        setEnabled(enabled);
    }

    @Override public final String getId() {
        return id;
    }

    @Override public final CommandConfiguration getCommandConfiguration() {
        return commandConfiguration;
    }

    public final void setCommandConfiguration(CommandConfiguration commandConfiguration) {
        Check.notNull(commandConfiguration, "commandConfiguration");

        this.commandConfiguration = commandConfiguration;

        notifyConfigurationChanged();
    }

    @Override public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            notifyEnableChanged();
        }
    }

    @Override public final void invoke() {
        if (commandQueue != null) {
            // Use command queue to invoke
            commandQueue.handleCommandInvocation(this);
        } else {
            // Invoke directly
            doCommand();
        }
    }

    @Override public final void addListener(CommandListener listener) {
        Check.notNull(listener, "listener");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(CommandListener listener) {
        listeners.remove(listener);
    }

    @Override public final CommandQueue getQueue() {
        return commandQueue;
    }

    public final void setQueue(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    private void notifyEnableChanged() {
        for (CommandListener listener : listeners) {
            listener.onCommandEnableChanged(this, enabled);
        }
    }

    private void notifyConfigurationChanged() {
        for (CommandListener listener : listeners) {
            listener.onCommandConfigurationChanged(this, commandConfiguration);
        }
    }
}
