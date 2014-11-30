package org.uiflow.propertyeditor.commands;

/**
 * Maintains undo-redo queue of commands for some object, and invokes the commands as they are issued.
 */
public interface CommandQueue {

    void handleCommandInvocation(Command command);

}
