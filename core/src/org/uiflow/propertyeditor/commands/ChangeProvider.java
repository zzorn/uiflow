package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;

/**
 *
 */
public interface ChangeProvider {

    /**
     * @param project project to create the change for.
     * @return change object that contains modifications to do to the project, and that contains state that can undo the changes as well.
     */
    Change createChange(Project project);

}
