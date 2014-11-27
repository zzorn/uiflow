package org.uiflow.propertyeditor.model.bean;

/**
 * A bean delegate that adds a postfix to the bean name.
 */
public class NamedPostfixBeanDelegate extends BeanDelegate {

    private final String namePostfix;

    public NamedPostfixBeanDelegate(Bean delegate, String namePostfix) {
        super(delegate);
        this.namePostfix = namePostfix;
    }

    @Override public String getName() {
        return super.getName() + namePostfix;
    }
}
