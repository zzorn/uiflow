package org.uiflow.propertyeditor.ui.editors.beangraph;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.*;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;
import org.uiflow.propertyeditor.model.beangraph.BeanGraphListener;
import org.uiflow.propertyeditor.ui.editors.EditorBase;
import org.uiflow.propertyeditor.ui.editors.bean.BeanEditor;
import org.uiflow.propertyeditor.ui.editors.bean.LabelLocation;
import org.uiflow.propertyeditor.ui.editors.bean.PropertyUi;
import org.uiflow.utils.Check;

import java.util.*;

/**
 *
 */
// TODO: Creating and deleting connections
// TODO: Updating value based on source
// TODO: Hide editor if source provided (maybe show value?)
// TODO: Add preview component
// TODO: Some kind of procedural generator example.
// TODO: Selecting beans and creating a group
// TODO: Copying a bean by clone or link

// TODO: Support closing and opening animations
// TODO: Support pan
// TODO: Support zoom?  Maybe keep beans fixed size, just zoom positions
// TODO: Support deleting beans
// TODO: Support bean palettes that can be used to add new beans to a graph?
// TODO: How to support undo/redo or version control?
public class BeanGraphEditor extends EditorBase<BeanGraph, BeanGraphConfiguration> {

    private static final String EDITOR_BACKGROUND = "frame";
    private static final int DRAG_BUTTON = Input.Buttons.LEFT;

    private Table workArea;
    private Table connectionLayer;
    private final Map<Container<Actor>, Bean> containersToBeans = new HashMap<Container<Actor>, Bean>();
    private final Map<Bean, Container<Actor>> beansToContainers = new HashMap<Bean, Container<Actor>>();

    private final Map<Bean, BeanEditor> beanEditors = new HashMap<Bean, BeanEditor>();
    private final List<Connection> connections = new ArrayList<Connection>();

    private Bean draggedBean;
    private final Vector2 dragOffset = new Vector2();

    private final BeanGraphListener graphListener = new BeanGraphListener() {
        @Override public void onBeanAdded(BeanGraph beanGraph, Bean bean, Vector2 position) {
            addBean(bean, position, false, null);
        }

        @Override public void onBeanMoved(BeanGraph beanGraph, Bean bean, Vector2 position) {
            moveBean(bean, position);
        }

        @Override public void onBeanRemoved(BeanGraph beanGraph, Bean bean, Vector2 position) {
            removeBean(bean, position);
        }
    };

    private final BeanListener connectionListener = new BeanListenerAdapter() {
        @Override public void onPropertyAdded(Bean bean, Property property) {
            if (property.getSource() != null) {
                final PropertyUi sourceUi = getPropertyUi(property.getSource());
                final PropertyUi targetUi = getPropertyUi(property);
                addConnection(sourceUi, targetUi);
            }
        }

        @Override public void onPropertyRemoved(Bean bean, Property property) {
            removeConnectionsWith(property);
        }

        @Override public void onSourceChanged(Bean bean, Property property, Property oldSource, Property newSource) {
            if (oldSource != null) {
                removeConnection(oldSource, property);
            }

            if (newSource != null) {
                final PropertyUi sourceUi = getPropertyUi(newSource);
                final PropertyUi targetUi = getPropertyUi(property);
                addConnection(sourceUi, targetUi);
            }
        }
    };

    private final InputListener dragListener = new InputListener() {
        @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!event.isHandled() && button == DRAG_BUTTON) {
                // Find touched bean
                final Actor beanContainer = getChildIn(BeanGraphEditor.this.workArea, event);
                final Bean bean = containersToBeans.get(beanContainer);
                if (bean != null) {
                    // Start drag
                    draggedBean = bean;
                    dragOffset.set(beanContainer.getCenterX() - x,
                                   beanContainer.getCenterY() - y);
                    getValue().setBeanPosition(bean,
                                               x + dragOffset.x,
                                               y + dragOffset.y);
                    return true;
                }
            }
            return false;
        }

        @Override public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (!event.isHandled() && draggedBean != null) {
                // Handle drag
                getValue().setBeanPosition(draggedBean,
                                           x + dragOffset.x,
                                           y + dragOffset.y);
            }
        }

        @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (!event.isHandled() && draggedBean != null && button == DRAG_BUTTON) {
                // Set at final position
                getValue().setBeanPosition(draggedBean,
                                           x + dragOffset.x,
                                           y + dragOffset.y);
                // Stop drag
                draggedBean = null;
            }
        }
    };

    public BeanGraphEditor() {
        this(new BeanGraphConfiguration());
    }



    public BeanGraphEditor(BeanGraphConfiguration configuration) {
        super(configuration);
    }

    @Override protected Actor createEditor(BeanGraphConfiguration configuration, UiContext uiContext) {
        final Table table = new Table(uiContext.getSkin());
        table.setBackground(uiContext.getSkin().getDrawable(EDITOR_BACKGROUND));

        workArea = new Table(uiContext.getSkin());
        workArea.setClip(true);

        /*
        connectionLayer = new Table(uiContext.getSkin());
        connectionLayer.setClip(true);

        // Stack the bean layer and connection layer on top of each other
        Stack stack = new Stack();
        stack.add(workArea);
        stack.add(connectionLayer);
        table.add(stack).fill().expand();
        */
        connectionLayer = workArea;
        table.add(workArea).fill().expand();

        // Listen to moves
        workArea.addListener(dragListener);

        // Add any beans that are in the current value
        rebuildGraphUi(getValue());

        return table;
    }

    private Actor getChildIn(final Table widget, InputEvent event) {
        Actor actor = event.getTarget();

        // Find parent actor that is within the work area
        while (actor.getParent() != null && actor.getParent() != widget) {
            actor = actor.getParent();
        }

        return actor;
    }

    @Override protected void onValueChanged(BeanGraph oldValue, BeanGraph newValue) {
        if (oldValue != null) {
            oldValue.removeGraphListener(graphListener);
        }

        if (isUiCreated()) {
            rebuildGraphUi(newValue);
        }

        if (newValue != null) {
            newValue.addGraphListener(graphListener);
        }
    }

    private void rebuildGraphUi(BeanGraph beanGraph) {
        // Remove any old bean editors
        for (Container<Actor> container : containersToBeans.keySet()) {
            workArea.removeActor(container);
        }
        for (BeanEditor beanEditor : beanEditors.values()) {
            beanEditor.dispose();
        }
        for (Connection connection : connections) {
            connectionLayer.removeActor(connection);
        }
        beansToContainers.clear();
        containersToBeans.clear();
        beanEditors.clear();
        connections.clear();

        // Add editors for beans in bean graph
        if (beanGraph != null) {
            for (Map.Entry<Bean, Vector2> entry : beanGraph.getBeansAndPositions().entrySet()) {
                // Get bean and position
                final Bean bean = entry.getKey();
                final Vector2 position = entry.getValue();

                // Check for interface beans
                PropertyDirection directionToShow = null;
                boolean mirrorDirections = false;
                if (bean == getValue().getInternalInputBean()) {
                    directionToShow = PropertyDirection.IN;
                    mirrorDirections = true;
                }
                else if (bean == getValue().getInternalOutputBean()) {
                    directionToShow = PropertyDirection.OUT;
                    mirrorDirections = true;
                }

                // Add bean to UI
                addBean(bean, position, mirrorDirections, directionToShow);
            }
        }

    }

    private void addBean(final Bean bean, Vector2 position, boolean mirrorDirections, PropertyDirection directionToShow) {
        Check.notNull(bean, "bean");
        Check.notNull(position, "position");
        Check.notContained(bean, beanEditors.keySet(), "beans");

        if (isUiCreated()) {
            // Create editor
            BeanEditor beanEditor = new BeanEditor(LabelLocation.LEFT, true, mirrorDirections, directionToShow, getConfiguration().isHideEditorWhenSourceUsed());
            beanEditor.setValue(bean);
            beanEditors.put(bean, beanEditor);

            // Create editor UI
            final Actor beanEditorUi = beanEditor.getUi(getUiContext());
            final Container<Actor> beanEditorUiContainer = new Container<Actor>(beanEditorUi);
            containersToBeans.put(beanEditorUiContainer, bean);
            beansToContainers.put(bean, beanEditorUiContainer);
            workArea.addActor(beanEditorUiContainer);

            // Update editor UI position
            beanEditorUiContainer.setCenterPosition(position.x, position.y);

            // Find new connections
            for (Property target : bean.getProperties()) {
                addConnection(target);
            }

            // Listen to connections made and removed
            bean.addListener(connectionListener);
        }
    }

    private void moveBean(Bean bean, Vector2 position) {
        Check.notNull(bean, "bean");
        Check.notNull(position, "position");
        Check.contained(bean, beanEditors.keySet(), "beans");

        if (isUiCreated()) {
            // Get editor UI
            final Container<Actor> ui = beansToContainers.get(bean);
            // (Get rid of occasional artifacts by rounding coordinates to integers)
            ui.setPosition((int) (position.x - ui.getWidth() * 0.5f),
                           (int) (position.y - ui.getHeight() * 0.5f));
            ui.layout();

            // Lift up bean and any connections connecting to it
            moveToFront(bean);
        }
    }

    /**
     * Makes the specified bean topmost in the UI.
     */
    private void moveToFront(Bean bean) {
        if (bean != null) {
            // Lift connections
            // (Loop from last to first connections, so that the connections to properties higher up cover the ones below).
            for (int i = connections.size() - 1; i >= 0; i--) {
                Connection connection = connections.get(i);
                if (bean.getProperties().contains(connection.getSourceProperty()) ||
                    bean.getProperties().contains(connection.getTargetProperty())) {
                    connection.toFront();
                }
            }

            // Lift bean
            final Container<Actor> ui = beansToContainers.get(bean);
            ui.toFront();
        }
    }

    private void removeBean(Bean bean, Vector2 position) {
        if (isUiCreated()) {
            bean.removeListener(connectionListener);

            // Find editor
            final BeanEditor beanEditor = beanEditors.get(bean);

            if (beanEditor != null) {
                // Remove bean editor
                beanEditor.setValue(null);
                beanEditor.dispose();
                beanEditors.remove(bean);

                final Container<Actor> container = beansToContainers.get(bean);
                beansToContainers.remove(bean);
                containersToBeans.remove(container);
                workArea.removeActor(container);

                removeConnectionsWith(bean);
            }
        }
    }


    private void addConnection(Property target) {
        if (target != null) {
            final Property source = target.getSource();
            if (source != null) {
                final PropertyUi sourceUi = getPropertyUi(source);
                final PropertyUi targetUi = getPropertyUi(target);
                addConnection(sourceUi, targetUi);
            }
        }
    }

    private void addConnection(PropertyUi source, PropertyUi target) {
        if (source != null && target != null) {
            final Connection connection = new Connection(getUiContext(), source, target);
            connectionLayer.addActor(connection);
            connections.add(connection);
        }
    }

    private void removeConnection(Property source, Property target) {
        for (Iterator<Connection> iterator = connections.iterator(); iterator.hasNext(); ) {
            Connection connection = iterator.next();
            if (connection.getSourceProperty() == source &&
                connection.getTargetProperty() == target) {
                iterator.remove();
                connectionLayer.removeActor(connection);
            }
        }
    }

    private void removeConnectionsWith(Bean bean) {
        for (Property property : bean.getProperties()) {
            removeConnectionsWith(property);
        }
    }

    private void removeConnectionsWith(Property removedProperty) {
        // Remove all connections that connect to the removed property
        for (Iterator<Connection> iterator = connections.iterator(); iterator.hasNext(); ) {
            Connection connection = iterator.next();
            if (connection.getSourceProperty() == removedProperty ||
                connection.getTargetProperty() == removedProperty) {
                iterator.remove();
                connectionLayer.removeActor(connection);
            }
        }
    }

    /**
     * @return the UI for the specified property, if it exists in this BeanGraphEditor.
     */
    private PropertyUi getPropertyUi(Property property) {
        // Find bean that the property is in
        if (property != null && property.getBean() != null) {
            Bean bean = property.getBean();

            // Handle special case for graph internal interface beans
            if (bean == getValue().getInterfaceBean()) {
                if (property.getDirection().isOutput()) {
                    bean = getValue().getInternalOutputBean();
                }
                else {
                    bean = getValue().getInternalInputBean();
                }
            }

            // Find editor for the bean
            final BeanEditor beanEditor = beanEditors.get(bean);
            if (beanEditor != null) {

                // Find property UI from the editor
                return beanEditor.getPropertyUi(property);
            }
        }

        return null;
    }



    @Override protected void updateValueInUi(BeanGraph value) {
        // Nothing to do here
    }

    @Override protected void setDisabled(boolean disabled) {
        // TODO: Implement

    }
}
