package org.uiflow.propertyeditor.model.project;

import org.uiflow.propertyeditor.commands.*;
import org.uiflow.propertyeditor.model.category.Category;

import java.util.Collection;

/**
 *
 */
public class DefaultProject extends CommandProviderBase implements Project {

    private Category rootCategory;

    public DefaultProject() {
    }

    public Category getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(Category rootCategory) {
        this.rootCategory = rootCategory;
    }
}
