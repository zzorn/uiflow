package org.uiflow.propertyeditor.model.project;

import org.uiflow.propertyeditor.commands.*;

import java.util.Collection;

/**
 *
 */
public class DefaultProject extends CommandProviderBase implements Project {

    private final CommandQueue commandQueue;

    public DefaultProject() {
        commandQueue = new CommandQueue(this);
        addCommandProvider(commandQueue);
    }

    @Override public final void applyChange(Change change) {
        commandQueue.applyChange(change);
    }

}
