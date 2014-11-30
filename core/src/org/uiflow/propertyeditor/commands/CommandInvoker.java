package org.uiflow.propertyeditor.commands;

/**
 * Something that provide the user (or a script) with some way to invoke commands.
 */
public interface CommandInvoker {
    void addCommandProvider(CommandProvider commandProvider);
    void removeCommandProvider(CommandProvider commandProvider);
}
