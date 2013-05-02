package com.inbloom.ui.components;

import com.uikit.coreElements.Component;
import com.uikit.coreElements.IFocusable;

public class DummyFocusable extends Component implements IFocusable {

    public DummyFocusable(int iWidth, int iHeight) {
        super(iWidth, iHeight);
    }

    public void onFocus() {
    }

    public void onDefocus() {
    }

    public boolean isFocused() {
        return true;
    }
}