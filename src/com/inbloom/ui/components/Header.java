package com.inbloom.ui.components;

import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.uikit.motion.MotionLinear;
import com.uikit.coreElements.Component;
//#if QVGA || WQVGA || QVGA_ADS
//# import com.inbloom.utils.Utils;
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.coreElements.UikitFont;
import com.uikit.painters.PatchPainter;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Header extends Component {

    private Image imgTitle; 
    //#if QVGA || WQVGA || QVGA_ADS
//#     private BitmapFont 
    //#elif WVGA
//#     private int fontColour;
//#     private SystemFont
    //#endif 
    font;
    private int x_coor, y_coor;
    private Image imgBg;

    public Header(int iWidth, Image imgBg, String title, UikitFont font) {
        super(iWidth, imgBg.getHeight());
        this.imgBg = imgBg;
        
        //#if QVGA || WQVGA || QVGA_ADS
//#         this.font = (BitmapFont) font;
        //#elif WVGA
//#         this.font = (SystemFont) font;
        //#endif 
        
        //#if QVGA || WQVGA || QVGA_ADS
//#         this.imgTitle = this.font.drawStringToImage(title);
//#         int txtColor = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MENU_TEXT_COLOR));
//#         this.imgTitle = Utils.replaceColor(imgTitle, txtColor);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MENU_TEXT_COLOR));
//#         this.imgTitle = font.drawStringToImage(title, fontColour);
        //#endif 

        x_coor = (iWidth - imgTitle.getWidth()) / 2;
        y_coor = (iHeight - font.getHeight()) / 2;
        updateBg();
    }

    protected void drawCurrentFrame(Graphics g) {
        g.drawImage(imgTitle, x_coor, y_coor, 20);
    }

    public void setTitle(String title) {
        //#if QVGA || WQVGA || QVGA_ADS
//#         this.imgTitle = font.drawStringToImage(title);
//#         int txtColor = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MENU_TEXT_COLOR));
//#         imgTitle = Utils.replaceColor(imgTitle, txtColor);
        //#elif WVGA
//#         this.imgTitle = font.drawStringToImage(title, fontColour);
        //#endif 
        
        x_coor = (iWidth - imgTitle.getWidth()) / 2;

    }

    public void setBgImage(Image imgBg) {
        this.imgBg = imgBg;
        updateBg();
    }
    
    private void updateBg(){
        getStyle(true).addRenderer(new PatchPainter(imgBg, 0, 5, 0, 5));
    }

    public void enter() {
        MotionLinear mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, x, 0, 10, 0.0f, 0.0f);
        this.motionFX = mfx_slide;
    }

    public void exit() {
        MotionLinear mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, x, -iHeight, 10, 0.0f, 0.0f);
        this.motionFX = mfx_slide;
    }
}
