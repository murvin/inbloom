package com.uikit.mvc.patterns;

import com.uikit.motion.IMotionListener;
import com.uikit.motion.Motion;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.SmartPanel;
import com.uikit.coreElements.IComponentEventListener;
import com.uikit.coreElements.IFocusable;

public abstract class Screen extends SmartPanel
        implements IComponentEventListener, IMotionListener {

    public static final int EVENT_ENTER_FINISHED = 0x0001;
    public static final int EVENT_EXIT_FINISHED = 0x0002;
    protected Motion mfx_slide;
    protected Controller controller;

    public Screen() {
        super(UiKitDisplay.getWidth(), UiKitDisplay.getHeight());
    }

    public Screen(int width, int height) {
        super(width, height);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public abstract void enter();

    public abstract void exit();

    public abstract void exitToRight();

    protected void notifyExitFinished() {
        if (cel != null) {
            cel.onComponentEvent(this, EVENT_EXIT_FINISHED, null, 0);
        }
    }

    protected void notifyEnterFinished() {
        if (cel != null) {
            cel.onComponentEvent(this, EVENT_ENTER_FINISHED, null, 0);
        }
    }

    protected void freeResources() {
        removeAllComponents();
    }

    protected void activate() {
        if (getCurrentFocusable() != null) {
            ((IFocusable) getCurrentFocusable()).onFocus();
        }
    }

    protected void deactivate() {
        if (getCurrentFocusable() != null) {
            ((IFocusable) getCurrentFocusable()).onDefocus();
        }
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
    }

    public void onMotionStarted(Motion mfx) {
    }

    public void onMotionFinished(Motion mfx) {
        if (x != 0) {
            notifyExitFinished();
        } else {
            notifyEnterFinished();
        }
    }

    public void onMotionProgressed(Motion mfx, int i) {
    }
}
