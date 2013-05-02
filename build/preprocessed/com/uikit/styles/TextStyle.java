package com.uikit.styles;

import com.uikit.utils.*;
import com.uikit.coreElements.SystemFont;
import com.uikit.coreElements.UikitFont;
import javax.microedition.lcdui.Font;

public class TextStyle extends Style {

    private UikitFont font;
    private int fontColour;
    private int lineSpacing;
    private int align = UikitConstant.LEFT;
    private int fontSize = Font.getDefaultFont().getSize();
    private static TextStyle defaultTextStyle;

    public TextStyle() {
        font = new SystemFont(Font.getDefaultFont().getFace(), Font.getDefaultFont().getStyle(), Font.getDefaultFont().getSize());
    }

    public TextStyle(UikitFont font) {
        if (font == null) {
            throw new IllegalArgumentException();
        }
        this.font = font;
    }

    public TextStyle(UikitFont font, int align, int fontColour, int lineSpacing, int fontSize) {
        this(font);
        this.fontColour = fontColour;
        this.lineSpacing = lineSpacing;
        this.fontSize = fontSize;
        if (align != UikitConstant.LEFT && align != UikitConstant.RIGHT && align != UikitConstant.HCENTER) {
            throw new IllegalArgumentException();
        }
        this.align = align;
    }

    public TextStyle(UikitFont font, int align, int fontColour, int lineSpacing) {
        this(font);
        this.fontColour = fontColour;
        this.lineSpacing = lineSpacing;
        if (align != UikitConstant.LEFT && align != UikitConstant.RIGHT && align != UikitConstant.HCENTER) {
            throw new IllegalArgumentException();
        }
        this.align = align;
    }

    public void setFont(UikitFont font) {
        if (font == null) {
            throw new IllegalArgumentException();
        }
        this.font = font;
        notifyStyleChanged();
    }

    public UikitFont getFont() {
        return font;
    }

    public void setFontColour(int fontColour) {
        this.fontColour = fontColour;
        notifyStyleChanged();
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        notifyStyleChanged();
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        font.setSize(fontSize);
        notifyStyleChanged();
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getFontColour() {
        return fontColour;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setAlign(int align) {
        if (align != UikitConstant.LEFT && align != UikitConstant.RIGHT && align != UikitConstant.HCENTER) {
            throw new IllegalArgumentException();
        }
        this.align = align;
        notifyStyleChanged();
    }

    public int getAlign() {
        return align;
    }

    public static TextStyle getDefaultTextStyle() {
        return defaultTextStyle == null ? new TextStyle() : defaultTextStyle;
    }

    public TextStyle copy() {
        TextStyle ts = new TextStyle(font);
        ts.fontColour = fontColour;
        ts.lineSpacing = lineSpacing;
        ts.align = align;
        ts.fontSize = fontSize;
        return ts;
    }
}