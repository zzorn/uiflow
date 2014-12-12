package org.uiflow.propertyeditor.commands;

import org.uiflow.utils.HotKey;

/**
 *
 */
public final class DelegatingCommand extends CommandBase {

    private Invokable implementation;

    /**
     * @param id unique id for this command
     * @param name user readable name of the command.
     * @param description user readable description / tooltip for the command.
     * @param iconId id used to look up the icon for the command.
     * @param hotKey hotkey to use for the command, none if null.
     * @param enabled true if the command can be invoked at the moment, false if not.
     * @param implementation code to run when the command is onvoked.
     * @param menuPaths places in menus and toolbars that the command should be put in,
     *                  a path with the name of the menu and submenu names and menu section, separated by periods.
     *                  E.g. MainMenu.File.3_export or MainToolbar.2_edit
     */
    public DelegatingCommand(String id,
                             String name,
                             String description,
                             String iconId,
                             HotKey hotKey,
                             boolean enabled,
                             Invokable implementation,
                             String... menuPaths) {
        super(id, name, description, iconId, hotKey, enabled, menuPaths);

        this.implementation = implementation;
    }

    @Override public void invoke(Object target) {
        if (implementation != null) {
            implementation.invoke(target);
        }
    }

    public Invokable getImplementation() {
        return implementation;
    }

    public void setImplementation(Invokable implementation) {
        this.implementation = implementation;
    }
}
