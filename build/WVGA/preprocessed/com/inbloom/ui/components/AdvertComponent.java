package com.inbloom.ui.components;

//#if QVGA_ADS
//# import com.inbloom.AdManager;
//# import com.inbloom.utils.GraphicsResources;
//# import com.inbloom.utils.Resources;
//# import com.uikit.coreElements.IFocusable;
//# import com.uikit.coreElements.ITouchEventListener;
//# import com.uikit.painters.BgColorPainter;
//# import com.uikit.painters.BorderPainter;
//# import javax.microedition.lcdui.Image;
//#endif

import javax.microedition.midlet.MIDlet;
import java.util.Hashtable;
import com.uikit.coreElements.Panel;

public class AdvertComponent extends Panel 
//#if QVGA_ADS
//# implements AdManager.IAdListener, IFocusable , ITouchEventListener
//#endif
{
    
//#if QVGA_ADS
//#     private AdManager adManager;
//#     
//#     private final String APP_ID = "DestinyTechnologies_inBloom_Nokia"; 
//#     
//#     // Resource variables
//#     private int colorBg, colorBgHigh, colorBorder;
//#     
//#     private BgColorPainter painterBg;
//#     
//#     private BorderPainter painterBorder;
//#     
//#endif 

    public AdvertComponent(int w, int h, MIDlet appMidlet, Hashtable optionalParams) {
        super(w, h);
        //#if QVGA_ADS
//#         init(appMidlet, APP_ID, optionalParams);
        //#endif
    }
    
    
    //#if QVGA_ADS
//#     private void initResources(){
//#         colorBg = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_AD_BG_COLOR));
//#         colorBgHigh = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_AD_BG_COLOR_HIGH));
//#         colorBorder = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_AD_BORDER_COLOR));
//#     }
//# 
//#     private void init(MIDlet appMidlet, String appID, Hashtable optionalParams) {
//#         initResources();
//#         adManager = new AdManager(this, appMidlet, appID, optionalParams);
//#         getStyle(true).addRenderer(painterBg = new BgColorPainter(colorBg));
//#         getStyle().addRenderer(painterBorder = new BorderPainter());
//#         painterBorder.setBorderColor(colorBorder);
//#         painterBorder.setBorderSize(1);
//#     }
//# 
//#     // IAdListener methods
//#     public void downloadAd() {
//#         adManager.downloadAd();
//#     }
//# 
//#     public void onAdDownloaded(Image image, String clickUrl) {
//#         // TODO animate advert change
//#     }
//# 
//#     public void onAdDownloadError() {
//#     }
//# 
//#     public void onAdDownloadStart() {
//#     }
//# 
//#     // IFocusable methods
//#     public boolean isFocused() {
//#         return true;
//#     }
//# 
//#     public void onFocus() {
//#         painterBg.setBgColor(colorBgHigh);
//#     }
//# 
//#     public void onDefocus() {
//#         painterBg.setBgColor(colorBg);
//#         painterBorder.setBorderColor(colorBorder);
//#     }
//# 
//#     public boolean onPress(int type, int x, int y) {
//#         if (type == ITouchEventListener.SINGLE_PRESS) {
//#             return true;
//#         }
//#         return false;
//#     }
//# 
//#     public boolean onDrag(int type, int startX, int startY, int deltaX, int deltaY) {
//#         return true;
//#     }
//#     
    //#endif
}
