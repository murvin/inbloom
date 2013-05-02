package com.uikit.coreElements;

import com.uikit.utils.ImageUtil;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class BitmapFont extends UikitFont {

    private int iH;
    private int iW;
    private int iBaseline;
    private int[] widths;
    private int[] positions;
    private int[] sa;
    private char[] lookUp;
    private String chars;

    public BitmapFont(Image bitmapFont, String chars, int style, int size, int baselinePosition) {
        super(style, size, true, false);
        if (chars == null || chars.equals("")) {
            throw new IllegalArgumentException();
        }
        if (bitmapFont == null) {
            throw new IllegalArgumentException();
        }
        buildFont(bitmapFont, chars, baselinePosition);
    }

    private void buildFont(Image bitmapFont, String chars, int baselinePosition) {
        this.chars = chars;
        iBaseline = baselinePosition - 1;
        iH = bitmapFont.getHeight();
        iW = bitmapFont.getWidth();

        int[] ds = new int[iW];
        bitmapFont.getRGB(ds, 0, iW, 0, 0, iW, 1);
        int d = ds[0];
        int c = 0;
        for (int i = 0; i < ds.length; i++) {
            if (ds[i] != d) {
                c++;
            }
        }

        if (c != chars.length()) {
            throw new IllegalArgumentException();
        }


        Vector ext = new Vector();
        loop:
        for (int i = 0; i < chars.length(); i++) {
            char ch = chars.charAt(i);
            if (ch > 255) {
                boolean bAdded = false;
                for (int j = 0; j < ext.size(); j++) {
                    if (ch == ((Character) ext.elementAt(j)).charValue()) {
                        bAdded = true;
                        continue loop;
                    }
                    if (ch > ((Character) ext.elementAt(j)).charValue()) {
                        ext.insertElementAt(new Character(ch), j);
                        bAdded = true;
                        break;
                    }
                }
                if (!bAdded) {
                    ext.addElement(new Character(ch));
                }
            }
        }

        lookUp = new char[ext.size()];
        for (int i = 0; i < lookUp.length; i++) {
            lookUp[i] = ((Character) ext.elementAt(i)).charValue();
        }

        ext = null;

        widths = new int[256 + lookUp.length];
        positions = new int[256 + lookUp.length];

        for (int i = 0; i < widths.length; i++) {
            widths[i] = -1;
            positions[i] = -1;
        }

        int j = 0;
        char ch = chars.charAt(j);

        if (ch <= 255) {
            positions[ch] = 0;
        } else {
            int pos = 256;
            for (int k = 0; k < lookUp.length; k++) {
                if (ch == lookUp[k]) {
                    pos = k + 256;
                    break;
                }
            }
            positions[pos] = 0;
        }

        for (int i = 0; i < ds.length; i++) {
            if (ds[i] != d) {

                if (ch <= 255) {
                    widths[ch] = i - positions[ch] + 1;
                } else {
                    int pos = 256;
                    for (int k = 0; k < lookUp.length; k++) {
                        if (ch == lookUp[k]) {
                            pos = k + 256;
                            break;
                        }
                    }
                    widths[pos] = i - positions[pos] + 1;
                }

                j++;
                if (j < chars.length()) {
                    ch = chars.charAt(j);
                    if (ch <= 255) {
                        positions[ch] = i + 1;
                    } else {
                        int pos = 256;
                        for (int k = 0; k < lookUp.length; k++) {
                            if (ch == lookUp[k]) {
                                pos = k + 256;
                                break;
                            }
                        }
                        positions[pos] = i + 1;
                    }
                }
            }
        }

        if (positions[32] == -1) {
            widths[32] = 5;
        }

        sa = new int[iW * iH];
        bitmapFont.getRGB(sa, 0, iW, 0, 0, iW, iH);
    }

    public BitmapFont() {
        super(Font.getDefaultFont().getStyle(), Font.getDefaultFont().getSize(), true, false);
    }

    public Image drawCharsToImage(char[] chars, int iOffset, int iLength) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        if (iOffset < 0 || iOffset > chars.length - 1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (iLength <= 0) {
            throw new IllegalArgumentException();
        }

        int pos = 32;
        boolean bFound;
        int x = 0;

        int width = charsWidth(chars, iOffset, iLength);
        int[] da = new int[width * getHeight()];

        for (int j = iOffset; j < ((iOffset + iLength > chars.length) ? (chars.length) : (iOffset + iLength)); j++) {
            int c = chars[j];

            if (c <= 255) {
                if (positions[c] == -1) {
                    c = 32;
                    pos = 32;
                } else {
                    pos = c;
                }
            } else {
                bFound = false;
                for (int k = 0; k < lookUp.length; k++) {
                    if (c == lookUp[k]) {
                        bFound = true;
                        pos = k + 256;
                        break;
                    }
                }
                if (!bFound) {
                    c = 32;
                    pos = 32;
                }
            }

            if (c == 32) {
                if (positions[32] == -1) {
                    x += widths[pos];
                    continue;
                }
            }

            ImageUtil.copyRGB(da, sa, x, 0, width, getHeight(), positions[pos], 1, widths[pos], iH - 1, iW);
            x += widths[pos];
        }

        return Image.createRGBImage(da, width, getHeight(), true);
    }

    public Image drawStringToImage(String string) {
        return drawCharsToImage(string.toCharArray(), 0, string.length());
    }

    public Image drawSubstringToImage(String string, int iOffset, int iLength) {
        return drawCharsToImage(string.toCharArray(), iOffset, iLength);
    }

    public Image drawCharToImage(char ch) {
        return drawCharsToImage(new char[]{ch}, 0, 1);
    }

    public int getStringWidth(String string) {
        int w = 0;
        int pos = 32;
        boolean bFound;
        for (int i = 0; i < string.length(); i++) {
            int c = (int) string.charAt(i);
            if (c <= 255) {
                if (positions[c] == -1) {
                    pos = 32;
                } else {
                    pos = c;
                }
            } else {
                bFound = false;
                for (int k = 0; k < lookUp.length; k++) {
                    if (c == lookUp[k]) {
                        bFound = true;
                        pos = k + 256;
                        break;
                    }
                }
                if (!bFound) {
                    pos = 32;
                }
            }
            w += widths[pos];
        }
        return w;
    }

    protected void notifySubclass_styleChanged(int iStyle) {
    }

    protected void notifySubclass_sizeChanged(int iSize) {
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

    public UikitFont clone() {
        return new BitmapFont(Image.createRGBImage(sa, iW, iH, true), chars, style, size, iBaseline + 1);
    }

    public int charWidth(char ch) {
        if (ch <= 255) {
            if (widths[ch] == -1) {
                return widths[32];
            } else {
                return widths[ch];
            }
        } else {
            for (int j = 0; j < lookUp.length; j++) {
                if (ch == lookUp[j]) {
                    return widths[j + 256];
                }
            }
            return widths[32];
        }
    }

    public int charsWidth(char[] chs, int iOffset, int iLength) {
        int w = 0;
        for (int i = iOffset; i < ((iOffset + iLength > chs.length) ? (chs.length) : (iOffset + iLength)); i++) {
            w += charWidth(chs[i]);
        }
        return w;
    }

    public int getBaselinePosition() {
        return iBaseline;
    }

    public int stringWidth(String str) {
        int w = 0;
        for (int i = 0; i < str.length(); i++) {
            w += charWidth(str.charAt(i));
        }
        return w;
    }

    public int substringWidth(String str, int iOffset, int iLength) {
        int w = 0;
        for (int i = iOffset; i < ((iOffset + iLength > str.length()) ? (str.length()) : (iOffset + iLength)); i++) {
            w += charWidth(str.charAt(i));
        }
        return w;
    }

    public int getHeight() {
        return iH - 1;
    }

    public void drawChar(Graphics g, char ch, int iX, int iY, int iAnchor) {
        g.drawImage(drawStringToImage(ch + ""), iX, iY, iAnchor);
    }

    public void drawChars(Graphics g, char[] chs, int iOffset, int iLength, int iX, int iY, int iAnchor) {
        drawSubstring(g, String.valueOf(chs), iOffset, iLength, iX, iY, iAnchor);
    }

    public void drawString(Graphics g, String str, int iX, int iY, int iAnchor) {
        Image img = drawStringToImage(str);
        if (img != null) {
            g.drawImage(img, iX, iY, iAnchor);
        }

    }

    public void drawSubstring(Graphics g, String str, int iOffset, int iLength, int iX, int iY, int iAnchor) {
        g.drawImage(drawStringToImage(str.substring(iOffset, iLength)), iX, iY, iAnchor);
    }

    public Image drawStringToImage(String str, int colour) {
        Image img = drawStringToImage(str);
        return img = ImageUtil.replaceColor(img, colour);
    }
}
