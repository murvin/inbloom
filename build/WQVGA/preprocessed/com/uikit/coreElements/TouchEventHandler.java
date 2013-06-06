package com.uikit.coreElements;

import com.uikit.datastructures.Stack;

public class TouchEventHandler implements IUiKitPointerHandler {

    private int iDragPrevX;
    private int iDragPrevY;
    private int iDragPrevDeltaX;
    private int iDragPrevDeltaY;
    private int iPressStartX;
    private int iPressStartY;
    private int iDoublePressStartX;
    private int iDoublePressStartY;
    private int iPressAccuracy = 15;
    private int iLongPressDuration = 500;
    private int iDoublePressInterval = 500;
    private int iDoublePressAccuracy = 50;
    private boolean bPressRegistered = false;
    private boolean bInPressRange = true;
    private boolean bDoublePressStarted = false;
    private long longPressStart;
    private long lDoublePressStart;
    private ITouchEventListener currentDragListener;
    private ITouchEventListener touchReleaseListener;
    private IContainer container;

    public TouchEventHandler(IContainer canvas) {
        this.container = canvas;
    }

    public boolean onPointerPressed(int iX, int iY) {
        iDragPrevX = iPressStartX = iX;
        iDragPrevY = iPressStartY = iY;
        bPressRegistered = true;
        bInPressRange = true;

        longPressStart = System.currentTimeMillis();

        if (System.currentTimeMillis() - lDoublePressStart > iDoublePressInterval) {
            bDoublePressStarted = false;
            lDoublePressStart = System.currentTimeMillis();
            iDoublePressStartX = iX;
            iDoublePressStartY = iY;
        }

        Stack stack = new Stack();
        findTouchListeners(iX, iY, container, stack);
        while (!stack.isEmpty()) {
            ITouchEventListener listener = (ITouchEventListener) stack.pop();
            if (listener.onPress(ITouchEventListener.TOUCH_DOWN, iX, iY)) {
                touchReleaseListener = listener;
                break;
            }
        }

        if (currentDragListener != null) {
            currentDragListener.onDrag(ITouchEventListener.DRAG, iDragPrevX = iX, iDragPrevY = iY, iDragPrevDeltaX = 0, iDragPrevDeltaY = 0);
        } else {
            while (!stack.isEmpty()) {
                ITouchEventListener listener = (ITouchEventListener) stack.pop();
                if (listener.onDrag(ITouchEventListener.DRAG, iDragPrevX = iX, iDragPrevY = iY, iDragPrevDeltaX = 0, iDragPrevDeltaY = 0)) {
                    currentDragListener = listener;
                    break;
                }
            }
        }
        return true;
    }

    public boolean onPointerReleased(int iX, int iY) {
        if (!bPressRegistered) {
            return onPointerPressed(iX, iY);
        }

        if (touchReleaseListener != null) {
            touchReleaseListener.onPress(ITouchEventListener.TOUCH_RELEASE, iX, iY);
            touchReleaseListener = null;
        }

        if (currentDragListener != null) {
//            currentDragListener.onDrag(ITouchEventListener.DRAG_RELEASE, iDragPrevX, iDragPrevY, iDragPrevDeltaX, (iDragPrevDeltaY = iY - iDragPrevY));
            currentDragListener.onDrag(ITouchEventListener.DRAG_RELEASE, iDragPrevX, iDragPrevY, iDragPrevDeltaX, iDragPrevDeltaY);
        } else {
            Stack stack = new Stack();
            findTouchListeners(iX, iY, container, stack);
            while (!stack.isEmpty()) {
                ITouchEventListener listener = (ITouchEventListener) stack.pop();
                if (listener.onDrag(ITouchEventListener.DRAG_RELEASE, iDragPrevX, iDragPrevY, iDragPrevDeltaX, iDragPrevDeltaY)) {
//                if (listener.onDrag(ITouchEventListener.DRAG_RELEASE, iDragPrevX, iDragPrevY, iDragPrevDeltaX, (iDragPrevDeltaY = iY - iDragPrevY))) {
                    currentDragListener = listener;
                    break;
                }
            }
        }

        if (bInPressRange && Math.abs(iX - iPressStartX) < iPressAccuracy && Math.abs(iY - iPressStartY) < iPressAccuracy) {
            if (System.currentTimeMillis() - longPressStart > iLongPressDuration) {
                Stack stack = new Stack();
                findTouchListeners(iPressStartX, iPressStartY, container, stack);
                while (!stack.isEmpty()) {
                    ITouchEventListener listener = (ITouchEventListener) stack.pop();
                    if (listener.onPress(ITouchEventListener.LONG_PRESS, iX - ((Component) listener).getAbsoluteX(), iY - ((Component) listener).getAbsoluteY())) {
                        break;
                    }
                }
                bDoublePressStarted = false;
            } else {
                Stack stack = new Stack();
                findTouchListeners(iPressStartX, iPressStartY, container, stack);
                while (!stack.isEmpty()) {
                    ITouchEventListener listener = (ITouchEventListener) stack.pop();
                    if (listener.onPress(ITouchEventListener.SINGLE_PRESS, iX - ((Component) listener).getAbsoluteX(), iY - ((Component) listener).getAbsoluteY())) {
                        break;
                    }
                }
                if (bDoublePressStarted && Math.abs(iX - iDoublePressStartX) < iDoublePressAccuracy && Math.abs(iY - iDoublePressStartY) < iPressAccuracy && System.currentTimeMillis() - lDoublePressStart < iDoublePressInterval) {
                    stack = new Stack();
                    findTouchListeners(iDoublePressStartX, iDoublePressStartY, container, stack);
                    while (!stack.isEmpty()) {
                        ITouchEventListener listener = (ITouchEventListener) stack.pop();
                        if (listener.onPress(ITouchEventListener.DOUBLE_PRESS, iX - ((Component) listener).getAbsoluteX(), iY - ((Component) listener).getAbsoluteY())) {
                            break;
                        }
                    }
                } else {
                    bDoublePressStarted = true;
                }
            }
        }
        bPressRegistered = false;
        currentDragListener = null;
        return true;
    }

    public boolean onPointerDragged(int iX, int iY) {
        if (currentDragListener != null) {
            currentDragListener.onDrag(ITouchEventListener.DRAG, iDragPrevX, iDragPrevY, iDragPrevDeltaX = iX - iDragPrevX, iDragPrevDeltaY = iY - iDragPrevY);
        } else {
            Stack stack = new Stack();
            findTouchListeners(iX, iY, container, stack);
            while (!stack.isEmpty()) {
                ITouchEventListener listener = (ITouchEventListener) stack.pop();
                if (listener.onDrag(ITouchEventListener.DRAG, iDragPrevX, iDragPrevY, iDragPrevDeltaX = iX - iDragPrevX, iDragPrevDeltaY = iY - iDragPrevY)) {
                    currentDragListener = listener;
                    break;
                }
            }
        }
        if (Math.abs(iX - iPressStartX) > iPressAccuracy || Math.abs(iY - iPressStartY) > iPressAccuracy) {
            bInPressRange = false;
            bDoublePressStarted = false;
        }
        iDragPrevX = iX;
        iDragPrevY = iY;
        return true;
    }

    private void findTouchListeners(int iX, int iY, IContainer container, Stack stack) {
        final int s = container.getComponentCount();
        Component c;
        for (int i = 0; i < s; i++) {
            c = container.componentAt(i);
            if (c.isVisible() && c instanceof ITouchEventListener && c.theAttachedCanvas != null) {
                final int x = c.getAbsoluteX();
                final int y = c.getAbsoluteY();
                if (x <= iX && x + c.iWidth > iX
                        && y <= iY && y + c.iHeight > iY) {
                    stack.push(c);
                }
            }
            if (c instanceof IContainer && c.theAttachedCanvas != null) {
                findTouchListeners(iX, iY, (IContainer) c, stack);
            }
        }
    }

    public void setSinglePressAccuracy(int iPressAccuracy) {
        if (iPressAccuracy <= 0) {
            throw new IllegalArgumentException();
        }
        this.iPressAccuracy = iPressAccuracy;
    }

    public void setLongPressDuration(int iLongPressDuration) {
        if (iLongPressDuration <= 0) {
            throw new IllegalArgumentException();
        }
        this.iLongPressDuration = iLongPressDuration;
    }

    public void setDoublePressAccuracy(int iDoublePressAccuracy) {
        if (iDoublePressAccuracy <= 0) {
            throw new IllegalArgumentException();
        }
        this.iDoublePressAccuracy = iDoublePressAccuracy;
    }

    public void setIDoublePressInterval(int iDoublePressInterval) {
        if (iDoublePressInterval <= 0) {
            throw new IllegalArgumentException();
        }
        this.iDoublePressInterval = iDoublePressInterval;
    }
}
