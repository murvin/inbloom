package com.uikit.coreElements;

import com.uikit.datastructures.BST;
import com.uikit.datastructures.IComparable;
import java.util.Enumeration;

public class View implements ILayerManager {

    protected BST layers;
    protected UikitCanvas canvas;

    public View(UikitCanvas canvas) {
        setCanvas(canvas);

        layers = new BST();
    }

    public int getLayerCount() {
        return layers.size();
    }

    public void addLayer(int index) {
        ContainerLayer layer = new ContainerLayer(canvas.getWidth(), canvas.getHeight(), index);
        layers.insert((IComparable) layer);
        canvas.insertComponentAt((Component) layer, getIndex(layer));
    }

    public boolean removeLayer(int index) {
        ContainerLayer layerToRemove = getLayer(index);
        if (layerToRemove == null) {
            return false;
        }
        if (layers.delete(layerToRemove) == false) {
            return false;
        }
        canvas.removeComponent(layerToRemove);
        return true;
    }

    public ContainerLayer getLayer(int index) {
        return (ContainerLayer) layers.search(new ContainerLayer(canvas.getWidth(), canvas.getHeight(), index));
    }

    public void removeAllLayers() {
        for (Enumeration enu = layers.flatten().elements(); enu.hasMoreElements();) {
            canvas.removeComponent((Component) enu.nextElement());
        }
        layers = new BST();
    }

    public int bringToFront(int zIndex) {
        return bringToFront(getLayer(zIndex));
    }

    public int bringToFront(ContainerLayer layer) {
        IComparable max = layers.getMaxElement();
        if (max instanceof ContainerLayer) {
            ContainerLayer maxLayer = (ContainerLayer) max;
            if (maxLayer.getZIndex() > layer.getZIndex()) {
                //layer is not on the top so it needs to be removed from it's present
                //position and re-added as the top component.
                layers.delete((IComparable) layer);
                canvas.removeComponent(layer);
                layer.setZIndex(maxLayer.getZIndex() + 1);
                //Add the layer to the top of the list of components
                canvas.insertComponentAt((Component) layer, canvas.getComponentCount() - 1);
                layers.insert((IComparable) layer);
            }
        }
        return layer.getZIndex();
    }

    public int sendtoBack(int zIndex) {
        return sendToBack(getLayer(zIndex));
    }

    public int sendToBack(ContainerLayer layer) {
        IComparable min = layers.getMinElement();
        if (min instanceof ContainerLayer) {
            ContainerLayer minLayer = (ContainerLayer) min;
            if (minLayer.getZIndex() < layer.getZIndex()) {
                //layer is not on the bottom so it needs to be removed from it's present
                //position and re-added as the bottom component.
                layers.delete((IComparable) layer);
                canvas.removeComponent(layer);
                layer.setZIndex(minLayer.getZIndex() - 1);
                //Add the layer to the bottom of the list of components
                canvas.insertComponentAt((Component) layer, 0);
                layers.insert((IComparable) layer);
            }
        }
        return layer.getZIndex();
    }

    public void setCanvas(UikitCanvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException();
        }

        this.canvas = canvas;
    }

    private int getIndex(ContainerLayer layer) {
        int i = 0;
        Enumeration enu;
        for (enu = layers.flatten().elements(), i = 0; enu.hasMoreElements(); i++) {
            if (enu.nextElement() == layer) {
                break;
            }
        }
        return i;
    }
}
