package org.uiflow.propertyeditor.ui.editors;

import org.uiflow.propertyeditor.model.EditorConfigurationBase;

/**
 * EditorInfo for text properties.
 */
public class TextEditorConfiguration extends EditorConfigurationBase {

    public static final TextEditorConfiguration DEFAULT = new TextEditorConfiguration(false);
    public static final TextEditorConfiguration DEFAULT_MULTILINE = new TextEditorConfiguration(true);

    private boolean multiLine;
    private int rowsToShow;

    private static final int DEFAULT_ROWS_TO_SHOW = 4;


    /**
     * Creates single line TextEditorInfo.
     */
    public TextEditorConfiguration() {
        this(false);
    }

    /**
     * @param multiLine true if an edit field with multiple lines should be used to edit the text, false if a single line editor is ok.
     */
    public TextEditorConfiguration(boolean multiLine) {
        this(multiLine, DEFAULT_ROWS_TO_SHOW);
    }

    /**
     * @param multiLine true if an edit field with multiple lines should be used to edit the text, false if a single line editor is ok.
     * @param rowsToShow number of rows to show by default, if this is a multiline text area.
     */
    public TextEditorConfiguration(boolean multiLine, int rowsToShow) {
        super(TextEditor.class);
        this.multiLine = multiLine;
        this.rowsToShow = rowsToShow;
    }

    /**
     * @return true if an edit field with multiple lines should be used to edit the text, false if a single line editor is ok.
     */
    public boolean isMultiLine() {
        return multiLine;
    }

    /**
     * @param multiLine true if an edit field with multiple lines should be used to edit the text, false if a single line editor is ok.
     */
    public void setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
    }

    public int getRowsToShow() {
        return rowsToShow;
    }

    public void setRowsToShow(int rowsToShow) {
        this.rowsToShow = rowsToShow;
    }
}
