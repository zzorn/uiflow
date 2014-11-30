package org.uiflow.propertyeditor.commands;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.uiflow.utils.HotKey;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CommandConfigurationImpl implements CommandConfiguration {
    private String name;
    private String description;
    private String iconId;
    private HotKey hotKey;
    private List<String> menuPaths = new ArrayList<String>();

    public CommandConfigurationImpl(String name,
                                    String description,
                                    String iconId,
                                    HotKey hotKey,
                                    String ... menuPaths) {
        this.name = name;
        this.description = description;
        this.iconId = iconId;
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

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public HotKey getHotKey() {
        return hotKey;
    }

    public HotKey setHotKey(HotKey hotKey) {
        this.hotKey = hotKey;
        return hotKey;
    }

    public List<String> getMenuPaths() {
        return menuPaths;
    }

    public void setMenuPaths(List<String> menuPaths) {
        this.menuPaths = menuPaths;
    }
}
