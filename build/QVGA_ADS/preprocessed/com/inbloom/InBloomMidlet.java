package com.inbloom;

//#if QVGA_ADS
import com.uikit.coreElements.UiKitDisplay;
import javax.microedition.lcdui.Display;
//#endif 
import javax.microedition.midlet.*;

public class InBloomMidlet extends MIDlet {

    public void startApp() {
        //#if QVGA_ADS
        DisplayableAdvert displayable = new DisplayableAdvert(this);
        Display.getDisplay(this).setCurrent(displayable);
        //#else
//#         InBloomController controller = new InBloomController();
//#         controller.init(this);
//#         controller.navigateScreen(InBloomController.SCREEN_LOGIN, false, new Boolean(true));
        //#endif 
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
