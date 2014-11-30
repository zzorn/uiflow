package org.uiflow.propertyeditor.commands;

/**
 * Allows reconfiguring the default locations of commands in the menu or toolbar, and the hotkeys used for them.
 */
public interface CommandConfigurationProvider  {

    CommandConfiguration getCommandConfig(String commandId, CommandConfiguration defaultConfiguration);

}


