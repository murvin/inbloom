package com.uikit.animations;

import com.uikit.painters.BgColorPainter;
import com.uikit.painters.BorderPainter;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.IEnableable;
import com.uikit.coreElements.IFocusable;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.IUikitInputHandler;
import com.uikit.coreElements.Panel;
import com.uikit.coreElements.UikitFont;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.Style;
import com.uikit.styles.TextStyle;
import com.uikit.textinputhandler.ITextInput;
import com.uikit.textinputhandler.ITextInputHandler;
import javax.microedition.lcdui.Graphics;

public class UikitTextInput extends Panel implements ITextInput, IUikitInputHandler, IFocusable, IEnableable, ITouchEventListener {

    public static final int STATE_ENABLED = 0;
    public static final int STATE_FOCUSED = 1;
    public static final int STATE_DISABLED = 2;
    public static final int COMP_SELF = 0;
    public static final int COMP_TEXTBOXTEXT = 1;
    public static final int COMP_INDICATOR = 2;
    public static final int COMP_INDICATORTEXT = 3;
    public static final int COMP_TEXTBOX = 4;
    public static final int SHOW_SPEC_CHARS = 4;
    private StringBuffer text = new StringBuffer("");
    private UikitTextBox textBox;
    private UikitTextBox indicator;
    private int iCurrentCaretPosition = 0;
    private int iCurrentCaretRow = 0;
    private int iCurrentCaretCol = 0;
    private int iCaretX = 0;
    private int iCaretY = 0;
    private int iCurrentInsertMode = INSERT_MODE_ADD;
    private int caretColour = 0x000000;
    private char lastChar = ' ';
    private int iFontHeight = 10;
    private int iBlinkTime = 500;
    private long iLastBlink = System.currentTimeMillis();
    private boolean bBlink = true;
    private boolean bFocused = false;
    private boolean bEnabled = true;
    private boolean bMultiline = false;
    private ITextInputHandler textInputHandler;
    private ITextInputHandler primaryInputHandler;
    private ITextInputHandler secondaryInputHandler;
    private int iCurState = STATE_ENABLED;
    private static boolean bStylesInited = false;
    private ComponentStyle[] mainStyles;
    private TextStyle[] mainTextStyles;
    private ComponentStyle[] indicatorStyles;
    private TextStyle[] indicatorTextStyles;
    private ComponentStyle[] textBoxStyles;
    private boolean bPassword = false;
    private StringBuffer pass = new StringBuffer("");
    private char passChar = '*';
    private boolean bDisplayCursor = true;
    private boolean bDisplayIndicator = true;
    private String indicatorString = "abc";

    public UikitTextInput(int iWidth, int iHeight, ITextInputHandler textInputHandler, boolean bMultiline, boolean bPassword) {
        super(iWidth, iHeight);

        if (textInputHandler == null) {
            throw new IllegalArgumentException();
        }

        this.bMultiline = bMultiline;
        this.bPassword = bPassword;
        this.primaryInputHandler = textInputHandler;
        this.textInputHandler = textInputHandler;
        textInputHandler.registerTextInputBox(this);

        addDefaultStyles();

        if (bMultiline) {
            textBox = new UikitTextBox(iWidth, text.toString());
            textBox.setRemoveWhitespaces(false);
            textBox.setRemoveEndLines(false);
            textBox.setTrim(false);
        } else {
            textBox = new UikitTextBox(text.toString(), null, null);
            textBox.setRemoveWhitespaces(false);
            textBox.setRemoveEndLines(true);
            textBox.setTrim(false);
        }

        addComponent(textBox);


        indicator = new UikitTextBox(50);
        setIndicatorString(indicatorString);


        bDisplayCursor = textInputHandler.isDisplayCursor();
        bDisplayIndicator = textInputHandler.isDisplayIndicator();

        applyCurrentStyleSet();

        textBox.y = getStyle() == null ? 3 : getStyle().getPadding(ComponentStyle.TOP);

        int tpt = 0;
        int tpl = 0;

        if (textBox.getStyle() != null) {
            tpt = textBox.getStyle().getPadding(ComponentStyle.TOP);
            tpl = textBox.getStyle().getPadding(ComponentStyle.LEFT);
        }

        iCaretX = tpl;
        iCaretY = tpt;
    }

    private void addDefaultStyles() {


        mainStyles = new ComponentStyle[3];
        mainTextStyles = new TextStyle[3];
        textBoxStyles = new ComponentStyle[3];

        indicatorStyles = new ComponentStyle[3];
        indicatorTextStyles = new TextStyle[3];

        ComponentStyle style;
        ComponentStyle textBoxStyle;
        TextStyle textStyle;

        textBoxStyle = new ComponentStyle();
        textBoxStyle.setPadding(2, 5, 2, 5);

        //main component styles
        //enabled
        style = new ComponentStyle();
        style.addRenderer(new BgColorPainter(0xffffff));
        BorderPainter br = new BorderPainter();
        br.setBorderSize(1);
        br.setBorderColor(0x333333);
        style.addRenderer(br);
        style.setPadding(2);

        textStyle = new TextStyle();
        textStyle.setFontColour(0x333333);
        mainStyles[STATE_ENABLED] = style;
        mainTextStyles[STATE_ENABLED] = textStyle;
        textBoxStyles[STATE_ENABLED] = textBoxStyle;

        //fousedstyle = new ComponentStyle();
        style = new ComponentStyle();
        style.addRenderer(new BgColorPainter(0xffffff));
        br = new BorderPainter();
        br.setBorderSize(2);
        br.setBorderColor(0xff6600);
        style.addRenderer(br);
        style.setPadding(2);

        textStyle = textStyle.copy();

        mainStyles[STATE_FOCUSED] = style;
        mainTextStyles[STATE_FOCUSED] = textStyle;
        textBoxStyles[STATE_FOCUSED] = textBoxStyle;


        //disabled
        style = new ComponentStyle();
        style.addRenderer(new BgColorPainter(0xffffff));
        br = new BorderPainter();
        br.setBorderSize(1);
        br.setBorderColor(0x888888);
        style.addRenderer(br);
        style.setPadding(2);

        textStyle = textStyle.copy();
        textStyle.setFontColour(0x888888);

        mainStyles[STATE_DISABLED] = style;
        mainTextStyles[STATE_DISABLED] = textStyle;
        textBoxStyles[STATE_DISABLED] = textBoxStyle;




        style = new ComponentStyle();
        style.addRenderer(new BgColorPainter(0xff6600));

        textStyle = new TextStyle();
        textStyle.setFontColour(0xffffff);

        indicatorStyles[STATE_FOCUSED] = style;
        indicatorTextStyles[STATE_FOCUSED] = textStyle;

        textStyle = textStyle.copy();

        indicatorStyles[STATE_ENABLED] = style;
        indicatorTextStyles[STATE_ENABLED] = textStyle;

        textStyle = textStyle.copy();

        indicatorStyles[STATE_DISABLED] = style;
        indicatorTextStyles[STATE_DISABLED] = textStyle;

    }

    public void insertChar(char ch) {
        if (!bMultiline && (ch == '\n' || ch == '\r')) {
            return;
        }
        if (iCurrentInsertMode == INSERT_MODE_REPLACE) {
            text.deleteCharAt(iCurrentCaretPosition - 1);
            if (bPassword) {
                pass.deleteCharAt(iCurrentCaretPosition - 1);
            }
            if (iCurrentCaretPosition - 1 >= text.length()) {
                text.append(ch);
                if (bPassword) {
                    pass.append(ch);
                }
            } else {
                text.insert(iCurrentCaretPosition - 1, ch);
                if (bPassword) {
                    pass.insert(iCurrentCaretPosition - 1, ch);
                }
            }
        } else {
            if (iCurrentCaretPosition >= text.length()) {
                text.append(ch);
                if (bPassword) {
                    pass.append(ch);
                }
            } else {
                text.insert(iCurrentCaretPosition, ch);
                if (bPassword) {
                    pass.insert(iCurrentCaretPosition, ch);
                }
            }
            iCurrentCaretPosition++;
        }
        if (!bPassword) {
            textBox.setText(text.toString());
        } else {
            textBox.setText(pass.toString());
        }

        findCaretPosition();
        lastChar = ch;
    }

    public boolean deleteChar() {
        if (iCurrentCaretPosition <= 0) {
            findCaretPosition();
            return false;
        }
        text.deleteCharAt(iCurrentCaretPosition - 1);
        if (bPassword) {
            pass.deleteCharAt(iCurrentCaretPosition - 1);
        }
        iCurrentCaretPosition--;
        if (!bPassword) {
            textBox.setText(text.toString());
        } else {
            textBox.setText(pass.toString());
        }
        findCaretPosition();
        return true;
    }

    private void findCaretPosition() {
        String[] strings = textBox.getWrappedStrings();
        int paddingTop = 0;
        int paddingBottom = 0;
        int paddingRight = 0;
        int paddingLeft = 0;

        if (getStyle() != null) {
            ComponentStyle style = getStyle();
            paddingTop = style.getPadding(ComponentStyle.TOP);
            paddingBottom = style.getPadding(ComponentStyle.BOTTOM);
            paddingLeft = style.getPadding(ComponentStyle.LEFT);
            paddingRight = style.getPadding(ComponentStyle.RIGHT);
        }

        int tpt = 0;
        int tpl = 0;

        if (textBox.getStyle() != null) {
            tpt = textBox.getStyle().getPadding(ComponentStyle.TOP);
            tpl = textBox.getStyle().getPadding(ComponentStyle.LEFT);
        }

        if (strings == null || strings.length == 0) {
            iCurrentCaretPosition = 0;
            iCaretX = tpl;
            iCaretY = tpt;
            iCurrentCaretCol = 0;
            iCurrentCaretRow = 0;
            return;
        }

        //iterate to find caret position
        iCurrentCaretRow = 0;
        iCurrentCaretCol = iCurrentCaretPosition;
        for (int i = 0; i < strings.length; i++) {
            if (iCurrentCaretCol <= strings[i].length()) {
                iCaretY = iCurrentCaretRow * textBox.getTextStyle().getFont().getHeight() + tpt;
                if (iCurrentCaretCol == 0) {
                    iCaretX = 0 + tpl;
                } else {
                    iCaretX = textBox.getTextStyle().getFont().substringWidth(strings[i], 0, iCurrentCaretCol) + tpl;
                }
                break;
            } else {
                iCurrentCaretRow++;
                iCurrentCaretCol -= strings[i].length();
            }
        }

        // position textBox
        if (bMultiline) {
            if (iCaretY + textBox.getTextStyle().getFont().getHeight() + textBox.y > iHeight - paddingBottom) {
                do {
                    textBox.y -= textBox.getTextStyle().getFont().getHeight() + textBox.getTextStyle().getLineSpacing();
                } while (iCaretY + textBox.getTextStyle().getFont().getHeight() + textBox.y > iHeight - paddingBottom);
            } else if (iCaretY + textBox.y < paddingTop) {
                do {
                    textBox.y += textBox.getTextStyle().getFont().getHeight() + textBox.getTextStyle().getLineSpacing();
                } while (iCaretY + textBox.y < paddingTop);
                if (textBox.y > paddingTop) {
                    textBox.y = paddingTop;
                }
            }
            if (textBox.y + textBox.getHeight() < iHeight - paddingBottom && textBox.getHeight() > iHeight - paddingTop - paddingBottom) {
                textBox.y = iHeight - textBox.getHeight() - paddingBottom;
            } else if (textBox.getHeight() < iHeight - paddingTop - paddingBottom) {
                textBox.y = paddingTop;
            }
        } else {
            if (iCaretX + textBox.x + 1 > iWidth - paddingRight) {
                do {
                    textBox.x -= iWidth / 2;
                } while (iCaretX + textBox.x + 1 > iWidth - paddingRight);
            } else if (iCaretX + textBox.x < paddingLeft) {
                do {
                    textBox.x += iWidth / 2;
                } while (iCaretX + textBox.x < paddingLeft);
                if (textBox.x > paddingLeft) {
                    textBox.x = paddingLeft;
                }
            }
        }
    }

    protected boolean animate() {
        super.animate();
        if (System.currentTimeMillis() - iLastBlink > iBlinkTime) {
            bBlink = !bBlink;
            iLastBlink = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    protected void drawCurrentFrame(Graphics g) {


        int paddingTop = 0;
        int paddingBottom = 0;
        int paddingRight = 0;
        int paddingLeft = 0;

        int cx = g.getClipX();
        int cy = g.getClipY();
        int cw = g.getClipWidth();
        int ch = g.getClipHeight();

        if (getStyle() != null) {
            ComponentStyle style = getStyle();
            paddingTop = style.getPadding(ComponentStyle.TOP);
            paddingBottom = style.getPadding(ComponentStyle.BOTTOM);
            paddingLeft = style.getPadding(ComponentStyle.LEFT);
            paddingRight = style.getPadding(ComponentStyle.RIGHT);
        }

        iFontHeight = textBox.getTextStyle().getFont().getHeight();

        g.clipRect(paddingLeft, paddingTop, iWidth - paddingLeft - paddingRight, iHeight - paddingTop - paddingBottom);
        super.drawCurrentFrame(g);
        g.setClip(cx, cy, cw, ch);

        if (bFocused && bDisplayCursor) {
            g.setColor(textBox.getTextStyle().getFontColour());
            if (iCurrentInsertMode == INSERT_MODE_REPLACE) {
                g.drawLine(textBox.x + iCaretX, textBox.y + iCaretY, textBox.x + iCaretX, textBox.y + iCaretY + iFontHeight);
                g.drawLine(textBox.x + iCaretX - textBox.getTextStyle().getFont().charWidth(lastChar), textBox.y + iCaretY + iFontHeight, textBox.x + iCaretX, textBox.y + iCaretY + iFontHeight);
            } else {
                if (bBlink) {
                    g.drawLine(textBox.x + iCaretX, textBox.y + iCaretY, textBox.x + iCaretX, textBox.y + iCaretY + iFontHeight);
                }
            }
        }

    }

    public boolean onKeyPressed(int iKeyCode) {
        boolean bHandled = false;
        if (textInputHandler != null) {
            bHandled = textInputHandler.onKeyPressed(iKeyCode);
        }
        return bHandled;
    }

    public boolean onKeyReleased(int iKeyCode) {
        boolean bHandled = false;
        if (textInputHandler != null) {
            bHandled = textInputHandler.onKeyReleased(iKeyCode);
        }
        return bHandled;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        boolean bHandled = false;
        if (textInputHandler != null) {
            bHandled = textInputHandler.onKeyRepeated(iKeyCode);
        }
        return bHandled;
    }

   
    public boolean moveCaretLeft() {
        if (iCurrentCaretPosition > 0) {
            iCurrentCaretPosition--;
            findCaretPosition();
            return true;
        }
        return false;
    }

   
    public boolean moveCaretRight() {
        if (iCurrentCaretPosition < text.length()) {
            iCurrentCaretPosition++;
            findCaretPosition();
            return true;
        }
        return false;
    }

   
    public boolean moveCaretUp() {
        String[] strings = textBox.getWrappedStrings();
        if (strings == null) {
            return false;
        }
        if (iCurrentCaretRow > 0) {
            iCurrentCaretRow--;
            iCurrentCaretPosition = 0;
            for (int i = 0; i < iCurrentCaretRow; i++) {
                iCurrentCaretPosition += strings[i].length();
            }
            int w = 0;
            UikitFont font = textBox.getTextStyle().getFont();
            boolean end = false;
            for (int i = 0; i < strings[iCurrentCaretRow].length(); i++) {
                if (w > iCaretX) {
                    end = true;
                    iCurrentCaretPosition += i;
                    break;
                } else {
                    w += font.charWidth(strings[iCurrentCaretRow].charAt(i));
                }
            }
            if (!end) {
                iCurrentCaretPosition += strings[iCurrentCaretRow].length();
            }
            findCaretPosition();
            return true;
        }
        return false;
    }

    public boolean moveCaretDown() {
        String[] strings = textBox.getWrappedStrings();
        if (strings == null) {
            return false;
        }
        if (iCurrentCaretRow < strings.length - 1) {
            iCurrentCaretRow++;
            iCurrentCaretPosition = 0;
            for (int i = 0; i < iCurrentCaretRow; i++) {
                iCurrentCaretPosition += strings[i].length();
            }
            int w = 0;
            UikitFont font = textBox.getTextStyle().getFont();
            boolean end = false;
            for (int i = 0; i < strings[iCurrentCaretRow].length(); i++) {
                if (w > iCaretX) {
                    end = true;
                    iCurrentCaretPosition += i;
                    break;
                } else {
                    w += font.charWidth(strings[iCurrentCaretRow].charAt(i));
                }
            }
            if (!end) {
                iCurrentCaretPosition += strings[iCurrentCaretRow].length();
            }
            findCaretPosition();
            return true;
        }
        return false;
    }

    public void moveCaretToStart() {
        iCurrentCaretPosition = 0;
        findCaretPosition();
    }

    public void moveCaretToEnd() {
        iCurrentCaretPosition = text.length();
        findCaretPosition();
    }

 
    public void setCaretPosition(int position) {
        if (iCurrentCaretPosition >= 0 && iCurrentCaretPosition <= text.length()) {
            iCurrentCaretPosition = position;
        } else if (position < 0) {
            iCurrentCaretPosition = 0;
        } else if (position > text.length()) {
            iCurrentCaretPosition = text.length();
        }
        findCaretPosition();
    }

    public int getCaretPosition() {
        return iCurrentCaretPosition;
    }

    public int getCaretRow() {
        return iCurrentCaretRow;
    }

    public int getCaretCol() {
        return iCurrentCaretCol;
    }

 
    public void setCharInsertMode(int iInsertMode) {
        if (iInsertMode != INSERT_MODE_ADD && iInsertMode != INSERT_MODE_REPLACE) {
            throw new IllegalArgumentException();
        }
        if (iCurrentInsertMode == INSERT_MODE_REPLACE && iInsertMode == INSERT_MODE_ADD) {
            bBlink = false;
            iLastBlink = System.currentTimeMillis();
            if (bPassword) {
                insertChar(passChar);
            }
        }
        this.iCurrentInsertMode = iInsertMode;
    }

 
    public void setIndicatorString(String string) {
        indicatorString = string;
        if (indicator != null) {
            indicator.setText(indicatorString);
        }
    }

  
    public String getText() {
        return text.toString();
    }

    public void setText(String string) {
        text = new StringBuffer(string);
        if (bPassword) {
            pass = new StringBuffer();
            for (int i = 0; i < string.length(); i++) {
                pass.append(passChar);
            }
            textBox.setText(pass.toString());
        } else {
            textBox.setText(string);
        }
        setCaretPosition(0);
    }

    public boolean isEnabled() {
        return bEnabled;
    }

    public void onFocus() {
        if (bFocused) {
            return;
        }
        bFocused = true;
        if (indicator.getContainingCanvas() == null && bDisplayIndicator) {
            UiKitDisplay.getCurrent().addComponent(indicator);
            indicator.setPosition(UikitConstant.TOP | UikitConstant.RIGHT);
        } else if (indicator.getContainingCanvas() != null) {
            indicator.getContainingCanvas().removeComponent(indicator);
        }

        iCurState = STATE_FOCUSED;
        applyCurrentStyleSet();
    }

    public void onDefocus() {
        if (!bFocused) {
            return;
        }
        bFocused = false;

        if (indicator.getContainingCanvas() != null) {
            indicator.getContainingCanvas().removeComponent(indicator);
        }

        iCurState = STATE_ENABLED;
        applyCurrentStyleSet();
    }

   
    public boolean isFocused() {
        return bFocused;
    }

    public void enable() {
        if (bEnabled) {
            return;
        }
        bEnabled = true;
        if (bFocused) {
            iCurState = STATE_FOCUSED;
        } else {
            iCurState = STATE_ENABLED;
        }
        applyCurrentStyleSet();
    }

    public void disable() {
        if (!bEnabled) {
            return;
        }

        bEnabled = false;
        if (indicator.getContainingCanvas() != null) {
            indicator.getContainingCanvas().removeComponent(indicator);
        }

        iCurState = STATE_DISABLED;

        applyCurrentStyleSet();
    }

    public UikitTextBox getIndicator() {
        return indicator;
    }

    public boolean isPassword() {
        return bPassword;
    }

    public boolean isMultiline() {
        return bMultiline;
    }

  
    public void setStyle(int componentId, int state, Style style) {
        if (state < 0 || state > STATE_DISABLED) {
            throw new IllegalArgumentException();
        }
        switch (componentId) {
            case COMP_INDICATOR:
                if (!(style instanceof ComponentStyle)) {
                    throw new IllegalArgumentException();
                }
                indicatorStyles[state] = (ComponentStyle) style;
                break;
            case COMP_INDICATORTEXT:
                if (!(style instanceof TextStyle)) {
                    throw new IllegalArgumentException();
                }
                indicatorTextStyles[state] = (TextStyle) style;
                break;
            case COMP_SELF:
                if (!(style instanceof ComponentStyle)) {
                    throw new IllegalArgumentException();
                }
                mainStyles[state] = (ComponentStyle) style;
                break;
            case COMP_TEXTBOXTEXT:
                if (!(style instanceof TextStyle)) {
                    throw new IllegalArgumentException();
                }
                mainTextStyles[state] = (TextStyle) style;
                break;
            case COMP_TEXTBOX:
                if (!(style instanceof ComponentStyle)) {
                    throw new IllegalArgumentException();
                }
                textBoxStyles[state] = (ComponentStyle) style;
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (state == iCurState) {
            switch (componentId) {
                case COMP_INDICATOR:
                    indicator.setStyle(indicatorStyles[state]);
                    break;
                case COMP_INDICATORTEXT:
                    indicator.setTextStyle(indicatorTextStyles[state]);
                    break;
                case COMP_SELF:
                    setStyle(mainStyles[state]);
                    break;
                case COMP_TEXTBOXTEXT:
                    textBox.setTextStyle(mainTextStyles[state]);
                    break;
                case COMP_TEXTBOX:
                    textBox.setStyle(textBoxStyles[state]);
                    break;
            }
        }
    }

  
    public void showSpecChars() {
        if (cel != null) {
            cel.onComponentEvent(this, SHOW_SPEC_CHARS, null, 0);
        }
    }

    public void switchInputHandler() {
        if (secondaryInputHandler == null) {
            return;
        } else if (textInputHandler == primaryInputHandler && secondaryInputHandler != null) {
            textInputHandler = secondaryInputHandler;
            textInputHandler.init();
        } else {
            textInputHandler = primaryInputHandler;
            textInputHandler.init();
        }
        bDisplayCursor = textInputHandler.isDisplayCursor();
        bDisplayIndicator = textInputHandler.isDisplayIndicator();
        if (indicator.getContainingCanvas() == null && bDisplayIndicator) {
            UiKitDisplay.getCurrent().addComponent(indicator);
            indicator.setPosition(UikitConstant.TOP | UikitConstant.RIGHT);
        } else if (indicator.getContainingCanvas() != null) {
            indicator.getContainingCanvas().removeComponent(indicator);
        }
    }

    public void setSecondaryInputHandler(ITextInputHandler secondaryInputHandler) {
        this.secondaryInputHandler = secondaryInputHandler;
        secondaryInputHandler.registerTextInputBox(this);
    }

    public boolean onPress(int type, int iX, int iY) {
        if (isEnabled()) {
            if (type == TOUCH_DOWN) {
                onFocus();
                return true;
            } else if (type == TOUCH_RELEASE) {
                onDefocus();
                return true;
            } else if (type == SINGLE_PRESS) {
                if (textInputHandler instanceof ITextInputHandler) {
                    textInputHandler.init();
                    bDisplayCursor = textInputHandler.isDisplayCursor();
                    bDisplayIndicator = textInputHandler.isDisplayIndicator();
                    if (indicator.getContainingCanvas() == null && bDisplayIndicator) {
                        UiKitDisplay.getCurrent().addComponent(indicator);
                        indicator.setPosition(UikitConstant.TOP | UikitConstant.RIGHT);
                    } else if (indicator.getContainingCanvas() != null) {
                        indicator.getContainingCanvas().removeComponent(indicator);
                    }
                }
                return true;
            }
        }
        return false;
    }

   
    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return false;
    }

    public ITextInputHandler getCurrentInputHandler() {
        return textInputHandler;
    }


    private void applyCurrentStyleSet() {

        setStyle(mainStyles[iCurState]);
        textBox.setTextStyle(mainTextStyles[iCurState]);
        textBox.setStyle(textBoxStyles[iCurState]);
        indicator.setStyle(indicatorStyles[iCurState]);
        indicator.setTextStyle(indicatorTextStyles[iCurState]);

        int w = iWidth;
        int y = 5;
        int x = 5;
        int br = 0;

        ComponentStyle style = getStyle();
        if (style != null) {
            br = style.getPadding(ComponentStyle.RIGHT);
            w -= style.getPadding(ComponentStyle.LEFT) + style.getPadding(ComponentStyle.RIGHT);
            x = style.getPadding(ComponentStyle.LEFT);
            y = style.getPadding(ComponentStyle.TOP);
        }

        if (bMultiline) {
            textBox.setWidth(w);
            textBox.x = x;
        } else {
            textBox.y = y;
        }
    }
}
