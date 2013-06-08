package com.inbloom.ui;

import com.inbloom.ui.components.DummyFocusable;
import com.inbloom.ui.components.SettingsDelimiter;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
import com.inbloom.utils.Utils;
//#endif 

import com.uikit.animations.UikitImageBox;
import com.uikit.animations.UikitTextBox;
//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.UikitCanvas;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.TextStyle;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class HelpScreen extends InBloomScreen {

    //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
    private BitmapFont
    //#elif WVGA
//#     private SystemFont
    //#endif
            
    med_font, large_font;
    
    
    private int lineColour;
    private TextStyle txtStyleDesc;
    private Image imgCapture1, imgCapture2, imgCapture3, imgCapture4;
    private ComponentStyle imgStyle;
    
    //#if WVGA
//#     private int fontColour;
    //#endif

    public HelpScreen() {
        initResources();
        initComponents();
    }

    private void initResources() {
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        Image imgFontMed = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_SMALL);
        med_font = new BitmapFont(imgFontMed, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_SMALL, 0);

        Image imgFontLarge = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        large_font = new BitmapFont(imgFontLarge, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         med_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
//#         large_font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
//#         
        //#endif 
        
        padding = 4 * UiKitDisplay.getWidth() / 100;
        lineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));

        txtStyleDesc = new TextStyle(med_font);

        imgCapture1 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_1);
        imgCapture2 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_2);
        imgCapture3 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_3);
        imgCapture4 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_4);
        
        imgStyle = new ComponentStyle();
        
    }


    private void initComponents() {

        if (!UikitCanvas.isTouch) {
            addComponent(new DummyFocusable(iWidth - (padding * 2), 1));
        }

        addComponent(getTitleItem(Resources.getInstance().getText(GlobalResources.TXT_HELP_YOURPHASES_TITLE)));
        addComponent(getDescription(Resources.getInstance().getText(GlobalResources.TXT_HELP_YOURPHASES_DESC)));
        addComponent(getImage(imgCapture1));


        addComponent(getTitleItem(Resources.getInstance().getText(GlobalResources.TXT_HELP_THECALENDAR_TITLE)));
        addComponent(getDescription(Resources.getInstance().getText(GlobalResources.TXT_HELP_THECALENDAR_DESC)));
        addComponent(getImage(imgCapture2));


        addComponent(getTitleItem(Resources.getInstance().getText(GlobalResources.TXT_HELP_ENTERING_TITLE)));
        addComponent(getDescription(Resources.getInstance().getText(GlobalResources.TXT_HELP_ENTERING_DESC)));
        addComponent(getImage(imgCapture3));

        addComponent(getTitleItem(Resources.getInstance().getText(GlobalResources.TXT_HELP_CHARTS_TITLE)));
        addComponent(getDescription(Resources.getInstance().getText(GlobalResources.TXT_HELP_CHARTS_DESC)));
        addComponent(getImage(imgCapture4));

        int menuBarHeight = Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight();
        bottomPadding = menuBarHeight + padding;
        getStyle(true).setPadding(padding, padding, bottomPadding, padding);
    }

    private SettingsDelimiter getTitleItem(String title) {
        int w = getWidth() - (padding * 2);
        int h = large_font.getHeight() * 2;
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
        return new SettingsDelimiter(title, w, h, lineColour, large_font);
        //#elif WVGA
//#         return new SettingsDelimiter(title, w, h, lineColour, large_font, fontColour);
        //#endif 
    }

    private UikitImageBox getImage(Image image) {
        UikitImageBox imgBox = new UikitImageBox(image);
        imgStyle.setPadding(padding, 0, 0, 0);
        imgBox.setStyle(imgStyle);
        return imgBox;
    }

    private UikitTextBox getDescription(String description) {
        int w = getWidth() - (padding * 2);
        UikitTextBox txtBox = new UikitTextBox(w, description, txtStyleDesc);
        return txtBox;
    }
}
