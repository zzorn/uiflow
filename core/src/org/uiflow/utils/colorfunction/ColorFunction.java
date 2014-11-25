package org.uiflow.utils.colorfunction;

import com.badlogic.gdx.graphics.Color;

/**
 * Returns color for a specific value.
 */
public interface ColorFunction {

    /**
     * @param value value to calculate color for.
     * @param colorOut color to update according to the provided value.
     * @return the colorOut if non null, otherwise new color instance.
     */
    Color getColor(double value, Color colorOut);

}
