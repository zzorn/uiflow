package org.uiflow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A context passed around to ui widgets that are created.
 * Contains ways to load textures, get the default skin, etc.
 */
public interface UiContext {

    Skin getSkin();

    TextureAtlas getTextureAtlas();

}
