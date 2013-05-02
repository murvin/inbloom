package com.inbloom.ui.components;

import com.inbloom.model.Date;
//#if WVGA
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
//#endif 
import com.uikit.animations.UikitTextBox;
import com.uikit.painters.PatchPainter;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.IComponentEventListener;
import com.uikit.coreElements.IFocusable;
import com.uikit.coreElements.ITouchEventListener;
import com.uikit.coreElements.IUikitInputHandler;
import com.uikit.coreElements.Panel;
import com.uikit.coreElements.UikitFont;
import com.uikit.styles.TextStyle;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class EntryItem extends Panel implements IFocusable, ITouchEventListener, IUikitInputHandler {

    private boolean isOnFocus;
    private PatchPainter onFocusHighlight;
    private String title, value;
    private Image img_icon, img_arrow;
    private int x_icon, x_arrow, y_icon, y_arrow, y_font;
    private UikitFont font;
    private int padding;
    private UikitTextBox btbValue;
    private int entryId;
    private int[] selectedIndices;
    private Date date;
    //#if WVGA
    private int fontColour;
    //#endif

    public EntryItem(int iWidth, int iHeight,
            PatchPainter onFocusHighLight,
            String title,
            String value,
            Image img_icon,
            Image img_arrow,
            UikitFont font,
            IComponentEventListener listener,
            int entryId,
            int[] selectedIndices) {
        super(iWidth, iHeight);
        this.onFocusHighlight = onFocusHighLight;
        this.title = title;
        this.value = value;
        this.img_icon = img_icon;
        this.img_arrow = img_arrow;
        this.font = font;
        this.cel = listener;
        this.entryId = entryId;
        this.selectedIndices = selectedIndices;

        padding = 2 * UiKitDisplay.getWidth() / 100;
        calcPos();
        
        //#if WVGA
        fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
        //#endif 
    }

    public void addBasicTextBox(String value) {
        TextStyle style = new TextStyle(font);
        btbValue = new UikitTextBox((x_arrow - x_icon), value, style);
        setSize(iWidth, (padding * 2) + btbValue.getHeight());
        btbValue.x = iWidth / 2;
        btbValue.y = (iHeight - btbValue.getHeight()) / 2;
        addComponent(btbValue);
        calcPos();
    }
    
    public String getValue(){
        return this.value;
    }
    
    public void setValue(String value){
        this.value = value;
    }

    private void calcPos() {
        x_icon = (iWidth - (img_icon == null ? 0 : img_icon.getWidth())) / 2;
        y_font = (iHeight - font.getHeight()) / 2;
        y_icon = img_icon == null ? y_font : (iHeight - img_icon.getHeight()) / 2;

        x_arrow = iWidth - img_arrow.getWidth() - padding - padding;
        y_arrow = (iHeight - img_arrow.getHeight()) / 2;
    }

    protected void drawCurrentFrame(Graphics g) {
        if (title != null) {
            //#if WVGA
            g.setColor(fontColour);
            //#endif 
            font.drawString(g, title, padding * 2, y_font, 20);
        }

        if (img_icon != null) {
            g.drawImage(img_icon, x_icon, y_icon, 20);
        }

        if (value != null) {
            //#if WVGA
            g.setColor(fontColour);
            //#endif 
            font.drawString(g, value, x_icon + (img_icon == null ? 0 : img_icon.getWidth() + padding), y_font, 20);
        } else {
            super.drawCurrentFrame(g);
        }

        if (img_arrow != null) {
            g.drawImage(img_arrow, x_arrow, y_arrow, 20);
        }

        if (isOnFocus) {
            onFocusHighlight.paint(this, g);
        }
    }

    private void updateCel() {
        if (cel != null) {
            cel.onComponentEvent(this, this.entryId, new Object[]{title, new Integer(entryId), selectedIndices}, -1);
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
        if (type == ITouchEventListener.SINGLE_PRESS) {
            updateCel();
            return true;
        }
        return false;
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return false;
    }

    public boolean onKeyPressed(int iKeyCode) {
        return false;
    }

    public boolean onKeyReleased(int iKeyCode) {
        int key = UiKitDisplay.getGameAction(iKeyCode);
        if (key == Canvas.FIRE) {
            updateCel();
            return true;
        }
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return false;
    }

    public int[] getSelectedIndices() {
        return selectedIndices;
    }
    
    public void setDate(Date date){
        this.date = date;
    }
    
    public Date getDate(){
        return this.date;
    }
}
