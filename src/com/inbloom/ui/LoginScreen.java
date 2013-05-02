package com.inbloom.ui;

import com.inbloom.InBloomController;
import com.inbloom.model.Settings;
import com.inbloom.utils.Database;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.animations.UikitImageBox;
import com.uikit.animations.UikitTextBox;
import com.uikit.animations.UikitTextInput;
import com.uikit.animations.UikitButton;
import com.uikit.mvc.patterns.Screen;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.IComponentEventListener;
//#if QVGA || WQVGA || QVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.styles.TextStyle;
import com.uikit.textinputhandler.NativeTextInputHandler;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class LoginScreen extends Screen {

    private Settings settings;
    private Image imgLogo;
    private UikitImageBox imgBoxLogo;
    private UikitTextBox txtBoxPassword;
    private UikitTextInput txtInputPassword;
    private UikitButton btnEnter;
    private int padding;
    //#if QVGA || WQVGA || QVGA_ADS
//#     private BitmapFont
    //#elif WVGA
//#     private SystemFont
    //#endif 
    largeFont;
    private InBloomController myController;

    public LoginScreen(InBloomController controller) {
        initResources();
        initComponents();
        addAllComponents();
        this.myController = controller;
    }

    private void initResources() {
        try {
            settings = (Settings) Database.getInstance().retrieveISerializable(Database.SETTINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.imgLogo = Resources.getInstance().getThemeImage(GraphicsResources.IMG_LOGO);
        padding = 5;
        
        //#if QVGA || WQVGA || QVGA_ADS
//#         Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
//#         largeFont = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         largeFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        //#endif 
    }

    private void initComponents() {
        setLayout(null);


        imgBoxLogo = new UikitImageBox(imgLogo);
        imgBoxLogo.x = (iWidth - imgBoxLogo.getWidth()) / 2;
        imgBoxLogo.y = iHeight / 9;

        String strPassword = null;
        if (settings.getProfile().getPassword() == 1234) {
            strPassword = Resources.getInstance().getText(GlobalResources.TXT_PRESS_ENTER);
        } else {
            strPassword = Resources.getInstance().getText(GlobalResources.TXT_ENTER_PASSWORD);
        }


        txtBoxPassword = new UikitTextBox(strPassword);
        txtBoxPassword.setTextStyle(new TextStyle(largeFont));
        txtBoxPassword.x = (iWidth - txtBoxPassword.getWidth()) / 2;
        txtBoxPassword.y = iHeight / 2 + (txtBoxPassword.getHeight() * 2);


        txtInputPassword = new UikitTextInput(iWidth * 75 / 100, iHeight * 9 / 100, new NativeTextInputHandler(strPassword, 1024, TextField.NUMERIC | TextField.PASSWORD), false, true);
        txtInputPassword.x = (iWidth - txtInputPassword.getWidth()) / 2;
        txtInputPassword.y = txtBoxPassword.y + txtBoxPassword.getHeight() + padding;
        if (settings.getProfile().getPassword() == 1234) {
            txtInputPassword.setText("" + 1234);
        }
        
        Utils.applyTextFieldStyles(txtInputPassword, largeFont);
        //#if WVGA 
//#         TextStyle st = new TextStyle(largeFont);
//#         st.setFontColour(Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR)));
//#         txtInputPassword.setStyle( UikitTextInput.COMP_TEXTBOXTEXT, UikitTextInput.STATE_ENABLED, st);
        //#endif 
         
       

        btnEnter = Utils.getButton(Resources.getInstance().getText(GlobalResources.TXT_ENTER), iWidth * 38 / 100);
        btnEnter.setEventListener(new IComponentEventListener() {

            public void onComponentEvent(Component c, int iEventId, Object paramObj, int iParam) {

                if (iEventId == UikitButton.EVENT_PRESSED) {
                    if (isValidPassword()) {
                        if (settings.isHasShownTOSOnStartUp()) {
                            LoginScreen.this.controller.navigateScreen(InBloomController.SCREEN_CAL, false, null);
                        } else {
                            LoginScreen.this.controller.navigateScreen(InBloomController.SCREEN_LOCALE, false, null);
                        }
                    } else {
                        myController.showAlertDialog(
                                Resources.getInstance().getText(GlobalResources.TXT_APP_NAME),
                                Resources.getInstance().getText(GlobalResources.TXT_ERROR_INVALID_PASSWORD));
                    }
                }
            }
        });
        btnEnter.x = (iWidth - btnEnter.getWidth()) / 2;
        btnEnter.y = txtInputPassword.y + txtInputPassword.getHeight() + padding;
    }

    private void addAllComponents() {
        addComponent(imgBoxLogo);
        addComponent(txtBoxPassword);
        addComponent(txtInputPassword);
        addComponent(btnEnter);
    }

    private boolean isValidPassword() {
        String enteredPassword = txtInputPassword.getText();
        if (settings.getProfile().getPassword() == 1234) {
            return true;
        } else {
            if (enteredPassword == null
                    || !(enteredPassword.equals("" + settings.getProfile().getPassword()))) {
                return false;
            }
        }
        return true;
    }

    public void enter() {
    }

    public void exit() {
    }

    public void exitToRight() {
    }
}
