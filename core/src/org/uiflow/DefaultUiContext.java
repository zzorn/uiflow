package org.uiflow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Default implementation of the UiContext.
 */
public class DefaultUiContext implements UiContext {

    private final Skin skin;
    private final TextureAtlas textureAtlas;

    public DefaultUiContext(Skin skin, TextureAtlas textureAtlas) {
        this.skin = skin;
        this.textureAtlas = textureAtlas;
    }

    public Skin getSkin() {
        return skin;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
}
