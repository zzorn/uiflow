package org.uiflow.utils.colorfunction;

import com.badlogic.gdx.graphics.Color;

/**
 * A color function with a fixed value.
 */
public final class FixedColorFunction implements ColorFunction {

    private final Color color = new Color();

    public FixedColorFunction(Color color) {
        if (color == null) color = Color.WHITE;

        this.color.set(color);
    }

    @Override public Color getColor(double value, Color colorOut) {
        if (colorOut == null) {
            colorOut = new Color(color);
        }
        else {
            colorOut.set(color);
        }
        return colorOut;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }
}
