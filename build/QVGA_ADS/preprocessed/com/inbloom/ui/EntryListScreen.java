package com.inbloom.ui;

//#if QVGA_ADS
import InneractiveSDK.IADView.IaOptionalParams;
import java.util.Hashtable;
import com.inbloom.ui.components.AdvertComponent;
//#endif 
import com.inbloom.InBloomController;
import com.inbloom.model.Date;
import com.inbloom.model.Entries;
import com.inbloom.model.Entry;
import com.inbloom.model.Settings;
import com.inbloom.model.User;
import com.inbloom.ui.components.EntryItem;
import com.inbloom.ui.components.SettingsDelimiter;
import com.inbloom.utils.Database;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;


import com.uikit.animations.UikitTextBox;
//#if QVGA || WQVGA || QVGA_ADS
import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.coreElements.Component;
import com.uikit.coreElements.IAdapter;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.painters.PatchPainter;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.TextStyle;


import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class EntryListScreen extends InBloomScreen implements IAdapter {

    private Vector entriesList;
    private Vector adapterList;
    private Image imgArrow;
    //#if QVGA || WQVGA || QVGA_ADS
    private BitmapFont 
    //#elif WVGA
//#                 private SystemFont
            //#endif 
            med_font, large_font;
    private PatchPainter onFocusHighLight;
    private int g, h;
    private int lineColour;
    private Date tempDate;
    private Settings settings;
    private EntryItem tempEntryItem;
    //#if WVGA
//#     private int fontColour;
    //#endif
    //#if QVGA_ADS
    private final int ADVERT_ID = 0x004;
    //#endif 

    public EntryListScreen() {
        initResources();
        initComponents();

    }

    private void initResources() {
        User user = null;
        try {
            user = (User) Database.getInstance().retrieveISerializable(Database.USER);
            if (user.getCycle() != null) {
                entriesList = user.getCycle().getEntries();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgArrow = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW);
        //#if QVGA || WQVGA || QVGA_ADS
        Image imgFontMed = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
        med_font = new BitmapFont(imgFontMed, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);

        Image imgFontLarge = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        large_font = new BitmapFont(imgFontLarge, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         large_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
//#         med_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        //#endif 

        int patchBord = 5;
        Image imgHighlight = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH);
        this.onFocusHighLight = new PatchPainter(imgHighlight, patchBord, patchBord, patchBord, patchBord);

        g = 2 * UiKitDisplay.getHeight() / 100;
        Image imgSmiley = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE0);
        padding = 4 * UiKitDisplay.getWidth() / 100;
        h = imgSmiley.getHeight() + (g * 2);

        lineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));

    }

    private void addElementsToList(Entries entries) {

        adapterList.addElement(entries.getDate());


        if (entries.getMoodEntry() != null) {
            entries.getMoodEntry().setDate(entries.getDate());
            adapterList.addElement(entries.getMoodEntry());
        }

        if (entries.getIntimacyEntry() != null) {
            entries.getIntimacyEntry().setDate(entries.getDate());
            adapterList.addElement(entries.getIntimacyEntry());
        }

        if (entries.getWeight() != null) {
            String weight = null;
            if (entries.getWeightUnit() == settings.getWeight_unit()) {
                weight = entries.getWeight();
            } else {
                if (entries.getWeightUnit() == Settings.WEIGHT_POUNDS) {
                    weight = String.valueOf(Utils.lbToKg(Float.parseFloat(entries.getWeight())));
                } else {
                    weight = String.valueOf(Utils.kgToLb(Float.parseFloat(entries.getWeight())));
                }
            }

            adapterList.addElement(new EntryStringItem(Utils.ENTRY_WEIGHT, weight, entries.getDate()));
        }

        if (entries.getTemp() != null) {
            String temp = null;
            if (entries.getTempUnit() == settings.getBasal_temp_unit()) {
                temp = entries.getTemp();
            } else {
                if (entries.getTempUnit() == Settings.TEMP_FAH) {
                    temp = String.valueOf(Utils.fahToCel(Float.parseFloat(entries.getTemp())));
                } else {
                    temp = String.valueOf(Utils.celToFah(Float.parseFloat(entries.getTemp())));
                }
            }
            adapterList.addElement(new EntryStringItem(Utils.ENTRY_TEMP, temp, entries.getDate()));
        }

        if (entries.getFlowEntry() != null) {
            entries.getFlowEntry().setDate(entries.getDate());
            adapterList.addElement(entries.getFlowEntry());
        }

        if (entries.getSpottingEntry() != null) {
            entries.getSpottingEntry().setDate(entries.getDate());
            adapterList.addElement(entries.getSpottingEntry());
        }

        if (entries.getSymptomsIndices() != null) {
            Vector v = new Vector();
            v.addElement(entries.getDate());
            v.addElement(entries.getSymptomsIndices());
            adapterList.addElement(v);
        }

        if (entries.getNote() != null) {
            adapterList.addElement(entries.getNote());
        }
    }

    private void initComponents() {
        if (entriesList != null) {
            adapterList = new Vector();
            //#if QVGA_ADS
            adapterList.addElement(new Integer(ADVERT_ID));
            //#endif 

            for (int i = 0; i < entriesList.size(); i++) {
                Entries entries = (Entries) entriesList.elementAt(i);
                addElementsToList(entries);
            }
            setAdapter(this);
        } else {
            addComponent(getTxtBox(Resources.getInstance().getText(GlobalResources.TXT_ALERT_NOENTRIES_ADDED)));
        }

        updateBottomOffset();
        getStyle(true).setPadding(0, padding, bottomPadding, padding);

    }

    private String getDate(Date date) {
        StringBuffer d = new StringBuffer();
        d.append(date.getDay()).append(" ").append(Utils.getMonthsText()[date.getMonth()]).append(" ").append(date.getYear());
        return d.toString();
    }

    private UikitTextBox getTxtBox(String text) {
        int w = getWidth() - (padding * 2);
        ComponentStyle st = new ComponentStyle();
        st.setPadding(padding);
        UikitTextBox txtBox = new UikitTextBox(w, text, st);
        txtBox.setTextStyle(new TextStyle(med_font));
        return txtBox;
    }

    private EntryItem getSymptoms(Date date, int[] indices) {
        int w = getWidth() - (padding * 2);
        EntryItem item = new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_SYMPTOMS),
                null,
                null,
                imgArrow, med_font, this, Utils.ENTRY_SYMPTOMS, indices);
        item.addBasicTextBox(Utils.getEntryText(Utils.ENTRY_SYMPTOMS, 0));
        item.setDate(date);
        return item;
    }

    private EntryItem getStringEntryItem(EntryStringItem e) {
        int w = getWidth() - (padding * 2);
        EntryItem item = new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(e.entryId),
                e.title,
                Utils.getEntryIcon(e.entryId, 0),
                imgArrow, med_font, this, e.entryId, null);
        item.setDate(e.date);
        return item;
    }

    private EntryItem getEntryItem(Entry entry) {
        int w = getWidth() - (padding * 2);
        EntryItem item = new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(entry.getId()),
                Utils.getEntryText(entry.getId(), entry.getSelectedIndex()),
                Utils.getEntryIcon(entry.getId(), entry.getSelectedIndex()),
                imgArrow, med_font, this, entry.getId(), new int[]{entry.getSelectedIndex()});

        item.setDate(entry.getDate());
        return item;
    }

    private SettingsDelimiter getDateItem(Date date) {
        int w = getWidth() - (padding * 2);
        //#if QVGA || WQVGA || QVGA_ADS
        return new SettingsDelimiter(getDate(date), w, h - 5, lineColour, large_font);
        //#elif WVGA
//#         return new SettingsDelimiter(getDate(date), w, h - 5, lineColour, large_font, fontColour);
        //#endif 
    }

    public int getCount() {
        return adapterList.size();
    }

    public Object getItem(int position) {
        return adapterList.elementAt(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemComponentType(int position) {
        return 0;
    }

    public Component getComponent(int position) {
        Object listItem = adapterList.elementAt(position);
        if (listItem instanceof Entry) {
            return getEntryItem((Entry) listItem);
        } else if (listItem instanceof Date) {
            return getDateItem((Date) listItem);
        } else if (listItem instanceof EntryStringItem) {
            return getStringEntryItem((EntryStringItem) listItem);
        } else if (listItem instanceof Vector) {
            Vector v = (Vector) listItem;
            return getSymptoms((Date) v.elementAt(0), (int[]) v.elementAt(1));
        } else if (listItem instanceof String) {
            return getTxtBox((String) listItem);
        } else if (listItem instanceof Integer) {
            //#if QVGA_ADS
            Hashtable metaData = new Hashtable();
            metaData.put(IaOptionalParams.Key_Age, "30");
            metaData.put(IaOptionalParams.Key_Gender, "F");
            metaData.put(IaOptionalParams.Key_Gps_Location, "53.542132,-2.239856");
            metaData.put(IaOptionalParams.Key_Keywords, "Games");
            metaData.put(IaOptionalParams.Key_Location, "US");
            AdvertComponent ad = new AdvertComponent(iWidth, 40, InBloomController.myMidlet, metaData);
            ad.downloadAd();
            return ad;
            //#endif 
        }

        return null;

    }

    public boolean isEmpty() {
        return adapterList.isEmpty();
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        super.onComponentEvent(c, e, o, p);
        if (e == Utils.ENTRY_WEIGHT || e == Utils.ENTRY_TEMP) {
            tempDate = ((EntryItem) c).getDate();
            tempEntryItem = (EntryItem) c;
            ((InBloomController) controller).showInputDialog(
                    e == Utils.ENTRY_WEIGHT
                    ? Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT) + " " + Utils.getCurrentWeightUnit()
                    : Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP) + " " + Utils.getCurrentTempUnit(),
                    null, e);
        } else {
            if (c instanceof EntryItem) {
                Date date = ((EntryItem) c).getDate();
                ((InBloomController) controller).setPrevious_screen_id(InBloomController.SCREEN_ENTRY_LIST);
                ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_ENTRY_SELECTION, true, new Object[]{o, date});

            }
        }
    }

    public void updateEntry(int entryId, String value) {
        Vector v = new Vector();
        v.addElement(value);
        if (entryId == Utils.ENTRY_TEMP) {
            Utils.replaceEntries(tempDate, entryId, v);
        } else if (entryId == Utils.ENTRY_WEIGHT) {
            Utils.replaceEntries(tempDate, entryId, v);
        }

        tempEntryItem.setValue(value);
        tempEntryItem = null;
        tempDate = null;
    }

    class EntryStringItem {

        public int entryId;
        public String title;
        public Date date;

        public EntryStringItem(int entryId, String title, Date date) {
            this.entryId = entryId;
            this.title = title;
            this.date = date;
        }
    }
}
