package org.uiflow.propertyeditor.commands;

import org.uiflow.UiContext;
import org.uiflow.utils.Check;
import org.uiflow.utils.HotKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Provides common functionality for a CommandProvider.
 */
public abstract class CommandProviderBase implements CommandProvider {

    private final List<Command> commands = new ArrayList<Command>();
    private final List<Command> readOnlyCommands = Collections.unmodifiableList(commands);

    private final List<CommandProviderListener> listeners = new ArrayList<CommandProviderListener>();

    private final List<CommandProvider> delegateCommandProviders = new ArrayList<CommandProvider>();

    private final CommandProviderListener delegateCommandProviderListener = new CommandProviderListener() {
        @Override public void commandAdded(CommandProvider commandProvider, Command command) {
            notifyCommandAdded(command);
        }

        @Override public void commandRemoved(CommandProvider commandProvider, Command command) {
            notifyCommandRemoved(command);
        }

        @Override public void commandEnabledChanged(CommandProvider commandProvider, Command command, boolean enabled) {
            notifyCommandEnabledChanged(command, enabled);
        }

        @Override public void commandConfigChanged(CommandProvider commandProvider,
                                                   Command command) {
            notifyCommandConfigChanged(command);
        }
    };

    private final CommandListener commandListener = new CommandListener() {
        @Override public void onCommandEnableChanged(Command command, boolean enabled) {
            notifyCommandEnabledChanged(command, enabled);
        }

        @Override public void onCommandConfigurationChanged(Command command) {
            notifyCommandConfigChanged(command);
        }
    };

    protected CommandProviderBase() {
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

    protected final Command addCommand(String id, String name, String description, String iconId, HotKey hotKey, String defaultPath1, String defaultPath2, Invokable implementation) {
        return addCommand(new DelegatingCommand(id,
                                                name,
                                                description,
                                                iconId,
                                                hotKey,
                                                true,
                                                implementation,
                                                defaultPath1, defaultPath2));
    }

    protected final <T extends Command> T addCommand(T command) {
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

    protected final void addCommandProvider(CommandProvider commandProvider) {
        Check.notNull(commandProvider, "commandProvider");
        if (!delegateCommandProviders.contains(commandProvider)) {
            delegateCommandProviders.add(commandProvider);
            commandProvider.addCommandListener(delegateCommandProviderListener);
        }
    }

    protected final void removeCommandProvider(CommandProvider commandProvider) {
        Check.notNull(commandProvider, "commandProvider");
        if (delegateCommandProviders.contains(commandProvider)) {
            delegateCommandProviders.remove(commandProvider);
            commandProvider.removeCommandListener(delegateCommandProviderListener);
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

    private void notifyCommandConfigChanged(Command command) {
        for (CommandProviderListener listener : listeners) {
            listener.commandConfigChanged(this, command);
        }
    }

}
