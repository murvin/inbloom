package com.inbloom.ui.components;

import com.uikit.coreElements.Component;
import com.uikit.coreElements.BitmapFont;

import javax.microedition.lcdui.Graphics;

public class SettingsDelimiter extends Component {

    private String text;
    private int lineColour;
    private BitmapFont font;

    public SettingsDelimiter(String text, int width, int height, int lineColour, BitmapFont font) {
        super(width, height + 1);
        this.text = text;
        this.lineColour = lineColour;
        this.font = font;
    }

    protected void drawCurrentFrame(Graphics g) {
        font.drawString(g, text, 0, iHeight - font.getHeight() - 1, 20);
        g.setColor(lineColour);
        g.drawLine(0, iHeight, iWidth, iHeight);
    }
}
