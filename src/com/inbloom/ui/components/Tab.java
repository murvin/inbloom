package com.inbloom.ui.components;

import com.uikit.painters.PatchPainter;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.IFocusable;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.IUikitInputHandler;

import com.uikit.coreElements.UikitCanvas;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Tab extends Component implements IFocusable, IUikitInputHandler, ITouchEventListener {

    private int id;
    private Image imgIcon;
    private Image imgLabel;
    private boolean isOnFocus;
    private PatchPainter patchOnFocusBg;
    private boolean isTouch;

    Tab(Image icon, int id, int width, int height, Image imgLabel) {
        super(width, height);
        this.imgIcon = icon;
        this.id = id;
        this.imgLabel = imgLabel;
        isTouch = UikitCanvas.isTouch;
    }

    public void setImgOnFocusBg(Image imgOnFocusBg) {
        int padding = 5;
        patchOnFocusBg = new PatchPainter(imgOnFocusBg, padding, padding, padding, padding);
    }

    public void setImgLabel(Image imgLabel) {
        this.imgLabel = imgLabel;
    }

    protected void drawCurrentFrame(Graphics g) {
        if (isOnFocus) {
            if (patchOnFocusBg != null && !isTouch) {
                patchOnFocusBg.paint(this, g);
            }
        }

        if (imgIcon != null) {
            g.drawImage(imgIcon, (iWidth - imgIcon.getWidth()) / 2, (iHeight - imgIcon.getHeight()) / 3, 20);
        }

        if (imgLabel != null) {
            g.drawImage(imgLabel, (iWidth - imgLabel.getWidth()) / 2, iHeight - imgLabel.getHeight(), 20);
        }
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

    private void updateListener() {
        if (cel != null) {
            cel.onComponentEvent(this, this.id, null, -1);
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

    public boolean onKeyReleased(int iKeyCode) {
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return onKeyPressed(iKeyCode);
    }

    public boolean onPress(int type, int iX, int iY) {
        if (type == ITouchEventListener.SINGLE_PRESS) {
            updateListener();
            return true;
        }
        return false;
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return false;
    }
}
