package com.uikit.coreElements;

public interface IAdapter {

    int getCount();

    Object getItem(int position);

    long getItemId(int position);

    int getItemComponentType(int position);

    Component getComponent(int position);

    boolean isEmpty();
}
