
package com.uikit.textinputhandler;

public interface ITextInput {

   
    /**
     * Indicates insert mode when one character is replaced by another.
     */
    public static final int INSERT_MODE_REPLACE = 0;
    
    /**
     * Indicates insert mode when character is added to string.
     */
    public static final int INSERT_MODE_ADD = 1;
    
    /**
     * Inserts character at current caret position.
     * @param ch The character to be inserted.
     */
    public void insertChar(char ch);

    /**
     * Deletes character before caret (caret position - 1).
     * @return true if character was deleted. False, if there is nothing to
     * delete.
     */
    public boolean deleteChar();

    /**
     * Moves caret by one position to left.
     * @return True if position was changed.
     */
    public boolean moveCaretLeft();

    /**
     * Moves caret by one position to right.
     * @return True if position was changed.
     */
    public boolean moveCaretRight();

    /**
     * Moves care to line above current position.
     * @return True if position was changed.
     */
    public boolean moveCaretUp();

    /**
     * Moves caret to line below current position.
     * @return True if position was changed.
     */
    public boolean moveCaretDown();

    /**
     * Moves caret to the begining of the edited text.
     */
    public void moveCaretToStart();

    /**
     * Moves caret to the end of the edited text.
     */
    public void moveCaretToEnd();

    /**
     * Moves caret to provided position.
     * @param position The position to where caret should be moved. If 
     * position < <code>0</code>, caret will be moved to the begining of the
     * edited text. If position > lenght of text, caret will be moved to the end
     * of the edited text.
     */
    public void setCaretPosition(int position);

    /**
     * Returns caret position in edited text.
     * @return Current caret position.
     */
    public int getCaretPosition();

    /**
     * Returns current caret's row.
     * @return Caret's row.
     */
    public int getCaretRow();

    /**
     * Return current caret's column.
     * @return Current caret's column.
     */
    public int getCaretCol();
    
    /**
     * Sets character insert mode of characters added when typing. Can be
     * INSERT_MODE_REPLACE or INSERT_MODE_ADD.
     * @param iInsertMode The insert mode.
     */
    public void setCharInsertMode(int iInsertMode);
    
    /**
     * Sets string displayed in indicator box.
     * @param string The text displayed in indicator box.
     */
    public void setIndicatorString(String string);
    
    /**
     * Returns the edited text.
     * @return The dited text.
     */
    public String getText();
    
    /**
     * Sets text of this text input.
     * @param string The text to be set.
     */
    public void setText(String text);
    
    /**
     * Requests to show special character panel.
     */
    public void showSpecChars();
    
    /**
     * Notify text input to switch input handler to alternative one.
     */
    public void switchInputHandler();
}
