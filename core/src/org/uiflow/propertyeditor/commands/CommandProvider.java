package org.uiflow.propertyeditor.commands;

import java.util.Collection;

/**
 *
 */
public interface CommandProvider {

    /**
     * @return the commands available from this command provider.
     */
    Collection<Command> getCommands();

    void addCommandListener(CommandProviderListener listener);

    void removeCommandListener(CommandProviderListener listener);

}
