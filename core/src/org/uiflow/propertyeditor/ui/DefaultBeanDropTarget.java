package org.uiflow.propertyeditor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.uiflow.propertyeditor.model.BeanContainer;
import org.uiflow.propertyeditor.model.bean.Bean;

/**
 *
 */
public class DefaultBeanDropTarget implements BeanDropTarget {

    private final BeanContainer beanContainer;
    private final boolean copyBean;

    public DefaultBeanDropTarget(final BeanContainer beanContainer) {
        this(beanContainer, true);
    }

    public DefaultBeanDropTarget(final BeanContainer beanContainer, final boolean copyBean) {
        this.beanContainer = beanContainer;
        this.copyBean = copyBean;
    }

    @Override
    public boolean handleBeanDrop(final Bean sourceBean, final BeanContainer sourceContainer, final Actor uiComponent, final float x, final float y) {
        if (sourceBean != null && isBeanAcceptable(sourceBean)) {
            Bean bean = copyBean ? sourceBean.createCopy() : sourceBean;
            addBean(bean, x, y);
            return true;
        }
        else {
            return false;
        }
    }

    protected void addBean(final Bean bean, final float x, final float y) {
        beanContainer.addBean(bean);
    }

    protected boolean isBeanAcceptable(Bean bean) {
        return true;
    }
}
