package com.uikit.animations;

import com.uikit.utils.UikitConstant;
import com.uikit.styles.ComponentStyle;
import com.uikit.coreElements.IEnableable;
import com.uikit.coreElements.IFocusable;
import com.uikit.coreElements.IUikitInputHandler;
import com.uikit.coreElements.Panel;
import com.uikit.styles.Style;
import com.uikit.styles.TextStyle;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.UikitCanvas;
import com.uikit.coreElements.SystemFont;
import com.uikit.coreElements.UikitFont;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class UikitButton extends Panel implements IFocusable, IUikitInputHandler, IEnableable, ITouchEventListener {

    public static final int STATE_DISABLED = 0;
    public static final int STATE_ENABLED = 1;
    public static final int STATE_PRESSED = 2;
    public static final int STATE_FOCUSED = 3;
    private static final int TOTAL_STATES = 4;
    public static final int EVENT_PRESSED = 0;
    public static final int EVENT_RELEASED = 1;
    public static final int EVENT_FOCUSED = 2;
    public static final int EVENT_DEFOCUSED = 3;
    public static final int COMP_SELF = 0;
    public static final int COMP_TEXT = 1;
    protected int id;
    protected int iCurState = STATE_ENABLED;
    protected UikitTextBox textBox;
    protected ComponentStyle[] styles = new ComponentStyle[TOTAL_STATES];
    protected TextStyle[] textStyles = new TextStyle[TOTAL_STATES];
    private boolean bAutoSize = true;
    protected boolean bFocused = false;
    protected boolean bEnabled = true;
    private String label;

    public UikitButton(int iX, int iY, int iWidth, int iHeight, String label, int id) {
        super(iX, iY, iWidth, iHeight);

        addDefaultStyles();

        this.label = label;
        this.id = id;

        setStyle(styles[iCurState]);
        textBox = new UikitTextBox(iWidth, label, textStyles[iCurState]);

        textBox.setRelWidth(100);
        textBox.useRelativeSize(UikitConstant.HORIZONTAL);
        addComponent(textBox);
        textBox.setPosition(Graphics.VCENTER | Graphics.HCENTER);
    }

    public UikitButton() {
        this(0, 0, 70, 40, " ", 0);
    }

    public UikitButton(String label, int id) {
        this(0, 0, 70, 40, label, id);
    }

    public void onFocus() {
        if (bEnabled && !bFocused) {
            bFocused = true;
            iCurState = STATE_FOCUSED;
            applyCurrentStyleSet();
            if (cel != null) {
                cel.onComponentEvent(this, EVENT_FOCUSED, null, id);
            }
        }
    }

    public void onDefocus() {
        if (bEnabled && bFocused) {
            bFocused = false;
            iCurState = STATE_ENABLED;
            applyCurrentStyleSet();
            if (cel != null) {
                cel.onComponentEvent(this, EVENT_DEFOCUSED, null, id);
            }
        }
    }

    public void onRelease() {
        if (bEnabled) {
            if (bFocused) {
                iCurState = STATE_FOCUSED;
                applyCurrentStyleSet();
            } else {
                iCurState = STATE_ENABLED;
                applyCurrentStyleSet();
            }
            if (cel != null) {
                cel.onComponentEvent(this, EVENT_RELEASED, null, id);
            }
        }
    }

    protected void enable(boolean bEnabled) {
        if (this.bEnabled == bEnabled) {
            return;
        }
        this.bEnabled = bEnabled;
        if (bEnabled) {
            iCurState = STATE_ENABLED;
            applyCurrentStyleSet();
        } else {
            iCurState = STATE_DISABLED;
            applyCurrentStyleSet();
        }
    }

    protected void setStyleSet(ComponentStyle style, TextStyle textStyle) {
        boolean bResizeNeeded = false;
        if (textBox != null) {
            //set the text box's style and dimensions
            int iW = textBox.getWidth();
            int iH = textBox.getHeight();
            textBox.setTextStyle(textStyle);
            if (bAutoSize) {
                if (iW != textBox.getWidth() || iH != textBox.getHeight()) {
                    bResizeNeeded = true;
                }
            }
        }
        //set the overall component's style and dimensions
        setStyle(style);
        if (bResizeNeeded) {
            int w = textBox.getWidth();
            int h = textBox.getHeight();
            if (style != null) {
                w += style.getPadding(ComponentStyle.LEFT) + style.getPadding(ComponentStyle.LEFT);
                h += style.getPadding(ComponentStyle.TOP) + style.getPadding(ComponentStyle.BOTTOM);
            }
            setSize(w, h, false);
        }
        //Text on a button should always be centered
        if (textBox != null) {
            textBox.setPosition(UikitConstant.VCENTER | UikitConstant.HCENTER);
        }
    }

    private void addStyleSet(int state, ComponentStyle style, TextStyle textStyle) {
        if (state > STATE_FOCUSED || state < 0) {
            throw new IllegalArgumentException();
        }
        styles[state] = style;
        if (textStyle != null) {
            textStyles[state] = textStyle;
        } else {
            textStyles[state] = TextStyle.getDefaultTextStyle();
        }
        if (state == iCurState) {
            setStyleSet(styles[state], textStyles[state]);
        }
    }

    public void setStyle(int componentId, int state, Style style) {
        if (state > STATE_FOCUSED || state < 0) {
            throw new IllegalArgumentException();
        }
        switch (componentId) {
            case COMP_SELF:
                if (!(style instanceof ComponentStyle)) {
                    throw new IllegalArgumentException();
                }
                styles[state] = (ComponentStyle) style;
                break;
            case COMP_TEXT:
                if (!(style instanceof TextStyle)) {
                    throw new IllegalArgumentException();
                }
                if (style == null) {
                    style = TextStyle.getDefaultTextStyle();
                }
                textStyles[state] = (TextStyle) style;
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (state == iCurState) {
            setStyleSet(styles[state], textStyles[state]);
        }
    }

    public boolean isFocused() {
        return bFocused;
    }

    public boolean isEnabled() {
        return bEnabled;
    }

    private void addDefaultStyles() {
        TextStyle textStyle;
        ComponentStyle style;


        UikitFont font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

        textStyle = new TextStyle(font);
        textStyle.setFontColour(0x888888);
        textStyle.setAlign(UikitConstant.HCENTER);

        style = new ComponentStyle();
        style.setPadding(5);

        addStyleSet(STATE_DISABLED, style, textStyle);

        textStyle = new TextStyle(font);
        textStyle.setFontColour(0x333333);
        textStyle.setAlign(UikitConstant.HCENTER);

        style = new ComponentStyle();
        style.setPadding(5);

        addStyleSet(STATE_ENABLED, style, textStyle);

        textStyle = new TextStyle(font);
        textStyle.setFontColour(0xffffff);
        textStyle.setAlign(UikitConstant.HCENTER);

        style = new ComponentStyle();
        style.setPadding(5);

        addStyleSet(STATE_FOCUSED, style, textStyle);

        textStyle = new TextStyle(font);
        textStyle.setFontColour(0xffffff);
        textStyle.setAlign(UikitConstant.HCENTER);

        style = new ComponentStyle();
        style.setPadding(5);

        addStyleSet(STATE_PRESSED, style, textStyle);

    }

    public boolean onKeyPressed(int iKeyCode) {
        if (UikitCanvas.getGameActionNoAlt(iKeyCode) == Canvas.FIRE && bEnabled) {
            iCurState = STATE_PRESSED;
            applyCurrentStyleSet();
            if (cel != null) {
                cel.onComponentEvent(this, EVENT_PRESSED, null, id);
            }
            return true;
        }
        return false;
    }

    public boolean onKeyReleased(int iKeyCode) {
        if (UikitCanvas.getGameActionNoAlt(iKeyCode) == Canvas.FIRE && bEnabled) {
            onRelease();
            return true;
        }
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        textBox.setText(label);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void enable() {
        enable(true);
    }

    public void disable() {
        enable(false);
    }

    public boolean onPress(int type, int iX, int iY) {
        if (bEnabled) {
            if (type == ITouchEventListener.SINGLE_PRESS) {
                if (cel != null) {
                    cel.onComponentEvent(this, EVENT_PRESSED, null, id);
                }
                return true;
            } else if (type == ITouchEventListener.TOUCH_DOWN) {
                iCurState = STATE_PRESSED;
                applyCurrentStyleSet();
                return true;
            } else if (type == ITouchEventListener.TOUCH_RELEASE) {
                onRelease();
                return true;
            }
        }
        return false;
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return false;
    }

    private void applyCurrentStyleSet() {
        setStyleSet(styles[iCurState], textStyles[iCurState]);
    }

    public void setIsAutoResize(boolean isAutoResize) {
        this.bAutoSize = isAutoResize;
    }
}
