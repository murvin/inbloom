package com.uikit.styles;

import com.uikit.painters.IPainter;
import java.util.Vector;

public class ComponentStyle extends Style {

    int padding;
    public static final int TOP = 24;
    public static final int BOTTOM = 16;
    public static final int LEFT = 8;
    public static final int RIGHT = 0;
    private Vector renderers;

    public ComponentStyle() {
    }

    public void addRenderer(IPainter renderer) {
        if (renderers == null) {
            renderers = new Vector();
        }
        renderers.addElement(renderer);
    }

    public Vector getRenderers() {
        return renderers;
    }

    public void clearAllRenderers() {
        if (renderers != null) {
            renderers.removeAllElements();
        }
    }

    public void setPadding(int padding, int alignment) {
        if ((alignment == TOP || alignment == BOTTOM || alignment == LEFT || alignment == RIGHT) && padding >= 0) {
            padding <<= alignment;
        } else {
            throw new IllegalArgumentException();
        }

        this.padding &= ~((0x000000ff << alignment));
        this.padding |= padding;
        notifyStyleChanged();
    }

    public int getPadding(int alignment) {
        if (alignment == TOP || alignment == BOTTOM || alignment == LEFT || alignment == RIGHT) {
            return (padding >>> alignment) & 0xff;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setPadding(int padding) {
        setPadding(new int[]{padding, padding, padding, padding});
    }

    public void setPadding(int top, int right, int bottom, int left) {
        setPadding(new int[]{top, right, bottom, left});
    }

    public void setPadding(int[] padding) {
        if (padding == null || padding.length != 4) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < padding.length; i++) {
            if (padding[i] < 0) {
                throw new IllegalArgumentException();
            }
        }
        if (padding[0] >= 0 || padding[1] >= 0 || padding[2] >= 0 || padding[3] >= 0) {
            setPadding(padding[0], TOP);
            setPadding(padding[1], RIGHT);
            setPadding(padding[2], BOTTOM);
            setPadding(padding[3], LEFT);
        }
    }

    public boolean hasPadding() {
        return padding != 0;
    }

    public int getCompoundPadding() {
        return this.padding;
    }

    public ComponentStyle copy() {
        ComponentStyle s = new ComponentStyle();
        s.padding = padding;
        if (renderers != null) {
            s.renderers = new Vector();
            for (int i = 0; i < renderers.size(); i++) {
                s.renderers.addElement(renderers.elementAt(i));
            }
        }
        return s;
    }
}
