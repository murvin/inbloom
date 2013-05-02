package com.uikit.animations;

import com.uikit.utils.TextWrapper;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.Style;
import com.uikit.styles.TextStyle;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class UikitTextBox extends Component {

    private String string;
    private String[] strings;
    private Image[] images;
    private int iAlign = UikitConstant.LEFT;
    private int yCoor = 0;
    private int xCoor = 0;
    private int a = 0;
    private int absX;
    private int absY;
    private boolean bTrim = true;
    private boolean bRemoveWhitespaces = true;
    private boolean bRemoveEndLines = true;
    private boolean bMemorySafe = true;
    private boolean bUseImagesOnly = false;
    private TextStyle textStyle;
    private boolean multiline = true;
    private boolean bClean = false;

    public UikitTextBox() {
        this(null, null, null);
    }

    public UikitTextBox(int iWidth) {
        this(0, 0, iWidth, null, null, null);
    }

    public UikitTextBox(int iWidth, String string) {
        this(0, 0, iWidth, string, null, null);
    }

    public UikitTextBox(int iWidth, String string, ComponentStyle style) {
        this(0, 0, iWidth, string, null, style);
    }

    public UikitTextBox(int iWidth, String string, TextStyle textStyle) {
        this(0, 0, iWidth, string, textStyle, null);
    }

    public UikitTextBox(String string) {
        this(string, null, null);
    }

    public UikitTextBox(String string, TextStyle textStyle, ComponentStyle style) {
        super(0, 0, 0, 0);
        if (textStyle == null) {
            textStyle = TextStyle.getDefaultTextStyle();
        }
        this.textStyle = textStyle;
        this.textStyle.addComponent(this);
        setStyle(style);
        this.string = string;
        if (string != null && !string.equals("")) {
            strings = new String[]{string};
            iWidth = textStyle.getFont().stringWidth(string) + ((style == null) ? 0 : style.getPadding(ComponentStyle.LEFT) + style.getPadding(ComponentStyle.RIGHT));
            calculateHeight();
        }
        multiline = false;
        iAlign = textStyle.getAlign();
    }

    public UikitTextBox(int iX, int iY, int iWidth, String string, TextStyle textStyle, ComponentStyle style) {
        super(iX, iY, iWidth, 0);
        if (textStyle == null) {
            textStyle = TextStyle.getDefaultTextStyle();
        }
        this.textStyle = textStyle;
        this.textStyle.addComponent(this);
        setStyle(style);
        this.string = string;
        if (string != null && !string.equals("")) {
            rewrap();
            calculateHeight();
        }
        iAlign = textStyle.getAlign();
    }

    public UikitTextBox(int iX, int iY, int iWidth, String string, TextStyle textStyle, ComponentStyle style, boolean removeWhitespaces, boolean removeEndLines, boolean memorySafe, boolean useImagesOnly, boolean trim) {
        super(iX, iY, iWidth, 0);
        if (textStyle == null) {
            textStyle = TextStyle.getDefaultTextStyle();
        }
        this.textStyle = textStyle;
        this.textStyle.addComponent(this);
        setStyle(style);
        this.string = string;
        this.bRemoveWhitespaces = removeWhitespaces;
        this.bRemoveEndLines = removeEndLines;
        this.bMemorySafe = memorySafe;
        this.bTrim = trim;
        this.bUseImagesOnly = useImagesOnly;
        if (string != null && !string.equals("")) {
            rewrap();
            calculateHeight();
        }
        iAlign = textStyle.getAlign();
    }

    public void setSize(int iWidth, int iHeight) {
        setWidth(iWidth);
    }

    public void setWidth(int iWidth) {
        if (iWidth != this.iWidth) {
            rewrap();
            calculateHeight();
        }
        super.setSize(iWidth, iHeight);
    }

    public synchronized void rewrap() {
        if (!multiline) {
            strings = new String[]{string};
            iWidth = textStyle.getFont().stringWidth(string) + ((getStyle() == null) ? 0 : getStyle().getPadding(ComponentStyle.LEFT) + getStyle().getPadding(ComponentStyle.RIGHT));
            if (string != null && !string.equals("")) {
                if (textStyle.getFont().isUseImagesOnly()) {
                    images = new Image[1];
                } else {
                    if (bUseImagesOnly) {
                        images = new Image[1];
                    }
                }
            } else {
                images = null;
            }
            return;
        }
        if (iWidth <= 0) {
            return;
        }
        if (string != null && !string.equals("")) {

            strings = null;
            images = null;
            int w = iWidth;
            if (getStyle() != null) {
                w -= getStyle().getPadding(ComponentStyle.LEFT);
                w -= getStyle().getPadding(ComponentStyle.RIGHT);
            }
            TextWrapper.Line[] ss = TextWrapper.wrapText(string, textStyle.getFont(), w, bRemoveWhitespaces, bRemoveEndLines);

            if (textStyle.getFont().isUseImagesOnly()) {
                images = new Image[ss.length];
                strings = new String[ss.length];
                for (int i = 0; i < ss.length; i++) {
                    strings[i] = bTrim ? ss[i].getString().trim() : ss[i].getString();
                }
            } else {
                strings = new String[ss.length];
                for (int i = 0; i < ss.length; i++) {
                    strings[i] = bTrim ? ss[i].getString().trim() : ss[i].getString();
                }
                if (bUseImagesOnly) {
                    images = new Image[ss.length];
                }
            }
        } else {
            strings = null;
            images = null;
        }
    }

    protected void animationRestarted(boolean bNeverStartedYet) {
    }

    protected void drawCurrentFrame(Graphics g) {
        yCoor = 0;
        boolean bPadding = false;
        if (getStyle() != null && getStyle().hasPadding()) {
            yCoor = getStyle().getPadding(ComponentStyle.TOP);
            bPadding = true;
        }
        if (iAlign == UikitConstant.LEFT) {
            if (bPadding) {
                xCoor = getStyle().getPadding(ComponentStyle.LEFT);
            } else {
                xCoor = 0;
            }
            a = Graphics.TOP | Graphics.LEFT;
        } else if (iAlign == UikitConstant.HCENTER) {
            if (bPadding) {
                xCoor = (iWidth - getStyle().getPadding(ComponentStyle.RIGHT) - getStyle().getPadding(ComponentStyle.LEFT)) / 2;
            } else {
                xCoor = iWidth / 2;
            }
            a = Graphics.TOP | Graphics.HCENTER;
        } else if (iAlign == UikitConstant.RIGHT) {
            if (bPadding) {
                xCoor = iWidth - getStyle().getPadding(ComponentStyle.RIGHT);
            } else {
                xCoor = iWidth;
            }
            a = Graphics.TOP | Graphics.RIGHT;
        }
        synchronized (this) {
            if (images != null) {
                {
                    try {
                        absX = getAbsoluteX();
                        absY = getAbsoluteY();
                    } catch (IllegalArgumentException ex) {
                        return;
                    }
                    if (absX + iWidth > 0 && absX < UiKitDisplay.getWidth() && absY + iHeight > 0 && absY < UiKitDisplay.getHeight()) {
                        for (int i = 0; i < images.length; i++) {
                            if (absY + yCoor + textStyle.getFont().getHeight() >= 0 && absY + yCoor < UiKitDisplay.getHeight()) {
                                if (images[i] == null) {
                                    images[i] = textStyle.getFont().drawStringToImage(strings[i], textStyle.getFontColour());
                                }
                                g.drawImage(images[i], xCoor, yCoor, a);
                            }
                            yCoor += textStyle.getFont().getHeight() + textStyle.getLineSpacing();
                        }
                    }
                }
            } else if (strings != null) {
                try {
                    absX = getAbsoluteX();
                    absY = getAbsoluteY();
                } catch (IllegalArgumentException ex) {
                    return;
                }
                if (absX + iWidth > 0 && absX < UiKitDisplay.getWidth() && absY + iHeight > 0 && absY < UiKitDisplay.getHeight()) {
                    g.setColor(textStyle.getFontColour());
                    for (int i = 0; i < strings.length; i++) {
                        textStyle.getFont().drawString(g, strings[i], xCoor, yCoor, a);
                        yCoor += textStyle.getFont().getHeight() + textStyle.getLineSpacing();
                    }
                }
            }
        }
    }

    protected boolean animate() {
        synchronized (this) {
            if (images != null) {
                yCoor = 0;
                try {
                    absX = getAbsoluteX();
                    absY = getAbsoluteY();
                } catch (IllegalArgumentException ex) {
                    return false;
                }
                if (absX + iWidth > 0 && absX < UiKitDisplay.getWidth() && absY + iHeight > 0 && absY < UiKitDisplay.getHeight()) {
                    bClean = true;
                    for (int i = 0; i < images.length; i++) {
                        if (absY + yCoor + textStyle.getFont().getHeight() >= 0 && absY + yCoor < UiKitDisplay.getHeight()) {
                            if (images[i] == null) {
                                images[i] = textStyle.getFont().drawStringToImage(strings[i], textStyle.getFontColour());
                            }
                        } else {
                            if (images[i] != null && bMemorySafe) {
                                images[i] = null;
                            }
                        }
                        yCoor += textStyle.getFont().getHeight() + textStyle.getLineSpacing();
                    }
                } else {
                    if (bMemorySafe && bClean) {
                        for (int i = 0; i < images.length; i++) {
                            if (images[i] != null) {
                                images[i] = null;
                            }
                        }
                        bClean = false;
                    }
                }
            }
        }
        return false;
    }

    public void setText(String string) {
        if (string == null) {
            throw new IllegalArgumentException();
        }
        if (this.string == null || !this.string.equals(string)) {
            this.string = string;
            rewrap();
            calculateHeight();
        }
    }

    public String getText() {
        return string;
    }

    public void setTextStyle(TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException();
        }
        boolean bRewrap = false;
        boolean bRedraw = false;
        boolean bResize = false;
        if (this.textStyle.getLineSpacing() != textStyle.getLineSpacing()) {
            bResize = true;
        }
        if (this.textStyle.getFont() != textStyle.getFont()) {
            bRewrap = true;
            bResize = true;
        }
        if (this.textStyle.getFontColour() != textStyle.getFontColour()) {
            bRedraw = true;
        }
        this.textStyle.removeComponent(this);
        this.textStyle = textStyle;
        textStyle.addComponent(this);
        if (bRewrap) {
            rewrap();
        }
        if (bResize) {
            calculateHeight();
        }
        synchronized (this) {
            if (bRedraw) {
                if (bUseImagesOnly) {
                    images = new Image[strings.length];
                    if (!bMemorySafe) {
                        for (int i = 0; i < images.length; i++) {
                            images[i] = textStyle.getFont().drawStringToImage(strings[i], textStyle.getFontColour());
                        }
                    }
                }
            }
        }
        iAlign = textStyle.getAlign();
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setStyle(ComponentStyle style) {
        int w = iWidth;
        if (getStyle() != null) {
            w -= getStyle().getPadding(ComponentStyle.LEFT);
            w -= getStyle().getPadding(ComponentStyle.RIGHT);
        }
        int w1 = iWidth;
        if (style != null) {
            w1 -= style.getPadding(ComponentStyle.LEFT);
            w1 -= style.getPadding(ComponentStyle.RIGHT);
        }
        super.setStyle(style);
        if (w1 != w) {
            rewrap();
        }
        calculateHeight();
    }

    private void calculateHeight() {
        if (strings == null || strings.length == 0) {
            iHeight = textStyle.getFont().getHeight();
            return;
        }
        if (getStyle() != null) {
            if (strings != null) {
                iHeight = strings.length * textStyle.getFont().getHeight() + textStyle.getLineSpacing() * (strings.length - 1) + getStyle().getPadding(ComponentStyle.TOP) + getStyle().getPadding(ComponentStyle.BOTTOM);
            } else if (images != null) {
                iHeight = images.length * textStyle.getFont().getHeight() + textStyle.getLineSpacing() * (strings.length - 1) + getStyle().getPadding(ComponentStyle.TOP) + getStyle().getPadding(ComponentStyle.BOTTOM);
            }
        } else {
            if (strings != null) {
                iHeight = strings.length * textStyle.getFont().getHeight() + textStyle.getLineSpacing() * (strings.length - 1);
            } else if (images != null) {
                iHeight = images.length * textStyle.getFont().getHeight() + textStyle.getLineSpacing() * (strings.length - 1);
            }
        }
    }

    public void setRemoveEndLines(boolean removeEndLines) {
        this.bRemoveEndLines = removeEndLines;
        rewrap();
        calculateHeight();
    }

    public void setRemoveWhitespaces(boolean removeWhitespaces) {
        this.bRemoveWhitespaces = removeWhitespaces;
        rewrap();
        calculateHeight();
    }

    public String[] getWrappedStrings() {
        return strings;
    }

    public void setUseImagesOnly(boolean useImagesOnly, boolean memorySafe) {
        synchronized (this) {
            if (bUseImagesOnly && !useImagesOnly) {
                images = null;
            }
            bUseImagesOnly = useImagesOnly;
            bMemorySafe = memorySafe;
        }
    }

    public void setStyle(Style style) {
        super.setStyle(style);
        if (style instanceof TextStyle) {
            setTextStyle((TextStyle) style);
        }
    }

    public void notifyStyleChanged(Style style) {
        rewrap();
        calculateHeight();
        synchronized (this) {
            if (style instanceof TextStyle && bUseImagesOnly) {
                images = new Image[strings.length];
                if (!bMemorySafe) {
                    for (int i = 0; i < images.length; i++) {
                        images[i] = textStyle.getFont().drawStringToImage(strings[i], textStyle.getFontColour());
                    }
                }
            }
        }
    }

    protected void notifyContainerSizeChanged() {
        super.notifyContainerSizeChanged();
        rewrap();
        calculateHeight();
    }

    public boolean isRemoveEndLines() {
        return bRemoveEndLines;
    }

    public boolean isRemoveWhitespaces() {
        return bRemoveWhitespaces;
    }

    public boolean isMemorySafe() {
        return bMemorySafe;
    }

    public boolean isUseImagesOnly() {
        return bUseImagesOnly;
    }

    public void setTrim(boolean bTrim) {
        this.bTrim = bTrim;
        rewrap();
    }

    public boolean isTrim() {
        return bTrim;
    }
}
