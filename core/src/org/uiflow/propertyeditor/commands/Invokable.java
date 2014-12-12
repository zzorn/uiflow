package org.uiflow.propertyeditor.commands;

/**
 *
 */
public interface Invokable {

    /**
     * Invokes a command on the specified target (may be null).
     */
    void invoke(Object target);

}
