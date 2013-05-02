package com.inbloom.ui;

import com.inbloom.model.Settings;
import com.inbloom.ui.components.EntrySelectionItem;
import com.inbloom.utils.Database;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Component;
import com.uikit.layout.BoxLayout;
import javax.microedition.lcdui.Image;

public class LocaleScreen extends EntrySelectionScreen {

    private Settings settings;
    private int selectedIndex;
    private String locale;

    public LocaleScreen(Object cDetails) {
        super(cDetails);
    }

    public LocaleScreen() {
        this.entryId = Utils.ENTRY_LOCALES;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (settings != null) {
            selectedIndex = Utils.getSelectedIndex(settings.getCurrentLocale());
            entryIndices = new int[]{selectedIndex};
        }
        initResources();

        initComponents();
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        super.onComponentEvent(c, e, o, p);
        if (e == EntrySelectionItem.EVENT_CHECKED) {
            selectedIndex = p;
        }
    }

    public void saveLocale() {
        locale = Utils.getEntryText(Utils.ENTRY_LOCALES, selectedIndex);
        settings.setCurrentLocale(locale);
        try {
            Database.getInstance().saveISerializable(settings, Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void initComponents() {
        setLayout(new BoxLayout(UikitConstant.VERTICAL, vgap));
        int w = getWidth() - (padding * 2);
        Image imgFlag = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_DE);
        int h = imgFlag.getHeight() + (vgap * 2);
        updateModel(entryId);
        addAllEntrySelectionItems(w, h, titles, icons);

        
        updateBottomOffset();
        getStyle(true).setPadding(padding, padding, bottomPadding - padding, padding);
    }

    public String getLocale() {
        return this.locale;
    }
}
