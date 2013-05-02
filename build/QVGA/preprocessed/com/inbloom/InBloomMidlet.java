package com.inbloom;

import javax.microedition.midlet.*;

public class InBloomMidlet extends MIDlet {

    public void startApp() {

        InBloomController controller = new InBloomController();
        controller.init(this);
        controller.navigateScreen(InBloomController.SCREEN_LOGIN, false, new Boolean(true));
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
