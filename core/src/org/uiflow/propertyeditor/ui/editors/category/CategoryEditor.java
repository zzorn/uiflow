package org.uiflow.propertyeditor.ui.editors.category;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.propertyeditor.model.category.Category;
import org.uiflow.propertyeditor.model.category.CategoryListener;
import org.uiflow.propertyeditor.ui.BeanDropTarget;
import org.uiflow.propertyeditor.ui.DefaultBeanDropTarget;
import org.uiflow.propertyeditor.ui.editors.EditorBase;

/**
 *
 */
public class CategoryEditor extends EditorBase<Category, CategoryEditorConfiguration>  {

    private Tree treeRoot;

    private final CategoryListener categoryListener = new CategoryListener() {
        @Override public void subcategoryAdded(Category parent, Category subCategory) {
            addNode(parent, createCategoryNode(subCategory));
        }

        @Override public void beanAdded(Category parent, Bean bean) {
            addNode(parent, createBeanNode(bean));
        }

        @Override public void subcategoryRemoved(Category parent, Category subCategory) {
            removeNode(parent, subCategory);
        }

        @Override public void beanRemoved(Category parent, Bean bean) {
            removeNode(parent, bean);
        }
    };

    public CategoryEditor() {
        this(CategoryEditorConfiguration.DEFAULT);
    }

    public CategoryEditor(CategoryEditorConfiguration configuration) {
        super(configuration);
    }

    @Override protected Actor createEditor(CategoryEditorConfiguration configuration, UiContext uiContext) {
        treeRoot = new Tree(uiContext.getSkin());
        treeRoot.setUserObject(this);

        return treeRoot;
    }

    @Override protected void updateValueInUi(Category value) {
        // Clear current tree
        treeRoot.clearChildren();

        // Build new tree using the new category as root
        // I wish Tree and Tree.Node in libgdx would implement the same interface with regard to child node management..  Would eliminate some code duplication.
        for (Category subCategory : value.getSubcategories()) {
            treeRoot.add(createCategoryNode(subCategory));
        }
        for (Bean bean : value.getBeans()) {
            treeRoot.add(createBeanNode(bean));
        }
    }

    private Tree.Node createCategoryNode(final Category category) {
        Label categoryLabel = new Label(category.getName(), getUiContext().getSkin());

        // Create node
        Tree.Node node = new Tree.Node(categoryLabel);
        node.setObject(category);
        node.setSelectable(true);

        // Listen to dropped beans
        categoryLabel.setUserObject(new DefaultBeanDropTarget(category));

        // Add subcategories
        for (Category subCategory : category.getSubcategories()) {
            node.add(createCategoryNode(subCategory));
        }

        // Add other children
        for (Bean bean : category.getBeans()) {
            node.add(createBeanNode(bean));
        }

        return node;
    }

    private Tree.Node createBeanNode(final Bean bean) {
        final Label beanLabel = new Label(bean.getName(), getUiContext().getSkin());

        // Listen to drags
        beanLabel.addListener(new DragListener() {
            @Override
            public void dragStop(final InputEvent event, final float x, final float y, final int pointer) {
                final Actor actor = getChildAt(beanLabel, event, true);
                final Object userObject = actor.getUserObject();
                if (userObject != null && userObject instanceof BeanDropTarget) {
                    BeanDropTarget beanDropTarget = (BeanDropTarget) userObject;
                    beanDropTarget.handleBeanDrop(bean, null, actor, x, y);
                }
            }
        });


        Tree.Node node = new Tree.Node(beanLabel);
        node.setObject(bean);
        node.setSelectable(true);

        return node;
    }


    @Override protected void setDisabled(boolean disabled) {
    }

    @Override protected void onValueChanged(Category oldValue, Category newValue) {
        if (oldValue != null) oldValue.removeListener(categoryListener);
        if (newValue != null) newValue.addListener(categoryListener);
    }


    private void addNode(Category parent, Tree.Node categoryNode) {
        if (getValue() == parent) {
            treeRoot.add(categoryNode);
        }
        else {
            for (Tree.Node node : treeRoot.getNodes()) {
                if (addNode(node, parent, categoryNode)) return;
            }
        }
    }

    private boolean addNode(Tree.Node node, Category parent, Tree.Node nodeToAdd) {
        if (node == null) {
            return false;
        }
        else if (node.getObject() == parent) {
            node.add(nodeToAdd);
            return true;
        }
        else {
            for (Tree.Node childNode : node.getChildren()) {
                if (addNode(childNode, parent, nodeToAdd)) return true;
            }

            // Parent node not found
            return false;
        }
    }

    private void removeNode(Category parent, Object objectToRemove) {
        if (getValue() == parent) {
            // Remove from root nodes in tree
            Tree.Node nodeToRemove = null;
            for (Tree.Node node : treeRoot.getRootNodes()) {
                if (node.getObject() == objectToRemove) {
                    nodeToRemove = node;
                    break;
                }
            }
            if (nodeToRemove != null) treeRoot.remove(nodeToRemove);
        }
        else {
            // Try removing from child nodes
            for (Tree.Node node : treeRoot.getNodes()) {
                if (removeNode(node, parent, objectToRemove)) return;
            }
        }
    }

    private boolean removeNode(Tree.Node node, Category parent, Object objectToRemove) {
        if (node == null) {
            return false;
        }
        else if (node.getObject() == parent) {
            Tree.Node nodeToRemove = null;
            for (Tree.Node childNode : node.getChildren()) {
                if (childNode.getObject() == objectToRemove) {
                    nodeToRemove = childNode;
                    break;
                }
            }
            if (nodeToRemove != null) node.remove(nodeToRemove);
            return true;
        }
        else {
            for (Tree.Node childNode : node.getChildren()) {
                if (removeNode(childNode, parent, objectToRemove)) return true;
            }

            // Parent node not found
            return false;
        }
    }



    private Actor getChildAt(final Actor widget, InputEvent event, final boolean touchableOnly) {
        Actor actor = event.getStage().hit(event.getStageX(), event.getStageY(), touchableOnly);

        // Find parent actor that is within the work area
        while (actor != null && actor.getParent() != null && actor.getParent() != widget) {
            actor = actor.getParent();
        }

        return actor;
    }


}
