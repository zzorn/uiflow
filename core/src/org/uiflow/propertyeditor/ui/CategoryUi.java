package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import org.uiflow.UiContext;
import org.uiflow.widgets.FlowWidgetBase;

/**
 *
 */
public class CategoryUi extends FlowWidgetBase {

    @Override protected Actor createUi(UiContext uiContext) {
        final Tree tree = new Tree(uiContext.getSkin());

//        tree.add(new Tree.Node());


        return tree;
    }

}
