package org.uiflow.propertyeditor.commands;

import org.uiflow.UiContext;
import org.uiflow.utils.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Provides common functionality for a CommandProvider.
 */
// TODO: What about invocation?
public abstract class CommandProviderBase implements CommandProvider {

    private final List<Command> commands = new ArrayList<Command>();
    private final List<Command> readOnlyCommands = Collections.unmodifiableList(commands);

    private final List<CommandProviderListener> listeners = new ArrayList<CommandProviderListener>();

    private final CommandListener commandListener = new CommandListener() {
        @Override public void onCommandEnableChanged(Command command, boolean enabled) {
            notifyCommandEnabledChanged(command, enabled);
        }

        @Override public void onCommandConfigurationChanged(Command command,
                                                            CommandConfiguration commandConfiguration) {
            notifyCommandConfigChanged(command, commandConfiguration);
        }
    };

    private CommandQueue commandQueue;

    private UiContext uiContext;

    protected CommandProviderBase(UiContext uiContext) {
        this(uiContext, null);
    }

    protected CommandProviderBase(UiContext uiContext, CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
        this.uiContext = uiContext;
    }

    protected final CommandQueue getCommandQueue() {
        return commandQueue;
    }

    protected final void setCommandQueue(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Override public final Collection<Command> getCommands() {
        return readOnlyCommands;
    }

    @Override public final void addCommandListener(CommandProviderListener listener) {
        Check.notNull(listener, "listener");

        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override public final void removeCommandListener(CommandProviderListener listener) {
        listeners.remove(listener);
    }

    protected final <T> Command addCommand(String id, String name, String description, String iconId, int hotKey, String defaultPath1, String defaultPath2, UndoableCommand<T> undoableCommand) {
        return addCommand(new DelegatingCommand<T>(id,
                                                   new CommandConfigurationImpl(name,
                                                                                description,
                                                                                uiContext.getDrawable(iconId),
                                                                                hotKey,
                                                                                defaultPath1, defaultPath2),
                                                   commandQueue,
                                                   undoableCommand));
    }

    protected final Command addCommand(Command command) {
        Check.notNull(command, "command");

        if (!commands.contains(command)) {
            commands.add(command);
            notifyCommandAdded(command);
            command.addListener(commandListener);
        }

        return command;
    }

    protected final void removeCommand(Command command) {
        Check.notNull(command, "command");

        if (commands.contains(command)) {
            commands.remove(command);
            command.removeListener(commandListener);
            notifyCommandRemoved(command);
        }
    }


    private void notifyCommandAdded(Command command) {
        for (CommandProviderListener listener : listeners) {
            listener.commandAdded(this, command);
        }
    }

    private void notifyCommandRemoved(Command command) {
        for (CommandProviderListener listener : listeners) {
            listener.commandRemoved(this, command);
        }
    }

    private void notifyCommandEnabledChanged(Command command, boolean enabled) {
        for (CommandProviderListener listener : listeners) {
            listener.commandEnabledChanged(this, command, enabled);
        }
    }

    private void notifyCommandConfigChanged(Command command, CommandConfiguration commandConfiguration) {
        for (CommandProviderListener listener : listeners) {
            listener.commandConfigChanged(this, command, commandConfiguration);
        }
    }

}
