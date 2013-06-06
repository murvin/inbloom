package com.inbloom.ui;

import com.inbloom.InBloomController;
import com.inbloom.model.Date;
import com.inbloom.model.Entries;
import com.inbloom.ui.components.EntrySelectionItem;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.painters.PatchPainter;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.layout.BoxLayout;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class EntrySelectionScreen extends InBloomScreen {

    //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#     private BitmapFont 
    //#elif WVGA
//#     private SystemFont
    //#endif 
    med_font;
    protected int vgap;
    private PatchPainter onFocusHighLight;
    protected String title;
    private Image imgCheckBoxOn, imageCheckBoxOff;
    protected String[] titles;
    protected Image[] icons;
    private boolean isMultipleSelection;
    protected int entryId;
    protected int[] entryIndices;
    private Object[] contentDetails;
    private Date date;

    public EntrySelectionScreen() {
    }

    public EntrySelectionScreen(Object cDetails) {
        this.contentDetails = (Object[]) cDetails;
        this.title = (String) contentDetails[0];
        this.entryId = ((Integer) contentDetails[1]).intValue();
        this.entryIndices = (int[]) contentDetails[2];

        initResources();
        initComponents();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDate() {
        StringBuffer d = new StringBuffer();
        d.append(date.getDay()).append(" ").append(Utils.getMonthsText()[date.getMonth()]).append(" ").append(date.getYear());
        return d.toString();
    }

    public void replaceEntryItem() {
        Vector selectedIndices = new Vector();
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = componentAt(i);
            if (c instanceof EntrySelectionItem) {
                if (((EntrySelectionItem) c).isChecked()) {
                    selectedIndices.addElement(new Integer(i));
                }
            }
        }
        
        Utils.replaceEntries(date, entryId, selectedIndices);

    }

    protected final void initResources() {
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#         Image imgFontMed = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
//#         med_font = new BitmapFont(imgFontMed, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);
        //#elif WVGA
//#         med_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        //#endif 
        
        padding = 4 * UiKitDisplay.getWidth() / 100;
        vgap = 2 * UiKitDisplay.getHeight() / 100;

        int patchBord = 5;
        Image imgHighlight = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH);
        this.onFocusHighLight = new PatchPainter(imgHighlight, patchBord, patchBord, patchBord, patchBord);

        imgCheckBoxOn = Resources.getInstance().getThemeImage(GraphicsResources.IMG_CHECKBOX_ON);
        imageCheckBoxOff = Resources.getInstance().getThemeImage(GraphicsResources.IMG_CHECKBOX_OFF);

    }

    protected void initComponents() {
        setLayout(new BoxLayout(UikitConstant.VERTICAL, vgap));
        int w = getWidth() - (padding * 2);
        Image imgSmiley = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE0);
        int h = imgSmiley.getHeight() + (vgap * 2);
        updateModel(entryId);
        addAllEntrySelectionItems(w, h, titles, icons);

        updateBottomOffset();
        getStyle(true).setPadding(padding, padding, bottomPadding - padding, padding);
    }

    protected void updateModel(int entryId) {
        switch (entryId) {
            case Utils.ENTRY_MOOD: {
                titles = new String[]{
                    Utils.getEntryText(entryId, 0),
                    Utils.getEntryText(entryId, 1),
                    Utils.getEntryText(entryId, 2),
                    Utils.getEntryText(entryId, 3),
                    Utils.getEntryText(entryId, 4),
                    Utils.getEntryText(entryId, 5)
                };

                icons = new Image[]{
                    Utils.getEntryIcon(entryId, 0),
                    Utils.getEntryIcon(entryId, 1),
                    Utils.getEntryIcon(entryId, 2),
                    Utils.getEntryIcon(entryId, 3),
                    Utils.getEntryIcon(entryId, 4),
                    Utils.getEntryIcon(entryId, 5)
                };
                break;
            }
            case Utils.ENTRY_SPOTTING: {
            }
            case Utils.ENTRY_INTIMACY: {
                titles = new String[]{
                    Utils.getEntryText(entryId, 0),
                    Utils.getEntryText(entryId, 1)
                };


                icons = new Image[]{
                    Utils.getEntryIcon(entryId, 0),
                    Utils.getEntryIcon(entryId, 1)
                };
                break;
            }
            case Utils.ENTRY_FLOW: {
                titles = new String[]{
                    Utils.getEntryText(entryId, 0),
                    Utils.getEntryText(entryId, 1),
                    Utils.getEntryText(entryId, 2),
                    Utils.getEntryText(entryId, 3)
                };


                icons = new Image[]{
                    Utils.getEntryIcon(entryId, 0),
                    Utils.getEntryIcon(entryId, 1),
                    Utils.getEntryIcon(entryId, 2),
                    Utils.getEntryIcon(entryId, 3)
                };
                break;
            }
            case Utils.ENTRY_SYMPTOMS: {
                titles = new String[]{
                    Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_HEADCHE),
                    Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_CRAVINGS),
                    Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_CRAMPS),
                    Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_BLOATING),
                    Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_BACKACHE),
                    Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_ACNE)
                };
                break;
            }
            case Utils.ENTRY_LOCALES: {
                titles = new String[]{
                    Resources.getInstance().getText(GlobalResources.LOCALE_ENGLISH),
                    Resources.getInstance().getText(GlobalResources.LOCALE_FRENCH),
                    Resources.getInstance().getText(GlobalResources.LOCALE_ITALIAN),
                    Resources.getInstance().getText(GlobalResources.LOCALE_GERMAN),
                    Resources.getInstance().getText(GlobalResources.LOCALE_SPANISH),};

                icons = new Image[]{
                    Utils.getEntryIcon(entryId, 0),
                    Utils.getEntryIcon(entryId, 1),
                    Utils.getEntryIcon(entryId, 2),
                    Utils.getEntryIcon(entryId, 3),
                    Utils.getEntryIcon(entryId, 4)
                };
                break;
            }
            case Utils.ENTRY_THEMES: {
                titles = new String[]{
                    Resources.getInstance().getText(GlobalResources.TXT_DEFAULT),
                    Resources.getInstance().getText(GlobalResources.TXT_AUTUMN),
                    Resources.getInstance().getText(GlobalResources.TXT_WINTER)
                };

                icons = new Image[]{
                    Resources.getInstance().getThemeImage(GraphicsResources.IMG_BG_DEFAULT),
                    Resources.getInstance().getThemeImage(GraphicsResources.IMG_BG_AUTUMN),
                    Resources.getInstance().getThemeImage(GraphicsResources.IMG_BG_WINTER)
                };
                break;
            }
            case Utils.ENTRY_TEMP: {
                titles = new String[]{
                    Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP_CELSIUS),
                    Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP_FAH)
                };
                break;
            }
            case Utils.ENTRY_WEIGHT: {
                titles = new String[]{
                    Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT_KG),
                    Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT_POUNDS)
                };
                break;
            }
        }
    }

    protected void addAllEntrySelectionItems(int w, int h, String[] titles, Image[] icons) {
        EntrySelectionItem item = null;
        Image image = null;
        for (int i = 0; i < titles.length; i++) {
            if (icons != null) {
                image = icons[i];
            }
            String t = titles[i];
            addComponent(item = new EntrySelectionItem(w, h, onFocusHighLight,
                    t,
                    image,
                    imgCheckBoxOn,
                    imageCheckBoxOff,
                    med_font, this, i));
            item.setEventListener(this);
            if (entryId == Utils.ENTRY_SYMPTOMS) {
                item.setIsUnCheckable(true);
                isMultipleSelection = true;
            }
            if (entryIndices != null) {
                for (int j = 0; j < entryIndices.length; j++) {
                    if (i == entryIndices[j]) {
                        item.setIsChecked(true);
                    }
                }
            }
        }
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        if (!isMultipleSelection) {
            resetAllComponentsToUnSelected(c);
        }
    }

    public void updateEntryIndices() {
        Vector selectedIndices = new Vector();
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = componentAt(i);
            if (c instanceof EntrySelectionItem) {
                if (((EntrySelectionItem) c).isChecked()) {
                    selectedIndices.addElement(new Integer(i));
                }
            }
        }

        Object[] entryDetails = ((InBloomController) controller).getEntryDetails();
        Entries entries = (Entries) entryDetails[0];
        if (entries == null) {
            entries = new Entries();
            entryDetails[0] = entries;
        }

        Utils.saveEntries(entries, entryId, selectedIndices);
    }

    private void resetAllComponentsToUnSelected(Component eventProducer) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = componentAt(i);
            if (c != eventProducer && c instanceof EntrySelectionItem) {
                ((EntrySelectionItem) c).setIsChecked(false);
            }
        }
    }

    public String getTitle() {
        return this.title;
    }
}
