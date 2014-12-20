package org.uiflow.propertyeditor.model.category;

import org.uiflow.propertyeditor.model.BeanContainerBase;
import org.uiflow.propertyeditor.model.bean.Bean;
import org.uiflow.utils.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DefaultCategory extends BeanContainerBase<CategoryListener> implements Category {

    private Category parentCategory;
    private String name;
    private String description;
    private String iconId;
    private final List<Category> subCategories = new ArrayList<Category>();
    private transient List<Category> readOnlySubCategories;

    public DefaultCategory(String name, Object ... contents) {
        this(name, null, null, contents);
    }

    public DefaultCategory(String name, String iconId, Object ... contents) {
        this(name, iconId, null, contents);
    }

    public DefaultCategory(String name, String iconId, String description, Object ... contents) {
        this.name = name;
        this.description = description;
        this.iconId = iconId;

        for (Object content : contents) {
            if (content != null && content instanceof Category) {
                addSubcategory((Category) content);
            }
            else if (content != null && content instanceof Bean) {
                addBean((Bean) content);
            }
            else {
                throw new IllegalArgumentException("Unknown content " + content + (content != null ? " of type " + content.getClass() : ""));
            }
        }
    }

    @Override public Category getParentCategory() {
        return parentCategory;
    }

    @Override public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    @Override public List<Category> getSubcategories() {
        if (readOnlySubCategories == null) readOnlySubCategories = Collections.unmodifiableList(subCategories);

        return readOnlySubCategories;
    }

    @Override public void addSubcategory(Category category) {
        Check.notNull(category, "category");

        if (!subCategories.contains(category)) {
            // Remove from previous tree
            if (category.getParentCategory() != null) {
                category.getParentCategory().removeSubcategory(category);
            }
            category.setParentCategory(this);

            subCategories.add(category);

            // Notify listeners
            for (CategoryListener listener : getListeners()) {
                listener.subcategoryAdded(this, category);
            }
        }
    }

    @Override public void removeSubcategory(Category category) {
        Check.notNull(category, "category");

        boolean removed = subCategories.remove(category);
        if (removed) {
            category.setParentCategory(null);

            // Notify listeners
            for (CategoryListener listener : getListeners()) {
                listener.subcategoryRemoved(this, category);
            }
        }
    }

    @Override protected void onBeanAdded(Bean bean) {
        for (CategoryListener listener : getListeners()) {
            listener.beanAdded(this, bean);
        }
    }

    @Override protected void onBeanRemoved(Bean bean) {
        for (CategoryListener listener : getListeners()) {
            listener.beanRemoved(this, bean);
        }
    }


}
