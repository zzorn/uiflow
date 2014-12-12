package org.uiflow.propertyeditor.commands;

/**
 *
 */
public interface CommandProviderListener {

    void commandAdded(CommandProvider commandProvider, Command command);

    void commandRemoved(CommandProvider commandProvider, Command command);

    void commandEnabledChanged(CommandProvider commandProvider, Command command, boolean enabled);

    void commandConfigChanged(CommandProvider commandProvider, Command command);

}
