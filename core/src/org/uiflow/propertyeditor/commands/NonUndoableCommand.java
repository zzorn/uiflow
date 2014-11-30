package org.uiflow.propertyeditor.commands;

/**
 *
 */
public abstract class NonUndoableCommand implements UndoableCommand<Object> {

    @Override public final Object doCommand() {
        runCommand();
        return null;
    }

    @Override public final void undoCommand(Object undoData) {
        // Not provided
    }

    protected abstract void runCommand();
}
