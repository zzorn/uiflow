package org.uiflow.propertyeditor.ui.editors.beangraph;

import org.uiflow.propertyeditor.model.bean.Bean;

/**
 * Listens to selections in a BeanGraphEditor.
 */
public interface GraphSelectionListener {

    /**
     * Called when the selection state for the specified bean changes.
     */
    void onSelectionChanged(BeanGraphEditor beanGraphEditor, Bean bean, boolean selected);

}
