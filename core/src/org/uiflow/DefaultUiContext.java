package org.uiflow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Default implementation of the UiContext.
 */
public class DefaultUiContext implements UiContext {

    public static final String DIALOG_FONT_NAME = "default-font";
    public static final String DIALOG_LARGE_FONT_NAME = "dialog-large-font";


    private static final String FONT_DIR = "uiflow/fonts/";
    private static final String UIFLOW_SKIN_BASE_NAME = "uiflow/skin/uiskin";
    private static final String DIALOG_FONT_FILE = "135atom_sans";

    private static final int DIALOG_FONT_HEIGHT_ON_PC = 32;
    private static final int MIN_DIALOG_FONT_HEIGHT_PIXELS = 16;
    private static final double LARGE_FONT_SCALE_FACTOR = 1.6;
    private static final float PC_DPI = 76;

    private static final float GAP_PIXELS_ON_PC = 8;
    private static final float SMALL_GAP_FACTOR = 0.3f;
    private static final float LARGE_GAP_FACTOR = 3f;
    private static final int MIN_GAP_SIZE = 2;

    private final Skin skin;
    private final TextureAtlas textureAtlas;
    private final float gapSize;

    /**
     * Uses the default skin and a font height depending on the height of the screen, and no custom textureAtlas.
     */
    public DefaultUiContext() {
        this(null);
    }

    /**
     * Uses the default skin and a font height depending on the height of the screen, and the user provided textureAtlas for addition UI graphic elements.
     *
     * @param textureAtlas texture atlas used to load icons and such for the UI.  Can be null if no custom UI graphics is accessed through UiContext.
     */
    public DefaultUiContext(TextureAtlas textureAtlas) {
        this(textureAtlas, calculatePixelSize(DIALOG_FONT_HEIGHT_ON_PC, MIN_DIALOG_FONT_HEIGHT_PIXELS));
    }

    /**
     * Uses the default skin.
     *
     * @param textureAtlas texture atlas used to load icons and such for the UI.  Can be null if no custom UI graphics is accessed through UiContext.
     * @param fontHeightPcPixels height of the default font, in approximately pc screen sized pixels.
     */
    public DefaultUiContext(TextureAtlas textureAtlas, int fontHeightPcPixels) {
        this(textureAtlas, calculatePixelSize(fontHeightPcPixels, fontHeightPcPixels), null);
    }

    /**
     * @param textureAtlas texture atlas used to load icons and such for the UI.  Can be null if no custom UI graphics is accessed through UiContext.
     * @param fontHeightAbsolutePixels height of the default font in absolute pixels on the current device.
     * @param skin the skin to use for the ui.  If null, the default skin is loaded.
     */
    public DefaultUiContext(TextureAtlas textureAtlas, int fontHeightAbsolutePixels, Skin skin) {
        this(textureAtlas, fontHeightAbsolutePixels, skin, calculatePixelSize(GAP_PIXELS_ON_PC, MIN_GAP_SIZE));
    }

    /**
     * @param textureAtlas texture atlas used to load icons and such for the UI.  Can be null if no custom UI graphics is accessed through UiContext.
     * @param fontHeightAbsolutePixels height of the default font in absolute pixels on the current device.
     * @param skin the skin to use for the ui.  If null, the default skin is loaded.
     * @param gapSize size of the default medium gap to use in UI, in screeen pixels.
     */
    public DefaultUiContext(TextureAtlas textureAtlas, int fontHeightAbsolutePixels, Skin skin, float gapSize) {
        if (skin == null) {
            skin = loadDefaultSkin(fontHeightAbsolutePixels);
        }

        this.gapSize = gapSize;
        this.skin = skin;
        this.textureAtlas = textureAtlas;
    }

    public Skin getSkin() {
        return skin;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    @Override public float getSmallGap() {
        return gapSize * SMALL_GAP_FACTOR;
    }

    @Override public float getGap() {
        return gapSize;
    }

    @Override public float getLargeGap() {
        return gapSize * LARGE_GAP_FACTOR;
    }

    @Override public void dispose() {
        if (skin != null) skin.dispose();
        if (textureAtlas != null) textureAtlas.dispose();
    }


    public static Skin loadDefaultSkin(int fontHeightPixels) {
        // Load skin with custom sized font

        // Create skin object, initialize texture atlas
        Skin skin = new Skin(new TextureAtlas(Gdx.files.internal(UIFLOW_SKIN_BASE_NAME +".atlas")));

        // Create bitmap fonts from true type fonts and add them to the style
        final String dialogFontFileLocation = FONT_DIR + DIALOG_FONT_FILE + ".ttf";
        addFontToSkin(skin, DIALOG_FONT_NAME, dialogFontFileLocation, fontHeightPixels);
        addFontToSkin(skin, DIALOG_LARGE_FONT_NAME, dialogFontFileLocation, (int) (fontHeightPixels * LARGE_FONT_SCALE_FACTOR));

        // Load json file with style settings
        skin.load(Gdx.files.internal(UIFLOW_SKIN_BASE_NAME + ".json"));

        return skin;
    }

    public static void addFontToSkin(Skin skin,
                                     final String fontName,
                                     final String pathToFontFile,
                                     final int fontSize_pixels) {
        final BitmapFont font = createFont(pathToFontFile, fontSize_pixels);
        skin.add(fontName, font, BitmapFont.class);
    }

    public static BitmapFont createFont(final String pathToFontFile, int fontSize_pixels) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(pathToFontFile));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Adjust size to available
        parameter.size = fontSize_pixels;

        BitmapFont font = generator.generateFont(parameter);

        // Dispose generator
        generator.dispose();

        return font;
    }

    public static int calculatePixelSize(final float pcSource_pixels, final int minTarget_pixels) {
        float currentScreenDPI = 160f * Gdx.graphics.getDensity();

        int size = (int) (pcSource_pixels * (currentScreenDPI / PC_DPI));

        if (size < minTarget_pixels) size = minTarget_pixels;

        return size;
    }


}
