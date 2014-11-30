package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;
import org.uiflow.utils.Check;

/**
 * Delegates change creation
 */
public final class DelegatingCommand extends CommandBase {

    private final ChangeProvider changeProvider;

    public DelegatingCommand(String id,
                             CommandConfiguration commandConfiguration,
                             ChangeProvider changeProvider) {
        this(id, commandConfiguration, true, changeProvider);
    }

    public DelegatingCommand(String id,
                             CommandConfiguration commandConfiguration,
                             boolean enabled,
                             ChangeProvider changeProvider) {
        super(id, commandConfiguration, enabled);
        Check.notNull(changeProvider, "changeProvider");
        this.changeProvider = changeProvider;
    }

    @Override public Change createChange(Project project) {
        return changeProvider.createChange(project);
    }

}
