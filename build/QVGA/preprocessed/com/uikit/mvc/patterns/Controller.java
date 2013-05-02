package com.uikit.mvc.patterns;

import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.IComponentEventListener;
import com.uikit.coreElements.UikitCanvas;
import com.uikit.coreElements.View;

import java.util.Stack;
import javax.microedition.midlet.MIDlet;

public abstract class Controller implements IComponentEventListener {

    protected View view;
    protected Stack screens;
    protected UikitCanvas canvas;
    protected Screen current_screen;
    protected MIDlet midlet;
    protected boolean hasTransitionFx;
    protected int previous_screen_id;

    protected Controller() {
        screens = new Stack();
    }

    public View getView() {
        return this.view;
    }

    public void setMIDlet(MIDlet midlet) {
        if (midlet == null) {
            throw new IllegalArgumentException();
        }
        this.midlet = midlet;
    }

    public MIDlet getMIDlet() {
        return this.midlet;
    }

    public abstract void addScreen(Screen screen);

    public abstract void removeScreen(Screen screen);

    public abstract void addLayers();

    public void init(MIDlet midlet) {
        setMIDlet(midlet);
        canvas = new UikitCanvas(30);
        UiKitDisplay.init(midlet);
        view = new View(canvas);
        addLayers();

        UiKitDisplay.setCurrent(canvas);
    }

    public abstract Screen loadScreen(int screen_id, Object param);

    public void onComponentEvent(Component c, int eventId, Object o, int param) {
        if (c instanceof Screen) {
            if (eventId == Screen.EVENT_EXIT_FINISHED) {
                ((Screen) c).freeResources();
                removeScreen((Screen) c);
            }
        }
    }

    public void navigateScreen(int screen_id, boolean addToBackStack, Object param) {
        if (current_screen != null) {
            removeScreen(current_screen);
        }

        current_screen = loadScreen(screen_id, param);
        current_screen.setEventListener(this);
        current_screen.setController(this);

        if (addToBackStack) {
            if (screens.isEmpty()) {
                screens.push(new Integer(screen_id));
            } else {
                if (((Integer) screens.peek()).intValue() != screen_id) {
                    screens.push(new Integer(screen_id));
                }
            }
        }

        current_screen.y = 0;
        addScreen(current_screen);

    }

    public void back() {
        if (!screens.isEmpty()) {
            screens.pop();
            if (!screens.isEmpty()) {
                navigateScreen(((Integer) screens.peek()).intValue(), false, new Boolean(false));
            }
        }
    }

    public void startAnimation() {
        if (canvas.isAnimationStopped()) {
            canvas.startStopAnimation();
        }
    }

    public void stopAnimation() {
        if (!canvas.isAnimationStopped()) {
            canvas.startStopAnimation();
        }
    }

    public int getPrevious_screen_id() {
        return previous_screen_id;
    }

    public void setPrevious_screen_id(int previous_screen_id) {
        this.previous_screen_id = previous_screen_id;
    }
}
