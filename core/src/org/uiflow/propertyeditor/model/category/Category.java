package org.uiflow.propertyeditor.model.category;

import org.uiflow.propertyeditor.model.BeanContainer;

import java.util.List;

/**
 * Contains zero or more other categories and beans.
 */
public interface Category extends BeanContainer<CategoryListener> {

    String getName();

    String getDescription();

    String getIconId();

    List<Category> getSubcategories();

    void addSubcategory(Category category);

    void removeSubcategory(Category category);
}
