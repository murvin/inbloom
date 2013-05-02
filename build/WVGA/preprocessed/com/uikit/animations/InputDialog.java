package com.uikit.animations;

import com.uikit.utils.UikitConstant;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.Style;
import com.uikit.styles.TextStyle;
import com.uikit.textinputhandler.NativeTextInputHandler;

public class InputDialog extends InfoDialog {

    /** The  text box for label */
    protected UikitTextBox labelTextBox;
    /** The text input component */
    protected UikitTextInput textInput;
    private int id;
    public static final int COMP_TEXT_INPUT = 0x310;
    public static final int COMP_TEXT_LABEL = 0x320;

    public InputDialog(int iX, int iY, int iWidth, int iHeight, boolean bMultiline, boolean bPassword, String title, int field) {
        super(iX, iY, iWidth, iHeight);

        /* Create the label. It is placed below the title */
        labelTextBox = new UikitTextBox(9 * containerPanel.getWidth() / 10, title);
        containerPanel.addComponent(labelTextBox);
        labelTextBox.x = (containerPanel.getWidth() - labelTextBox.getWidth()) / 2;

        /* Create the input control. It is placed below the label  */
        textInput = new UikitTextInput(9 * containerPanel.getWidth() / 10,
                labelTextBox.getHeight() + 5,
                new NativeTextInputHandler(title, 1024, field),
                bMultiline,
                bPassword);
        containerPanel.addComponent(textInput);
        textInput.x = (containerPanel.getWidth() - textInput.getWidth()) / 2;

        initDefaultStyles();

        /* Calculate the Y positions of the label and Textinput */
        repositionContent();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    private void initDefaultStyles() {
        TextStyle txtStyle = new TextStyle();
        txtStyle.setAlign(UikitConstant.LEFT);
        setStyle(COMP_TEXT_LABEL, txtStyle);

        ComponentStyle indciatorStyle = new ComponentStyle();
        setStyle(COMP_TEXT_INPUT, indciatorStyle);
    }

    public void setStyle(int component, Style style) {
        if (style != null) {
            switch (component) {
                case COMP_TEXT_LABEL: {
                    labelTextBox.setStyle(style);
                    repositionContent();
                    break;
                }
                case COMP_TEXT_INPUT: {
                    textInput.setStyle(style);
                    repositionContent();
                    break;
                }
            }
        }
    }

    public Style getStyle(int component) {
        return null;
    }

    public void setLabelText(String txt) {
        if (txt == null) {
            return;
        }
        labelTextBox.setText(txt);
        repositionContent();
    }

    public void setLabelTextStyle(TextStyle style) {
        if (style == null) {
            return;
        }
        labelTextBox.setTextStyle(style);
        repositionContent();
    }

    public TextStyle getLabelTextStyle() {
        return labelTextBox.getTextStyle();
    }

    public void setInputTextStyle(TextStyle style) {
        if (style == null) {
            return;
        }
        labelTextBox.setTextStyle(style);
        repositionContent();
    }

    public String getInput() {
        return textInput.getText();
    }

    private void repositionContent() {
        int containerHeight = containerPanel.getHeight();

        labelTextBox.y = containerHeight / 4;
        textInput.y = labelTextBox.y + labelTextBox.getHeight() + 3;
    }

    public UikitTextInput getTextInput() {
        return this.textInput;
    }
}
