package com.uikit.utils;

import com.uikit.coreElements.UikitFont;
import java.util.Vector;

public class TextWrapper {

    private StringBuffer string;
    private UikitFont font;
    private boolean removeWhiteSpaces = true;
    private boolean removeEndLines = true;
    private boolean lastCharSpace = false;

    public TextWrapper(String string, UikitFont font) {
        if (string == null) {
            throw new IllegalArgumentException();
        }
        if (font == null) {
            throw new IllegalArgumentException();
        }
        this.string = new StringBuffer(string);
        this.font = font;
    }

    public TextWrapper() {
    }

    public Line getNextLine(int width) {
        return getNextLine(width, true);
    }

    public Line getNextLine(int width, boolean bForceWrapping) {
        if (width <= 0) {
            throw new IllegalArgumentException();
        }
        if (width <= font.charWidth(string.charAt(0))) {
            throw new IllegalArgumentException();
        }
        int x = 0;
        int index = 0;
        int lastWorldBreak = -1;
        int lastWorldBreakWidth = 0;

        while (x < width) {
            if (index < string.length()) {
                if (string.charAt(index) == ' ') {
                    if ((removeWhiteSpaces && lastCharSpace) || (removeWhiteSpaces && index == 0)) {
                        string.deleteCharAt(index);
                        continue;
                    }
                    lastWorldBreak = index;
                    lastWorldBreakWidth = x;
                    lastCharSpace = true;
                } else {
                    lastCharSpace = false;
                }
                if (string.charAt(index) == '-') {
                    lastWorldBreak = index;
                    lastWorldBreakWidth = x;
                }

                if (string.charAt(index) == '\r') {
                    if (removeWhiteSpaces) {
                        if (index > 0 && string.charAt(index - 1) == ' ') {
                            lastCharSpace = true;
                        }
                        if (removeEndLines) {
                            string.setCharAt(index, ' ');
                            continue;
                        }
                    } else if (removeEndLines) {
                        string.deleteCharAt(index);
                    }
                    if (string.length() > index + 1 && string.charAt(index + 1) == '\n') {
                        string.deleteCharAt(index + 1);
                    }
                    Line line = new Line(getStringBuffersStart(string, index), x);
                    string.delete(0, index + 1);
                    return line;
                } else if (string.charAt(index) == '\n') {
                    if (removeWhiteSpaces) {
                        if (index > 0 && string.charAt(index - 1) == ' ') {
                            lastCharSpace = true;
                        }
                        if (removeEndLines) {
                            string.setCharAt(index, ' ');
                            continue;
                        }
                    } else if (removeEndLines) {
                        string.deleteCharAt(index);
                    }
                    Line line = new Line(getStringBuffersStart(string, index), x);
                    string.delete(0, index + 1);
                    return line;
                }


                if (font.isStringBasedWidth()) {
                    x = font.stringWidth(string.toString().substring(0, index + 1));
                } else {
                    x += font.charWidth(string.charAt(index));
                }

                if (x >= width) {
                    if (lastWorldBreak == -1) {
                        if (bForceWrapping) {
                            lastWorldBreak = index - 1;
                            x -= font.charWidth(string.charAt(index));
                            lastWorldBreakWidth = x;
                        } else {
                            return null;
                        }
                    }
                    break;
                }
                index++;
            } else {
                // last line
                Line line = new Line(string.toString(), x);
                string.delete(0, index + 1);
                return line;
            }
        }
        Line line = new Line(getStringBuffersStart(string, lastWorldBreak + 1), lastWorldBreakWidth);
        string.delete(0, lastWorldBreak + 1);
        lastCharSpace = false;
        return line;
    }

    private static String getStringBuffersStart(StringBuffer stringBuffer, int index) {
        char[] ch = new char[index <= stringBuffer.length() ? index : stringBuffer.length()];
        stringBuffer.getChars(0, ch.length, ch, 0);
        return new String(ch);
    }

    public boolean hasNextLine() {
        if (string.length() > 0) {
            return true;
        }
        return false;
    }

    public void setString(String string) {
        if (string == null) {
            throw new IllegalArgumentException();
        }
        this.string = new StringBuffer(string);
    }

    public void setFont(UikitFont font) {
        if (font == null) {
            throw new IllegalArgumentException();
        }
        this.font = font;
    }

    public void setRemoveWhiteSpaces(boolean removeWhiteSpaces) {
        this.removeWhiteSpaces = removeWhiteSpaces;
    }

    public void setRemoveEndLines(boolean removeEndLines) {
        this.removeEndLines = removeEndLines;
    }

    public static Line[] wrapText(String string, UikitFont font, int width, boolean removeWhiteSpaces, boolean removeEndLines) {
        if (font == null) {
            throw new IllegalArgumentException();
        }
        if (string == null) {
            throw new IllegalArgumentException();
        }
        if (width <= 0) {
            throw new IllegalArgumentException();
        }
        TextWrapper wrapper = new TextWrapper(string, font);
        wrapper.removeWhiteSpaces = removeWhiteSpaces;
        wrapper.removeEndLines = removeEndLines;
        Vector vElements = new Vector();
        while (wrapper.hasNextLine()) {
            vElements.addElement(wrapper.getNextLine(width));
        }
        Line[] l = new Line[vElements.size()];
        vElements.copyInto(l);
        return (Line[]) l;
    }

    public class Line {

        String string;
        int width;

        public Line(String string, int width) {
            this.string = string;
            this.width = width;
        }

        public String getString() {
            return string;
        }

        public int getWidth() {
            return width;
        }
    }
}
