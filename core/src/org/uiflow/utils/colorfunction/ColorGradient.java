package org.uiflow.utils.colorfunction;

import com.badlogic.gdx.graphics.Color;
import org.uiflow.utils.MathUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.uiflow.utils.MathUtils.*;

/**
 * Simple color gradient.
 */
public class ColorGradient implements ColorFunction {

    private static final Color DEFAULT_COLOR = Color.BLACK;

    private static final Color NICE_GREEN = new Color(0, 0.8f, 0, 1);
    private static final Color NICE_YELLOW = new Color(0.9f, 0.9f, 0, 1);
    private static final Color NICE_RED = new Color(0.9f, 0, 0, 1);

    private final TreeMap<Double, Color> colors = new TreeMap<Double, Color>();

    public static final ColorGradient GREYSCALE = new ColorGradient(Color.BLACK, Color.WHITE);
    public static final ColorGradient COMPUTER_COLORS = new ColorGradient(Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA);
    public static final ColorGradient SPECTRUM = new ColorGradient(Color.BLACK, Color.BLUE, NICE_GREEN, Color.YELLOW, Color.ORANGE, Color.RED);
    public static final ColorGradient RED_YELLOW = new ColorGradient(Color.RED, Color.ORANGE, Color.YELLOW);
    public static final ColorGradient FLAME = new ColorGradient(Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.WHITE);
    public static final ColorGradient BLUE_RED = new ColorGradient(Color.BLUE, new Color(0.7f, 0, 0.7f, 1), Color.RED);
    public static final ColorGradient GREEN_RED = new ColorGradient(NICE_GREEN, NICE_YELLOW, NICE_RED);
    public static final ColorGradient RED_GREEN = new ColorGradient(NICE_RED, NICE_YELLOW, NICE_GREEN);
    public static final ColorGradient RED_GREEN_RED = new ColorGradient(NICE_RED, NICE_YELLOW, NICE_GREEN, NICE_YELLOW, NICE_RED);

    /**
     * Create a new empty color gradient.
     */
    public ColorGradient() {
        this(null);
    }

    /**
     * Create a new color gradient with the specified initial colors.
     */
    public ColorGradient(Map<Double, Color> colors) {
        if (colors != null) {
            for (Map.Entry<Double, Color> entry : colors.entrySet()) {
                addColor(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Create a new color gradient with the specified colors at regular intervals between -1 and 1.
     */
    public ColorGradient(Color firstColor, Color ... additionalColors) {
        this(-1, 1, firstColor, additionalColors);
    }

    /**
     * Create a new color gradient with the specified colors at regular intervals between the specified minimum and maximum value.
     */
    public ColorGradient(double min, double max, Color firstColor, Color ... additionalColors) {
        // Add first color
        addColor(min, firstColor);

        // Add additional colors
        if (additionalColors.length > 0) {
            double step = (max - min) * (1.0 / additionalColors.length);
            double v = min + step;

            for (Color color: additionalColors) {
                addColor(v, color);
                v += step;
            }
        }
    }


    /**
     * Adds a color to the gradient
     * @param value value for the color
     * @param r red component
     * @param g green component
     * @param b blue component
     */
    public void addColor(double value, double r, double g, double b) {
        addColor(value, r, g, b, 1);
    }

    /**
     * Adds a color to the gradient
     * @param value value for the color
     * @param r red component
     * @param g green component
     * @param b blue component
     * @param a alpha component
     */
    public void addColor(double value, double r, double g, double b, double a) {
        addColor(value, new Color((float) r,
                                  (float) g,
                                  (float) b,
                                  (float) a));
    }

    /**
     * Adds a color to the gradient
     * @param value value for the color
     * @param color color for the specified value
     */
    public void addColor(double value, Color color) {
        colors.put(value, color.cpy());
    }

    /**
     * Removes the color closest to the specified value.
     */
    public void removeColor(double value) {
        final Map.Entry<Double, Color> floor = colors.floorEntry(value);
        final Map.Entry<Double, Color> ceiling = colors.ceilingEntry(value);

        // Remove closest existing entry.
        if (floor != null || ceiling != null) {
            if (floor == null) colors.remove(ceiling.getKey());
            else if (ceiling == null) colors.remove(floor.getKey());
            else {
                final double distanceToFloor = value - floor.getKey();
                final double distanceToCeiling = ceiling.getKey() - value;
                if (distanceToFloor < distanceToCeiling) colors.remove(floor.getKey());
                else colors.remove(ceiling.getKey());
            }
        }
    }

    /**
     * @param value value to calculate color for.
     * @param colorOut color to update according to the provided value.
     *                 Set to the color for the specified point in the gradient.
     *                 Interpolated if between colors, but clamped to end colors if outside the gradient range.
     * @return the provided colorOut with the correct color, or a new Color if colorOut was null.
     */
    public Color getColor(double value, Color colorOut) {
        final Map.Entry<Double, Color> floor = colors.floorEntry(value);
        final Map.Entry<Double, Color> ceiling = colors.ceilingEntry(value);

        if (colorOut == null) colorOut = new Color();

        if (floor == null && ceiling == null) colorOut.set(DEFAULT_COLOR);
        else if (floor == null) colorOut.set(ceiling.getValue());
        else if (ceiling == null) colorOut.set(floor.getValue());
        else {
            if (value == floor.getKey()) colorOut.set(floor.getValue());
            else if (value == ceiling.getKey()) colorOut.set(ceiling.getValue());
            else {
                float t = (float) map(value, floor.getKey(), ceiling.getKey(), 0, 1);
                final Color fCol = floor.getValue();
                final Color cCol = ceiling.getValue();

                float r = mix(t, fCol.r, cCol.r);
                float g = mix(t, fCol.g, cCol.g);
                float b = mix(t, fCol.b, cCol.b);
                float a = mix(t, fCol.a, cCol.a);
                colorOut.set(r, g, b, a);
            }
        }

        return colorOut;
    }

    /**
     * Removes all colors from the gradient.
     */
    public void clear() {
        colors.clear();
    }

    /**
     * @return the underlying treemap.
     * Modifications to it are allowed, although not normally necessary.
     */
    public TreeMap<Double, Color> getColors() {
        return colors;
    }

}
