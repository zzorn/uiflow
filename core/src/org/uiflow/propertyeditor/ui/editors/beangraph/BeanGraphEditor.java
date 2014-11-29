package org.uiflow.propertyeditor.ui.editors.beangraph;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.uiflow.UiContext;
import org.uiflow.propertyeditor.model.bean.*;
import org.uiflow.propertyeditor.model.beangraph.BeanGraph;
import org.uiflow.propertyeditor.model.beangraph.BeanGraphListener;
import org.uiflow.propertyeditor.ui.editors.EditorBase;
import org.uiflow.propertyeditor.ui.editors.bean.BeanEditor;
import org.uiflow.propertyeditor.ui.editors.bean.ConnectorButton;
import org.uiflow.propertyeditor.ui.editors.bean.LabelLocation;
import org.uiflow.propertyeditor.ui.editors.bean.PropertyUi;
import org.uiflow.utils.Check;
import org.uiflow.utils.MathUtils;
import org.uiflow.utils.ScrollInputListener;

import java.util.*;

/**
 * Editor for BeanGraphs.
 */
// TODO: Smaller font, shrink editors to normal UI size where possible.

// TODO: Add selection support to beans (click, ctrl-click, or ctrl-drag / left mouse drag).  Show selected beans with highlighted background and border.  Click background area once to clear selection)
// TODO: Support deleting selected beans (delete, or delete button in UI)
// TODO: Support creating a group of selected beans
// TODO: Project concept.  Has a list of (or tree, or hierarchy, or tagged) bean (graph)s.  Importing other projects as libraries?
// TODO: Support bean palettes that can be used to add new beans to a graph (either other beans/bean graphs from the current project, or beans/beangraphs from builtin projects or imported projects.

// TODO: Add support for value conversion

// TODO: How to support undo/redo or version control? - handle undo-redo on the project model level?  Listen to edits, removals, etc, put them in edit history.

// TODO: Add support for copying a bean (linking to common source and updating from it, or making a new deep copy)
// TODO: Add support for making a group unique (breaking link to common source)

// TODO: Adjust bean feature size (font, hiding editors, connection width, connector size) depending on zoom level
// TODO: Support closing and opening animations

// TODO: Add preview component
// TODO: Some kind of procedural generator example.
public class BeanGraphEditor extends EditorBase<BeanGraph, BeanGraphConfiguration> {

    private static final String EDITOR_BACKGROUND = "frame";

    private static final int DRAG_BUTTON = Input.Buttons.LEFT;

    private static final int CONNECTION_DRAG_BUTTON = DRAG_BUTTON;
    private static final int PAN_BUTTON = Input.Buttons.RIGHT;
    private static final int CANCEL_DRAG_BUTTON = PAN_BUTTON;

    private static final float MIN_ZOOM = 1f / 8f;
    private static final float MAX_ZOOM = 8f;

    private Table workArea;
    private Table connectionLayer;
    private final Map<Container<Actor>, Bean> containersToBeans = new HashMap<Container<Actor>, Bean>();
    private final Map<Bean, Container<Actor>> beansToContainers = new HashMap<Bean, Container<Actor>>();

    private final Map<Bean, BeanEditor> beanEditors = new HashMap<Bean, BeanEditor>();
    private final List<Connection> connections = new ArrayList<Connection>();

    private Bean draggedBean;
    private final Vector2 dragOffset = new Vector2();

    private float zoom = 1;
    private Vector2 viewPan = new Vector2(0.5f, 0);


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

        private final Vector2 tempPos = new Vector2();

        @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!event.isHandled() && button == DRAG_BUTTON) {
                // Find touched bean
                final Actor beanContainer = getChildAt(BeanGraphEditor.this.workArea, event, false);
                if (beanContainer instanceof Container) {
                    final Bean bean = containersToBeans.get(beanContainer);
                    if (bean != null) {
                        // Start drag
                        draggedBean = bean;
                        dragOffset.set(beanContainer.getCenterX() - x,
                                       beanContainer.getCenterY() - y);
                        setBeanPos(x, y);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (!event.isHandled() && draggedBean != null) {
                // Handle drag
                setBeanPos(x, y);
            }
        }

        @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (!event.isHandled() && draggedBean != null && button == DRAG_BUTTON) {
                // Set at final position
                setBeanPos(x, y);

                // Stop drag
                draggedBean = null;
            }
        }

        private void setBeanPos(float x, float y) {
            tempPos.set(x + dragOffset.x, y + dragOffset.y);
            workAreaToGraphCoordinates(tempPos);
            getValue().setBeanPosition(draggedBean, tempPos);
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

        workArea = new Table(uiContext.getSkin()) {
            @Override public void layout() {
                super.layout();

                // When table size changed, relayout the contents
                repositionBeanEditors();
            }
        };
        workArea.setClip(true);

        // Receive mouse events for whole area, not just children
        workArea.setTouchable(Touchable.enabled);

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

        // Listen to connectors
        connectionLayer.addListener(createConnectionListener());

        // Listen to pan and zoom
        workArea.addListener(new ScrollInputListener() {
            private boolean panning = false;
            private Vector2 viewCenterOnPanStart = new Vector2();
            private Vector2 panOffset = new Vector2();
            private Vector2 panPos = new Vector2();

            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled() && !panning) {
                    Actor actor = event.getStage().hit(event.getStageX(), event.getStageY(), false);
                    if (actor == workArea) {
                        panning = true;
                        viewCenterOnPanStart.set(getViewCenter());
                        panOffset.set(viewCenterOnPanStart);
                        graphToWorkAreaCoordinates(panOffset);
                        panOffset.sub(x, y);
                        return true;
                    }
                }

                return false;
            }

            @Override public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!event.isHandled() && panning) {
                    updatePanning(x, y);
                }
            }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled() && panning) {
                    updatePanning(x, y);
                    panning = false;
                }
            }

            @Override public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (!event.isHandled()) {
                    Actor actor = event.getStage().hit(event.getStageX(), event.getStageY(), false);
                    if (actor == workArea) {
                        setZoom(getZoom() * (float) Math.pow(2, -amount * 0.5), x, y);
                        return true;
                    }
                }

                return false;
            }

            private void updatePanning(float x, float y) {
                panPos.set(x, y).add(panOffset);
                workAreaToGraphCoordinates(panPos, viewCenterOnPanStart);
                setViewCenter(panPos);
            }
        });

        // Add any beans that are in the current value
        rebuildGraphUi(getValue());

        return table;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        setZoom(zoom, workArea.getCenterX(), workArea.getCenterY());
    }

    public void setZoom(float zoom, float xOrigin, float yOrigin) {
        Check.positive(zoom, "zoom");

        // xOrigin and yOrigin should be at the same position after zooming
        Vector2 pos = new Vector2();
        pos.set(xOrigin, yOrigin);
        workAreaToGraphCoordinates(pos);

        this.zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);

        Vector2 pos2 = new Vector2();
        pos2.set(xOrigin, yOrigin);
        workAreaToGraphCoordinates(pos2);
        viewPan.add(pos2.sub(pos));

        repositionBeanEditors();
    }

    public Vector2 getViewCenter() {
        return viewPan;
    }

    public void setViewCenter(Vector2 panning) {
        setViewCenter(panning.x, panning.y);
    }

    public void setViewCenter(float x, float y) {
        viewPan.set(x, y);

        repositionBeanEditors();
    }

    private InputListener createConnectionListener() {
        return new InputListener() {

            private Connection draggedConnection;

            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final ConnectorButton connector = getParentOfType(ConnectorButton.class, event.getTarget());
                if (connector != null) {
                    final boolean isInput = connector.isInput();
                    final PropertyUi propertyUi = connector.getPropertyUi();

                    if (button == CONNECTION_DRAG_BUTTON) {
                        // If we already have a dragged connection, connect it to this property if possible
                        if (draggedConnection != null) {
                            connect(propertyUi, isInput);
                        }
                        else {
                            if (isInput && propertyUi.getProperty() != null && propertyUi.getProperty().getSource() != null) {
                                // Disconnect input when clicked
                                propertyUi.getProperty().setSource(null);
                            }
                            else {
                                // Start drag
                                final PropertyUi source = isInput ? null : propertyUi;
                                final PropertyUi target = isInput ? propertyUi : null;
                                draggedConnection = createUnfinishedConnection(source, target);
                                updateDragPosition(event);
                            }
                        }

                        return true;
                    }
                }
                else if (button == CONNECTION_DRAG_BUTTON) {
                    // Cancel drag if it was active
                    return cancelDrag();
                }

                return false;
            }

            @Override public void touchDragged(InputEvent event, float x, float y, int pointer) {
                updateDragPosition(event);
            }

            @Override public boolean mouseMoved(InputEvent event, float x, float y) {
                return updateDragPosition(event);
            }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (draggedConnection != null) {
                    // Check if we are above a connector
                    final ConnectorButton connectorButton = getParentOfType(ConnectorButton.class, getActorAt(event));
                    if (connectorButton != null) {
                        final boolean isInput = connectorButton.isInput();
                        final PropertyUi propertyUi = connectorButton.getPropertyUi();

                        // Only try connecting to other buttons than the one we started from
                        if (!draggedConnection.containsPropertyUi(propertyUi)) {
                            // Try connecting
                            connect(connectorButton.getPropertyUi(), isInput);
                        }
                    }
                    else {
                        // Button up on no target, drop connection
                        cancelDrag();
                    }
                }
            }

            private Actor getActorAt(InputEvent event) {
                return event.getStage().hit(event.getStageX(), event.getStageY(), true);
            }

            private boolean updateDragPosition(InputEvent event) {
                if (draggedConnection != null) {
                    // Update dragged position
                    final float stageX = event.getStageX();
                    final float stageY = event.getStageY();

                    if (draggedConnection.getSource() == null) {
                        draggedConnection.setStartPos(stageX, stageY);
                    }
                    else if (draggedConnection.getTarget() == null) {
                        draggedConnection.setEndPos(stageX, stageY);
                    }

                    // Check if we could connect it here
                    final ConnectorButton connectorButton = getParentOfType(ConnectorButton.class, getActorAt(event));
                    if (connectorButton != null) {
                        final boolean isTarget = connectorButton.isInput();
                        final PropertyUi propertyUi = connectorButton.getPropertyUi();
                        if (draggedConnection.canConnectTo(propertyUi, isTarget)) {
                            draggedConnection.setHighlightState(Connection.ConnectionHighlight.ACCEPTABLE);
                        }
                        else {
                            draggedConnection.setHighlightState(Connection.ConnectionHighlight.INVALID);
                        }
                    }
                    else {
                        draggedConnection.setHighlightState(Connection.ConnectionHighlight.DRAGGED);
                    }


                    return true;
                }
                else {
                    return false;
                }
            }

            private void connect(final PropertyUi propertyUi, final boolean providedUiIsTarget) {
                if (draggedConnection != null) {
                    if (draggedConnection.canConnectTo(propertyUi, providedUiIsTarget)) {
                        // Connect
                        draggedConnection.connectTo(propertyUi, providedUiIsTarget);
                    }
                    else {
                        // Wasn't acceptable, remove connection
                        cancelDrag();
                    }

                    draggedConnection = null;
                }
            }

            private boolean cancelDrag() {
                if (draggedConnection != null) {
                    removeUnfinishedConnection(draggedConnection);
                    draggedConnection = null;
                    return true;
                }
                else {
                    return false;
                }
            }
        };
    }

    private Actor getChildIn(final Table widget, InputEvent event) {
        Actor actor = event.getTarget();

        // Find parent actor that is within the work area
        while (actor.getParent() != null && actor.getParent() != widget) {
            actor = actor.getParent();
        }

        return actor;
    }

    private Actor getChildAt(final Actor widget, InputEvent event, final boolean touchableOnly) {
        Actor actor = event.getStage().hit(event.getStageX(), event.getStageY(), touchableOnly);

        // Find parent actor that is within the work area
        while (actor != null && actor.getParent() != null && actor.getParent() != widget) {
            actor = actor.getParent();
        }

        return actor;
    }

    private <T extends Actor> T getParentOfType(Class<T> desiredType, Actor actor) {
        // Find parent actor that is within the work area
        while (actor != null && !desiredType.isInstance(actor)) {
            actor = actor.getParent();
        }

        return (T) actor;
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

    private void repositionBeanEditors() {
        Vector2 pos = new Vector2();
        final BeanGraph beanGraph = getValue();
        if (isUiCreated() && beanGraph != null) {
            for (Map.Entry<Bean, Container<Actor>> entry : beansToContainers.entrySet()) {
                final Bean bean = entry.getKey();
                final Container<Actor> beanEditorUiContainer = entry.getValue();

                beanGraph.getBeanPosition(bean, pos);
                graphToWorkAreaCoordinates(pos);
                beanEditorUiContainer.setPosition(pos.x, pos.y);
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
            setViewPosition(position, beanEditorUi);

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
            setViewPosition(position, ui);

            // Lift up bean and any connections connecting to it
            moveToFront(bean);
        }
    }

    private void setViewPosition(Vector2 graphPos, Actor ui) {
        Vector2 viewPos = graphToWorkAreaCoordinates(graphPos.cpy()).sub(ui.getWidth() * 0.5f, ui.getHeight() * 0.5f);
        ui.setPosition(viewPos.x, viewPos.y);
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


    private Connection createUnfinishedConnection(PropertyUi source, PropertyUi target) {
        final Connection connection = new Connection(getUiContext(), source, target);
        connectionLayer.addActor(connection);
        connections.add(connection);
        return connection;
    }

    private void removeUnfinishedConnection(Connection connection) {
        connection.dispose();
        connectionLayer.removeActor(connection);
        connections.remove(connection);
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
                connection.dispose();
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
                connection.dispose();
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

    public Vector2 graphToWorkAreaCoordinates(Vector2 graphCoordinates) {
        float scale = 0.5f * Math.max(1, Math.min(workArea.getWidth(), workArea.getHeight()));
        graphCoordinates.mulAdd(viewPan, 1)
                        .scl(zoom)
                        .scl(scale)
                        .add(workArea.getWidth() * 0.5f, workArea.getHeight() * 0.5f);
        return graphCoordinates;
    }

    public Vector2 workAreaToGraphCoordinates(Vector2 workAreaCoordinates) {
        return workAreaToGraphCoordinates(workAreaCoordinates, viewPan);
    }

    private Vector2 workAreaToGraphCoordinates(Vector2 workAreaCoordinates, Vector2 panning) {
        float scale = 0.5f * Math.max(1, Math.min(workArea.getWidth(), workArea.getHeight()));
        workAreaCoordinates.sub(workArea.getWidth() * 0.5f, workArea.getHeight() * 0.5f)
                           .scl(1f / scale)
                           .scl(1f / zoom)
                           .mulAdd(panning, -1);

        return workAreaCoordinates;
    }


    @Override protected void updateValueInUi(BeanGraph value) {
        // Nothing to do here
    }

    @Override protected void setDisabled(boolean disabled) {
        // TODO: Implement

    }



}
