package org.uiflow.propertyeditor.commands;

import org.uiflow.utils.Check;

/**
 * Delegates actual command execution
 */
public final class DelegatingCommand<T> extends CommandBase<T> {

    private final UndoableCommand<T> undoableCommand;

    public DelegatingCommand(String id,
                             CommandConfiguration commandConfiguration,
                             UndoableCommand<T> undoableCommand) {
        super(id, commandConfiguration);
        Check.notNull(undoableCommand, "undoableCommand");
        this.undoableCommand = undoableCommand;
    }

    public DelegatingCommand(String id,
                             CommandConfiguration commandConfiguration,
                             CommandQueue commandQueue,
                             UndoableCommand<T> undoableCommand) {
        super(id, commandConfiguration, commandQueue);
        Check.notNull(undoableCommand, "undoableCommand");
        this.undoableCommand = undoableCommand;
    }

    public DelegatingCommand(String id,
                             CommandConfiguration commandConfiguration,
                             CommandQueue commandQueue,
                             boolean enabled,
                             UndoableCommand<T> undoableCommand) {
        super(id, commandConfiguration, commandQueue, enabled);
        Check.notNull(undoableCommand, "undoableCommand");
        this.undoableCommand = undoableCommand;
    }

    @Override public T doCommand() {
        return undoableCommand.doCommand();
    }

    @Override public void undoCommand(T undoData) {
        undoableCommand.undoCommand(undoData);
    }
}
