package org.uiflow.propertyeditor.commands;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class CommandConfigurationImpl implements CommandConfiguration {
    private String name;
    private String description;
    private Drawable icon;
    private int hotKey;
    private List<String> menuPaths = new ArrayList<String>();

    public CommandConfigurationImpl(String name,
                                    String description,
                                    Drawable icon,
                                    int hotKey,
                                    String ... menuPaths) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.hotKey = hotKey;

        for (String menuPath : menuPaths) {
            if (menuPath != null) this.menuPaths.add(menuPath);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable  getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getHotKey() {
        return hotKey;
    }

    public void setHotKey(int hotKey) {
        this.hotKey = hotKey;
    }

    public List<String> getMenuPaths() {
        return menuPaths;
    }

    public void setMenuPaths(List<String> menuPaths) {
        this.menuPaths = menuPaths;
    }
}
