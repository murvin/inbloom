package com.inbloom.ui;

import com.inbloom.ui.components.DummyFocusable;
import com.inbloom.ui.components.SettingsDelimiter;
import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.animations.UikitImageBox;
import com.uikit.animations.UikitTextBox;
import com.uikit.coreElements.BitmapFont;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.UikitCanvas;
import com.uikit.styles.TextStyle;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class HelpScreen extends InBloomScreen {

    private BitmapFont med_font, large_font;
    private int lineColour;
    private TextStyle txtStyleDesc;
    private Image imgCapture1, imgCapture2, imgCapture3, imgCapture4;

    public HelpScreen() {
        initResources();
        initComponents();
    }

    private void initResources() {
        Image imgFontMed = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_SMALL);
        med_font = new BitmapFont(imgFontMed, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_SMALL, 0);

        Image imgFontLarge = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
        large_font = new BitmapFont(imgFontLarge, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);

        padding = 4 * UiKitDisplay.getWidth() / 100;
        lineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_THEME_MAIN_COLOR));

        txtStyleDesc = new TextStyle(med_font);

        imgCapture1 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_1);
        imgCapture2 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_2);
        imgCapture3 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_3);
        imgCapture4 = Resources.getInstance().getThemeImage(GraphicsResources.IMG_HELP_CAPTURE_4);
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
        return new SettingsDelimiter(title, w, h, lineColour, large_font);
    }

    private UikitImageBox getImage(Image image) {
        return new UikitImageBox(image);
    }

    private UikitTextBox getDescription(String description) {
        int w = getWidth() - (padding * 2);
        UikitTextBox txtBox = new UikitTextBox(w, description, txtStyleDesc);
        return txtBox;
    }
}
