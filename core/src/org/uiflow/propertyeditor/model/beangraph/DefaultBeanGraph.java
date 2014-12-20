package org.uiflow.propertyeditor.model.beangraph;

import com.badlogic.gdx.math.Vector2;
import org.uiflow.propertyeditor.model.bean.*;
import org.uiflow.propertyeditor.model.bean.dynamic.DynamicBean;
import org.uiflow.utils.Check;

import java.util.*;
import java.util.Map.Entry;

/**
 * Default implementation of BeanGraph.  Can be extended if desired.
 */
public class DefaultBeanGraph implements BeanGraph {

    private final Map<Bean, Vector2> beansAndPositions = new HashMap<Bean, Vector2>();
    private final Map<Bean, Vector2> readOnlyBeansAndPositions = Collections.unmodifiableMap(beansAndPositions);

    private final Set<BeanGraphListener> listeners = new HashSet<BeanGraphListener>();

    private final DynamicBean interfaceBean;
    private final BeanDelegate internalInputBean;
    private final BeanDelegate internalOutputBean;

    public DefaultBeanGraph() {
        this(null);
    }

    public DefaultBeanGraph(String name) {
        interfaceBean = createInterfaceBean();
        interfaceBean.setName(name);

        // Create internal input and output views of the interface bean
        internalInputBean = new NamedPostfixBeanDelegate(interfaceBean, " Inputs");
        internalOutputBean = new NamedPostfixBeanDelegate(interfaceBean, " Outputs");
        addBean(internalOutputBean, -0.5f, 0);
        addBean(internalInputBean, 0.5f, 0);
    }

    @Override public final Bean addBean(Bean bean) {
        return addBean(bean, 0, 0);
    }

    @Override public final Bean addBean(Bean bean, Vector2 position) {
        Check.notNull(position, "position");

        addBean(bean, position.x, position.y);

        return bean;
    }

    @Override public final Bean addBean(Bean bean, float x, float y) {
        Check.notNull(bean, "bean");
        Check.notContained(bean, beansAndPositions.keySet(), "beans");

        // Add bean and position
        final Vector2 position = new Vector2(x, y);
        beansAndPositions.put(bean, position);

        // Notify derived classes and listeners
        onBeanAdded(bean, position);
        notifyBeanAdded(bean, position);

        return bean;
    }

    @Override public final void removeBean(Bean bean) {
        if (bean != null && beansAndPositions.containsKey(bean)) {
            // Get position
            final Vector2 position = beansAndPositions.get(bean);

            // Remove bean
            beansAndPositions.remove(bean);

            // Notify derived classes and listeners
            onBeanRemoved(bean, position);
            notifyBeanRemoved(bean, position);
        }
    }

    @Override public final Collection<Bean> getBeans() {
        return readOnlyBeansAndPositions.keySet();
    }

    @Override public final Map<Bean, Vector2> getBeansAndPositions() {
        return readOnlyBeansAndPositions;
    }

    @Override public final Vector2 getBeanPosition(Bean bean) {
        return getBeanPosition(bean, null);
    }

    @Override public final Vector2 getBeanPosition(Bean bean, Vector2 positionOut) {
        final Vector2 position = beansAndPositions.get(bean);
        if (position == null) throw new IllegalArgumentException("The specified bean '"+bean+"' is not a member of this bean graph");

        if (positionOut != null) {
            positionOut.set(position);
            return positionOut;
        }
        else {
            return position;
        }
    }


    @Override public final void setBeanPosition(Bean bean, Vector2 position) {
        Check.notNull(position, "position");

        setBeanPosition(bean, position.x, position.y);
    }

    @Override public final void setBeanPosition(Bean bean, float x, float y) {
        Check.notNull(bean, "bean");
        Check.contained(bean, beansAndPositions, "beans");

        // Update pos
        final Vector2 position = beansAndPositions.get(bean);
        position.set(x, y);

        // Notify derived classes and listeners
        onBeanMoved(bean, position);
        notifyBeanMoved(bean, position);
    }

    @Override public final boolean containsBean(Bean bean) {
        return beansAndPositions.containsKey(bean);
    }

    @Override public final void addListener(BeanGraphListener listener) {
        Check.notNull(listener, "listener");
        listeners.add(listener);
    }

    @Override public final void removeListener(BeanGraphListener listener) {
        listeners.remove(listener);
    }

    @Override public DynamicBean getInterfaceBean() {
        return interfaceBean;
    }

    @Override public BeanDelegate getInternalInputBean() {
        return internalInputBean;
    }

    @Override public BeanDelegate getInternalOutputBean() {
        return internalOutputBean;
    }

    @Override public final String getName() {
        return interfaceBean.getName();
    }

    @Override public final List<Property> getProperties() {
        return interfaceBean.getProperties();
    }

    @Override public final void addListener(BeanListener listener) {
        interfaceBean.addListener(listener);
    }

    @Override public final void removeListener(BeanListener listener) {
        interfaceBean.removeListener(listener);
    }

    @Override public Property getProperty(String propertyName) {
        return interfaceBean.getProperty(propertyName);
    }

    /**
     * @return a bean containing the input and output properties of this BeanGraph, functioning as an interface to this BeanGraph.
     */
    protected DynamicBean createInterfaceBean() {
        return new DynamicBean();
    }


    /**
     * Called whenever a bean is added.
     */
    protected void onBeanAdded(Bean bean, Vector2 position) {
    }

    /**
     * Called whenever a bean is removed.
     */
    protected void onBeanRemoved(Bean bean, Vector2 position) {
    }

    /**
     * Called when a bean moves.  Can be used to do anything necessary on a move.
     */
    protected void onBeanMoved(Bean bean, Vector2 position) {
    }

    /**
     * Notifies listeners that the specified bean was added.
     */
    protected final void notifyBeanAdded(Bean bean, Vector2 position) {
        for (BeanGraphListener listener : listeners) {
            listener.onBeanAdded(this, bean, position);
        }
    }

    /**
     * Notifies listeners that the specified bean was moved.
     */
    protected final void notifyBeanMoved(Bean bean, Vector2 position) {
        for (BeanGraphListener listener : listeners) {
            listener.onBeanMoved(this, bean, position);
        }
    }

    /**
     * Notifies listeners that the specified bean was removed.
     */
    protected final void notifyBeanRemoved(Bean bean, Vector2 position) {
        for (BeanGraphListener listener : listeners) {
            listener.onBeanRemoved(this, bean, position);
        }
    }


    @Override
    public Bean createCopy() {
        // Create a copy with any references intact

        DefaultBeanGraph copy = new DefaultBeanGraph(getName());

        // Copy interface properties
        for (Property property : interfaceBean.getProperties()) {
            Object value = property.get();
            if (value instanceof Bean) value = ((Bean)value).createCopy();
            copy.interfaceBean.addProperty(property.getName(),
                                           property.getType(),
                                           value,
                                           property.getEditorConfiguration(),
                                           property.getDirection());
        }

        Map<Bean, Bean> sourceToCopy = new HashMap<Bean, Bean>();

        sourceToCopy.put(interfaceBean, copy.interfaceBean);

        // Copy nodes
        for (Entry<Bean, Vector2> entry : beansAndPositions.entrySet()) {
            final Bean bean = entry.getKey();
            final Vector2 pos = entry.getValue();

            final Bean beanCopy = bean.createCopy();
            copy.addBean(beanCopy, pos);

            sourceToCopy.put(bean, beanCopy);
        }

        // Fix references
        for (Bean bean : beansAndPositions.keySet()) {
            for (Property property : bean.getProperties()) {
                final Property source = property.getSource();
                if (source != null) {
                    final Property sourcePropertyCopy = sourceToCopy.get(source.getBean()).getProperty(source.getName());
                    sourceToCopy.get(bean).getProperty(property.getName()).setSource(sourcePropertyCopy);
                }
            }
        }

        sourceToCopy.clear();

        return copy;
    }
}
