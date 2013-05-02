package com.inbloom.ui.components;

import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ChartKey extends Component {

    private Image icon;
    private int colour;
    private int padding;
    private int x_icon, y_icon, line_length, line_height, gap, x_line, y_line;

    public ChartKey(Image icon,
            int colour) {
        super(0, 0);
        this.icon = icon;
        this.colour = colour;
        initCoors();
    }

    private void initCoors() {
        padding = 3 * UiKitDisplay.getWidth() / 100;
        gap = 2;
        line_height = 3;
        line_length = padding * 4;

        x_icon = padding - 2;

        int w = padding + icon.getWidth() + line_length + (gap * 2) + x_icon + x_icon;
        int h = icon.getHeight() + (padding * 2);
        setSize(w, h);


        y_icon = (iHeight - icon.getWidth()) / 2;


        x_line = x_icon + icon.getWidth() + gap;
        y_line = (iHeight - line_height) / 2;

    }

    protected void drawCurrentFrame(Graphics g) {
        g.drawImage(icon, x_icon, y_icon, 20);
        g.setColor(colour);

        g.drawLine(x_line, y_line, x_line + line_length, y_line);
        g.drawLine(x_line, y_line - 1, x_line + line_length, y_line - 1);
        g.drawLine(x_line, y_line + 1, x_line + line_length, y_line + 1);
    }
}
