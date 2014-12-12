package org.uiflow.propertyeditor.commands;

import org.uiflow.propertyeditor.model.project.Project;
import org.uiflow.utils.HotKey;

import java.util.List;

/**
 *
 */
// TODO: Create icon object that takes one drawable and creates disabled, hovered, and pressed versions from it.
public interface Command extends Invokable {

    /**
     * @return unique id for this command.  Used for configuration, internationalization, etc.
     */
    String getId();

    /**
     * @return user readable short name.
     */
    String getName();

    /**
     * @return user readable description.
     */
    String getDescription();

    /**
     * @return id of icon for this command, or null for none.
     */
    String getIconId();


    /**
     * @return hotkey to activate this command, or 0 if none.
     */
    HotKey getHotKey();

    /**
     * @return locations in menu structures.
     *         Denotes a path from a main menu to sub menu to section in menu (use "" as the default section in a menu.).
     *         Different menus/toolbars have different root names.
     */
    List<String> getMenuPaths();

    /**
     * @return true if this command is currently enabled (can be invoked).
     */
    boolean isEnabled();

    /**
     * @param listener listens to changes in this command (e.g. enable/disable).
     */
    void addListener(CommandListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(CommandListener listener);

}
