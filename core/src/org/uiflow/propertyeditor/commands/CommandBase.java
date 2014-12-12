package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;
import org.uiflow.utils.Check;
import org.uiflow.utils.HotKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Common functionality for a command.
 */
public abstract class CommandBase implements Command {

    private final String id;
    private String name;
    private String description;
    private String iconId;
    private HotKey hotKey;
    private List<String> menuPaths = new ArrayList<String>();

    private boolean enabled;
    private List<CommandListener> listeners = new ArrayList<CommandListener>();

    /**
     * @param id unique id for this command
     * @param name user readable name of the command.
     * @param description user readable description / tooltip for the command.
     * @param iconId id used to look up the icon for the command.
     * @param hotKey hotkey to use for the command, none if null.
     * @param enabled true if the command can be invoked at the moment, false if not.
     * @param menuPaths places in menus and toolbars that the command should be put in,
     *                  a path with the name of the menu and submenu names and menu section, separated by periods.
     *                  E.g. MainMenu.File.3_export or MainToolbar.2_edit
     */
    public CommandBase(String id,
                       String name,
                       String description,
                       String iconId,
                       HotKey hotKey,
                       boolean enabled,
                       String ... menuPaths) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconId = iconId;
        this.hotKey = hotKey;
        this.enabled = enabled;

        for (String menuPath : menuPaths) {
            if (menuPath != null) this.menuPaths.add(menuPath);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyConfigurationChanged();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyConfigurationChanged();
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
        notifyConfigurationChanged();
    }

    public HotKey getHotKey() {
        return hotKey;
    }

    public HotKey setHotKey(HotKey hotKey) {
        this.hotKey = hotKey;
        notifyConfigurationChanged();
        return hotKey;
    }

    public List<String> getMenuPaths() {
        return menuPaths;
    }

    public void setMenuPaths(List<String> menuPaths) {
        this.menuPaths = menuPaths;
        notifyConfigurationChanged();
    }

    @Override public final String getId() {
        return id;
    }


    @Override public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            notifyEnableChanged();
        }
    }


    @Override public final void addListener(CommandListener listener) {
        Check.notNull(listener, "listener");

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override public final void removeListener(CommandListener listener) {
        listeners.remove(listener);
    }


    private void notifyEnableChanged() {
        for (CommandListener listener : listeners) {
            listener.onCommandEnableChanged(this, enabled);
        }
    }

    private void notifyConfigurationChanged() {
        for (CommandListener listener : listeners) {
            listener.onCommandConfigurationChanged(this);
        }
    }
}
