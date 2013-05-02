package com.inbloom.ui;

import com.inbloom.InBloomController;
import com.inbloom.model.Settings;
import com.inbloom.ui.components.EntryItem;
import com.inbloom.ui.components.SettingsCycleEntry;
import com.inbloom.ui.components.SettingsDelimiter;
import com.inbloom.utils.Database;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.painters.PatchPainter;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.Panel;
//#if QVGA || WQVGA || QVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.layout.BoxLayout;
import com.uikit.styles.ComponentStyle;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class SettingsScreen extends InBloomScreen {

    private Settings settings;
    //#if QVGA || WQVGA || QVGA_ADS
//#     private BitmapFont
    //#elif WVGA
//#     private SystemFont
    //#endif 
            large_font, small_font, med_font;
    private int lineColour;
    private int vgap;
    private ComponentStyle[] styles;
    private Image imgOnTextInputFocus, imgTextInputEnabled;
    public static final int UNITS_WEIGHT_ID = 0x001;
    public static final int UNITS_TEMP_ID = 0x002;
    private SettingsCycleEntry entryAveCycleTime, entryAvePeriodTime, entryAveLutealTime;
    private EntryItem entryWeightUnit, entryTempUnit;
    private SettingsCycleEntry entryProfileName, entryProfilePassword;
    private EntryItem entryLanguage, entryTheme, entryTerms;
    private PatchPainter patchPainter;
    //#if WVGA
//#     private int fontColour;
    //#endif

    public SettingsScreen() {
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initResources();
        initComponents();
    }

    private void initResources() {
        imgOnTextInputFocus = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HIGHLIGHT_PATCH_ROUNDCORNER);
        imgTextInputEnabled = Resources.getInstance().getThemeImage(GraphicsResources.IMG_TEXTINPUT_PATCH);

        //#if QVGA || WQVGA || QVGA_ADS
//#         Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
//#         large_font = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
//# 
//#         Image imgFontSmall = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_SMALL);
//#         small_font = new BitmapFont(imgFontSmall, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_SMALL, 0);
//# 
//#         Image imgFontMed = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
//#         med_font = new BitmapFont(imgFontMed, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         large_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
//#         small_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
//#         med_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        //#endif 

        lineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));
        padding = 4 * UiKitDisplay.getWidth() / 100;

        //#if QVGA || WQVGA || QVGA_ADS
//#         vgap = 2 * UiKitDisplay.getHeight() / 100;
        //#elif WVGA
//#         vgap = 1 * UiKitDisplay.getHeight() / 100;
        //#endif 

        styles = new ComponentStyle[2];
        ComponentStyle style_enabled = new ComponentStyle();
        ComponentStyle style_onfocus = new ComponentStyle();

        int pBorder = 10;

        PatchPainter patchRendererEnabled = new PatchPainter(imgTextInputEnabled, pBorder, pBorder, pBorder, pBorder);
        style_enabled.addRenderer(patchRendererEnabled);

        patchPainter = new PatchPainter(imgOnTextInputFocus, pBorder, pBorder, pBorder, pBorder);
        style_onfocus.addRenderer(patchRendererEnabled);
        style_onfocus.addRenderer(patchPainter);

        styles[0] = style_enabled;
        styles[1] = style_onfocus;

    }

    private void initComponents() {
        int botPadding = 
        //#if QVGA || WQVGA || QVGA_ADS
//#         padding;
        //#elif WVGA
//#         padding + Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight();
        //#endif 
        
        getStyle(true).setPadding(padding * 2, padding, botPadding, padding);
        setLayout(new BoxLayout(UikitConstant.VERTICAL, vgap * 2));

        Panel cycle = getSettingsPanel();
        //#if QVGA || WQVGA || QVGA_ADS
//#         cycle.addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_CYCLE), cycle.getWidth(), large_font.getHeight(), lineColour, large_font));
        //#elif WVGA
//#         cycle.addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_CYCLE), cycle.getWidth(), large_font.getHeight(), lineColour, large_font, fontColour));
        //#endif 
        
        cycle.addComponent(entryAveCycleTime = new SettingsCycleEntry(cycle.getWidth(),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_CYCLE_TITLE),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_CYCLE_DESC),
                "" + settings.getAve_cycle_time(), med_font, small_font, styles, false, TextField.DECIMAL, patchPainter));

        cycle.addComponent(entryAvePeriodTime = new SettingsCycleEntry(cycle.getWidth(),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_PERIOD_TITLE),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_PERIOD_DESC),
                "" + settings.getAve_period_time(), med_font, small_font, styles, false, TextField.DECIMAL, patchPainter));

        cycle.addComponent(entryAveLutealTime = new SettingsCycleEntry(cycle.getWidth(),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_LUTEAL_TITLE),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_LUTEAL_DESC),
                "" + settings.getAve_luteal_time(), med_font, small_font, styles, false, TextField.DECIMAL, patchPainter));
        addComponent(cycle);

        Image imgFlag = Resources.getInstance().getThemeImage(GraphicsResources.IMG_FLAG_DE);
        int h = imgFlag.getHeight() + (vgap * 2);
        int w = iWidth - (padding * 2);

        Panel units = getSettingsPanel();
        //#if QVGA || WQVGA || QVGA_ADS
//#         units.addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_UNITS), units.getWidth(), large_font.getHeight(), lineColour, large_font));
        //#elif WVGA
//#         units.addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_UNITS), units.getWidth(), large_font.getHeight(), lineColour, large_font, fontColour));
        //#endif 
        units.addComponent(entryWeightUnit = new EntryItem(
                w,
                h,
                patchPainter,
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT),
                settings.getWeight_unit() == Settings.WEIGHT_KG ? Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT_KG) : Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_WEIGHT_POUNDS),
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_WEIGHT),
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW),
                med_font,
                this,
                Utils.ENTRY_WEIGHT,
                new int[]{settings.getWeight_unit() == Settings.WEIGHT_KG ? 0 : 1}));
        units.addComponent(entryTempUnit = new EntryItem(
                w,
                h,
                patchPainter,
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP),
                settings.getBasal_temp_unit() == Settings.TEMP_CEL ? Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP_CELSIUS) : Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_TEMP_FAH),
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_TEMP),
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW),
                med_font,
                this,
                Utils.ENTRY_TEMP,
                new int[]{settings.getBasal_temp_unit() == Settings.TEMP_CEL ? 0 : 1}));
        addComponent(units);

        Panel personal = getSettingsPanel();
        //#if QVGA || WQVGA || QVGA_ADS
//#         personal.addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_PERSONAL), personal.getWidth(), large_font.getHeight(), lineColour, large_font));
        //#elif WVGA
//#         personal.addComponent(new SettingsDelimiter(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_PERSONAL), personal.getWidth(), large_font.getHeight(), lineColour, large_font, fontColour));
        //#endif 
        personal.addComponent(entryProfileName = new SettingsCycleEntry(personal.getWidth(),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_YOURNAME),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_YOURNAME_DESC),
                settings.getProfile().getName(), med_font, small_font, styles, true, TextField.ANY, patchPainter));
        personal.addComponent(entryProfilePassword = new SettingsCycleEntry(personal.getWidth(),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_YOURPIN),
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_YOURPIN_DESC),
                "" + settings.getProfile().getPassword(), med_font, small_font, styles, true, TextField.DECIMAL, patchPainter));
        addComponent(personal);


        entryLanguage = new EntryItem(w, h, patchPainter,
                Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_LANGUAGE),
                Utils.getLocaleLanguageName(settings.getCurrentLocale()),
                Utils.getEntryIcon(Utils.ENTRY_LOCALES, Utils.getSelectedIndex(settings.getCurrentLocale())),
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW),
                med_font, this, Utils.ENTRY_LOCALES, new int[]{0});
        addComponent(entryLanguage);

        entryTheme = new EntryItem(w, h, patchPainter, Resources.getInstance().getText(GlobalResources.TXT_THEME),
                Utils.getThemeName(settings.getCurrentTheme()),
                null,
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW), med_font, this, Utils.ENTRY_LOCALES, new int[]{0});
        addComponent(entryTheme);

        entryTerms = new EntryItem(w, h, patchPainter, Resources.getInstance().getText(GlobalResources.TXT_TERMS), null, null,
                Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_ARROW), med_font, this, 0, null);
        addComponent(entryTerms);

        setBottomOffset(Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight());
    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        super.onComponentEvent(c, e, o, p);
        if (c == entryLanguage) {
            ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_LOCALE, false, null);
        } else if (c == entryTheme) {
            ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_THEME, false, null);
        } else if (c == entryTerms) {
            ((InBloomController) controller).setPrevious_screen_id(InBloomController.SCREEN_SETTINGS);
            ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_TOS, false, null);
        } else if(c == entryWeightUnit){
            ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_WEIGHTTEMP, true, new Integer(Utils.ENTRY_WEIGHT));
        } else if(c == entryTempUnit){
            ((InBloomController) controller).navigateScreen(InBloomController.SCREEN_WEIGHTTEMP, true, new Integer(Utils.ENTRY_TEMP));            
        }
    }

    private Panel getSettingsPanel() {
        Panel panel = new Panel(iWidth - (padding * 2), 0);
        panel.setLayout(new BoxLayout(UikitConstant.VERTICAL, vgap));

        return panel;
    }

    public final boolean saveSettings() {
        boolean hasError = false;
        String title = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_CORRECTION_REQUIRED);
        String reason_suffix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_ISNOTVALID);
        String reason_prefix = "";
        if (entryAveCycleTime.getValue() == null
                || entryAveCycleTime.getValue().trim().equals("")
                || Integer.parseInt(entryAveCycleTime.getValue()) > 50
                || Integer.parseInt(entryAveCycleTime.getValue()) <= 0) {
            hasError = true;
            reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_CYCLE_TITLE);
        } else if (entryAveLutealTime.getValue() == null
                || entryAveLutealTime.getValue().trim().equals("")
                || Integer.parseInt(entryAveLutealTime.getValue()) > 50
                || Integer.parseInt(entryAveLutealTime.getValue()) <= 0) {
            hasError = true;
            reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_LUTEAL_TITLE);
        } else if (entryAvePeriodTime.getValue() == null
                || entryAvePeriodTime.getValue().trim().equals("")
                || Integer.parseInt(entryAvePeriodTime.getValue()) > 50
                || Integer.parseInt(entryAvePeriodTime.getValue()) <= 0) {
            hasError = true;
            reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_PERIOD_TITLE);
        } else if (entryProfileName.getValue() == null
                || entryProfileName.getValue().trim().equals("")) {
            hasError = true;
            reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_YOURNAME);
        } else if (entryProfilePassword.getValue() == null
                || entryProfilePassword.getValue().trim().equals("")) {
            hasError = true;
            reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_YOURPIN);
        }

        if (!hasError) {
            if (Integer.parseInt(entryAveLutealTime.getValue()) > Integer.parseInt(entryAveCycleTime.getValue())) {
                hasError = true;
                reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_LUTEAL_ERROR);
                reason_suffix = "";
            }

            if (Integer.parseInt(entryAvePeriodTime.getValue()) > Integer.parseInt(entryAveCycleTime.getValue())) {
                hasError = true;
                reason_prefix = Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_PERIOD_ERROR);
                reason_suffix = "";
            }
        }

        if (!hasError) {
            try {
                updateSettingsModel();
                Database.getInstance().saveISerializable(settings, Database.SETTINGS);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ((InBloomController) controller).showAlertDialog(title, reason_prefix + reason_suffix);
        }

        return false;
    }

    private void updateSettingsModel() {
        settings.setAve_cycle_time(Integer.parseInt(entryAveCycleTime.getValue()));
        settings.setAve_luteal_time(Integer.parseInt(entryAveLutealTime.getValue()));
        settings.setAve_period_time(Integer.parseInt(entryAvePeriodTime.getValue()));
        
        int idx = entryWeightUnit.getSelectedIndices()[0];
        if (idx == 0) {
            settings.setWeight_unit(Settings.WEIGHT_KG);
        }else{
            settings.setWeight_unit(Settings.WEIGHT_POUNDS);
        }
        
        idx = entryTempUnit.getSelectedIndices()[0];
        if (idx == 0) {
            settings.setBasal_temp_unit(Settings.TEMP_CEL);
        }else{
            settings.setBasal_temp_unit(Settings.TEMP_FAH);
        }

        settings.getProfile().setName(entryProfileName.getValue());
        settings.getProfile().setPassword(Integer.parseInt(entryProfilePassword.getValue()));
    }
}
