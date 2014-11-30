package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;
import org.uiflow.utils.Check;

import java.util.ArrayList;
import java.util.List;

/**
 * Common functionality for a command.
 */
public abstract class CommandBase implements Command, ChangeProvider {

    private final String id;
    private CommandConfiguration commandConfiguration;
    private boolean enabled;
    private List<CommandListener> listeners = new ArrayList<CommandListener>();

    /**
     * @param id unique id for this command
     * @param commandConfiguration configuration details for the command.
     */
    protected CommandBase(String id,
                          CommandConfiguration commandConfiguration) {
        this(id, commandConfiguration, true);
    }

    /**
     * @param id unique id for this command
     * @param commandConfiguration configuration details for the command.
     * @param enabled true if the command is initially enabled.
     */
    protected CommandBase(String id,
                          CommandConfiguration commandConfiguration,
                          boolean enabled) {
        Check.nonEmptyString(id, "id");

        this.id = id;

        setCommandConfiguration(commandConfiguration);
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


    @Override public final void invoke(Project project) {
        project.applyChange(createChange(project));
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
