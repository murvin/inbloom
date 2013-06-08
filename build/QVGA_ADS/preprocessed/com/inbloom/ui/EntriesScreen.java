package com.inbloom.ui;

import com.inbloom.InBloomController;
import com.inbloom.model.Cycle;
import com.inbloom.model.Date;
import com.inbloom.model.Entries;
import com.inbloom.model.Settings;
import com.inbloom.model.User;
import com.inbloom.ui.components.EntryItem;
import com.inbloom.ui.components.EntrySelectionItem;
import com.inbloom.ui.components.SettingsDelimiter;
import com.inbloom.utils.Database;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.animations.UikitTextInput;
import com.uikit.painters.PatchPainter;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.layout.BoxLayout;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.TextStyle;
import com.uikit.textinputhandler.NativeTextInputHandler;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class EntriesScreen extends InBloomScreen {

    //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
    private BitmapFont 
    //#elif WVGA
//#     private SystemFont
    //#endif 
    large_font, med_font;
    private int lineColour;
    private int txtInputNotesHeight;
    private int vgap;
    private PatchPainter onFocusHighLight;
    private Image imgArrow;
    private Date date;
    private Entries entries;
    private UikitTextInput btiNotes;
    private Image imgOnTextInputFocus, imgTextInputEnabled;
    private Image imgCheckBoxOn, imgCheckBoxOff;
    private ComponentStyle[] styles;
    private Object[] entryDetails;
    private boolean isPeriodToday;
    private Settings settings;
    private EntrySelectionItem periodStartsItem;
    private EntryItem entryTemp, entryWeight;
    //#if WVGA 
//#     private int fontColour;
    //#endif

    public EntriesScreen(Object o) {
        Object[] objs = (Object[]) o;
        this.entryDetails = objs;
        this.entries = (Entries) objs[0];
        this.date = (Date) objs[1];
        isPeriodToday = ((Boolean) objs[2]).booleanValue();
        initResources();
        initComponents();
    }

    private void initResources() {
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        large_font = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);

        Image imgFontMed = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
        med_font = new BitmapFont(imgFontMed, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         large_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
//#         med_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
//# 
        //#endif 

        lineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));
        padding = 4 * UiKitDisplay.getWidth() / 100;

        vgap = 2 * UiKitDisplay.getHeight() / 100;
        txtInputNotesHeight = 40 * UiKitDisplay.getHeight() / 100;

        imgArrow = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW);

        int patchBord = 5;
        Image imgHighlight = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH);
        this.onFocusHighLight = new PatchPainter(imgHighlight, patchBord, patchBord, patchBord, patchBord);


        imgOnTextInputFocus = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH_ROUNDCORNER);
        imgTextInputEnabled = Resources.getInstance().getThemeImage(GraphicsResources.IMG_TEXTINPUT_PATCH);

        styles = new ComponentStyle[2];
        ComponentStyle style_enabled = new ComponentStyle();
        ComponentStyle style_onfocus = new ComponentStyle();

        int pBorder = 10;

        PatchPainter patchRendererEnabled = new PatchPainter(imgTextInputEnabled, pBorder, pBorder, pBorder, pBorder);
        style_enabled.addRenderer(patchRendererEnabled);


        PatchPainter patchRenderer = new PatchPainter(imgOnTextInputFocus, pBorder, pBorder, pBorder, pBorder);
        style_onfocus.addRenderer(patchRendererEnabled);
        style_onfocus.addRenderer(patchRenderer);

        styles[0] = style_enabled;
        styles[1] = style_onfocus;


        imgCheckBoxOn = Resources.getInstance().getThemeImage(GraphicsResources.IMG_CHECKBOX_ON);
        imgCheckBoxOff = Resources.getInstance().getThemeImage(GraphicsResources.IMG_CHECKBOX_OFF);

    }

    private int getEntryIndex(int entryId) {
        if (entries == null) {
            return 0;
        } else {
            switch (entryId) {
                case Utils.ENTRY_FLOW: {
                    return entries.getFlowEntry() == null ? 0 : entries.getFlowEntry().getSelectedIndex();
                }
                case Utils.ENTRY_INTIMACY: {
                    return entries.getIntimacyEntry() == null ? 0 : entries.getIntimacyEntry().getSelectedIndex();
                }
                case Utils.ENTRY_MOOD: {
                    return entries.getMoodEntry() == null ? 0 : entries.getMoodEntry().getSelectedIndex();
                }
                case Utils.ENTRY_SPOTTING: {
                    return entries.getSpottingEntry() == null ? 0 : entries.getSpottingEntry().getSelectedIndex();
                }
            }
        }
        return 0;
    }

    private void initComponents() {
        int botPadding = 
        //#if QVGA || QVGA_ADS 
        padding;
        //#elif WVGA || WQVGA || WQVGA_ADS
//#         padding + Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight();
        //#endif 
        
        getStyle(true).setPadding(padding * 2, padding, botPadding, padding);
        
        setLayout(new BoxLayout(UikitConstant.VERTICAL, vgap / 2));
        int w = getWidth() - (padding * 2);
        Image imgSmiley = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE0);
        int h = imgSmiley.getHeight() + (vgap * 2);
        EntryItem item = null;

        periodStartsItem = new EntrySelectionItem(w, h, onFocusHighLight, Resources.getInstance().getText(GlobalResources.TXT_ENTRY_ISPERIOD_TODAY), null, imgCheckBoxOn, imgCheckBoxOff, large_font, this, -1);
        periodStartsItem.setIsUnCheckable(true);
        periodStartsItem.setIsChecked(isPeriodToday);
        addComponent(periodStartsItem);

        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_ENTRY_EMOTIONAL), w, large_font.getHeight(), lineColour, large_font));
        //#elif WVGA
//#         addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_ENTRY_EMOTIONAL), w, large_font.getHeight(), lineColour, large_font, fontColour));
        //#endif 
        int index = getEntryIndex(Utils.ENTRY_MOOD);
        addComponent(new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_MOOD),
                Utils.getEntryText(Utils.ENTRY_MOOD, index),
                Utils.getEntryIcon(Utils.ENTRY_MOOD, index),
                imgArrow, med_font, this, Utils.ENTRY_MOOD, new int[]{index}));
        index = getEntryIndex(Utils.ENTRY_INTIMACY);
        addComponent(new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_INTIMACY),
                Utils.getEntryText(Utils.ENTRY_INTIMACY, index),
                Utils.getEntryIcon(Utils.ENTRY_INTIMACY, index),
                imgArrow, med_font, this, Utils.ENTRY_INTIMACY, new int[]{index}));

        //#if QVGA || WQVGA || WQVGA_ADS
//#         addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_ENTRY_PHYSIOLOGICAL), w, large_font.getHeight(), lineColour, large_font));
        //#elif WVGA
//#         addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_ENTRY_PHYSIOLOGICAL), w, large_font.getHeight(), lineColour, large_font, fontColour));
        //#endif 
        
        String weight = null;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entries == null || entries.getWeight() == null) {
            if (InBloomController.tempWeight == null) {
                weight = Utils.getEntryText(Utils.ENTRY_WEIGHT, 0);
            } else {
                weight = InBloomController.tempWeight;
            }
        } else {

            if (entries.getWeightUnit() == settings.getWeight_unit()) {
                weight = entries.getWeight();
            } else {
                if (entries.getWeightUnit() == Settings.WEIGHT_POUNDS) {
                    weight = String.valueOf(Utils.lbToKg(Float.parseFloat(entries.getWeight())));
                } else {
                    weight = String.valueOf(Utils.kgToLb(Float.parseFloat(entries.getWeight())));
                }
            }
        }

        addComponent(entryWeight = new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_WEIGHT),
                weight,
                Utils.getEntryIcon(Utils.ENTRY_WEIGHT, 0), imgArrow, med_font, this, Utils.ENTRY_WEIGHT, null));

        String temp = null;
        if (entries == null || entries.getTemp() == null) {
            if (InBloomController.tempTemp == null) {
                temp = Utils.getEntryText(Utils.ENTRY_TEMP, 0);
            } else {
                temp = InBloomController.tempTemp;
            }
        } else {

            if (entries.getTempUnit() == settings.getBasal_temp_unit()) {
                temp = entries.getTemp();
            } else {
                if (entries.getTempUnit() == Settings.TEMP_FAH) {
                    temp = String.valueOf(Utils.fahToCel(Float.parseFloat(entries.getTemp())));
                } else {
                    temp = String.valueOf(Utils.celToFah(Float.parseFloat(entries.getTemp())));
                }
            }
        }

        addComponent(entryTemp = new EntryItem(w, h, onFocusHighLight,
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP),
                temp,
                Utils.getEntryIcon(Utils.ENTRY_TEMP, 0), imgArrow, med_font, this, Utils.ENTRY_TEMP, null));


        index = getEntryIndex(Utils.ENTRY_FLOW);
        addComponent(new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_FLOW),
                Utils.getEntryText(Utils.ENTRY_FLOW, index),
                Utils.getEntryIcon(Utils.ENTRY_FLOW, index), imgArrow, med_font, this, Utils.ENTRY_FLOW,
                new int[]{index}));
        index = getEntryIndex(Utils.ENTRY_SPOTTING);
        addComponent(new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_SPOTTING),
                Utils.getEntryText(Utils.ENTRY_SPOTTING, index),
                Utils.getEntryIcon(Utils.ENTRY_SPOTTING, index), imgArrow, med_font, this, Utils.ENTRY_SPOTTING,
                new int[]{index}));

        addComponent(item = new EntryItem(w, h, onFocusHighLight,
                Utils.getEntryTitleText(Utils.ENTRY_SYMPTOMS), null,
                null, imgArrow, med_font, this, Utils.ENTRY_SYMPTOMS,
                entries == null ? null : entries.getSymptomsIndices()));

        item.addBasicTextBox(Utils.getEntryText(Utils.ENTRY_SYMPTOMS, 0));

        String notesTitle = Utils.getEntryTitleText(Utils.ENTRY_NOTES);
        //#if QVGA || WQVGA || WQVGA_ADS
//#         addComponent(new SettingsDelimiter(notesTitle, w, large_font.getHeight(), lineColour, large_font));
        //#elif WVGA
//#         addComponent(new SettingsDelimiter(notesTitle, w, large_font.getHeight(), lineColour, large_font, fontColour));
        //#endif 
        addComponent(new Component(w, padding / 2));


        btiNotes = new UikitTextInput(w, txtInputNotesHeight, new NativeTextInputHandler(notesTitle, 1024, TextField.ANY), true, false);
        btiNotes.setStyle(UikitTextInput.COMP_SELF, UikitTextInput.STATE_ENABLED, styles[0]);
        btiNotes.setStyle(UikitTextInput.COMP_SELF, UikitTextInput.STATE_FOCUSED, styles[1]);
        btiNotes.setStyle(UikitTextInput.COMP_TEXTBOXTEXT, UikitTextInput.STATE_ENABLED, new TextStyle(med_font));
        btiNotes.setStyle(UikitTextInput.COMP_TEXTBOXTEXT, UikitTextInput.STATE_FOCUSED, new TextStyle(med_font));

        if (entries != null) {
            if (entries.getNote() != null) {
                btiNotes.setText(entries.getNote());
            }
        }

        addComponent(btiNotes);
        setBottomOffset(Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight());
        if (InBloomController.tempTemp != null) {
            InBloomController.tempTemp = null;
        }

        if (InBloomController.tempWeight != null) {
            InBloomController.tempWeight = null;
        }
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        if (c == periodStartsItem) {
            this.entryDetails[2] = new Boolean(e == EntrySelectionItem.EVENT_CHECKED);
        } else {
            if (e == Utils.ENTRY_WEIGHT || e == Utils.ENTRY_TEMP) {
                ((InBloomController) controller).showInputDialog(
                        e == Utils.ENTRY_WEIGHT
                        ? Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT) + " " + Utils.getCurrentWeightUnit()
                        : Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP) + " " + Utils.getCurrentTempUnit(),
                        null, e);
            } else {
                if (entryTemp.getValue() != null
                        && !(entryTemp.getValue().equals(Utils.getEntryText(Utils.ENTRY_TEMP, 0)))) {
                    InBloomController.tempTemp = entryTemp.getValue();
                }

                if (entryWeight.getValue() != null
                        && !(entryWeight.getValue().equals(Utils.getEntryText(Utils.ENTRY_WEIGHT, 0)))) {
                    InBloomController.tempWeight = entryWeight.getValue();
                }
                ((InBloomController) controller).setPrevious_screen_id(InBloomController.SCREEN_ENTRY);
                ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_ENTRY_SELECTION, true, o);
            }
        }
    }

    public String getDate() {
        StringBuffer d = new StringBuffer();
        d.append(date.getDay()).append(" ").append(Utils.getMonthsText()[date.getMonth()]).append(" ").append(date.getYear());
        return d.toString();
    }

    public void updateEntry(int entryId, String value) {
        if (entryId == Utils.ENTRY_TEMP) {
            entryTemp.setValue(value);
        } else if (entryId == Utils.ENTRY_WEIGHT) {
            entryWeight.setValue(value);
        }
    }

    public Object[] entryDetails() {
        return this.entryDetails;
    }

    public boolean hasError() {
        boolean hasError = false;
        if (entryTemp.getValue() != null
                && !(entryTemp.getValue().equals(Utils.getEntryText(Utils.ENTRY_TEMP, 0)))
                && !(entryTemp.getValue().trim().equals(""))) {
            try {
                Float.parseFloat(entryTemp.getValue());
            } catch (Exception e) {
                hasError = true;
                ((InBloomController) controller).showAlertDialog(
                        Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP),
                        Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP) + " " + Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_ISNOTVALID));
            }
        }

        if (!hasError) {
            if (entryWeight.getValue() != null
                    && !(entryWeight.getValue().equals(Utils.getEntryText(Utils.ENTRY_WEIGHT, 0)))
                    && !(entryWeight.getValue().trim().equals(""))) {
                try {
                    Float.parseFloat(entryWeight.getValue());
                } catch (Exception e) {
                    hasError = true;
                    ((InBloomController) controller).showAlertDialog(
                            Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT),
                            Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT) + " " + Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_ISNOTVALID));
                }
            }

        }

        return hasError;
    }

    public void saveEntries() {
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (periodStartsItem.isChecked()) {
            settings.setCycle_start_date(date);
            try {
                Database.getInstance().saveISerializable(settings, Database.SETTINGS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (entries == null) {
            entries = new Entries();
        }

        if (btiNotes.getText() != null && !(btiNotes.getText().trim().equals(""))) {
            entries.setNote(btiNotes.getText());
        }
        entries.setDate(date);
        if (entryTemp.getValue() != null
                && !(entryTemp.getValue().equals(Utils.getEntryText(Utils.ENTRY_TEMP, 0)))
                && !(entryTemp.getValue().trim().equals(""))) {
            entries.setTemp(entryTemp.getValue());
            entries.setTempUnit(settings.getBasal_temp_unit());
        }

        if (entryWeight.getValue() != null
                && !(entryWeight.getValue().equals(Utils.getEntryText(Utils.ENTRY_WEIGHT, 0)))
                && !(entryWeight.getValue().trim().equals(""))) {
            entries.setWeight(entryWeight.getValue());
            entries.setWeightUnit(settings.getWeight_unit());
        }


        User user = null;
        try {
            user = (User) Database.getInstance().retrieveISerializable(Database.USER);
            Cycle cycle = user.getCycle();
            if (cycle == null) {
                cycle = new Cycle();
            }

            Utils.removeEntriesForDate(date, cycle);
            cycle.addEntries(entries);
            user.setCycle(cycle);
            try {
                Database.getInstance().saveISerializable(user, Database.USER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
