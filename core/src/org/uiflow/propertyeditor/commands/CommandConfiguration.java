package org.uiflow.propertyeditor.commands;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.uiflow.utils.HotKey;

import java.util.List;

/**
 * Configuration for a command that can change depending on settings.
 */
// TODO: Add listener?
public interface CommandConfiguration {

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

}
