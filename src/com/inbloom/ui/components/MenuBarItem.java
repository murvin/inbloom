package com.inbloom.ui.components;

import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;

import com.uikit.coreElements.Component;
import com.uikit.coreElements.ITouchEventListener;

//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//# import com.inbloom.utils.Utils;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MenuBarItem extends Component implements ITouchEventListener {

    private int id;
    //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#     private BitmapFont 
    //#elif WVGA
//#     private int fontColour;
//#     private SystemFont
    //#endif 
            font;
    private int x_coor, y_coor;
    private boolean isLsk;
    private Image imgText;

    public MenuBarItem(int height, boolean isLsk) {
        super(0, height);
        this.isLsk = isLsk;
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#         Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_LARGE);
//#         font = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_LARGE, 0);
        //#elif WVGA
//#         font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MENU_TEXT_COLOR));
        //#endif 
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setText(String text) {
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#         imgText = font.drawStringToImage(text);
//#         int txtColor = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MENU_TEXT_COLOR));
//#         imgText = Utils.replaceColor(imgText, txtColor);
        //#elif WVGA
//#         imgText = font.drawStringToImage(text, fontColour);
        //#endif 
        
        iWidth = imgText.getWidth() * 2;
        setSize(iWidth, iHeight);
        x_coor = isLsk ? 0 : iWidth - imgText.getWidth();
        y_coor = (iHeight - font.getHeight()) / 2;
    }

    protected void drawCurrentFrame(Graphics g) {
        if (this.imgText != null) {
            g.drawImage(imgText, x_coor, y_coor, 20);
        }
    }

    private void updateListener() {
        if (cel != null) {
            cel.onComponentEvent(this.getContainingPanel(), id, null, -1);
        }
    }

    public boolean onPress(int type, int iX, int iY) {
        if (type == ITouchEventListener.SINGLE_PRESS) {
            updateListener();
            return true;
        }
        return false;
    }
    

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        return true;
    }
}
