package org.uiflow.propertyeditor.ui.project;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import org.uiflow.UiContext;
import org.uiflow.widgets.FlowWidget;
import org.uiflow.widgets.FlowWidgetBase;

/**
 * Tree list with the contents of a project/library (the beans in it, plus a project bean with project global settings)
 */
public class ProjectInventoryUi extends FlowWidgetBase {

    @Override protected Actor createUi(UiContext uiContext) {
        final Tree tree = new Tree(uiContext.getSkin());

//        tree.add(new Tree.Node());


        return tree;
    }
}
