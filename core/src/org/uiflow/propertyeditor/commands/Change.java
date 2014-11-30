package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;

/**
 * A change to a project.
 * May be addition/removal/modification/configuration of a BeanGraph, Bean, or Property.
 * Custom state change can also be supported for beans using some blob type serializable state objects.
 */
public interface Change {

    /**
     * @param project project to apply the change to.
     * @return true if the change is undoable and should be stored in an undo queue,
     *         false if the change can not be undone, and should not be stored in the undo queue (e.g. saving a project or exporting some result).
     */
    boolean apply(Project project);

    void undo(Project project);

    /**
     * @return describes the change briefly.  Shows up in undo/redo menu items.
     */
    String getDescription();
}
