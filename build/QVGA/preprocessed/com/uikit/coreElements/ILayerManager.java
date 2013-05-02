package com.uikit.coreElements;

public interface ILayerManager {

    void addLayer(int index);

    boolean removeLayer(int index);

    ContainerLayer getLayer(int index);
    
    int getLayerCount();

    void removeAllLayers();
}
