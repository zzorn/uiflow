package org.uiflow.propertyeditor.model;

/**
 * Whether a property can be used as an input, output, or both.
 */
public enum PropertyDirection {

    IN(true, false),
    OUT(false, true),
    INOUT(true, true)
    ;

    private final boolean input;
    private final boolean output;

    PropertyDirection(boolean input, boolean output) {
        this.input = input;
        this.output = output;
    }

    public boolean isInput() {
        return input;
    }

    public boolean isOutput() {
        return output;
    }

    public boolean canConnect(PropertyDirection other) {
        return (isInput() && other.isOutput()) ||
               (isOutput() && other.isInput());
    }
}
