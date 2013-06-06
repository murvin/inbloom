package com.uikit.coreElements;

import com.uikit.datastructures.IComparable;

public class ContainerLayer extends Panel implements IComparable {

    protected int zIndex;

    ContainerLayer(int width, int height) {
        super(width, height);
        useClippingMode(false);
    }

    ContainerLayer(int width, int height, int zIndex) {
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
        return ((ContainerLayer) comparable).getZIndex() == this.zIndex ? 0 : (((ContainerLayer) comparable).getZIndex() < this.zIndex ? 1 : -1);
    }
}
