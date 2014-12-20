package org.uiflow.propertyeditor.model.category;

import org.uiflow.propertyeditor.model.BeanContainerListener;
import org.uiflow.propertyeditor.model.bean.Bean;

/**
 *
 */
public interface CategoryListener {

    void subcategoryAdded(Category parent, Category subCategory);

    void subcategoryRemoved(Category parent, Category subCategory);

    void beanAdded(Category parent, Bean bean);

    void beanRemoved(Category parent, Bean bean);


}
