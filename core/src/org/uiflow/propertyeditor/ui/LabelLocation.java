package org.uiflow.propertyeditor.ui;

/**
 * Location of a label relative to the UI element it describes.
 */
public enum LabelLocation {

    /**
     * Locate label to the left of the content.
     */
    LEFT(true),

    /**
     * Locate label above the content.
     */
    ABOVE(true),

    /**
     * Locate label below the content.
     */
    BELOW(false),

    /**
     * Do not show a label at all.
     */
    NONE(false)
    ;

    private final boolean before;


    LabelLocation(boolean before) {
        this.before = before;
    }

    /**
     * @return true if the label is before the content in normal reading order.
     */
    public boolean isBefore() {
        return before;
    }


}
