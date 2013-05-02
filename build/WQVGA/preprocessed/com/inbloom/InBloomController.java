package com.inbloom;

import com.inbloom.model.Date;
import com.inbloom.model.Settings;
import com.inbloom.model.User;
import com.inbloom.ui.AboutScreen;
import com.inbloom.ui.EntriesScreen;
import com.inbloom.ui.CalScreen;
import com.inbloom.ui.LoginScreen;
import com.inbloom.ui.SettingsScreen;
import com.inbloom.ui.ChartsScreen;
import com.inbloom.ui.EntryListScreen;
import com.inbloom.ui.EntrySelectionScreen;
import com.inbloom.ui.HelpScreen;
import com.inbloom.ui.LocaleScreen;
import com.inbloom.ui.TermsAndCondScreen;
import com.inbloom.ui.ThemeScreen;
import com.inbloom.ui.WeightAndTempScreen;
import com.inbloom.ui.components.Header;
import com.inbloom.ui.components.MenuBar;
import com.inbloom.ui.components.Tab;
import com.inbloom.ui.components.TabPanel;
import com.inbloom.utils.Database;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.animations.UikitButton;
import com.uikit.animations.AlertDialog;
import com.uikit.animations.InputDialog;
import com.uikit.mvc.patterns.Controller;
import com.uikit.mvc.patterns.Screen;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.IUikitInputHandler;
import com.uikit.coreElements.TouchEventHandler;
import com.uikit.coreElements.UikitCanvas;
//#if QVGA || WQVGA
import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
        
import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

public class InBloomController extends Controller {

    private MenuBar menuBar;
    private TabPanel tabPanel;
    private Header header;
    private Object[] entryDetails;
    private TouchEventHandler currentHandler;
    private IUikitInputHandler handler;
    public static String tempWeight, tempTemp;
    // LAYER CONSTANTS //
    public static final int LAYER_SCREEN = 0x011;
    public static final int LAYER_DIALOG = 0x019;
    public static final int LAYER_MENUBAR = 0x013;
    // SCREEN CONSTANTS //
    public static final int SCREEN_CAL = 0x001;
    public static final int SCREEN_SETTINGS = 0x004;
    public static final int SCREEN_CHARTS = 0x003;
    public static final int SCREEN_ENTRY = 0x007;
    public static final int SCREEN_LOGIN = 0x008;
    public static final int SCREEN_ENTRY_SELECTION = 0x009;
    public static final int SCREEN_LOCALE = 0x010;
    public static final int SCREEN_THEME = 0x011;
    public static final int SCREEN_TOS = 0x012;
    public static final int SCREEN_WEIGHTTEMP = 0x013;
    public static final int SCREEN_ENTRY_LIST = 0x014;
    public static final int SCREEN_ABOUT = 0x015;
    public static final int SCREEN_HELP = 0x016;
    // MENU CONSTANTS 
    public static final int MENU_EXIT = 0x101;
    public static final int MENU_BACK = 0x102;
    public static final int MENU_ABOUT = 0x103;
    public static final int MENU_SAVE = 0x104;
    public static final int MENU_CANCEL = 0x105;
    public static final int MENU_ACCEPT = 0x106;
    public static final int MENU_DECLINE = 0x107;
    public static final int MENU_HELP = 0x108;
    final int ALERT_DIALOG = 0x401;
    final int ALERT_DIALOG_YES = 0x402;
    final int ALERT_DIALOG_NO = 0x403;
    final int INPUT_DIALOG_OK = 0x404;
    final int INPUT_DIALG_CANCEL = 0x405;

    public InBloomController() {
        Settings settings = null;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (settings == null) {
            settings = new Settings();
            settings.initDefault();
            try {
                Database.getInstance().saveISerializable(settings, Database.SETTINGS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        User user = null;
        try {
            user = (User) Database.getInstance().retrieveISerializable(Database.USER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user == null) {
            user = new User();
            try {
                Database.getInstance().saveISerializable(user, Database.USER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Resources.getInstance().initResources("GlobalResources", settings.getCurrentLocale());
            Resources.getInstance().initTheme("GraphicsResources", Utils.getEntryText(Utils.ENTRY_THEMES, settings.getCurrentTheme()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void init(MIDlet midlet) {
        super.init(midlet);
        Settings settings = null;
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        canvas.setBgImage(Resources.getInstance().getThemeImage(GraphicsResources.BG), UikitCanvas.BGPOSMODE_CENTER);
        this.menuBar = new MenuBar();
        this.menuBar.setEventListener(this);
        menuBar.y = canvas.getHeight();
        canvas.setTwuikInputHandler(menuBar);
        view.getLayer(LAYER_MENUBAR).addComponent(this.menuBar);

        this.tabPanel = new TabPanel(this);
        this.tabPanel.loadTabLabel(settings.getCurrentLocale());
        this.tabPanel.y = -(this.tabPanel.getHeight());
        view.getLayer(LAYER_MENUBAR).addComponent(this.tabPanel);
    }

    public void showAlertDialog(String title, String message) {
        int width = UiKitDisplay.getWidth() * 80 / 100;
        int height = width * 65 / 100;

        AlertDialog dialog = new AlertDialog((UiKitDisplay.getWidth() - width) / 2, (UiKitDisplay.getHeight() - height) / 2, width, height, title);
        dialog.setTitle(title);
        dialog.setAlertText(message);
        Utils.applyTextStyles(dialog);
        try {
            dialog.setIcon(Image.createImage("/iconSmall.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        dialog.setStyle(Utils.getDialogComponentStyle());
        UikitButton btn = Utils.getButton(Resources.getInstance().getText(GlobalResources.TXT_COMMON_OK), width * 50 / 100);
        btn.setId(ALERT_DIALOG);
        dialog.addButton(btn);

        btn.setEventListener(this);
        view.getLayer(LAYER_DIALOG).addComponent(dialog);
        handler = canvas.getTwuikInputHandler();
        canvas.setTwuikInputHandler(dialog);
        currentHandler = canvas.getTouchEventHandler();
        canvas.setTouchEventHandler(new TouchEventHandler(dialog.getContainerPanel()));

    }

    public void showYesNoDialog(String title, String message) {
        int width = UiKitDisplay.getWidth() * 80 / 100;
        int height = width * 65 / 100;

        AlertDialog dialog = new AlertDialog((UiKitDisplay.getWidth() - width) / 2, (UiKitDisplay.getHeight() - height) / 2, width, height, title);
        dialog.setTitle(title);
        dialog.setAlertText(message);
        Utils.applyTextStyles(dialog);
        try {
            dialog.setIcon(Image.createImage("/iconSmall.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        dialog.setStyle(Utils.getDialogComponentStyle());

        UikitButton btnYes = Utils.getButton(Resources.getInstance().getText(GlobalResources.TXT_COMMON_YES), width * 35 / 100);
        btnYes.setId(ALERT_DIALOG_YES);
        UikitButton btnNo = Utils.getButton(Resources.getInstance().getText(GlobalResources.TXT_COMMON_NO), width * 35 / 100);
        btnNo.setId(ALERT_DIALOG_NO);
        dialog.addButton(btnNo);
        dialog.addButton(btnYes);

        btnYes.setEventListener(this);
        btnNo.setEventListener(this);
        view.getLayer(LAYER_DIALOG).addComponent(dialog);
        handler = canvas.getTwuikInputHandler();
        canvas.setTwuikInputHandler(dialog);
        currentHandler = canvas.getTouchEventHandler();
        canvas.setTouchEventHandler(new TouchEventHandler(dialog.getContainerPanel()));
    }

    public void showInputDialog(String title, String message, int entryId) {
        int width = UiKitDisplay.getWidth() * 80 / 100;
        int height = width * 65 / 100;
        //#if QVGA || WQVGA
        Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
        BitmapFont f = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);
        //#elif WVGA
//#         SystemFont f = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        //#endif 

        InputDialog dialog = new InputDialog((UiKitDisplay.getWidth() - width) / 2, (UiKitDisplay.getHeight() - height) / 2, width, height, false, false, title, TextField.ANY);
        dialog.setStyle(Utils.getDialogComponentStyle());
        dialog.setId(entryId);

        dialog.setIcon(entryId == Utils.ENTRY_TEMP ? Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_TEMP) : Resources.getInstance().getThemeImage(GraphicsResources.IMG_ENTRY_WEIGHT));

        Utils.applyTextFieldStyles(dialog.getTextInput(), f);

        UikitButton btnYes = Utils.getButton(Resources.getInstance().getText(GlobalResources.TXT_COMMON_OK), width * 35 / 100);
        btnYes.setId(INPUT_DIALOG_OK);
        dialog.addButton(btnYes);
        UikitButton btnNo = Utils.getButton(Resources.getInstance().getText(GlobalResources.TXT_COMMON_CANCEL), width * 35 / 100);
        btnNo.setId(INPUT_DIALG_CANCEL);
        dialog.addButton(btnNo);

        btnYes.setEventListener(this);
        btnNo.setEventListener(this);
        view.getLayer(LAYER_DIALOG).addComponent(dialog);
        handler = canvas.getTwuikInputHandler();
        canvas.setTwuikInputHandler(dialog);
        currentHandler = canvas.getTouchEventHandler();
        canvas.setTouchEventHandler(new TouchEventHandler(dialog.getContainerPanel()));
    }

    public void addLayers() {
        view.addLayer(LAYER_SCREEN);
        view.addLayer(LAYER_DIALOG);
        view.addLayer(LAYER_MENUBAR);
    }

    public void enterMenuBar() {
        this.menuBar.enter(canvas.getHeight() - this.menuBar.getHeight());
    }

    public void enterTabbedPanel() {
        this.tabPanel.enter();
    }

    public void exitTabbedPanel() {
        this.tabPanel.exit();
    }

    public TabPanel getTabPanel() {
        return this.tabPanel;
    }

    public MenuBar getMenuBar() {
        return this.menuBar;
    }

    private void initHeader(String title) {
        //#if QVGA || WQVGA
        Image imgFontSmall = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        BitmapFont large_font = new BitmapFont(imgFontSmall, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         SystemFont large_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        //#endif 
        
        
        this.header = new Header(
                UiKitDisplay.getWidth(),
                Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG),
                title,
                large_font);
        header.y = -header.getHeight();
        view.getLayer(LAYER_MENUBAR).addComponent(header);
    }

    public void exitHeader() {
        if (header != null) {
            header.exit();
        }
    }

    public void enterHeader(String title) {
        if (header == null) {
            initHeader(title);
        }
        header.setTitle(title);
        header.enter();
    }

    public Screen loadScreen(int screen_id, Object param) {
        switch (screen_id) {
            case SCREEN_CAL: {
                return new CalScreen();
            }
            case SCREEN_SETTINGS: {
                return new SettingsScreen();
            }
            case SCREEN_CHARTS: {
                return new ChartsScreen();
            }
            case SCREEN_ENTRY: {
                return new EntriesScreen(param);
            }
            case SCREEN_ENTRY_SELECTION: {
                if (getPrevious_screen_id() == SCREEN_ENTRY_LIST) {
                    Object[] params = (Object[]) param;
                    EntrySelectionScreen sc = new EntrySelectionScreen(params[0]);
                    sc.setDate((Date) params[1]);
                    return sc;
                } else {
                    return new EntrySelectionScreen(param);
                }
            }
            case SCREEN_LOGIN: {
                return new LoginScreen(this);
            }
            case SCREEN_LOCALE: {
                return new LocaleScreen();
            }
            case SCREEN_THEME: {
                return new ThemeScreen();
            }
            case SCREEN_TOS: {
                return new TermsAndCondScreen();
            }
            case SCREEN_WEIGHTTEMP: {
                return new WeightAndTempScreen(param);
            }
            case SCREEN_ENTRY_LIST: {
                return new EntryListScreen();
            }
            case SCREEN_ABOUT:{
                return new AboutScreen();
            }
            case SCREEN_HELP:{
                return new HelpScreen();
            }
            default:
                throw new IllegalStateException();
        }
    }

    public void addScreen(Screen screen) {
        if (screen instanceof LoginScreen) {
            screen.y = 0;
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_EXIT), MENU_EXIT);
            enterMenuBar();
        } else {
            screen.y = tabPanel.getHeight();
            if (screen instanceof SettingsScreen
                    || screen instanceof EntriesScreen
                    || screen instanceof ChartsScreen
                    || screen instanceof CalScreen
                    || screen instanceof LocaleScreen
                    || screen instanceof ThemeScreen
                    || screen instanceof TermsAndCondScreen
                    || screen instanceof WeightAndTempScreen
                    || screen instanceof EntryListScreen
                    || screen instanceof AboutScreen
                    || screen instanceof HelpScreen
                    || screen instanceof EntrySelectionScreen) {
                screen.setStartOffsetY(tabPanel.getHeight());
            }
        }

        view.getLayer(LAYER_SCREEN).addComponent(screen);
        if (screen instanceof EntriesScreen) {
            exitTabbedPanel();
            entryDetails = ((EntriesScreen) screen).entryDetails();
            enterHeader(((EntriesScreen) screen).getDate());
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_SAVE), MENU_SAVE);
            menuBar.setLsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_CANCEL), MENU_CANCEL);
        } else if (screen instanceof SettingsScreen) {
            exitHeader();
            enterTabbedPanel();
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_SAVE), MENU_SAVE);
            menuBar.setLsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_ABOUT), MENU_ABOUT);
        } else if (screen instanceof CalScreen) {
            exitHeader();
            enterTabbedPanel();
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_EXIT), MENU_EXIT);
            menuBar.setLsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_HELP), MENU_HELP);
            if (entryDetails != null) {
                entryDetails = null;
            }
        } else if (screen instanceof WeightAndTempScreen) {
            exitTabbedPanel();
            enterHeader(((WeightAndTempScreen) screen).getHeaderTitle());
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_SAVE), MENU_SAVE);
        } else if (screen instanceof LocaleScreen) {
            exitTabbedPanel();
            enterHeader(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_LANGUAGE));
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_SAVE), MENU_SAVE);
        } else if (screen instanceof ThemeScreen) {
            exitTabbedPanel();
            enterHeader(Resources.getInstance().getText(GlobalResources.TXT_THEME));
        } else if (screen instanceof EntrySelectionScreen) {
            exitTabbedPanel();
            enterHeader(((EntrySelectionScreen) screen).getTitle());
            menuBar.removeSoftKey(true);
            if (getPrevious_screen_id() == SCREEN_ENTRY_LIST) {
                menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_SAVE), MENU_SAVE);
            } else {
                menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_BACK), MENU_BACK);
            }

        } else if (screen instanceof ChartsScreen) {
            menuBar.removeSoftKey(false);
            menuBar.removeSoftKey(true);
        } else if (screen instanceof TermsAndCondScreen) {
            exitTabbedPanel();
            enterHeader(Resources.getInstance().getText(GlobalResources.TXT_TERMS));
            if (getPrevious_screen_id() == SCREEN_SETTINGS) {
                menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_BACK), MENU_BACK);
            } else {
                menuBar.setLsk(Resources.getInstance().getText(GlobalResources.TXT_MENU_DECLINE), MENU_DECLINE);
                menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_MENU_ACCEPT), MENU_ACCEPT);
            }

        } else if (screen instanceof EntryListScreen) {
            exitHeader();
            enterTabbedPanel();
            menuBar.removeSoftKey(false);
        } else if(screen instanceof AboutScreen){
            exitTabbedPanel();
            enterHeader(Resources.getInstance().getText(GlobalResources.TXT_COMMON_ABOUT));
            menuBar.removeSoftKey(true);
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_BACK), MENU_BACK);
        } else if(screen instanceof HelpScreen){
            exitTabbedPanel();
            enterHeader(Resources.getInstance().getText(GlobalResources.TXT_COMMON_HELP));
            menuBar.setRsk(Resources.getInstance().getText(GlobalResources.TXT_COMMON_BACK), MENU_BACK);
        } 
    }

    public void removeScreen(Screen screen) {
        if (screen instanceof CalScreen) {
            menuBar.removeSoftKey(true);
        } else if(screen instanceof SettingsScreen){
            menuBar.removeSoftKey(true);
        }
        view.getLayer(LAYER_SCREEN).removeComponent(screen);
    }

    public void onComponentEvent(Component c, int eventId, Object o, int param) {
        super.onComponentEvent(c, eventId, o, param);
        if (c instanceof Tab) {
            switch (eventId) {
                case TabPanel.TAB_CAL: {
                    if (!(current_screen instanceof CalScreen)) {
                        navigateScreen(SCREEN_CAL, true, null);
                    }
                    break;
                }
                case TabPanel.TAB_LIST: {
                    if (!(current_screen instanceof EntryListScreen)) {
                        navigateScreen(SCREEN_ENTRY_LIST, true, null);
                    }
                    break;
                }
                case TabPanel.TAB_CHARTS: {
                    if (!(current_screen instanceof ChartsScreen)) {
                        navigateScreen(SCREEN_CHARTS, true, null);
                    }
                    break;
                }
                case TabPanel.TAB_SETTINGS: {
                    if (!(current_screen instanceof SettingsScreen)) {
                        navigateScreen(SCREEN_SETTINGS, true, null);
                    }
                    break;
                }
            }
        } else if (c instanceof MenuBar) {
            switch (eventId) {
                case MENU_EXIT: {
                    showYesNoDialog(Resources.getInstance().getText(GlobalResources.TXT_DIALOG_EXIT_TITLE), Resources.getInstance().getText(GlobalResources.TXT_DIALOG_EXIT_DESC));
                    break;
                }
                case MENU_CANCEL: {
                    if (current_screen instanceof EntriesScreen) {
                        navigateScreen(SCREEN_CAL, true, null);
                    }
                    break;
                }
                case MENU_SAVE: {
                    if (current_screen instanceof SettingsScreen) {
                        if (((SettingsScreen) current_screen).saveSettings()) {
                            showAlertDialog(Resources.getInstance().getText(GlobalResources.TXT_SETTINGS), Resources.getInstance().getText(GlobalResources.TXT_SETTINGS_SAVED));
                        }
                    } else if (current_screen instanceof EntriesScreen) {
                        if (!((EntriesScreen) current_screen).hasError()) {
                            ((EntriesScreen) current_screen).saveEntries();
                            showAlertDialog(((EntriesScreen) current_screen).getDate(), Resources.getInstance().getText(GlobalResources.TXT_ALERT_ENTRY_UPDATED));
                            navigateScreen(SCREEN_CAL, true, null);
                        }
                    } else if (current_screen instanceof LocaleScreen) {
                        ((LocaleScreen) current_screen).saveLocale();
                        try {
                            Resources.getInstance().initResources("GlobalResources", ((LocaleScreen) current_screen).getLocale());
                            tabPanel.loadTabLabel(((LocaleScreen) current_screen).getLocale());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            Settings settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
                            if (settings.isHasShownTOSOnStartUp()) {
                                navigateScreen(SCREEN_SETTINGS, true, null);
                            } else {
                                navigateScreen(SCREEN_TOS, true, null);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (current_screen instanceof ThemeScreen) {
                        ((ThemeScreen) current_screen).saveTheme();
                        try {
                            Resources.getInstance().initTheme("GraphicsResources", Utils.getEntryText(Utils.ENTRY_THEMES, ((ThemeScreen) current_screen).getTheme()));
                            canvas.setBgImage(Resources.getInstance().getThemeImage(GraphicsResources.BG), UikitCanvas.BGPOSMODE_CENTER);
                            tabPanel.updateBg();
                            header.setBgImage(Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG));
                            menuBar.updateBg();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        navigateScreen(SCREEN_SETTINGS, true, null);

                    } else if (current_screen instanceof WeightAndTempScreen) {
                        ((WeightAndTempScreen) current_screen).saveValue();
                        navigateScreen(SCREEN_SETTINGS, true, null);
                    } else if (current_screen instanceof EntrySelectionScreen) {
                        ((EntrySelectionScreen) current_screen).replaceEntryItem();
                        showAlertDialog(((EntrySelectionScreen) current_screen).getDate(), Resources.getInstance().getText(GlobalResources.TXT_ALERT_ENTRY_UPDATED));
                        navigateScreen(SCREEN_ENTRY_LIST, false, null);
                    }
                    break;
                }
                case MENU_BACK: {
                    if (current_screen instanceof EntrySelectionScreen) {
                        ((EntrySelectionScreen) current_screen).updateEntryIndices();
                        navigateScreen(SCREEN_ENTRY, true, entryDetails);
                    } else if (current_screen instanceof TermsAndCondScreen) {
                        navigateScreen(SCREEN_SETTINGS, false, null);
                    } else if(current_screen instanceof AboutScreen){
                        navigateScreen(SCREEN_SETTINGS, false, null);
                    } else if(current_screen instanceof HelpScreen){
                        navigateScreen(SCREEN_CAL, false, null);
                    }
                    break;
                }
                case MENU_ACCEPT: {
                    try {
                        Settings settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
                        settings.setHasShownTOSOnStartUp(true);
                        Database.getInstance().saveISerializable(settings, Database.SETTINGS);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    navigateScreen(SCREEN_CAL, false, null);
                    break;
                }
                case MENU_DECLINE: {
                    midlet.notifyDestroyed();
                    break;
                }
                case MENU_ABOUT:{
                    navigateScreen(SCREEN_ABOUT, false, null);
                    break;
                }
                case MENU_HELP:{
                    navigateScreen(SCREEN_HELP, false, null);
                    break;
                }
            }
        } else if (c instanceof UikitButton) {

            if (eventId == UikitButton.EVENT_RELEASED) {
                if (((UikitButton) c).getId() == ALERT_DIALOG) {
                } else if (((UikitButton) c).getId() == ALERT_DIALOG_YES) {
                    midlet.notifyDestroyed();
                } else if (((UikitButton) c).getId() == INPUT_DIALOG_OK) {
                    InputDialog d = (InputDialog) c.getContainingPanel().getContainingPanel().getContainingPanel();
                    if (current_screen instanceof EntriesScreen) {
                        ((EntriesScreen) current_screen).updateEntry(d.getId(), d.getInput());
                    } else if(current_screen instanceof EntryListScreen){
                        ((EntryListScreen) current_screen).updateEntry(d.getId(), d.getInput());
                    }
                }

                view.getLayer(LAYER_DIALOG).removeAllComponents();
                canvas.setTwuikInputHandler(handler);
                canvas.setTouchEventHandler(currentHandler);
            }
        }
    }

    public Object[] getEntryDetails() {
        return this.entryDetails;
    }
}
