package org.uiflow.propertyeditor.model.category;

import org.uiflow.propertyeditor.model.BeanContainerListener;

/**
 *
 */
public interface CategoryListener extends BeanContainerListener {

    void subcategoryAdded(Category parent, Category subCategory);

    void subcategoryRemoved(Category parent, Category subCategory);

}
