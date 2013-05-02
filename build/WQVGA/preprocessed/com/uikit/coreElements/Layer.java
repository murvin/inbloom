package com.uikit.coreElements;

import com.uikit.datastructures.IComparable;

public class Layer extends Panel implements IComparable {

    protected int zIndex;

    Layer(int width, int height) {
        super(width, height);
        useClippingMode(false);
    }

    Layer(int width, int height, int zIndex) {
        this(width, height);
        this.zIndex = zIndex;
        useClippingMode(false);
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public int compareTo(Object comparable) {
        return ((Layer) comparable).getZIndex() == this.zIndex ? 0 : (((Layer) comparable).getZIndex() < this.zIndex ? 1 : -1);
    }
}
