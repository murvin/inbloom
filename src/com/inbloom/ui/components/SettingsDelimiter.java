package com.inbloom.ui.components;

import com.uikit.coreElements.Component;

import com.uikit.coreElements.UikitFont;
import javax.microedition.lcdui.Graphics;

public class SettingsDelimiter extends Component {

    private String text;
    private int lineColour, fontColour;
    private UikitFont font;

    public SettingsDelimiter(String text, int width, int height, int lineColour, UikitFont font) {
        super(width, height + 1);
        this.fontColour  = -1;
        this.text = text;
        this.lineColour = lineColour;
        this.font = font;
    }
    
    public SettingsDelimiter(String text, int width, int height, int lineColour, UikitFont font, int fontColour) {
        this(text, width, height, lineColour, font);
        this.fontColour = fontColour;
    }

    protected void drawCurrentFrame(Graphics g) {
        if (fontColour != -1) {
            g.setColor(fontColour);
        }
        font.drawString(g, text, 0, iHeight - font.getHeight() - 1, 20);
        g.setColor(lineColour);
        g.drawLine(0, iHeight, iWidth, iHeight);
    }
}
