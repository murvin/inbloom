package com.uikit.textinputhandler;

import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.UikitCanvas;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

public class NativeTextInputHandler implements ITextInputHandler, CommandListener {

    private ITextInput textInput;
    private String nameStr;
    private int iMaxSize;
    private int iConstraintFlags;
    private int iAllowedMaxSize;
    private TextBox theTextBox;
    private Command okCommand;
    private Command cancelCommand;
    private Displayable prevDisplayable;
    private boolean bMultiline = true;
    private boolean bRenew = false;

    public NativeTextInputHandler(String nameStr, int iMaxSize, int iConstraintFlags) {
        this.nameStr = nameStr;
        this.iMaxSize = iMaxSize;
        this.iConstraintFlags = iConstraintFlags;

        if (iConstraintFlags == TextField.PASSWORD && bMultiline) {
            bMultiline = false;
        }

        prevDisplayable = null;
    }

    public void registerTextInputBox(ITextInput textInput) {
        this.textInput = textInput;
    }

    public boolean onKeyPressed(int iKeyCode) {
        if (UikitCanvas.getGameAction(iKeyCode) == Canvas.FIRE || iKeyCode >= 10) {
            showUp();
            return true;
        }
        return false;
    }

    public void showUp() {
        prevDisplayable = UiKitDisplay.getDisplay().getCurrent();
        if (iMaxSize == -1) {
            iMaxSize = iAllowedMaxSize;
        }
        String text = textInput.getText();
        if (text.length() > iMaxSize) {
            text = text.substring(0, iMaxSize);
        }

        if (theTextBox == null || bRenew) {
            try {
                theTextBox = new javax.microedition.lcdui.TextBox(nameStr, text, iMaxSize, iConstraintFlags);
            } catch (IllegalArgumentException ex) {
                theTextBox = new javax.microedition.lcdui.TextBox(nameStr, "", iMaxSize, iConstraintFlags);
            }
            theTextBox.addCommand(okCommand = new Command("OK", Command.OK, 1));
            theTextBox.addCommand(cancelCommand = new Command("Cancel", Command.CANCEL, 2));
        } else {
            try {
                theTextBox.setTitle(nameStr);
                theTextBox.delete(0, theTextBox.size());
                theTextBox.setConstraints(iConstraintFlags);
                theTextBox.setString(text);
                theTextBox.setMaxSize(iMaxSize);
                theTextBox.addCommand(okCommand = new Command("OK", Command.OK, 1));
                theTextBox.addCommand(cancelCommand = new Command("Cancel", Command.CANCEL, 2));
            } catch (Exception exception) {
                exception.printStackTrace();
                try {
                    theTextBox = new javax.microedition.lcdui.TextBox(nameStr, text, iMaxSize, iConstraintFlags);
                } catch (IllegalArgumentException ex) {
                    theTextBox = new javax.microedition.lcdui.TextBox(nameStr, "", iMaxSize, iConstraintFlags);
                }
                theTextBox.addCommand(okCommand = new Command("OK", Command.OK, 1));
                theTextBox.addCommand(cancelCommand = new Command("Cancel", Command.CANCEL, 2));
            }
        }
        theTextBox.setCommandListener(this);
        UiKitDisplay.getDisplay().setCurrent(theTextBox);
    }

    public String getTextBoxContent() {
        if (theTextBox != null && theTextBox.getString() != null) {
            return theTextBox.getString();
        } else {
            return "";
        }
    }

    public boolean onKeyReleased(int iKeyCode) {
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return false;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == okCommand) {
            textInput.setText(theTextBox.getString());
        }
        textInput.moveCaretToEnd();
        UiKitDisplay.getDisplay().setCurrent(prevDisplayable);
        theTextBox.removeCommand(okCommand);
        theTextBox.removeCommand(cancelCommand);
        textInput.switchInputHandler();
        prevDisplayable = null;
    }

    public boolean isDisplayCursor() {
        return false;
    }

    public boolean isDisplayIndicator() {
        return false;
    }

    public void init() {
        if (prevDisplayable == null) {
            showUp();
        }
    }

    public void resetBeforeShow(boolean bReset) {
        bRenew = bReset;
    }
}
