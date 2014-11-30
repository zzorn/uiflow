package org.uiflow.propertyeditor.commands;

/**
 *
 */
// TODO: Need to think through the case of commands with parameter state
// TODO: Commands cause changes.  Changes have state, commands not..
// TODO: So command could produce a change object, that then gets applied or unapplied to a model
// TODO: Unapplying a change packet is usually quite challenging thou.  But doable, as we only have to worry about beans, properties and beangraphs.  Bean instance type needs to be handled (type needs to have a no-arg constructor).  Custom bean state may be supported with an optional get/set extra state command (also used for serialization).
// TODO: We may need unique ids for beans, beangraphs, properties, etc - but we might also be able to use the path from the project root to refer to them (as long as we can always get the parent/host of an object - so we'll need to re-add property graph / project fields to beans).

// TODO: Create icon object that takes one drawable and creates disabled, hovered, and pressed versions from it.
public interface Command {

    /**
     * @return unique id for this command.  Used for configuration, internationalization, etc.
     */
    String getId();

    /**
     * @return default configuration for this command, including name, description, icon, hotkey, and menu location(s).
     */
    CommandConfiguration getCommandConfiguration();

    /**
     * Invokes the command.
     */
    void invoke();

    /**
     * @return true if this command is currently enabled (can be invoked).
     */
    boolean isEnabled();

    /**
     * @param listener listens to changes in this command (e.g. enable/disable).
     */
    void addListener(CommandListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(CommandListener listener);

    /**
     * @return the command queue that this command uses to handle undo and redo of itself, or null if it does not use any command queue.
     */
    CommandQueue getQueue();

}
