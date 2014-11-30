package org.uiflow.propertyeditor.ui.menu;

import org.uiflow.propertyeditor.commands.CommandInvoker;
import org.uiflow.propertyeditor.commands.CommandProvider;

/**
 * Menubar that various actions can be added to.
 */
// TODO: Add possibility to listen to action providers, that will add/remove and enable/disable or rename actions depending on their
//       current state
// TODO: Sort sections alphabetically in a menu?  Allowing number prefixes to indicate relative position.  Or just add in first come basis?  (although things like exit are by default last).
public class MenuBar implements CommandInvoker {
    // TODO


    @Override public void addCommandProvider(CommandProvider commandProvider) {
        // TODO: Implement

    }

    @Override public void removeCommandProvider(CommandProvider commandProvider) {
        // TODO: Implement

    }
}
