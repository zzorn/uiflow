package org.uiflow.propertyeditor.commands;

import java.util.Collection;

/**
 *
 */
// TODO: Should it have a command queue or be provided one?  Implementation specific detail maybe, add to CommandProviderBase.
public interface CommandProvider {

    /**
     * @return the commands available from this command provider.
     */
    Collection<Command> getCommands();

    void addCommandListener(CommandProviderListener listener);

    void removeCommandListener(CommandProviderListener listener);

}
