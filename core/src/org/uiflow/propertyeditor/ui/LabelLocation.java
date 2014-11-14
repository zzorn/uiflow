package org.uiflow.propertyeditor.ui;

/**
 * Location of a label relative to the UI element it describes.
 */
public enum LabelLocation {

    LEFT(true),
    ABOVE(true),
    UNDER(false),
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
