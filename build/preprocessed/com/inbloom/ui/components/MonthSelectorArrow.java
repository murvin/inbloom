package com.inbloom.ui.components;

import com.uikit.animations.UikitImageBox;
import com.uikit.painters.PatchPainter;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.IFocusable;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.IUikitInputHandler;

import com.uikit.coreElements.UikitCanvas;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MonthSelectorArrow extends UikitImageBox implements IFocusable, IUikitInputHandler, ITouchEventListener {

    private boolean isOnFocus;
    private PatchPainter painter;
    private boolean isTouch;

    public MonthSelectorArrow(Image image, PatchPainter renderer) {
        super(image);
        this.painter = renderer;
        isTouch = UikitCanvas.isTouch;
    }

    public void onFocus() {
        isOnFocus = true;
    }

    public void onDefocus() {
        isOnFocus = false;
    }

    public boolean isFocused() {
        return isOnFocus;
    }

    protected void drawCurrentFrame(Graphics g) {
        super.drawCurrentFrame(g);

        if (isOnFocus && !isTouch) {
            painter.paint(this, g);
        }
    }

    public boolean onKeyPressed(int iKeyCode) {
        int key = UiKitDisplay.getGameAction(iKeyCode);
        if (key == Canvas.FIRE) {
            updateListener();
            return true;
        }
        return false;
    }

    private void updateListener() {
        if (cel != null) {
            cel.onComponentEvent(this, -1, null, -1);
        }
    }

    public boolean onKeyReleased(int iKeyCode) {
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return false;
    }

    public boolean onPress(int type, int iX, int iY) {
        if (type == ITouchEventListener.SINGLE_PRESS) {
            updateListener();
            return true;
        }
        return false;
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return true;
    }
}
