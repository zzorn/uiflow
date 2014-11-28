package org.uiflow.propertyeditor.model.bean;

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

    /**
     * @return reverse direction from this direction if reverse is true, else this direction.
     */
    public PropertyDirection getReverse(boolean reverse) {
        if (reverse) return getReverse();
        else return this;
    }

    public PropertyDirection getReverse() {
        switch (this) {
            case IN:
                return OUT;
            case OUT:
                return IN;
            case INOUT:
                return INOUT;
        }
        throw new IllegalStateException("Unhandled case " + this);
    }
}
