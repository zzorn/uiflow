package org.uiflow.propertyeditor.commands;

/**
 *
 */
public interface UndoableCommand<T> {

    T doCommand();

    void undoCommand(T undoData);

}
