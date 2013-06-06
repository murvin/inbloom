package com.inbloom.ui;

import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.uikit.motion.MotionLinear;
import com.uikit.mvc.patterns.Screen;
import com.uikit.painters.PatchPainter;
import com.uikit.coreElements.UiKitDisplay;

import javax.microedition.lcdui.Graphics;

public class InBloomScreen extends Screen {

    protected PatchPainter bgPainter;
    protected int patchBorder;
    protected int padding, bottomPadding;

    public InBloomScreen() {
        initBg();
    }

    public InBloomScreen(int width, int height) {
        super(width, height);
        initBg();
    }

    protected void drawCurrentFrame(Graphics g) {
        if (bgPainter != null) {
            bgPainter.paint(this, g);
        }
        super.drawCurrentFrame(g);
    }

    private void initBg() {
        patchBorder = 5;
        bgPainter = new PatchPainter(Resources.getInstance().getThemeImage(GraphicsResources.IMG_BG_PANEL), patchBorder, patchBorder, patchBorder, patchBorder);

    }

    public void exit() {
        mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, -UiKitDisplay.getWidth(), y, 10, 5.0f, 0.0f);
        mfx_slide.setMotionFXListener(this);
        this.motionFX = mfx_slide;
    }

    public void exitToRight() {
        mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, UiKitDisplay.getWidth(), y, 10, 5.0f, 0.0f);
        mfx_slide.setMotionFXListener(this);
        this.motionFX = mfx_slide;
    }

    public void enter() {
        mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, 0, y, 10, 5.0f, 0.0f);
        mfx_slide.setMotionFXListener(this);
        this.motionFX = mfx_slide;
    }

    protected void updateBottomOffset() {
        //#if WQVGA || WQVGA_ADS
//#         int menuBarHeight = Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG).getHeight();
        //#else
        int menuBarHeight = Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR).getHeight();
        //#endif
        int tabPanelHeight = Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG).getHeight();

        bottomPadding = (UiKitDisplay.getHeight() - menuBarHeight - tabPanelHeight) - iHeight;
        if (bottomPadding < 0) {

            //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#         bottomPadding = padding * 4;
            //#elif WVGA
//#             bottomPadding = padding + menuBarHeight;
            //#endif 
            setBottomOffset(menuBarHeight);
        } else {
            setIsScrollable(false);
        }

    }
}
