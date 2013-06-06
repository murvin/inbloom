package com.uikit.coreElements;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class UikitFont {

    protected boolean isUseImagesOnly;
    protected boolean isStringBasedWidth;
    protected int style = Font.getDefaultFont().getStyle();
    protected int size = Font.getDefaultFont().getSize();

    protected UikitFont(int iStyle, int iSize, boolean bUseImagesOnly, boolean bStringBasedWidth) {
        setStyle(iStyle);
        setSize(iSize);
        this.isStringBasedWidth = bStringBasedWidth;
        this.isUseImagesOnly = bUseImagesOnly;
    }

    public final void setStyle(int iStyle) throws IllegalArgumentException {
        this.style = iStyle;

        notifySubclass_styleChanged(iStyle);
    }

    public int getStyle() {
        return style;
    }

    public void setSize(int iSize) throws IllegalArgumentException {
        this.size = iSize;

        notifySubclass_sizeChanged(iSize);
    }

    public int getSize() {
        return size;
    }

    public boolean isUseImagesOnly() {
        return isUseImagesOnly;
    }

    protected abstract void notifySubclass_styleChanged(int style);

    protected abstract void notifySubclass_sizeChanged(int size);

    public abstract boolean isBold();

    public abstract boolean isItalic();

    public abstract boolean isPlain();

    public abstract boolean isUnderlined();

    public abstract UikitFont clone();

    public abstract int charWidth(char ch);

    public abstract int charsWidth(char[] chs, int offset, int length);

    public abstract int getBaselinePosition();

    public abstract int stringWidth(String str);

    public abstract int substringWidth(String str, int offset, int length);

    public abstract int getHeight();

    public abstract void drawChar(Graphics g, char ch, int x, int y, int anchor);

    public abstract void drawChars(Graphics g, char[] chs, int offset, int length, int x, int y, int anchor);

    public abstract void drawString(Graphics g, String str, int x, int y, int anchor);

    public abstract void drawSubstring(Graphics g, String str, int offset, int length, int x, int y, int anchor);

    public abstract Image drawStringToImage(String str, int color);

    public void setStringBasedWidth(boolean isStringBasedWidth) {
        this.isStringBasedWidth = isStringBasedWidth;
    }

    public boolean isStringBasedWidth() {
        return isStringBasedWidth;
    }
}
