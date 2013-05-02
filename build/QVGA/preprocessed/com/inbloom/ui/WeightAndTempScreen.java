package com.inbloom.ui;

import com.inbloom.model.Settings;
import com.inbloom.ui.components.EntrySelectionItem;
import com.inbloom.utils.Database;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.coreElements.Component;

public class WeightAndTempScreen extends EntrySelectionScreen {

    private Settings settings;
    private int selectedIndex;

    public WeightAndTempScreen(Object param) {
        entryId = ((Integer) param).intValue();
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (settings != null) {
            if (entryId == Utils.ENTRY_WEIGHT) {
                selectedIndex = settings.getWeight_unit() == Settings.WEIGHT_KG ? 0 : 1;
            } else {

                selectedIndex = settings.getBasal_temp_unit() == Settings.TEMP_CEL ? 0 : 1;
            }
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

    public String getHeaderTitle() {
        return entryId == Utils.ENTRY_TEMP ? Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP) : Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT);
    }

    public void saveValue() {
        try {
            if (entryId == Utils.ENTRY_WEIGHT) {
                settings.setWeight_unit(selectedIndex == 0 ? Settings.WEIGHT_KG : Settings.WEIGHT_POUNDS);
            } else {
                settings.setBasal_temp_unit(selectedIndex == 0 ? Settings.TEMP_CEL : Settings.TEMP_FAH);
            }
            Database.getInstance().saveISerializable(settings, Database.SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
