package com.inbloom.utils;

import com.inbloom.model.Coordinates;
import com.inbloom.model.Cycle;
import com.inbloom.model.Date;
import com.inbloom.model.Entries;
import com.inbloom.model.Entry;
import com.inbloom.model.Settings;
import com.inbloom.model.User;
import com.inbloom.model.visitors.DateVisitor;
import com.inbloom.model.visitors.CoordinatesVisitor;

import com.uikit.animations.UikitTextInput;
import com.uikit.animations.UikitButton;
import com.uikit.animations.AlertDialog;
import com.uikit.painters.BgImagePainter;
import com.uikit.painters.BorderPainter;
import com.uikit.painters.PatchPainter;
import com.uikit.utils.ImageUtil;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Component;
//#if WVGA 
//# import com.uikit.coreElements.SystemFont;
//#elif QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
import com.uikit.coreElements.BitmapFont;
//#endif 
import com.uikit.coreElements.UikitFont;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.TextStyle;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class Utils {

    public static int LSK = -6;
    public static int RSK = -7;
    public static final String FONT_CHARS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~¡£©«®°´»¿ÀÁÂÃÄÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝßàáâãäçèéêëìíîïñòóôõöùúûüýÿ™€";
    public static final int ENTRY_MOOD = 0x001;
    public static final int ENTRY_INTIMACY = 0x002;
    public static final int ENTRY_WEIGHT = 0x003;
    public static final int ENTRY_TEMP = 0x004;
    public static final int ENTRY_FLOW = 0x005;
    public static final int ENTRY_SPOTTING = 0x006;
    public static final int ENTRY_SYMPTOMS = 0x007;
    public static final int ENTRY_NOTES = 0x008;
    public static final int ENTRY_LOCALES = 0X009;
    public static final int ENTRY_THEMES = 0x010;

    public static String getEntryTitleText(int entryId) {
        switch (entryId) {
            case ENTRY_FLOW: {
                return Resources.getInstance().getText(GlobalResources.TXT_ENTRY_FLOW);
            }
            case ENTRY_INTIMACY: {
                return Resources.getInstance().getText(GlobalResources.TXT_ENTRY_INTIMATE);
            }
            case ENTRY_MOOD: {
                return Resources.getInstance().getText(GlobalResources.TXT_ENTRY_MOOD);
            }
            case ENTRY_NOTES: {
                return Resources.getInstance().getText(GlobalResources.TXT_ENTRY_NOTES);
            }
            case ENTRY_SPOTTING: {
                return Resources.getInstance().getText(GlobalResources.TXT_ENTRY_SPOTTING);
            }
            case ENTRY_SYMPTOMS: {
                return Resources.getInstance().getText(GlobalResources.TXT_ENTRY_SYMPTOMS);
            }
            case ENTRY_TEMP: {
                return Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP);
            }
            case ENTRY_WEIGHT: {
                return Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT);
            }
        }
        return null;
    }

    public static String getEntryText(int entryId, int entryIndex) {
        StringBuffer text = new StringBuffer();
        switch (entryId) {
            case ENTRY_FLOW: {
                if (entryIndex == 0) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_FLOW_NON));
                } else if (entryIndex == 1) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_FLOW_LOW));
                } else if (entryIndex == 2) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_FLOW_MED));
                } else if (entryIndex == 3) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_FLOW_HIGH));
                }
                break;
            }
            case ENTRY_WEIGHT: {
            }
            case ENTRY_TEMP: {
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_FLOW_NON));
                break;
            }
            case ENTRY_SPOTTING: {
            }
            case ENTRY_INTIMACY: {
                if (entryIndex == 0) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_COMMON_NO));
                } else {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_COMMON_YES));
                }
                break;
            }
            case ENTRY_MOOD: {
                if (entryIndex == 0) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_FLOW_NON));
                } else if (entryIndex == 1) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_MOOD_1));
                } else if (entryIndex == 2) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_MOOD_2));
                } else if (entryIndex == 3) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_MOOD_3));
                } else if (entryIndex == 4) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_MOOD_4));
                } else if (entryIndex == 5) {
                    text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_MOOD_5));
                }
                break;
            }
            case ENTRY_SYMPTOMS: {
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_HEADCHE)).append(", ");
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_CRAVINGS)).append(", ");
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_CRAMPS)).append(", ");
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_BLOATING)).append(", ");
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_BACKACHE)).append(", ");
                text.append(Resources.getInstance().getText(GlobalResources.TXT_ENTEY_SYMPTOM_ACNE));
                break;
            }
            case ENTRY_LOCALES: {
                if (entryIndex == 0) {
                    text.append("en-GB");
                } else if (entryIndex == 1) {
                    text.append("fr-FR");
                } else if (entryIndex == 2) {
                    text.append("it-IT");
                } else if (entryIndex == 3) {
                    text.append("de-DE");
                } else if (entryIndex == 4) {
                    text.append("es-ES");
                }
                break;
            }
            case ENTRY_THEMES: {
                if (entryIndex == 0) {
                    text.append("themeDefault");
                } else if (entryIndex == 1) {
                    text.append("themeAutumn");
                } else if (entryIndex == 2) {
                    text.append("themeWinter");
                }
                break;
            }
        }
        return text.toString();
    }

    public static void saveEntries(Entries entries, int entryId, Vector entryIndices) {
        switch (entryId) {
            case ENTRY_FLOW: {
                if (entries.getFlowEntry() == null) {
                    entries.setFlowEntry(new Entry(entryId, -1));
                }
                entries.getFlowEntry().setSelectedIndex(((Integer) entryIndices.firstElement()).intValue());
                break;
            }
            case ENTRY_INTIMACY: {
                if (entries.getIntimacyEntry() == null) {
                    entries.setIntimacyEntry(new Entry(entryId, -1));
                }
                entries.getIntimacyEntry().setSelectedIndex(((Integer) entryIndices.firstElement()).intValue());
                break;
            }
            case ENTRY_MOOD: {
                if (entries.getMoodEntry() == null) {
                    entries.setMoodEntry(new Entry(entryId, -1));
                }
                entries.getMoodEntry().setSelectedIndex(((Integer) entryIndices.firstElement()).intValue());
                break;
            }
            case ENTRY_SPOTTING: {
                if (entries.getSpottingEntry() == null) {
                    entries.setSpottingEntry(new Entry(entryId, -1));
                }
                entries.getSpottingEntry().setSelectedIndex(((Integer) entryIndices.firstElement()).intValue());
                break;
            }
            case ENTRY_SYMPTOMS: {
                if (entryIndices != null && !entryIndices.isEmpty()) {
                    int[] symptomIndices = new int[entryIndices.size()];
                    for (int i = 0; i < entryIndices.size(); i++) {
                        symptomIndices[i] = ((Integer) entryIndices.elementAt(i)).intValue();

                    }
                    entries.setSymptomsIndices(symptomIndices);
                } else {
                    entries.setSymptomsIndices(null);
                }
                break;
            }
            case ENTRY_TEMP:{
                entries.setTemp((String)entryIndices.elementAt(0));
                break;
            }
            case ENTRY_WEIGHT:{
                entries.setWeight((String)entryIndices.elementAt(0));
                break;
            }
            case ENTRY_NOTES:{
                entries.setNote((String)entryIndices.elementAt(0));
                break;
            }
        }
    }

    public static Image getEntryIcon(int entryId, int entryIndex) {
        Image img = null;
        switch (entryId) {
            case ENTRY_FLOW: {
                if (entryIndex == 0) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_FLOW0);
                } else if (entryIndex == 1) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_FLOW1);
                } else if (entryIndex == 2) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_FLOW2);
                } else if (entryIndex == 3) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_FLOW3);
                }
                break;
            }
            case ENTRY_INTIMACY: {
                if (entryIndex == 0) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_INT0);
                } else {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_INT1);
                }
                break;
            }
            case ENTRY_MOOD: {
                if (entryIndex == 0) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE0);
                } else if (entryIndex == 1) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE1);
                } else if (entryIndex == 2) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE2);
                } else if (entryIndex == 3) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE3);
                } else if (entryIndex == 4) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE4);
                } else if (entryIndex == 5) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SMILE5);
                }

                break;
            }
            case ENTRY_SPOTTING: {
                if (entryIndex == 0) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SPOT0);
                } else {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_SPOT1);
                }
                break;
            }
            case ENTRY_TEMP: {
                img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_TEMP);
                break;
            }
            case ENTRY_WEIGHT: {
                img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_WEIGHT);
                break;
            }
            case ENTRY_LOCALES: {
                if (entryIndex == 0) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_EN);
                } else if (entryIndex == 1) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_FR);
                } else if (entryIndex == 2) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_IT);
                } else if (entryIndex == 3) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_DE);
                } else if (entryIndex == 4) {
                    img = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_ES);
                }
                break;
            }
        }
        return img;
    }

    public static void drawBorder(Component c, int colour) {
        BorderPainter border = new BorderPainter();
        border.setBorderColor(colour);
        border.setBorderSize(1);
        c.getStyle(true).addRenderer(border);
    }

    public static String[] getMonthsText() {
        return new String[]{
                    Resources.getInstance().getText(GlobalResources.TXT_JAN), Resources.getInstance().getText(GlobalResources.TXT_FEB),
                    Resources.getInstance().getText(GlobalResources.TXT_MAR), Resources.getInstance().getText(GlobalResources.TXT_APR),
                    Resources.getInstance().getText(GlobalResources.TXT_MAY), Resources.getInstance().getText(GlobalResources.TXT_JUN),
                    Resources.getInstance().getText(GlobalResources.TXT_JULY), Resources.getInstance().getText(GlobalResources.TXT_AUG),
                    Resources.getInstance().getText(GlobalResources.TXT_SEP), Resources.getInstance().getText(GlobalResources.TXT_OCT),
                    Resources.getInstance().getText(GlobalResources.TXT_NOV), Resources.getInstance().getText(GlobalResources.TXT_DEC)
                };

    }

    public static String[] getDaysText() {
        return new String[]{
                    Resources.getInstance().getText(GlobalResources.TXT_MON), Resources.getInstance().getText(GlobalResources.TXT_TUE),
                    Resources.getInstance().getText(GlobalResources.TXT_WED), Resources.getInstance().getText(GlobalResources.TXT_THURS),
                    Resources.getInstance().getText(GlobalResources.TXT_FRI),
                    Resources.getInstance().getText(GlobalResources.TXT_SAT),
                    Resources.getInstance().getText(GlobalResources.TXT_SUN)
                };
    }

    public static int getMonthLength(int year, int month) {
        switch (month) {
            case 0:
                return 31;
            case 1:
                if (year % 400 == 0) {
                    return 29;
                } else if (year % 100 == 0) {
                    return 28;
                } else if (year % 4 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            case 2:
                return 31;
            case 3:
                return 30;
            case 4:
                return 31;
            case 5:
                return 30;
            case 6:
                return 31;
            case 7:
                return 31;
            case 8:
                return 30;
            case 9:
                return 31;
            case 10:
                return 30;
            case 11:
                return 31;
            default:
                return 31;
        }
    }

    public static int getZellerDay(int year, int month, int day) {
        month += 1;
        if (month < 3) {
            month += 12;
            year -= 1;
        }


        int k = year % 100;
        int j = year / 100;
        int d = ((day + (((month + 1) * 26) / 10) + k + (k / 4) + (j / 4))
                + (5 * j)) % 7;
        return d;
    }

    public static int getStartingDay(Date firstStartDate, Date targetStartDate, int cycleLength) {
        int startDay = firstStartDate.getDay();
        int startMonth = firstStartDate.getMonth();
        int startYear = firstStartDate.getYear();

        int targetMonth = targetStartDate.getMonth();
        int targetYear = targetStartDate.getYear();

        boolean isGreater = targetStartDate.isGreater(firstStartDate);

        int monthGap = 0;
        int yearGap = Math.abs(targetYear - startYear);
        int currMonth = startMonth;

        for (int i = 0; i <= yearGap; i++) {
            while ((isGreater ? currMonth : Math.abs(currMonth)) != targetMonth) {
                if (isGreater) {
                    currMonth++;
                } else {
                    currMonth--;
                }

                currMonth %= 12;
                monthGap++;
            }
        }

        if (isGreater) {
            currMonth = startMonth;
        } else {
            currMonth = startMonth - 1;
        }

        for (int i = 0; i < monthGap; i++) {
            currMonth %= 12;
            if (isGreater) {
                if (currMonth == 0) {
                    startYear++;
                }
            } else {
                if (Math.abs(currMonth) == 11) {
                    startYear--;
                }
            }

            if (isGreater) {
                startDay += cycleLength;
                startDay -= getMonthLength(startYear, currMonth);
                currMonth++;
            } else {
                startDay -= cycleLength;
                startDay += getMonthLength(startYear, Math.abs(currMonth));
                currMonth--;
            }
        }
        return (startDay);
    }

    public static void removeEntriesForDate(Date date, Cycle cycle) {
        Entries entries = null;
        try {
            DateVisitor visitor = new DateVisitor(date);
            cycle.accept(visitor);
            entries = visitor.getEntries();
            if (entries != null) {
                cycle.getEntries().removeElement(entries);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Entries getEntriesForDate(Date date) {
        Entries entries = null;
        User user = null;
        try {
            user = (User) Database.getInstance().retrieveISerializable(Database.USER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            if (user.getCycle() != null) {
                try {
                    DateVisitor visitor = new DateVisitor(date);
                    user.getCycle().accept(visitor);
                    return visitor.getEntries();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return entries;
    }

    public static Coordinates[] getCoordinates(int entryId, int month, int year) {
        Coordinates[] coors = null;
        User user = null;
        Settings settings = null;
        try {
            user = (User) Database.getInstance().retrieveISerializable(Database.USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (user != null) {
            if (user.getCycle() != null) {
                try {
                    CoordinatesVisitor visitor = new CoordinatesVisitor(month, year, entryId, entryId == Utils.ENTRY_TEMP ? settings.getBasal_temp_unit() : settings.getWeight_unit());
                    user.getCycle().accept(visitor);
                    return visitor.getCoordinates();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return coors;
    }

    public static UikitButton getButton(String label, int width) {
        ComponentStyle[] buttonStyles;
        ComponentStyle style_onfocus = new ComponentStyle();

        Image imgOnTextInputFocus = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH_ROUNDCORNER);
        int pBorder = 10;

        PatchPainter patchRenderer = new PatchPainter(imgOnTextInputFocus, pBorder, pBorder, pBorder, pBorder);
        style_onfocus.addRenderer(patchRenderer);

        Image imgButtonPatch = Resources.getInstance().getThemeImage(GraphicsResources.IMG_BUTTON_PATCH);
        ComponentStyle style = new ComponentStyle();
        pBorder = 5;
        PatchPainter rend = null;
        style.addRenderer(rend = new PatchPainter(imgButtonPatch, pBorder, pBorder, pBorder, pBorder));
        ComponentStyle styleOnFocus = new ComponentStyle();
        styleOnFocus.addRenderer(rend);
        styleOnFocus.addRenderer(patchRenderer);
        buttonStyles = new ComponentStyle[]{style, styleOnFocus};
        UikitButton button = new UikitButton(0, 0, width, imgButtonPatch.getHeight(), label, 0);
        button.setStyle(UikitButton.COMP_SELF, UikitButton.STATE_ENABLED, buttonStyles[0]);
        button.setStyle(UikitButton.COMP_SELF, UikitButton.STATE_PRESSED, buttonStyles[0]);
        button.setStyle(UikitButton.COMP_SELF, UikitButton.STATE_FOCUSED, buttonStyles[1]);
        Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        BitmapFont largeFont = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         SystemFont largeFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        //#endif 
        
        
        button.setIsAutoResize(false);

        TextStyle st = new TextStyle(largeFont);
        st.setFontColour(Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MENU_TEXT_COLOR)));
        st.setAlign(UikitConstant.HCENTER);
        button.setStyle(UikitButton.COMP_TEXT, UikitButton.STATE_ENABLED, st);
        button.setStyle(UikitButton.COMP_TEXT, UikitButton.STATE_PRESSED, st);
        button.setStyle(UikitButton.COMP_TEXT, UikitButton.STATE_FOCUSED, st);
        return button;
    }

    public static ComponentStyle getDialogComponentStyle() {
        ComponentStyle containerStyle = new ComponentStyle();
        containerStyle.setPadding(20);
        int colour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_LIGHTBOX_COLOR));
        Image bg = ImageUtil.generateTransparentImage(60, 60, (byte) 60, colour);
        containerStyle.addRenderer(new BgImagePainter(bg, UikitConstant.REPEAT));
        return containerStyle;
    }

    public static void applyTextStyles(AlertDialog dialog) {
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        BitmapFont largeFont = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         SystemFont largeFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        //#endif 
        
        TextStyle txtStyle = new TextStyle(largeFont);
        txtStyle.setAlign(UikitConstant.HCENTER);
        //#if WVGA 
//#         txtStyle.setFontColour(Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR)));
        //#endif 

        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        Image imgFontDesc = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
        BitmapFont descFont = new BitmapFont(imgFontDesc, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);
        //#elif WVGA
//#         SystemFont descFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        //#endif 
        
        TextStyle txtStyleDesc = new TextStyle(descFont);
        txtStyleDesc.setAlign(UikitConstant.HCENTER);
        //#if WVGA
//#         txtStyle.setFontColour(Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR)));
        //#endif 

        dialog.setTitleTextStyle(txtStyle);
        dialog.setStyle(AlertDialog.COMP_TEXT, txtStyleDesc);
    }

    public static void applyTextFieldStyles(UikitTextInput bti, UikitFont largeFont) {

        Image imgOnTextInputFocus = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH_ROUNDCORNER);
        Image imgTextInputEnabled = Resources.getInstance().getThemeImage(GraphicsResources.IMG_TEXTINPUT_PATCH);


        ComponentStyle[] styles = new ComponentStyle[2];
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

        bti.setStyle(UikitTextInput.COMP_SELF, UikitTextInput.STATE_ENABLED, styles[0]);
        bti.setStyle(UikitTextInput.COMP_SELF, UikitTextInput.STATE_FOCUSED, styles[1]);
        bti.setStyle(UikitTextInput.COMP_TEXTBOXTEXT, UikitTextInput.STATE_ENABLED, new TextStyle(largeFont));
        
    }

    public static int getSelectedIndex(String locale) {
        int index = 0;
        if (locale.equals("en-GB")) {
            index = 0;
        } else if (locale.equals("fr-FR")) {
            index = 1;
        } else if (locale.equals("it-IT")) {
            index = 2;
        } else if (locale.equals("de-DE")) {
            index = 3;
        } else if (locale.equals("es-ES")) {
            index = 4;
        }
        return index;
    }

    public static String getLocaleLanguageName(String locale) {
        String name = null;
        if (locale.equals("en-GB")) {
            name = Resources.getInstance().getText(GlobalResources.LOCALE_ENGLISH);
        } else if (locale.equals("fr-FR")) {
            name = Resources.getInstance().getText(GlobalResources.LOCALE_FRENCH);
        } else if (locale.equals("it-IT")) {
            name = Resources.getInstance().getText(GlobalResources.LOCALE_ITALIAN);
        } else if (locale.equals("de-DE")) {
            name = Resources.getInstance().getText(GlobalResources.LOCALE_GERMAN);
        } else if (locale.equals("es-ES")) {
            name = Resources.getInstance().getText(GlobalResources.LOCALE_SPANISH);
        }
        return name;
    }

    public static String getThemeName(int index) {
        switch (index) {
            case 0: {
                return Resources.getInstance().getText(GlobalResources.TXT_DEFAULT);
            }
            case 1: {
                return Resources.getInstance().getText(GlobalResources.TXT_AUTUMN);
            }
            case 2: {
                return Resources.getInstance().getText(GlobalResources.TXT_WINTER);
            }
        }
        return null;
    }

    public static Image replaceColor(Image img, int iReplacementArgb) {
        if (img == null) {
            throw new IllegalArgumentException();
        }

        int iWidth = img.getWidth();
        int iHeight = img.getHeight();

        int[] argb = new int[iWidth * iHeight];

        img.getRGB(argb, 0, iWidth, 0, 0, iWidth, iHeight);

        for (int i = 0; i < argb.length; i++) {
            argb[i] = (0x00ffffff & iReplacementArgb) | (argb[i] & 0xff000000);
        }
        Image img2 = Image.createRGBImage(argb, iWidth, iHeight, true);
        argb = null;
        return img2;
    }

    public static String getCurrentTempUnit() {
        Settings settings = null;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
            if (settings.getBasal_temp_unit() == Settings.TEMP_CEL) {
                return "°C";
            } else {
                return "°F";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getCurrentWeightUnit() {
        Settings settings = null;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
            if (settings.getWeight_unit() == Settings.WEIGHT_KG) {
                return "Kg";
            } else {
                return "lb";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static float celToFah(float cel) {
        return ((cel * 9) / 5) + 32;
    }

    public static float fahToCel(float fah) {
        return ((fah - 32) * 5) / 9;
    }

    public static float kgToLb(float kg) {
        return (float) (kg * 2.2);
    }

    public static float lbToKg(float lb) {
        return (float) (lb / 2.2);
    }

    public static void replaceEntries(Date date, int entryId, Vector selectedIndices) {
        Entries entries = Utils.getEntriesForDate(date);
        Utils.saveEntries(entries, entryId, selectedIndices);
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
