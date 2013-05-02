package com.uikit.coreElements;

public interface ITouchEventListener {

    public static final int SINGLE_PRESS = 0x000;
    public static final int LONG_PRESS = 0x001;
    public static final int DOUBLE_PRESS = 0x002;
    public static final int DRAG = 0x003;
    public static final int DRAG_RELEASE = 0x004;
    public static final int TOUCH_DOWN = 0x005;
    public static final int TOUCH_RELEASE = 0x006;

    public boolean onPress(int type, int x, int y);

    public boolean onDrag(int type, int startX, int startY, int deltaX, int deltaY);
}
