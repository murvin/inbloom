package com.uikit.styles;

import com.uikit.datastructures.LinkedList;
import com.uikit.coreElements.Component;
import java.lang.ref.WeakReference;
import java.util.Enumeration;

public abstract class Style {

    LinkedList components;

    public void addComponent(Component c) {
        if (components == null) {
            components = new LinkedList();
        }
        components.addElement(new WeakReference(c));
    }

    public void removeComponent(Component c) {
        if (components == null || components.isEmpty()) {
            return;
        }
        Enumeration en = components.elements();
        while (en.hasMoreElements()) {
            WeakReference wr = (WeakReference) en.nextElement();
            if (wr.get() == null) {
                components.removeElement(wr);
            } else if (wr.get() == c) {
                wr.clear();
                components.removeElement(wr);
            }
        }
    }

    protected void notifyStyleChanged() {
        if (components == null || components.isEmpty()) {
            return;
        }
        System.gc();
        Enumeration en = components.elements();
        while (en.hasMoreElements()) {
            WeakReference wr = (WeakReference) en.nextElement();
            if (wr.get() == null) {
                components.removeElement(wr);
            } else {
                ((Component) wr.get()).notifyStyleChanged(this);
            }
        }
    }
}
