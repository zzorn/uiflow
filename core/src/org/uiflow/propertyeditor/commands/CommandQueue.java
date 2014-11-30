package org.uiflow.propertyeditor.commands;

import com.badlogic.gdx.Input;
import org.uiflow.propertyeditor.model.project.Project;
import org.uiflow.utils.Check;
import org.uiflow.utils.HotKey;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Maintains undo-redo queue of commands for some object, and invokes the commands as they are issued.
 */
public final class CommandQueue extends CommandProviderBase {

    private final Project project;

    private Deque<Change> undoQueue = new ArrayDeque<Change>();
    private Deque<Change> redoQueue = new ArrayDeque<Change>();

    private final CommandBase undoCommand;
    private final CommandBase redoCommand;

    public CommandQueue(Project project) {
        Check.notNull(project, "project");
        this.project = project;

        undoCommand = addCommand(new CommandBase("undo", new CommandConfigurationImpl("Undo",
                                                                                      "Undoes the last change",
                                                                                      "undo",
                                                                                      HotKey.control(Input.Keys.Z),
                                                                                      "menu.edit.10_undoredo",
                                                                                      "toolbar.20_undoredo")) {
            @Override public Change createChange(Project project) {
                final CommandQueue commandQueue = CommandQueue.this;

                return new ChangeBase() {
                    @Override public boolean apply(Project project) {
                        commandQueue.undo();
                        return false;
                    }
                };
            }
        });

        redoCommand = addCommand(new CommandBase("redo", new CommandConfigurationImpl("Redo",
                                                                                      "Redoes the last undoed change",
                                                                                      "redo",
                                                                                      HotKey.control(Input.Keys.Z).andShift(),
                                                                                      "menu.edit.10_undoredo",
                                                                                      "toolbar.20_undoredo")) {
            @Override public Change createChange(Project project) {
                final CommandQueue commandQueue = CommandQueue.this;

                return new ChangeBase() {
                    @Override public boolean apply(Project project) {
                        commandQueue.redo();
                        return false;
                    }
                };
            }
        });
    }

    public void applyChange(Change change) {
        Check.notNull(change, "change");

        final boolean undoable = change.apply(project);
        if (undoable) {
            undoQueue.push(change);
            redoQueue.clear();
        }

        updateCommands();
    }

    public void undo() {
        if (undoQueue.isEmpty()) {
            throw new IllegalStateException("No change to undo");
        }
        else {
            final Change changeToUndo = undoQueue.pop();
            changeToUndo.undo(project);
            redoQueue.push(changeToUndo);
        }

        updateCommands();
    }

    public void redo() {
        if (redoQueue.isEmpty()) {
            throw new IllegalStateException("No change to redo");
        }
        else {
            final Change changeToRedo = redoQueue.pop();
            changeToRedo.apply(project);
            undoQueue.push(changeToRedo);
        }

        updateCommands();
    }

    public boolean canUndo() {
        return !undoQueue.isEmpty();
    }

    public boolean canRedo() {
        return !redoQueue.isEmpty();
    }

    public String getUndoDescription() {
        return getChangeDescription(undoQueue);
    }

    public String getRedoDescription() {
        return getChangeDescription(redoQueue);
    }

    public void clearUndoRedoQueues() {
        undoQueue.clear();
        redoQueue.clear();

        updateCommands();
    }

    private String getChangeDescription(final Deque<Change> queue) {
        if (queue.isEmpty()) return "";
        else return queue.peek().getDescription();
    }

    private void updateCommands() {
        undoCommand.setEnabled(canUndo());
        redoCommand.setEnabled(canRedo());

        // TODO: Set descriptions to describe the actions to undo / redo
    }

}
