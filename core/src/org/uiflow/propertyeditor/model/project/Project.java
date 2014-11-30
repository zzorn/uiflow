package org.uiflow.propertyeditor.model.project;

import org.uiflow.propertyeditor.commands.Change;

/**
 * A project contains zero or more Beans, arranged in some hierarchy (perhaps also tagged, or named?).
 * It may also have a root bean with project specific settings.
 *
 * A project has an undo-redo stack.
 *
 * It also has an increasing version number.
 *
 * It can import beans from other projects, although they can be copy-imported as well, so that the project is stand alone (they
 * remember from what project and what version they came from thou, and there could be functionality to upgrade to newer
 * versions of imported beans if detected/desired).
 *
 * Projects have pseudo-unique packages / names, to facilitate import handling without collisions.
 *
 * Projects provide actions for adding/removing beans to beangraphs, editing bean properties, connecting beans,
 * creating new beans, deleting beans, copying beans (linked or unique), making a linked copy unique, grouping a set of beans
 * in a bean graph into a subgraph, ungrouping a subgraph and re-inserting it into all places where it is used, or only one place,
 * moving beans in a graph, renaming beans in a graph, adding/removing input/output properties to a beangraph,
 * and renaming interface properties of a beangraph.
 *
 * The actions should be easy to add to a menu automatically (a menu could listen to the active project to determine the actions available?)
 * The actions in the menu also depend on the currently edited beangraph, and the selections in it, which is available from the ui.
 * This could be done by the project implementing an ActionProvider interface that the menu listens to (basically has add/removeActionListener methods,
 * where ActionListeners are notified of added/removed/enabled/disabled/renamed actions).
 *
 *
 */
public interface Project {

    /**
     * Applies a change to the project.
     * The change is stored in the undo-redo queue of the project.
     *
     * @param change change to apply to the project.
     */
    void applyChange(Change change);

}
