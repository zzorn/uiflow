package org.uiflow.propertyeditor.ui.editors.text;

import org.uiflow.propertyeditor.ui.editors.EditorConfigurationBase;

/**
 * EditorInfo for text properties.
 */
public class TextEditorConfiguration extends EditorConfigurationBase {

    public static final TextEditorConfiguration DEFAULT = new TextEditorConfiguration();
    public static final TextEditorConfiguration DEFAULT_MULTILINE = new TextEditorConfiguration(4);

    private int rows;


    /**
     * Creates single line TextEditorConfiguration.
     */
    public TextEditorConfiguration() {
        this(1);
    }

    /**
     * @param rows number of rows in the text editor, 1 for single line editor.
     */
    public TextEditorConfiguration(int rows) {
        super(TextEditor.class);
        if (rows <= 0) throw new IllegalArgumentException("Rows to show must be 1 or larger, but was " + rows);

        this.rows = rows;
    }

    /**
     * @return true if an edit field with multiple lines should be used to edit the text, false if a single line editor is ok.
     */
    public boolean isMultiLine() {
        return rows > 1;
    }

    /**
     * @return number of rows visible in the editor at once.
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows number of rows visible in the editor at once.
     */
    public void setRows(int rows) {
        this.rows = rows;
    }
}
