package com.uikit.coreElements;

import com.uikit.motion.MotionQuadOut;
import com.uikit.utils.UikitConstant;
import com.uikit.layout.BoxLayout;

import com.uikit.layout.ILayout;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;

public class SmartPanel extends Panel implements IFocusManager {
    
    private boolean bFocused;
    private Vector focusables;
    private Component currentFocusable;
    protected int iTargetX;
    protected int iTargetY;
    private int iHorizontalScrolling;
    private int iVerticalScrolling;
    private boolean bUseMotion = true;
    private boolean isScrollable = true;
    private float fSpeedX;
    private float fSpeedY;
    private float fX;
    private float fY;
    private float fSlowDown = 0.95f;
    /** Used when container does not starts at (0,0)*/
    private int startOffsetY;
    private int bottomOffset = 0;
    
    public SmartPanel(int iWidth, int iHeight) {
        super(iWidth, iHeight);
        focusables = new Vector();
        iHorizontalScrolling = iHeight / 2;
        iVerticalScrolling = iWidth / 2;
        setLayout(new BoxLayout(UikitConstant.VERTICAL, 0));
        
        isLayout = true;
        upModelIndex = -1;

        viewport_height = iHeight;
        viewport_width = iWidth;
        viewportOffset = viewport_height / 2;
    }
    
    public SmartPanel(int iX, int iY, int iWidth, int iHeight) {
        super(iX, iY, iWidth, iHeight);
        focusables = new Vector();
        iHorizontalScrolling = iHeight / 2;
        iVerticalScrolling = iWidth / 2;
        setLayout(new BoxLayout(UikitConstant.VERTICAL, 0));
    }
    
    public void setVecticalScrolling(int iVerticalScrolling){
        this.iVerticalScrolling = iVerticalScrolling;
    }
        
    
    private void findFirstTopLeftFocusable() {
        currentFocusable = null;
        IContainer parent = null;
        if (getContainingPanel() != null) {
            parent = getContainingPanel();
        } else if (getContainingCanvas() != null) {
            parent = getContainingCanvas();
        } else {
            return;
        }
        int distance = -1;
        for (int i = focusables.size() - 1; i >= 0; i--) {
            Component c = (Component) focusables.elementAt(i);
            if ((c instanceof IEnableable && !((IEnableable) c).isEnabled()) || !c.isVisible()) {
                continue;
            }
            int iRelX = 0;
            int iRelY = 0;
            try {
                iRelX = relativeX(c);
                iRelY = relativeY(c);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                continue;
            }
            
            if (iRelX + c.getWidth() + iTargetX < 0 || iRelY + c.getHeight() + iTargetY < 0 || iRelX + iTargetX > parent.getWidth() || iRelY + iTargetY > parent.getHeight()) {
                continue;
            }
            if (iRelX + iTargetX + iRelY + iTargetY < distance || distance == -1) {
                currentFocusable = c;
                distance = iRelX + iTargetX + iRelY + iTargetY;
            }
        }
        if (currentFocusable != null) {
            ((IFocusable) currentFocusable).onFocus();
        }
    }
    
    public void registerFocusables(Component c) {
        if (c instanceof IFocusable) {
            focusables.addElement(c);
            if (currentFocusable == null && theAttachedCanvas != null) {
                findFirstTopLeftFocusable();
            }
        } else if (c instanceof IContainer) {
            final int j = ((IContainer) c).getComponentCount();
            for (int i = 0; i < j; i++) {
                registerFocusables(((IContainer) c).componentAt(i));
            }
        }
    }
    
    public void unregisterFocusables(Component c) {
        if (c instanceof IFocusable) {
            for (int i = focusables.size() - 1; i >= 0; i--) {
                if (focusables.elementAt(i) == c) {
                    focusables.removeElementAt(i);
                    if (c == currentFocusable) {
                        ((IFocusable) currentFocusable).onDefocus();
                        findFirstTopLeftFocusable();
                    }
                    break;
                }
            }
        } else if (c instanceof IContainer) {
            final int j = ((IContainer) c).getComponentCount();
            for (int i = 0; i < j; i++) {
                unregisterFocusables(((IContainer) c).componentAt(i));
            }
        }
    }
    
    public void unregisterFocusable(Component c) {
        for (int i = focusables.size() - 1; i >= 0; i--) {
            if (focusables.elementAt(i) == c) {
                focusables.removeElementAt(i);
                if (c == currentFocusable) {
                    ((IFocusable) currentFocusable).onDefocus();
                    findFirstTopLeftFocusable();
                }
            }
        }
    }
    
    public synchronized void removeComponent(Component c) {
        if (adapter == null) {
            super.removeComponent(c);
            unregisterFocusables(c);
        } else {
            isLayout = false;
            
            super.removeComponent(c);
            unregisterFocusables(c);
            isLayout = true;
        }
    }
    
    public synchronized void removeComponent(Component c, boolean isRelayout) {
        super.removeComponent(c, isRelayout);
        unregisterFocusables(c);
    }
    
    public void touchModeChanged(boolean inTouchMode) {
        if (inTouchMode) {
            motionFX = null;
            if (currentFocusable != null && currentFocusable.getContainingCanvas() == null) {
                currentFocusable = null;
            } else if (currentFocusable != null) {
                ((IFocusable) currentFocusable).onDefocus();
            }
        } else {
            motionFX = null;
            iTargetX = x;
            iTargetY = y;
            fSpeedX = 0f;
            fSpeedY = 0;
            findFirstTopLeftFocusable();
        }
    }
    
    protected boolean animate() {
        IContainer parent = null;
        if (getContainingPanel() != null) {
            parent = getContainingPanel();
        } else if (getContainingCanvas() != null) {
            parent = getContainingCanvas();
        } else {
            return false;
        }
        if (isScrollable && fSpeedX != 0 || fSpeedY != 0) {
            if (Math.abs(fSpeedX) < 1 && Math.abs(fSpeedY) < 1) {
                fSpeedX = 0;
                fSpeedY = 0;
            } else {
                fSpeedX *= fSlowDown;
                fSpeedY *= fSlowDown;
                fX += fSpeedX;
                fY += fSpeedY;
                if (fX > 0) {
                    fX = 0;
                    fSpeedX = 0;
                } else if (fX + getWidth() < parent.getWidth()) {
                    fX = parent.getWidth() - getWidth();
                    fSpeedX = 0;
                }
                
                if (fY > startOffsetY) {
                    fY = startOffsetY;
                    fSpeedY = 0;
                } else if (fY + getHeight() < parent.getHeight() - bottomOffset) {
                    fY = parent.getHeight() - getHeight() - bottomOffset;
                    fSpeedY = 0;
                }
                
                x = (int) fX;
                y = (int) fY;
                iTargetX = x;
                iTargetY = y;
            }
            return true;
        }
        
        if (adapter != null) {
            addModelItemsToBottom();
            removeModelItemsFromTop();
            
            addModelItemsToTop();
            removeModelItemsFromBottom();
        }
        
        return false;
    }
    
    public boolean go(int direction) {
        IContainer parent = null;
        if (getContainingPanel() != null) {
            parent = getContainingPanel();
        } else if (getContainingCanvas() != null) {
            parent = getContainingCanvas();
        } else {
            return false;
        }
        //check if current focusable is on screen
        if (currentFocusable != null && currentFocusable.getContainingCanvas() == null) {
            currentFocusable = null;
        }
        // Step 1
        // scroll current focusable if not fully on screen
        try {
            if (currentFocusable != null) {
                if (direction == UikitConstant.UP) {
                    int currentFocusablePos = relativeY(currentFocusable);
                    if (currentFocusablePos + iTargetY < 0) {
                        int diff = iTargetY + currentFocusablePos;
                        if (Math.abs(diff) < iVerticalScrolling) {
                            iTargetY -= diff;
                        } else {
                            iTargetY += iVerticalScrolling;
                        }
                        scrollToTarget();
                        return true;
                    }
                } else if (direction == UikitConstant.DOWN) {
                    int currentFocusablePos = relativeY(currentFocusable);
                    if (currentFocusablePos + iTargetY + currentFocusable.getHeight() > parent.getHeight()) {
                        int diff = currentFocusablePos + iTargetY + currentFocusable.getHeight() - parent.getHeight();
                        if (Math.abs(diff) < iVerticalScrolling) {
                            iTargetY -= diff;
                        } else {
                            iTargetY -= iVerticalScrolling;
                        }
                        scrollToTarget();
                        return true;
                    }
                } else if (direction == UikitConstant.LEFT) {
                    int currentFocusablePos = relativeX(currentFocusable);
                    if (currentFocusablePos + iTargetX < 0) {
                        int diff = iTargetX + currentFocusablePos;
                        if (Math.abs(diff) < iHorizontalScrolling) {
                            iTargetX -= diff;
                        } else {
                            iTargetX += iHorizontalScrolling;
                        }
                        scrollToTarget();
                        return true;
                    }
                } else if (direction == UikitConstant.RIGHT) {
                    int currentFocusablePos = relativeX(currentFocusable);
                    if (currentFocusablePos + iTargetX + currentFocusable.getWidth() > parent.getWidth()) {
                        int diff = currentFocusablePos + iTargetX + currentFocusable.getWidth() - parent.getWidth();
                        if (Math.abs(diff) < iHorizontalScrolling) {
                            iTargetX -= diff;
                        } else {
                            iTargetX -= iHorizontalScrolling;
                        }
                        scrollToTarget();
                        return true;
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            currentFocusable = null;
        }
        // Step 2
        // if not scrolled find next focusable in provided direction
        int iAnchorX = -iTargetX;
        int iAnchorY = -iTargetY;
        try {
            if (currentFocusable != null) {
                if (direction == UikitConstant.UP) {
                    iAnchorX = relativeX(currentFocusable) + currentFocusable.getWidth() / 2;
                    iAnchorY = relativeY(currentFocusable);
                } else if (direction == UikitConstant.DOWN) {
                    iAnchorX = relativeX(currentFocusable) + currentFocusable.getWidth() / 2;
                    iAnchorY = relativeY(currentFocusable) + currentFocusable.getHeight();
                } else if (direction == UikitConstant.LEFT) {
                    iAnchorX = relativeX(currentFocusable);
                    iAnchorY = relativeY(currentFocusable) + currentFocusable.getHeight() / 2;
                } else {
                    iAnchorX = relativeX(currentFocusable) + currentFocusable.getWidth();
                    iAnchorY = relativeY(currentFocusable) + currentFocusable.getHeight() / 2;
                }
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            currentFocusable = null;
        }
        
        Component tempItem;
        int diffX;
        int diffY;
        int tempCenX = 0;
        int tempCenY = 0;
        int i;
        int distance = -1;
        Component next = null;
        
        for (i = 0; i < focusables.size(); i++) {
            tempItem = (Component) focusables.elementAt(i);
            if (tempItem instanceof IEnableable) {
                if (!((IEnableable) tempItem).isEnabled()) {
                    continue;
                }
            }
            if (!tempItem.isVisible()) {
                continue;
            }
            
            int tempRelX = 0;
            int tempRelY = 0;
            
            try {
                tempRelX = relativeX(tempItem);
                tempRelY = relativeY(tempItem);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                continue;
            }
            
            if (direction == UikitConstant.UP) {
                tempCenX = tempRelX + tempItem.getWidth() / 2;
                tempCenY = tempRelY + tempItem.getHeight();
            } else if (direction == UikitConstant.DOWN) {
                tempCenX = tempRelX + tempItem.getWidth() / 2;
                tempCenY = tempRelY;
            } else if (direction == UikitConstant.LEFT) {
                tempCenX = tempRelX + tempItem.getWidth();
                tempCenY = tempRelY + tempItem.getHeight() / 2;
            } else {
                tempCenX = tempRelX;
                tempCenY = tempRelY + tempItem.getHeight() / 2;
            }
            
            diffX = Math.abs(iAnchorX - tempCenX);
            diffY = Math.abs(iAnchorY - tempCenY);
            if (direction == UikitConstant.UP) {
                if (iAnchorY >= tempCenY && ((diffX + diffY) < distance || distance == -1)) {
                    if (iAnchorY - tempRelY < iVerticalScrolling || -iTargetY < tempRelY + tempItem.getHeight()) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                }
            } else if (direction == UikitConstant.DOWN) {
                if (iAnchorY <= tempCenY && ((diffX + diffY) < distance || distance == -1)) {
                    if (tempRelY + tempItem.getHeight() - iAnchorY < iVerticalScrolling || tempRelY + iTargetY < parent.getHeight()) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                }
            } else if (direction == UikitConstant.LEFT) {
                if (iAnchorX >= tempCenX && ((diffX + diffY) < distance || distance == -1)) {
                    if (iAnchorX - tempRelX < iHorizontalScrolling || -iTargetX < tempRelX + tempItem.getWidth()) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                }
            } else {
                if (iAnchorX <= tempCenX && ((diffX + diffY) < distance || distance == -1)) {
                    if (tempRelX + tempItem.getWidth() - iAnchorX < iHorizontalScrolling || tempRelX + iTargetX < parent.getWidth()) {
                        distance = diffX + diffY;
                        next = tempItem;
                    }
                }
            }
        }


        //scroll screen to next focusable

        if (next != null) {
            if (currentFocusable != null) {
                ((IFocusable) currentFocusable).onDefocus();
            }
            currentFocusable = next;
            ((IFocusable) currentFocusable).onFocus();
            
            int rX = 0;
            int rY = 0;
            try {
                rX = relativeX(currentFocusable);
                rY = relativeY(currentFocusable);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                return go(direction);
            }
            
            if (rX + x < 0) {
                if (currentFocusable.getWidth() <= parent.getWidth()) {
                    iTargetX = -rX;
                } else {
                    iTargetX = -rX - (currentFocusable.getWidth() - parent.getWidth());
                }
            }
            if (rX + x + currentFocusable.getWidth() > parent.getWidth()) {
                if (currentFocusable.getWidth() <= parent.getWidth()) {
                    iTargetX = -rX - currentFocusable.getWidth() + parent.getWidth();
                } else {
                    iTargetX = -rX;
                }
            }
            if (rY + y < 0) {
                if (currentFocusable.getHeight() <= parent.getHeight()) {
                    iTargetY = -rY;
                } else {
                    iTargetY = -rY - (currentFocusable.getHeight() - parent.getHeight());
                }
            }
            if (rY + y + currentFocusable.getHeight() > parent.getHeight()) {
                if (currentFocusable.getHeight() <= parent.getHeight()) {
                    iTargetY = -rY - currentFocusable.getHeight() + parent.getHeight();
                } else {
                    iTargetY = -rY;
                }
            }
            if (iTargetX != x || iTargetY != y) {
                scrollToTarget();
            }
            return true;
        }

        // Step 3
        // If there is no current or next item scroll screen
        if (x < 0 || x + iWidth > parent.getWidth() || y < 0 || y + iHeight > parent.getHeight()) {
            if (direction == UikitConstant.UP) {
                if (iTargetY + iVerticalScrolling <= 0) {
                    iTargetY += iVerticalScrolling;
                    scrollToTarget();
                    return true;
                } else if (iTargetY < 0) {
                    iTargetY = 0;
                    scrollToTarget();
                    return true;
                }
            } else if (direction == UikitConstant.DOWN) {
                if (iTargetY - iVerticalScrolling + getHeight() > parent.getHeight() - bottomOffset) {
                    iTargetY -= iVerticalScrolling;
                    scrollToTarget();
                    return true;
                } else if (iTargetY + getHeight() > parent.getHeight() - bottomOffset) {
                    iTargetY = parent.getHeight() - getHeight() - bottomOffset;
                    scrollToTarget();
                    return true;
                }
            } else if (direction == UikitConstant.LEFT) {
                if (iTargetX + iHorizontalScrolling <= 0) {
                    iTargetX += iHorizontalScrolling;
                    scrollToTarget();
                    return true;
                } else if (iTargetX < 0) {
                    iTargetX = 0;
                    scrollToTarget();
                    return true;
                }
            } else {
                if (iTargetX - iHorizontalScrolling + getWidth() > parent.getWidth()) {
                    iTargetX -= iHorizontalScrolling;
                    scrollToTarget();
                    return true;
                } else if (iTargetX + getWidth() > parent.getWidth()) {
                    iTargetX = parent.getWidth() - getWidth();
                    scrollToTarget();
                    return true;
                }
            }
        }

        // Step 4
        // check if current focusable is still on screen

        // Step 5
        // cater for starting offset 
        if (currentFocusable != null
                && startOffsetY != 0
                && direction == UikitConstant.UP
                && getAbsoluteY() == 0) {
            iTargetY = startOffsetY;
            scrollToTarget();
            return true;
        }
        
        return false;
    }
    
    public void setIsScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }
    
    private void scrollToTarget() {
        if (isScrollable) {
            if (bUseMotion) {
                MotionQuadOut motion = new MotionQuadOut();
                motion.init(x, y, iTargetX, iTargetY, 5);
                motionFX = motion;
            } else {
                x = iTargetX;
                y = iTargetY;
            }
        }
    }
    
    public void onFocus() {
        bFocused = true;
        if (currentFocusable != null && !((IFocusable) currentFocusable).isFocused() && currentFocusable.getContainingCanvas() != null) {
            IContainer parent = null;
            if (getContainingPanel() != null) {
                parent = getContainingPanel();
            } else if (getContainingCanvas() != null) {
                parent = getContainingCanvas();
            } else {
                return;
            }
            try {
                int iRelX = relativeX(currentFocusable);
                int iRelY = relativeY(currentFocusable);
                if (!(iRelX + iTargetX < 0 || iRelY + iTargetY < 0 || iRelX + iTargetX + currentFocusable.getWidth() > parent.getWidth() || iRelY + iTargetY + currentFocusable.getHeight() > parent.getHeight())) {
                    ((IFocusable) currentFocusable).onFocus();
                } else {
                    findFirstTopLeftFocusable();
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                findFirstTopLeftFocusable();
            }
        } else {
            findFirstTopLeftFocusable();
        }
    }
    
    public void onDefocus() {
        bFocused = false;
        if (currentFocusable != null && ((IFocusable) currentFocusable).isFocused()) {
            ((IFocusable) currentFocusable).onDefocus();
        }
    }
    
    public boolean isFocused() {
        return bFocused;
    }
    
    public boolean onKeyPressed(int iKeyCode) {
        if (currentFocusable != null && currentFocusable.getContainingCanvas() != null && currentFocusable instanceof IUikitInputHandler) {
            if (((IUikitInputHandler) currentFocusable).onKeyPressed(iKeyCode)) {
                return true;
            }
        }
        switch (UikitCanvas.getGameActionNoAlt(iKeyCode)) {
            case Canvas.UP:
                return go(UikitConstant.UP);
            case Canvas.DOWN:
                return go(UikitConstant.DOWN);
            case Canvas.LEFT:
                return go(UikitConstant.LEFT);
            case Canvas.RIGHT:
                return go(UikitConstant.RIGHT);
        }
        return false;
    }
    
    public boolean onKeyReleased(int iKeyCode) {
        if (currentFocusable != null && currentFocusable.getContainingCanvas() != null && currentFocusable instanceof IUikitInputHandler) {
            if (((IUikitInputHandler) currentFocusable).onKeyReleased(iKeyCode)) {
                return true;
            }
        }
        switch (UikitCanvas.getGameActionNoAlt(iKeyCode)) {
            case Canvas.UP:
            case Canvas.DOWN:
            case Canvas.LEFT:
            case Canvas.RIGHT:
                return true;
        }
        return false;
    }
    
    public boolean onKeyRepeated(int iKeyCode) {
        if (currentFocusable != null && currentFocusable.getContainingCanvas() != null && currentFocusable instanceof IUikitInputHandler) {
            if (((IUikitInputHandler) currentFocusable).onKeyRepeated(iKeyCode)) {
                return true;
            }
        }
        return onKeyPressed(iKeyCode);
    }
    
    public boolean onPress(int type, int iX, int iY) {
        return false;
    }
    
    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        if (motionFX != null) {
            motionFX = null;
        }
        if (type == ITouchEventListener.DRAG && isScrollable) {
            fSpeedX = 0;
            fSpeedY = 0;
            IContainer parent = null;
            if (getContainingPanel() != null) {
                parent = getContainingPanel();
            } else if (getContainingCanvas() != null) {
                parent = getContainingCanvas();
            } else {
                return false;
            }
            //handle horizontal position
            if (x < 0 || x + getWidth() > parent.getWidth()) {
                x += iDeltaX;
                if (x > 0) {
                    x = 0;
                } else if (x + getWidth() < parent.getWidth()) {
                    x = parent.getWidth() - getWidth();
                }
            }
            if (y < startOffsetY || y + getHeight() > parent.getHeight()) {
                y += iDeltaY;
                if (y > startOffsetY) {
                    y = startOffsetY;
                } else if (y + getHeight() < parent.getHeight()) {
                    y = parent.getHeight() - getHeight();
                }
            }
            fX = iTargetX = x;
            fY = iTargetY = y;
            return true;
        } else if (type == ITouchEventListener.DRAG_RELEASE && isScrollable) {
            fSpeedX = iDeltaX;
            fSpeedY = iDeltaY;
            return true;
        }
        return false;
    }
    
    private int relativeX(Component c) {
        try {
            return c.getAbsoluteX() - (getAbsoluteX());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    private int relativeY(Component c) {
        return c.getAbsoluteY() - getAbsoluteY();
    }
    
    public void setUseMotion(boolean bUseMotion) {
        this.bUseMotion = bUseMotion;
    }
    
    public void resetTargetPosition() {
        iTargetX = x;
        iTargetY = y;
    }
    
    public Component getCurrentFocusable() {
        return currentFocusable;
    }
    
    public void setCurrentFocusable(Component focusable) {
        if (focusable == null) {
            throw new IllegalArgumentException("Focusable is null.");
        }
        if (!(focusable instanceof IFocusable)) {
            throw new IllegalArgumentException("Focusable is not IFocusable.");
        }
        if (!focusables.contains(focusable)) {
            throw new IllegalArgumentException("Focusable is not registered on focusables list.");
        }
        if (theAttachedCanvas == null) {
            currentFocusable = focusable;
            return;
        }
        if (UiKitDisplay.getCurrent() != null && UiKitDisplay.getCurrent().isInTouchMode()) {
            currentFocusable = focusable;
        } else {
            if (currentFocusable != null) {
                ((IFocusable) currentFocusable).onDefocus();
            }
            currentFocusable = focusable;
            ((IFocusable) currentFocusable).onFocus();
        }
        
        IContainer parent = null;
        if (getContainingPanel() != null) {
            parent = getContainingPanel();
        } else if (getContainingCanvas() != null) {
            parent = getContainingCanvas();
        } else {
            return;
        }
        
        int rX = 0;
        int rY = 0;
        try {
            rX = relativeX(currentFocusable);
            rY = relativeY(currentFocusable);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        if (rX + x < 0) {
            if (currentFocusable.getWidth() <= parent.getWidth()) {
                iTargetX = -rX;
            } else {
                iTargetX = -rX - (currentFocusable.getWidth() - parent.getWidth());
            }
        }
        if (rX + x + currentFocusable.getWidth() > parent.getWidth()) {
            if (currentFocusable.getWidth() <= parent.getWidth()) {
                iTargetX = -rX - currentFocusable.getWidth() + parent.getHeight();
            } else {
                iTargetX = -rX;
            }
        }
        if (rY + y < 0) {
            if (currentFocusable.getHeight() <= parent.getHeight()) {
                iTargetY = -rY;
            } else {
                iTargetY = -rY - (currentFocusable.getHeight() - parent.getHeight());
            }
        }
        if (rY + y + currentFocusable.getHeight() > parent.getHeight()) {
            if (currentFocusable.getHeight() <= parent.getHeight()) {
                iTargetY = -rY - currentFocusable.getHeight() + parent.getHeight();
            } else {
                iTargetY = -rY;
            }
        }
        if (iTargetX != x || iTargetY != y) {
            x = iTargetX;
            y = iTargetY;
        }
    }
    
    public void setStartOffsetY(int startOffsetY) {
        this.startOffsetY = startOffsetY;
    }
    
    public int getStartOffsetY(){
        return this.startOffsetY;
    }
    
    public void setBottomOffset(int bottomOffset) {
        this.bottomOffset = bottomOffset;
    }
    private IAdapter adapter;
    private int downModelIndex;
    private int upModelIndex;
    private int viewport_height;
    private int viewport_width;
    private int viewportOffset;
    private boolean isLayout = true;
    
    public void setAdapter(IAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }
        
        this.adapter = adapter;
        notifyDatasetChanged();
    }
    
    public void notifyDatasetChanged() {
        removeAllComponents();
        setSize(viewport_width, viewport_height);
        x = 0;
        y = 0;
        resetTargetPosition();
        
        upModelIndex = -1;
        downModelIndex = 0;
        
        addDownModelItem();
        addModelItemsToBottom();
    }
    
    public void setViewportOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException();
        }
        
        viewportOffset = offset;
    }
    
    public void relayout() {
        if (isLayout) {
            super.relayout();
        }
    }
    
    public void setLayout(ILayout layout) {
        super.setLayout(layout);
        if (layout != null && adapter != null) {
            BoxLayout l = (BoxLayout) getLayout();
            l.setUseFirstItemPos(true);
            if (l.getOrientation() == UikitConstant.HORIZONTAL) {
                viewportOffset = viewport_width / 2;
            } else {
                viewportOffset = viewport_height / 2;
            }
            
            notifyDatasetChanged();
        }
        
    }
    
    public synchronized void insertComponentAt(Component c, int index) {
        isLayout = false;
        super.insertComponentAt(c, index);
        isLayout = true;
    }
    
    private void addDownModelItem() {
        if (adapter != null) {
            Component c = adapter.getComponent(downModelIndex);
            this.addComponent(c);
            downModelIndex++;
        }
    }
    
    private void addUpModelItem() {
        Component c = adapter.getComponent(upModelIndex);
        Component currTop = componentAt(0);
        this.insertComponentAt(c, 0);
        BoxLayout layout = (BoxLayout) getLayout();
        if (layout.getOrientation() == UikitConstant.VERTICAL) {
            c.x = currTop.x;
            c.y = currTop.y - layout.getGap() - c.getHeight();
        } else {
            c.y = currTop.y;
            c.x = currTop.x - layout.getGap() - c.getWidth();
        }
        
        upModelIndex--;
    }
    
    private void addModelItemsToBottom() {
        if (adapter != null) {
            if (downModelIndex < adapter.getCount()) {
                Component c = componentAt(getComponentCount() - 1);
                
                BoxLayout layout = (BoxLayout) getLayout();
                if (layout.getOrientation() == UikitConstant.VERTICAL) {
                    if ((iTargetY + c.y + c.getHeight() + layout.getGap()) < (viewport_height + viewportOffset)) {
                        addDownModelItem();
                        addModelItemsToBottom();
                    }
                } else {
                    if ((iTargetX + c.x + c.getWidth() + layout.getGap()) < (viewport_width + viewportOffset)) {
                        addDownModelItem();
                        addModelItemsToBottom();
                    }
                }
            }
        }
    }
    
    private void addModelItemsToTop() {
        if (upModelIndex < adapter.getCount() && upModelIndex > -1) {
            Component c = componentAt(0);
            
            BoxLayout layout = (BoxLayout) getLayout();
            if (layout.getOrientation() == UikitConstant.VERTICAL) {
                if ((iTargetY + c.y) > -viewportOffset) {
                    addUpModelItem();
                    addModelItemsToTop();
                }
            } else {
                if ((iTargetX + c.x) > -viewportOffset) {
                    addUpModelItem();
                    addModelItemsToTop();
                }
            }
        }
    }
    
    private void removeModelItemsFromTop() {
        Component c = componentAt(0);
        BoxLayout layout = (BoxLayout) getLayout();
        if (layout.getOrientation() == UikitConstant.VERTICAL) {
            if (iTargetY + c.y + c.getHeight() < -viewportOffset) {
                removeComponent(c);
                upModelIndex++;
                removeModelItemsFromTop();
            }
        } else {
            if (iTargetX + c.x + c.getWidth() < -viewportOffset) {
                removeComponent(c);
                upModelIndex++;
                removeModelItemsFromTop();
            }
        }
    }
    
    private void removeModelItemsFromBottom() {
        Component c = componentAt(getComponentCount() - 1);
        BoxLayout layout = (BoxLayout) getLayout();
        if (layout.getOrientation() == UikitConstant.VERTICAL) {
            if (iTargetY + c.y > (viewport_height + viewportOffset)) {
                removeComponent(c);
                downModelIndex--;
                removeModelItemsFromBottom();
            }
        } else {
            if (iTargetX + c.x > (viewport_width + viewportOffset)) {
                removeComponent(c);
                downModelIndex--;
                removeModelItemsFromBottom();
            }
        }
    }
}
