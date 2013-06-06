package com.uikit.coreElements;

import com.uikit.utils.TextUtil;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class SystemFont extends UikitFont {

    private int fontFace = Font.getDefaultFont().getFace();
    private Font systemFont;
    private int vCenterOffset;

    public SystemFont(int iFontFace, int iStyle, int iSize) {
        super(iStyle, iSize, false, false);

        if (iFontFace != Font.FACE_MONOSPACE && iFontFace != Font.FACE_PROPORTIONAL
                && iFontFace != Font.FACE_SYSTEM) {
            throw new IllegalArgumentException();
        }

        this.fontFace = iFontFace;
        systemFont = Font.getFont(iFontFace, iStyle, iSize);

        if (iSize == Font.SIZE_SMALL) {
            vCenterOffset = -getHeight() / 2;
        } else if (iSize == Font.SIZE_MEDIUM) {
            vCenterOffset = -getHeight() / 2;
        } else if (iSize == Font.SIZE_LARGE) {
            vCenterOffset = -getHeight() / 2;
        } else {
            vCenterOffset = -getHeight() / 2;
        }
    }

    public SystemFont() {
        this(Font.getDefaultFont().getFace(), Font.getDefaultFont().getStyle(), Font.getDefaultFont().getSize());
    }

    protected final void notifySubclass_styleChanged(int style) {
        if (style != Font.STYLE_PLAIN && style != Font.STYLE_BOLD
                && style != Font.STYLE_ITALIC && style != Font.STYLE_UNDERLINED
                && style != (Font.STYLE_BOLD | Font.STYLE_ITALIC)
                && style != (Font.STYLE_BOLD | Font.STYLE_UNDERLINED)
                && style != (Font.STYLE_ITALIC | Font.STYLE_UNDERLINED)
                && style != (Font.STYLE_BOLD | Font.STYLE_ITALIC | Font.STYLE_UNDERLINED)) {
            throw new IllegalArgumentException();
        }

        systemFont = Font.getFont(fontFace, style, size);
    }

    protected final void notifySubclass_sizeChanged(int size) {
        if (size != Font.SIZE_SMALL && size != Font.SIZE_MEDIUM && size != Font.SIZE_LARGE) {
            throw new IllegalArgumentException();
        }

        systemFont = Font.getFont(fontFace, style, size);
    }

    public final boolean isBold() {
        return ((style & Font.STYLE_BOLD) != 0);
    }

    public final boolean isItalic() {
        return ((style & Font.STYLE_ITALIC) != 0);
    }

    public final boolean isPlain() {
        return (style == 0);
    }

    public final boolean isUnderlined() {
        return ((style & Font.STYLE_UNDERLINED) != 0);
    }

    public final UikitFont clone() {
        return new SystemFont(fontFace, style, size);
    }

    public final void drawChar(Graphics g, char ch, int iX, int iY, int iAnchor) {
        if (g == null) {
            throw new IllegalArgumentException();
        }

        g.setFont(systemFont);
        if ((iAnchor & Graphics.VCENTER) != 0) {
            g.drawChar(ch, iX, iY + vCenterOffset, ((iAnchor ^ Graphics.VCENTER) | Graphics.TOP));
        } else {
            g.drawChar(ch, iX, iY, iAnchor);
        }
    }

    public final void drawChars(Graphics g, char[] chs, int iOffset, int iLength, int iX, int iY, int iAnchor) {
        if (g == null) {
            throw new IllegalArgumentException();
        }

        g.setFont(systemFont);
        if ((iAnchor & Graphics.VCENTER) != 0) {
            g.drawChars(chs, iOffset, iLength, iX, iY + vCenterOffset, ((iAnchor ^ Graphics.VCENTER) | Graphics.TOP));
        } else {
            g.drawChars(chs, iOffset, iLength, iX, iY, iAnchor);
        }
    }

    public final void drawString(Graphics g, String str, int iX, int iY, int iAnchor) throws IllegalArgumentException {
        if (g == null) {
            throw new IllegalArgumentException();
        }

        g.setFont(systemFont);
        if ((iAnchor & Graphics.VCENTER) != 0) {
            g.drawString(str, iX, iY + vCenterOffset, ((iAnchor ^ Graphics.VCENTER) | Graphics.TOP));
        } else {
            g.drawString(str, iX, iY, iAnchor);
        }
    }

    public final void drawSubstring(Graphics g, String str, int iOffset, int iLength, int iX, int iY, int iAnchor) throws IllegalArgumentException {
        if (g == null) {
            throw new IllegalArgumentException();
        }

        g.setFont(systemFont);
        if ((iAnchor & Graphics.VCENTER) != 0) {
            g.drawSubstring(str, iOffset, iLength, iX, iY + vCenterOffset, ((iAnchor ^ Graphics.VCENTER) | Graphics.TOP));
        } else {
            g.drawSubstring(str, iOffset, iLength, iX, iY, iAnchor);
        }
    }

    public final int charWidth(char ch) {
        return systemFont.charWidth(ch);
    }

    public final int charsWidth(char[] ch, int offset, int length) {
        return systemFont.charsWidth(ch, offset, length);
    }

    public final int getBaselinePosition() {
        return systemFont.getBaselinePosition();
    }

    public final int stringWidth(String str) {
        return systemFont.stringWidth(str);
    }

    public final int substringWidth(String str, int iOffset, int iLength) {
        return systemFont.substringWidth(str, iOffset, iLength);
    }

    public final int getHeight() {
        return systemFont.getHeight();
    }

    public Image drawStringToImage(String str, int colour) {
        return TextUtil.generateTransparentTextImage(str, this, colour);
    }
    
}
