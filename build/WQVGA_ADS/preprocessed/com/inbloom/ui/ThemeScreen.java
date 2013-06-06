package com.inbloom.ui;

import com.inbloom.model.Settings;
import com.inbloom.ui.components.EntrySelectionItem;
import com.inbloom.utils.Database;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.coreElements.Component;

public class ThemeScreen extends EntrySelectionScreen {

    private Settings settings;
    private int selectedIndex;
    private int theme;

    public ThemeScreen(Object cDetails) {
        super(cDetails);
    }

    public ThemeScreen() {
        this.entryId = Utils.ENTRY_THEMES;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (settings != null) {
            selectedIndex = settings.getCurrentTheme();
            entryIndices = new int[]{selectedIndex};
        }
        initResources();

        initComponents();
        int h = Resources.getInstance().getThemeImage(GraphicsResources.IMG_BG_DEFAULT).getHeight() + (vgap * 2);
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = componentAt(i);
            c.setSize(componentAt(i).getWidth(), h);
            ((EntrySelectionItem)c).reCalcPos();
        }
        relayout();
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        super.onComponentEvent(c, e, o, p);
        if (e == EntrySelectionItem.EVENT_CHECKED) {
            selectedIndex = p;
        }
    }

    public void saveTheme() {
        theme = selectedIndex;
        settings.setCurrentTheme(theme);
        try {
            Database.getInstance().saveISerializable(settings, Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getTheme() {
        return this.theme;
    }
}
