package org.uiflow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A context passed around to ui widgets that are created.
 * Contains ways to load textures, get the default skin, etc.
 */
public interface UiContext {

    /**
     * @return the skin to use for the UI components.
     */
    Skin getSkin();

    /**
     * @return optional texture atlas with custom UI graphics for user defined UI elements.  Null if not specified.
     */
    TextureAtlas getTextureAtlas();

    /**
     * @return small gap for UI layout.
     */
    float getSmallGap();

    /**
     * @return medium gap for UI layout.
     */
    float getGap();

    /**
     * @return large gap for UI layout.
     */
    float getLargeGap();

    /**
     * Disposes any loaded skins, textures, and other files.
     */
    void dispose();
}
