package com.uikit.painters;

import com.uikit.coreElements.Component;
import javax.microedition.lcdui.Graphics;

public class BgColorPainter implements IPainter {

    private int bgColor = 0x000000;

    public BgColorPainter() {
    }

    public BgColorPainter(int color) {
        this.bgColor = color;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgcolor) {
        this.bgColor = bgcolor;
    }

    public void paint(Component c, Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
    }
}
