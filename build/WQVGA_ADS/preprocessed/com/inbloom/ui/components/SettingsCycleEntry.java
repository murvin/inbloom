package com.inbloom.ui.components;

//#if WVGA
//# import com.inbloom.utils.GraphicsResources;
//# import com.inbloom.utils.Resources;
//#endif 
import com.uikit.animations.UikitTextBox;
import com.uikit.animations.UikitTextInput;
import com.uikit.painters.PatchPainter;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.IFocusable;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.IUikitInputHandler;
import com.uikit.coreElements.Panel;
import com.uikit.coreElements.UikitFont;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.TextStyle;
import com.uikit.textinputhandler.NativeTextInputHandler;

import javax.microedition.lcdui.Graphics;

public class SettingsCycleEntry extends Panel implements IFocusable, ITouchEventListener, IUikitInputHandler {

    private String title, description;
    private String value;
    private UikitFont titleFont, descFont;
    private TextStyle titleStyle, descStyle;
    private ComponentStyle[] styles;
    private boolean isLongVal;
    private UikitTextInput bti_value;
    private int fieldType;
    private boolean isOnFocus;
    private PatchPainter painter;
    private int padding;

    public SettingsCycleEntry(int width, String title, String description, String value, UikitFont titleFont, UikitFont descFont, ComponentStyle[] styles, boolean isLongVal, int fieldType, PatchPainter renderer) {
        super(width, 0);
        this.title = title;
        this.description = description;
        this.value = value;
        this.titleFont = titleFont;
        this.descFont = descFont;
        this.isLongVal = isLongVal;
        this.fieldType = fieldType;
        this.painter = renderer;

        initResources();
        this.styles = styles;
        initComponents();

        getStyle(true).setPadding(padding, 0, padding, 0);
        expandToFitContent();

    }
    

    private void initResources() {
        titleStyle = new TextStyle(titleFont);
        descStyle = new TextStyle(descFont);
        
        //#if WVGA
//#         int fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         titleStyle.setFontColour(fontColour);
//#         descStyle.setFontColour(fontColour);
        //#endif 
        padding = 4 * UiKitDisplay.getWidth() / 100;
    }

    public String getValue() {
        return bti_value.getText();
    }

    private void initComponents() {
        int w = (iWidth - padding - padding);
        UikitTextBox btb_title = new UikitTextBox(isLongVal ? w * 50 / 100 : w * 80 / 100, title, titleStyle);
        btb_title.x = padding;
        addComponent(btb_title);

        UikitTextBox btb_desc = new UikitTextBox(isLongVal ? w * 50 / 100 : w * 80 / 100, description, descStyle);
        btb_desc.y = btb_title.y + btb_title.getHeight();
        btb_desc.x = padding;
        addComponent(btb_desc);

        bti_value = new UikitTextInput(isLongVal ? w * 50 / 100 : w * 20 / 100, w * 12 / 100, new NativeTextInputHandler(title, 1024, fieldType), false, false);
        bti_value.setStyle(UikitTextInput.COMP_SELF, UikitTextInput.STATE_ENABLED, styles[0]);
        bti_value.setStyle(UikitTextInput.COMP_SELF, UikitTextInput.STATE_FOCUSED, styles[0]);
        bti_value.setStyle(UikitTextInput.COMP_TEXTBOXTEXT, UikitTextInput.STATE_ENABLED, descStyle);

        ComponentStyle txtBoxStyle = new ComponentStyle();
        txtBoxStyle.setPadding(5, 0, 0, 5);
        bti_value.setStyle(UikitTextInput.COMP_TEXTBOX, UikitTextInput.STATE_ENABLED, txtBoxStyle);
        bti_value.setStyle(UikitTextInput.COMP_TEXTBOX, UikitTextInput.STATE_FOCUSED, txtBoxStyle);

        if (this.value != null) {
            bti_value.setText(this.value);
        }
        bti_value.x = btb_title.getWidth() + padding;
        addComponent(bti_value);
    }

    protected void drawCurrentFrame(Graphics g) {
        super.drawCurrentFrame(g);
        if (isOnFocus) {
            painter.paint(this, g);
        }
    }

    public void onFocus() {
        isOnFocus = true;
    }

    public void onDefocus() {
        isOnFocus = false;
    }

    public boolean isFocused() {
        return isOnFocus;
    }

    public boolean onPress(int type, int iX, int iY) {
        return bti_value.onPress(type, iX, iY);
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return bti_value.onDrag(type, iStartX, iStartY, iDeltaX, iDeltaY);
    }

    public boolean onKeyPressed(int iKeyCode) {
        return bti_value.onKeyPressed(iKeyCode);
    }

    public boolean onKeyReleased(int iKeyCode) {
        return bti_value.onKeyReleased(iKeyCode);
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return bti_value.onKeyRepeated(iKeyCode);
    }
}
