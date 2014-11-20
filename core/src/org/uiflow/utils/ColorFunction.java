package org.uiflow.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Returns color for a specific value.
 */
public interface ColorFunction {

    /**
     *
     * @param value value to calculate color for.
     * @param colorOut color to update according to the provided value.
     */
    void getColor(double value, Color colorOut);

}
