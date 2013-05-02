package com.inbloom.ui.components;

import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;

import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Panel;
import com.uikit.layout.BoxLayout;

import javax.microedition.lcdui.Image;

public class ChartKeysPanel extends Panel {

    private ChartKey keyTemp, keyWeight;
    private Image iconWeight, iconTemp;
    private int colourWeight, colourTemp;
    private int padding;

    public ChartKeysPanel(int width, int height) {
        super(width, height);
        initResources();
        initComponents();
    }

    private void initResources() {
        iconWeight = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_WEIGHT);
        iconTemp = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_TEMP);
        colourTemp = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CHARTKEY_TEMP));
        colourWeight = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CHARTKEY_WEIGHT));
        padding = 6 * UiKitDisplay.getWidth() / 100;
    }

    private void initComponents() {
        keyTemp = new ChartKey(iconTemp, colourTemp);
        keyWeight = new ChartKey(iconWeight, colourWeight);

        addComponent(keyTemp);
        addComponent(keyWeight);

        setLayout(new BoxLayout(UikitConstant.HORIZONTAL, 0));
        getStyle(true).setPadding(padding, 0, padding, 0);
    }
}
