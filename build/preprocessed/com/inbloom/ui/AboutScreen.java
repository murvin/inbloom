package com.inbloom.ui;

import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
//#if QVGA
//# import com.inbloom.utils.Utils;
//#endif

import com.uikit.animations.UikitImageBox;
//#if QVGA
//# import com.uikit.coreElements.BitmapFont;
//#endif
//#if WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif
import com.uikit.coreElements.UiKitDisplay;

import com.uikit.layout.BoxLayout;
import com.uikit.styles.ComponentStyle;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class AboutScreen extends InBloomScreen {

    private Image imgLogo;
    private Image imgAppNameTitle, imgAppName;
    private Image imgVersionNameTitle, imgVersionName;
    private Image imgDeveloperNameTitle, imgDeveloperName;
    private Image imgDedicatedNameTitle, imgDedicatedTitle;
    //#if QVGA
//#     private BitmapFont 
    //#elif WVGA
//#     private SystemFont 
    //#endif
            fontTitle, fontDesc;
    private int colourTitle;
    private int vgap;
    private ComponentStyle titleStyle;
    
    //#if WVGA
//#     private int fontColour;
    //#endif
    public AboutScreen() {
        initResources();
        initComponents();
    }

    private void initResources() {
        //#if QVGA
//#         Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
//#         fontTitle = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
//# 
//#         Image imgFontDesc = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_MEDIUM);
//#         fontDesc = new BitmapFont(imgFontDesc, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_MEDIUM, 0);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         fontTitle = new SystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
//#         fontDesc = new SystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
//# 
        //#endif

        colourTitle = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));
        padding = 4 * UiKitDisplay.getWidth() / 100;

        vgap = 1 * UiKitDisplay.getHeight() / 100;

        imgLogo = Resources.getInstance().getThemeImage(GraphicsResources.IMG_LOGO);

        String title = Resources.getInstance().getText(GlobalResources.TXT_ABOUT_APP_NAME);
        imgAppNameTitle = fontTitle.drawStringToImage(title, colourTitle);
        title = UiKitDisplay.midlet.getAppProperty("MIDlet-Name");
        //#if QVGA
//#         imgAppName = fontDesc.drawStringToImage(title);
        //#elif WVGA
//#             imgAppName = fontDesc.drawStringToImage(title, fontColour);
        //#endif

        title = Resources.getInstance().getText(GlobalResources.TXT_ABOUT_VERSION);
        imgVersionNameTitle = fontTitle.drawStringToImage(title, colourTitle);
        title = UiKitDisplay.midlet.getAppProperty("MIDlet-Version");
        //#if QVGA
//#         imgVersionName = fontDesc.drawStringToImage(title);
        //#elif WVGA
//#         imgVersionName = fontDesc.drawStringToImage(title, fontColour);
        //#endif

        title = Resources.getInstance().getText(GlobalResources.TXT_ABOUT_DEVELOPER);
        imgDeveloperNameTitle = fontTitle.drawStringToImage(title, colourTitle);
        title = UiKitDisplay.midlet.getAppProperty("MIDlet-Vendor");
        //#if QVGA
//#         imgDeveloperName = fontDesc.drawStringToImage(title);
        //#elif WVGA
//#         imgDeveloperName = fontDesc.drawStringToImage(title, fontColour);
        //#endif

        title = Resources.getInstance().getText(GlobalResources.TXT_ABOUT_DEDICATED_TITLE);
        imgDedicatedNameTitle = fontTitle.drawStringToImage(title, colourTitle);
        title = Resources.getInstance().getText(GlobalResources.TXT_ABOUT_DEDICATED_TEXT);
        //#if QVGA
//#         imgDedicatedTitle = fontDesc.drawStringToImage(title);
        //#elif WVGA
//#         imgDeveloperName = fontDesc.drawStringToImage(title, fontColour);
        //#endif

        titleStyle = new ComponentStyle();
        titleStyle.setPadding(vgap * 2, 0, 0, 0);


    }

    private void initComponents() {

        ((BoxLayout) getLayout()).setGap(vgap);

        UikitImageBox imgBoxLogo = new UikitImageBox(imgLogo);
        ComponentStyle st = new ComponentStyle();
        st.setPadding(padding);
        imgBoxLogo.setStyle(st);
        addComponent(imgBoxLogo);

        addComponent(new UikitImageBox(0, 0, imgAppNameTitle, titleStyle));
        addComponent(new UikitImageBox(imgAppName));

        addComponent(new UikitImageBox(0, 0, imgVersionNameTitle, titleStyle));
        addComponent(new UikitImageBox(imgVersionName));

        addComponent(new UikitImageBox(0, 0, imgDeveloperNameTitle, titleStyle));
        addComponent(new UikitImageBox(imgDeveloperName));

        addComponent(new UikitImageBox(0, 0, imgDedicatedNameTitle, titleStyle));
        addComponent(new UikitImageBox(imgDedicatedTitle));

        updateBottomOffset();

        getStyle(true).setPadding(0, padding, bottomPadding, padding);

    }
}
