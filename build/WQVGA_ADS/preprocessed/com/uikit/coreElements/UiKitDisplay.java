package com.uikit.coreElements;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.*;

public class UiKitDisplay {

    private static class MainCanvas extends Canvas {

        Image offscreenImg;
        Graphics offscreenImg_g;
        private UikitCanvas curCanvas;
        private int iPrevWidth;
        private int iPrevHeight;
        private boolean bSpecifiedScreenSize;
        private boolean bUpdatingScreen = false;
        private Display display;
        private MIDlet midlet;
        private int iUntouchX, iUntouchY, iUntouchWidth, iUntouchHeight;
        private boolean bHasUntouchRegion = false;

        private void setUntouchedRegion(int x, int y, int width, int height) {
            if (width <= 0 || height <= 0) {
                bHasUntouchRegion = false;
                return;
            }
            bHasUntouchRegion = true;
            iUntouchX = x;
            iUntouchY = y;
            iUntouchWidth = width;
            iUntouchHeight = height;
        }

        MainCanvas(MIDlet midlet, int iSpecifiedWidth, int iSpecifiedHeight) {

            curCanvas = null;
            iPrevWidth = -1;
            iPrevHeight = -1;

            if (iSpecifiedWidth > 0 && iSpecifiedHeight > 0) {
                bSpecifiedScreenSize = true;
            }

            setFullScreenMode(true);
            if (bSpecifiedScreenSize) {
                sizeChangedEx(iSpecifiedWidth, iSpecifiedHeight);
            } else {
                sizeChangedEx(super.getWidth(), super.getHeight());
            }
            this.midlet = midlet;
            display = Display.getDisplay(midlet);
        }

        public final void setFullScreenMode(boolean bFullScreenMode) {
            super.setFullScreenMode(true);
        }

        public final int getWidth() {
            return iPrevWidth;
        }

        public final int getHeight() {
            return iPrevHeight;
        }

        public final void sizeChanged(int iW, int iH) {

            super.sizeChanged(iW, iH);

            sizeChangedEx(iW, iH);

        }

        private void changeToFullScreenSize() {
            sizeChangedEx(super.getWidth(), super.getHeight());
        }

        private void sizeChangedEx(int iWidth, int iHeight) {
            if (iPrevWidth == iWidth && iPrevHeight == iHeight) {
                return;
            }

            if (offscreenImg != null) {
                offscreenImg = null;
                offscreenImg_g = null;
                System.gc();
            }
            offscreenImg = Image.createImage(iWidth, iHeight);
            offscreenImg_g = offscreenImg.getGraphics();

            iPrevWidth = iWidth;
            iPrevHeight = iHeight;

            if (curCanvas != null && curCanvas.getEventListener() != null) {
                curCanvas.getEventListener().screenModeChanged(curCanvas);
            }
        }

        private void refreshScreen() {

            if (curCanvas == null) {
                return;
            }

            if (isShown() && (!curCanvas.isAnimationStopped() || curCanvas.bNeverPaintedYet)) {
                bUpdatingScreen = true;


                repaint();
                serviceRepaints();

                bUpdatingScreen = false;

                if (curCanvas != null) {
                    curCanvas.bNeverPaintedYet = false;
                }

            }
        }

        private void init() {
            if (display == null) {
                display = Display.getDisplay(midlet);
            }
            display.setCurrent(this);
        }

        protected final void paint(Graphics gReal) {
            if (!bUpdatingScreen) {
                return;
            }
            super.setFullScreenMode(true);
            sizeChangedEx(getWidth(), getHeight());

            Graphics g = (bUseDoubleBuffering ? offscreenImg_g : gReal);

            if (curCanvas != null) {
                curCanvas.paint(g);
            }

            if (bUseDoubleBuffering) {
                if (iDisplayMode == DISPLAY_LANDSCAPE) {
                    gReal.drawRegion(offscreenImg, 0, 0, offscreenImg.getWidth(), offscreenImg.getHeight(), Sprite.TRANS_ROT90, 0, 0, Graphics.TOP | Graphics.LEFT);
                } else {
                    if (!bHasUntouchRegion) {
                        gReal.drawImage(offscreenImg, 0, 0, Graphics.LEFT | Graphics.TOP);
                    } else {
                        if (iUntouchY > 0) {
                            gReal.drawRegion(offscreenImg, 0, 0, offscreenImg.getWidth(), iUntouchY, Sprite.TRANS_NONE, 0, 0, Graphics.TOP | Graphics.LEFT);
                        }
                        if (offscreenImg.getHeight() - iUntouchY - iUntouchHeight > 0) {
                            gReal.drawRegion(offscreenImg, 0, iUntouchY + iUntouchHeight, offscreenImg.getWidth(), offscreenImg.getHeight() - iUntouchY - iUntouchHeight, Sprite.TRANS_NONE, 0, iUntouchY + iUntouchHeight, Graphics.TOP | Graphics.LEFT);
                        }
                        if (iUntouchX > 0) {
                            gReal.drawRegion(offscreenImg, 0, iUntouchY, iUntouchX, iUntouchHeight, Sprite.TRANS_NONE, 0, iUntouchY, Graphics.TOP | Graphics.LEFT);
                        }
                        if (offscreenImg.getWidth() - iUntouchX - iUntouchWidth > 0) {
                            gReal.drawRegion(offscreenImg, iUntouchX + iUntouchWidth, iUntouchY, offscreenImg.getWidth() - iUntouchX - iUntouchWidth, iUntouchHeight, Sprite.TRANS_NONE, iUntouchX + iUntouchWidth, iUntouchY, Graphics.TOP | Graphics.LEFT);
                        }
                    }
                }
            }

        }

        /**
         * @exclude just to override
         */
        protected final void showNotify() {
            super.showNotify();

            if (curCanvas != null) {
                AnimationEngine.setDelay(curCanvas.getFrameDelay());
            }
            if (curCanvas != null && curCanvas.getEventListener() != null) {
                curCanvas.getEventListener().showNotify(curCanvas);
            }

        }

        /**
         * @exclude just to override
         */
        protected final void hideNotify() {

            super.hideNotify();

            if (curCanvas != null && curCanvas.getEventListener() != null) {
                curCanvas.getEventListener().hideNotify(curCanvas);
            }

        }

        /**
         * @exclude just to override
         */
        protected final void keyPressed(int iKeyCode) {
            if (curCanvas == null || !curCanvas.isEnabled()) {
                return;
            }
            curCanvas.onKeyPressed(iKeyCode);
        }

        protected final void keyReleased(int iKeyCode) {
            if (curCanvas == null || !curCanvas.isEnabled()) {
                return;
            }
            curCanvas.onKeyReleased(iKeyCode);
        }

        protected final void keyRepeated(int iKeyCode) {
            if (curCanvas == null || !curCanvas.isEnabled()) {
                return;
            }
            curCanvas.onKeyRepeated(iKeyCode);
        }

        protected final void pointerPressed(int iX, int iY) {
            if (curCanvas == null || !curCanvas.isEnabled()) {
                return;
            }
            curCanvas.onPointerPressed(iX, iY);
        }

        protected final void pointerReleased(int iX, int iY) {
            if (curCanvas == null || !curCanvas.isEnabled()) {
                return;
            }
            curCanvas.onPointerReleased(iX, iY);
        }

        protected final void pointerDragged(int iX, int iY) {
            if (curCanvas == null || !curCanvas.isEnabled()) {
                return;
            }
            curCanvas.onPointerDragged(iX, iY);
        }
    }
    public static final int DISPLAY_PORTRAIT = 0x1001;
    public static final int DISPLAY_LANDSCAPE = 0x1002;
    private static int iDisplayMode;
    private static boolean bUseDoubleBuffering;
    public static MIDlet midlet;
    static MainCanvas mainCanvas;

    public static void init(MIDlet midlet) {
        UiKitDisplay.midlet = midlet;
        bUseDoubleBuffering = true; //by default is true

        // create placeholder for UI
        mainCanvas = new MainCanvas(midlet, 0, 0);

    }

    public static void init(MIDlet midlet, int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            throw new IllegalArgumentException("screen width and height cannot be negative");
        }

        UiKitDisplay.midlet = midlet;
        bUseDoubleBuffering = true; //by default is true

        // create placeholder for UI
        mainCanvas = new MainCanvas(midlet, screenWidth, screenHeight);

    }

    public static int getWidth() {
        return mainCanvas.getWidth();
    }

    public static int getHeight() {
        return mainCanvas.getHeight();
    }

    public static void useDoubleBuffering(boolean bUseDoubleBuffering) {
        if (UiKitDisplay.bUseDoubleBuffering == bUseDoubleBuffering) {
            return;
        }

        UiKitDisplay.bUseDoubleBuffering = bUseDoubleBuffering;


        if (!bUseDoubleBuffering) {
            mainCanvas.offscreenImg = null;
            mainCanvas.offscreenImg_g = null;
            System.gc();
        } else if (mainCanvas.offscreenImg == null) {
            mainCanvas.iPrevWidth = mainCanvas.iPrevHeight = -1;
            mainCanvas.changeToFullScreenSize();
        }

    }

    public static Display getDisplay() {
        return Display.getDisplay(UiKitDisplay.midlet);
    }

    public static void setCurrent(UikitCanvas canvas) {
        if (mainCanvas.curCanvas == canvas) {
            return;
        }

        UikitCanvas prevCanvas = mainCanvas.curCanvas;

        if (prevCanvas != null && prevCanvas.getEventListener() != null) {
            prevCanvas.getEventListener().hideNotify(prevCanvas);
        }
        if (canvas != null && canvas.getEventListener() != null) {
            canvas.getEventListener().showNotify(canvas);
        }
        AnimationEngine.setDelay(canvas.getFrameDelay());
        mainCanvas.init();
        mainCanvas.curCanvas = canvas;
        if (canvas.isAnimationStopped()) {
            canvas.startStopAnimation();
        }
        // start animation engine
        AnimationEngine.init();
    }

    public static UikitCanvas getCurrent() {
        return mainCanvas.curCanvas;
    }

    public static void setUntouchedRegion(int x, int y, int width, int height) {
        if (!bUseDoubleBuffering) {
            useDoubleBuffering(true);
        }

        mainCanvas.setUntouchedRegion(x, y, width, height);
    }

    public static void setPortraitLandscapeMode(int _iDisplayMode) {
        if ((_iDisplayMode != DISPLAY_LANDSCAPE) && (_iDisplayMode != DISPLAY_PORTRAIT)) {
            return;
        }

        iDisplayMode = _iDisplayMode;
    }

    public static Displayable getLcduiDisplayable() {
        return (Displayable) mainCanvas;
    }

    public static int getGameAction(int iKeyCode) {
        int key;
        try {
            key = mainCanvas.getGameAction(iKeyCode);
        } catch (IllegalArgumentException iae) {
            key = iKeyCode;
        }
        return key;
    }

    public static void updateScreen(UikitCanvas theCanvas, int iX, int iY, int iWidth, int iHeight) {
        if (theCanvas != mainCanvas.curCanvas) {
            return;
        } //no effect if theCanvas is not currently being displayed

        mainCanvas.bUpdatingScreen = true;
        mainCanvas.repaint(iX, iY, iWidth, iHeight);
        mainCanvas.serviceRepaints();
        mainCanvas.bUpdatingScreen = false;
    }

    public static boolean isShown(UikitCanvas theCanvas) {
        return (mainCanvas.curCanvas == theCanvas);
    }

    public static Image getMainCanvasOffscreenImage() {
        return mainCanvas.offscreenImg;
    }

    public static Graphics getMainCanvasOffscreenGraphics() {
        return mainCanvas.offscreenImg_g;
    }

    public static void refreshScreen() {
        mainCanvas.refreshScreen();
    }

    public static void setAssociatedMIDlet(MIDlet midlet) {
        UiKitDisplay.midlet = midlet;
    }
}
