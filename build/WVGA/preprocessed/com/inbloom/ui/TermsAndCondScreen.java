package com.inbloom.ui;

import com.inbloom.ui.components.DummyFocusable;
import com.inbloom.ui.components.SettingsDelimiter;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
//#if QVGA || WQVGA || QVGA_ADS
//# import com.inbloom.utils.Utils;
//#endif

import com.uikit.animations.UikitTextBox;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
//#if QVGA || WQVGA || QVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.coreElements.UikitCanvas;
import com.uikit.layout.BoxLayout;
import com.uikit.styles.TextStyle;

import javax.microedition.lcdui.Font;
//#if QVGA || WQVGA || QVGA_ADS
//# import javax.microedition.lcdui.Image;
//#endif 

public class TermsAndCondScreen extends InBloomScreen {

    private String[] titles;
    private String[] description;
    //#if QVGA || WQVGA || QVGA_ADS
//#     private BitmapFont 
    //#elif WVGA
    private SystemFont
    //#endif 
    largeFont, smallFont;
    private int lineColour;
    private TextStyle txtBoxStyle;
    private int vgap;
    //#if WVGA
    private int fontColour;
    //#endif

    public TermsAndCondScreen() {
        initResources();
        initComponents();
    }

    private void initResources() {
        titles = new String[]{
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_HEADER_1),
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_HEADER_2),
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_HEADER_3),
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_HEADER_4)
        };
        description = new String[]{
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_DESC_1),
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_DESC_2),
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_DESC_3),
            Resources.getInstance().getText(GlobalResources.TXT_TERMS_DESC_4)
        };

        //#if QVGA || WQVGA || QVGA_ADS
//#         Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
//#         largeFont = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
//# 
//#         Image imgFontSmall = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_SMALL);
//#         smallFont = new BitmapFont(imgFontSmall, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_SMALL, 0);
        //#elif WVGA
        fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
        largeFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        smallFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        //#endif 

        lineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));
        padding = 4 * UiKitDisplay.getWidth() / 100;

        vgap = 2 * UiKitDisplay.getHeight() / 100;

        txtBoxStyle = new TextStyle(smallFont);
        //#if WVGA
        txtBoxStyle.setFontColour(fontColour);
        //#endif
    }

    private void initComponents() {
        int botPadding = 
        //#if QVGA || QVGA_ADS
//#         padding;
        //#elif WVGA || WQVGA
        padding + Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight();
        //#endif 
        getStyle(true).setPadding(padding * 2, padding, botPadding, padding);
        setLayout(new BoxLayout(UikitConstant.VERTICAL, vgap * 2));
        int w = iWidth - (padding * 2);


        if (!UikitCanvas.isTouch) {
            addComponent(new DummyFocusable(w, 1));
        }

        for (int i = 0; i < titles.length; i++) {
            //#if QVGA || WQVGA || QVGA_ADS
//#             addComponent(new SettingsDelimiter(titles[i], w, largeFont.getHeight(), lineColour, largeFont));
            //#elif WVGA
            addComponent(new SettingsDelimiter(titles[i], w, largeFont.getHeight(), lineColour, largeFont, fontColour));
            //#endif 
            addComponent(new UikitTextBox(w, description[i], txtBoxStyle));
        }

        setBottomOffset(Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight());
    }
}
