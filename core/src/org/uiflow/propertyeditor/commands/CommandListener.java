package org.uiflow.propertyeditor.commands;

/**
 * Listens to a command
 */
public interface CommandListener {

    void onCommandEnableChanged(Command command, boolean enabled);

    void onCommandConfigurationChanged(Command command, CommandConfiguration commandConfiguration);

}
