package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;

/**
 *
 */
public abstract class ChangeBase implements Change {

    private String description;

    protected ChangeBase() {
        this(null);
    }

    public ChangeBase(String description) {
        this.description = description;
    }

    @Override public void undo(Project project) {
    }

    @Override public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
