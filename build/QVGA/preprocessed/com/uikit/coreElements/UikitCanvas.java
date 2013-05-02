package com.uikit.coreElements;

import javax.microedition.lcdui.*;
import java.util.*;
import com.uikit.utils.ImageUtil;
import java.lang.ref.WeakReference;

public class UikitCanvas implements IContainer, IUikitInputHandler, IUiKitPointerHandler {

    public static final int BGPOSMODE_CENTER = 0x1001;
    public static final int BGPOSMODE_TILE = 0x1003;
    private int frameDelay;
    private Vector components;
    private boolean isEnabled;
    private int bgColor;
    private Image imgBackground;
    private int iBgPositionMode;
    private boolean isAnimationStopped;
    boolean bStopAnimatingComponents;
    public boolean bNeverPaintedYet;
    private ICanvasEventListener cel;
    private IUikitInputHandler mainInputHandler;
    private IUiKitPointerHandler pointerHandler;
    private TouchEventHandler tei;
    private Vector focusManagers = new Vector();
    private IFocusManager currentFocusManager;
    public static boolean isTouch;

    public UikitCanvas(int iFrameDelay) {
        this.frameDelay = iFrameDelay;
        isAnimationStopped = true;
        bNeverPaintedYet = true;
        components = new Vector();
        isEnabled = true;
        bgColor = 0xffffff;
        iBgPositionMode = BGPOSMODE_CENTER;
        tei = new TouchEventHandler(this);
    }

    public void paint(Graphics g) {
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        g.setClip(0, 0, getWidth(), getHeight());

        if (imgBackground != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (iBgPositionMode == BGPOSMODE_CENTER) {
                g.drawImage(imgBackground, getWidth() / 2, getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
            } else if (iBgPositionMode == BGPOSMODE_TILE) {
                ImageUtil.drawTiledRectangle(g, 0, 0, getWidth(), getHeight(), imgBackground);
            }
        } else {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());

        }

        for (int i = 0; i < components.size(); i++) {
            try {
                Component c = (Component) components.elementAt(i);
                drawComponent(g, c, true);
            } catch (IndexOutOfBoundsException iobe) {
                iobe.printStackTrace();
            }
        }

    }

    private void drawComponent(Graphics g, Component c, boolean bbb0) {
        if (c.isVisible || !bbb0) {
            final int iX = c.x;
            final int iY = c.y;
            g.translate(iX, iY);
            c.drawCurrentFrameEx(g);
            g.translate(-iX, -iY);


            if (!bbb0 || !bStopAnimatingComponents) {
                c.tick();
            }
        }
    }

    public final int getWidth() {
        return UiKitDisplay.getWidth();
    }

    public final int getHeight() {
        return UiKitDisplay.getHeight();
    }

    public final boolean isShown() {
        return UiKitDisplay.isShown(this) && UiKitDisplay.getCurrent() == this;
    }

    public final void setFrameDelay(int iFrameDelay) {
        this.frameDelay = iFrameDelay;

        if (isShown()) {
            AnimationEngine.setDelay(iFrameDelay);
        }
    }

    public final int getFrameDelay() {
        return frameDelay;
    }

    public final void updateScreen() {
        UiKitDisplay.updateScreen(this, 0, 0, getWidth(), getHeight());
    }

    public final void updateScreen(int iX, int iY, int iWidth, int iHeight) {
        UiKitDisplay.updateScreen(this, iX, iY, iWidth, iHeight);
    }

    public final void startStopAnimation() {
        if (isAnimationStopped) {
            isAnimationStopped = false;
        } else {
            isAnimationStopped = true; //(requesting to) stop
        }
    }

    public final boolean isAnimationStopped() {
        return isAnimationStopped;
    }

    public final void setTwuikInputHandler(IUikitInputHandler c) {
        this.mainInputHandler = c;
    }

    public final IUikitInputHandler getTwuikInputHandler() {
        return mainInputHandler;
    }

    public final void setEventListener(ICanvasEventListener elCanvas) {
        this.cel = elCanvas;
    }

    public final ICanvasEventListener getEventListener() {
        return cel;
    }

    static void setAttachedCanvasRecursively(Component c, UikitCanvas toSetTheAttachedCanvas) {
        final UikitCanvas prev = c.theAttachedCanvas;
        c.theAttachedCanvas = toSetTheAttachedCanvas;
        if (c instanceof Panel) {
            Panel p = (Panel) c;
            for (int i = 0; i < p.components.size(); i++) {
                Component curC = (Component) p.components.elementAt(i);
                setAttachedCanvasRecursively(curC, toSetTheAttachedCanvas);
            }
        }
        if (c instanceof IFocusManager) {
            if (toSetTheAttachedCanvas != null) {
                toSetTheAttachedCanvas.focusManagers.addElement(new WeakReference(c));
                if (toSetTheAttachedCanvas.currentFocusManager == null) {
                    toSetTheAttachedCanvas.currentFocusManager = (IFocusManager) c;
                    if (!toSetTheAttachedCanvas.isTouch) {
                        toSetTheAttachedCanvas.currentFocusManager.onFocus();
                    }
                }
            } else if (prev != null) {
                prev.unregisterFocusManager((IFocusManager) c);
            }
        }
    }

    public final synchronized void addComponent(Component c) {
        if (c == null) {
            return;
        }
        if (components.indexOf(c) != -1) {
            return;
        }
        setAttachedCanvasRecursively(c, this);
        components.addElement(c);
        if (!c.hasAnimationStarted()) {
            c.startStopAnimation();
        }
    }

    public final int indexOfComponent(Component c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }

        return components.indexOf(c);
    }

    public final Component componentAt(int iIndex) {
        if (iIndex < 0 || iIndex > components.size() - 1) {
            throw new IndexOutOfBoundsException();
        }

        return (Component) components.elementAt(iIndex);
    }

    public final synchronized void insertComponentAt(Component c, int iIndex) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        if (iIndex < 0 || iIndex > components.size()) {
            throw new IndexOutOfBoundsException();
        }
        setAttachedCanvasRecursively(c, this);
        components.insertElementAt(c, iIndex);
        if (c.isAnimationStopped()) {
            c.startStopAnimation();
        }
    }

    public final synchronized void replaceComponent(Component oldC, Component newC) {
        if (oldC == null || newC == null) {
            throw new IllegalArgumentException();
        }
        final int iIndex = components.indexOf(oldC);
        if (iIndex == -1) {
            throw new IllegalArgumentException();
        }
        if (components.indexOf(newC) != -1) {
            throw new IllegalArgumentException(); 
        }
        setAttachedCanvasRecursively(newC, this);
        components.setElementAt(newC, iIndex);
        setAttachedCanvasRecursively(oldC, null);
    }

    public final synchronized void removeComponent(Component c) {
        if (c == null) {
            return;
        }
        final int iIndex;
        if ((iIndex = components.indexOf(c)) == -1) {
            return;
        }
        components.removeElementAt(iIndex);
        setAttachedCanvasRecursively(c, null);
    }

    public final synchronized void removeAllComponents() {
        while (!components.isEmpty()) {
            removeComponent((Component) components.elementAt(0));
        }
    }

    public final int getComponentCount() {
        return components.size();
    }

    public static final int getGameAction(int iKeyCode) {
        return UiKitDisplay.getGameAction(iKeyCode);
    }

    public static final int getGameActionNoAlt(int iKeyCode) {
        if (iKeyCode == Canvas.KEY_NUM2 || iKeyCode == Canvas.KEY_NUM8
                || iKeyCode == Canvas.KEY_NUM4 || iKeyCode == Canvas.KEY_NUM6 || iKeyCode == Canvas.KEY_NUM5) {
            return iKeyCode;
        } else {
            return UiKitDisplay.getGameAction(iKeyCode);
        }
    }

    public final void setBgColor(int iBgColor) {
        this.bgColor = iBgColor;
    }

    public final void setBgImage(Image imgBackground, int iBgPositionMode) {
        this.imgBackground = imgBackground;
        this.iBgPositionMode = iBgPositionMode;
    }

    public final void setEnabledMode(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public final boolean isEnabled() {
        return isEnabled;
    }

    public final void enableStopAnimatingComponents(boolean bStopAnimatingComponents) {
        this.bStopAnimatingComponents = bStopAnimatingComponents;
    }

    public void setPointerHandler(IUiKitPointerHandler pointerInputHandler) {
        this.pointerHandler = pointerInputHandler;
    }

    public TouchEventHandler getTouchEventHandler() {
        return tei;
    }

    public void setTouchEventHandler(TouchEventHandler tei) {
        this.tei = tei;
    }

    public IUiKitPointerHandler getPointerHandler() {
        return pointerHandler;
    }

    public boolean isInTouchMode() {
        return isTouch;
    }

    public boolean onKeyPressed(int iKeyCode) {
        if (isTouch && currentFocusManager != null && ((Component) currentFocusManager).getContainingCanvas() == this) {
            currentFocusManager.touchModeChanged(false);
            isTouch = false;
        }
        if (mainInputHandler != null) {
            if (mainInputHandler.onKeyPressed(iKeyCode)) {
                return true;
            }
        }

        if (currentFocusManager != null && ((Component) currentFocusManager).getContainingCanvas() == this) {
            if (currentFocusManager.onKeyPressed(iKeyCode)) {
                return true;
            } else {

                findFocusManagerTowards(iKeyCode);
            }
        }

        return false;
    }

    public boolean onKeyReleased(int iKeyCode) {
        if (mainInputHandler != null) {
            if (mainInputHandler.onKeyReleased(iKeyCode)) {
                return true;
            }
        }
        if (currentFocusManager != null && ((Component) currentFocusManager).getContainingCanvas() == this) {
            return currentFocusManager.onKeyReleased(iKeyCode);
        }
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        if (mainInputHandler != null) {
            if (mainInputHandler.onKeyRepeated(iKeyCode)) {
                return true;
            }
        }
        if (currentFocusManager != null && ((Component) currentFocusManager).getContainingCanvas() == this) {
            currentFocusManager.onKeyRepeated(iKeyCode);
        }
        return false;
    }

    public boolean onPointerPressed(int iX, int iY) {
        if (!isTouch && currentFocusManager != null && ((Component) currentFocusManager).getContainingCanvas() == this) {
            currentFocusManager.touchModeChanged(true);
            currentFocusManager.onDefocus();
            isTouch = true;
        }
        if (pointerHandler != null) {
            if (pointerHandler.onPointerPressed(iX, iY)) {
                return true;
            }
        }
        return tei.onPointerPressed(iX, iY);
    }

    public boolean onPointerReleased(int iX, int iY) {
        if (pointerHandler != null) {
            if (pointerHandler.onPointerReleased(iX, iY)) {
                return true;
            }
        }
        return tei.onPointerReleased(iX, iY);
    }

    public boolean onPointerDragged(int iX, int iY) {
        if (pointerHandler != null) {
            if (pointerHandler.onPointerDragged(iX, iY)) {
                return true;
            }
        }
        return tei.onPointerDragged(iX, iY);
    }

    public void setCurrentFocusManager(IFocusManager currentFocusManager) {
        if (this.currentFocusManager != null && !isTouch) {
            this.currentFocusManager.onDefocus();
        }
        this.currentFocusManager = currentFocusManager;
        if (!isTouch) {
            currentFocusManager.onFocus();
        }
    }

    private void findTopLeftFocusManager() {
        if (currentFocusManager == null || ((Component) currentFocusManager).getContainingCanvas() != this) {
            int distance = -1;
            for (int i = focusManagers.size() - 1; i >= 0; i--) {
                Object o = ((WeakReference) focusManagers.elementAt(i)).get();
                if (o != null) {
                    Component tfm = (Component) o;
                    try {
                        if (tfm instanceof IEnableable && !((IEnableable) tfm).isEnabled()) {
                            continue;
                        }
                        if (!tfm.isVisible()) {
                            continue;
                        }
                        if (Math.abs(tfm.getAbsoluteX()) + Math.abs(tfm.getAbsoluteY()) < distance || distance == -1) {
                            currentFocusManager = (IFocusManager) tfm;
                            distance = Math.abs(tfm.getAbsoluteX()) + Math.abs(tfm.getAbsoluteY());
                        }
                    } catch (IllegalArgumentException ex) {
                        //tfm not on canvas
                    }
                }
            }
            if (currentFocusManager != null) {
                currentFocusManager.onFocus();
            }
        }
    }

    private void findFocusManagerTowards(int iKeyCode) {
        if (currentFocusManager == null || ((Component) currentFocusManager).getContainingCanvas() != this) {
            findTopLeftFocusManager();
            return;
        } else {
            Component currentFocusable = (Component) currentFocusManager;
            int iAnchorX = 0;
            int iAnchorY = 0;
            try {
                if (getGameActionNoAlt(iKeyCode) == Canvas.UP) {
                    iAnchorX = currentFocusable.getAbsoluteX() + currentFocusable.getWidth() / 2;
                    iAnchorY = currentFocusable.getAbsoluteY();
                } else if (getGameActionNoAlt(iKeyCode) == Canvas.DOWN) {
                    iAnchorX = currentFocusable.getAbsoluteX() + currentFocusable.getWidth() / 2;
                    iAnchorY = currentFocusable.getAbsoluteY() + currentFocusable.getHeight();
                } else if (getGameActionNoAlt(iKeyCode) == Canvas.LEFT) {
                    iAnchorX = currentFocusable.getAbsoluteX();
                    iAnchorY = currentFocusable.getAbsoluteY() + currentFocusable.getHeight() / 2;
                } else if (getGameActionNoAlt(iKeyCode) == Canvas.RIGHT) {
                    iAnchorX = currentFocusable.getAbsoluteX() + currentFocusable.getWidth();
                    iAnchorY = currentFocusable.getAbsoluteY() + currentFocusable.getHeight() / 2;
                } else {
                    return;
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                findTopLeftFocusManager();
                return;
            }

            Component tempItem;
            int diffX;
            int diffY;
            int tempCenX;
            int tempCenY;
            int i;
            int distance = -1;
            Component next = null;

            for (i = focusManagers.size() - 1; i >= 0; i--) {
                if (((WeakReference) focusManagers.elementAt(i)).get() == null) {
                    focusManagers.removeElementAt(i);
                    continue;
                }
                tempItem = (Component) ((WeakReference) focusManagers.elementAt(i)).get();
                if (tempItem instanceof IEnableable) {
                    if (!((IEnableable) tempItem).isEnabled()) {
                        continue;
                    }
                }
                if (!tempItem.isVisible() || tempItem.theAttachedCanvas == null) {
                    continue;
                }

                try {
                    tempCenX = tempItem.getAbsoluteX() + tempItem.getWidth() / 2;
                    tempCenY = tempItem.getAbsoluteY() + tempItem.getHeight() / 2;
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    continue;
                }

                diffX = Math.abs(iAnchorX - tempCenX);
                diffY = Math.abs(iAnchorY - tempCenY);

                if (getGameActionNoAlt(iKeyCode) == Canvas.UP) {
                    if (iAnchorY > tempCenY && (diffX + diffY < distance || distance == -1)) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                } else if (getGameActionNoAlt(iKeyCode) == Canvas.DOWN) {
                    if (iAnchorY < tempCenY && (diffX + diffY < distance || distance == -1)) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                } else if (getGameActionNoAlt(iKeyCode) == Canvas.LEFT) {
                    if (iAnchorX > tempCenX && (diffX + diffY < distance || distance == -1)) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                } else {
                    if (iAnchorX < tempCenX && (diffX + diffY < distance || distance == -1)) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                }
            }
            if (next != null && !isTouch) {
                currentFocusManager.onDefocus();
                currentFocusManager = (IFocusManager) next;
                currentFocusManager.onFocus();
            }
        }
    }

    private void unregisterFocusManager(IFocusManager focusManager) {
        for (int i = focusManagers.size() - 1; i >= 0; i--) {
            if (((WeakReference) focusManagers.elementAt(i)).get() == focusManager) {
                focusManagers.removeElementAt(i);
            }
        }
        if (focusManager == currentFocusManager) {
            currentFocusManager = null;
            findTopLeftFocusManager();
        }
    }

    public void setInTouchMode(boolean bInTouchMode) {
        if (this.isTouch != bInTouchMode) {
            this.isTouch = bInTouchMode;
            if (currentFocusManager != null) {
                currentFocusManager.touchModeChanged(bInTouchMode);
            }
        }
    }

    public IFocusManager getCurrentFocusManager() {
        return currentFocusManager;
    }
}
