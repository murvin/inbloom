package com.uikit.coreElements;

public interface IContainer {

    void addComponent(Component c);

    int indexOfComponent(Component c);

    Component componentAt(int index);

    void insertComponentAt(Component c, int index);

    void replaceComponent(Component oldC, Component newC);

    void removeComponent(Component c);

    void removeAllComponents();

    int getComponentCount();

    int getWidth();

    int getHeight();
}
