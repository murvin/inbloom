package com.uikit.textinputhandler;

import com.uikit.coreElements.IUikitInputHandler;

public interface ITextInputHandler extends IUikitInputHandler {

    public static final int ANY = 0;
    public static final int EMAILADDR = 1;
    public static final int NUMERIC = 2;
    public static final int PHONENUMBER = 3;
    public static final int URL = 4;
    public static final int DECIMAL = 5;

    public void registerTextInputBox(ITextInput textInput);

    public boolean isDisplayCursor();

    public boolean isDisplayIndicator();

    public void init();
}