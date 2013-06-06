package com.inbloom.ui.components;

import com.inbloom.utils.Utils;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;

import com.uikit.motion.MotionLinear;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.IUikitInputHandler;
import com.uikit.coreElements.Panel;

import com.uikit.painters.PatchPainter;
import javax.microedition.lcdui.Image;

public class MenuBar extends Panel implements IUikitInputHandler {

    private Image bg;
    private MenuBarItem lsk, rsk;
    private int softKeyPadding;
    private int offset;

    public MenuBar() {
        super(UiKitDisplay.getWidth(), 0);
        initResources();

        getStyle(true).addRenderer(new PatchPainter(bg, 0, 5, 0, 5));
        
        setSize(iWidth, bg.getHeight());
    }

    public void removeSoftKey(boolean isLSK) {
        if (isLSK) {
            if (this.lsk != null && this.lsk.getContainingPanel() == this) {
                removeComponent(lsk);
            }
        } else {
            if (this.rsk != null && this.rsk.getContainingPanel() == this) {
                removeComponent(rsk);
            }
        }
    }
    
    public void updateBg(){
        //#if WQVGA || WQVGA_ADS
        this.bg = Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG);
        //#else
//#         this.bg = Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR);
        //#endif
        getStyle().clearAllRenderers();
        getStyle().addRenderer(new PatchPainter(bg, 0, 5, 0, 5));
    }

    public void setLsk(String text, int id) {
        if (this.lsk == null) {
            lsk = new MenuBarItem(bg.getHeight(), true);
            lsk.setEventListener(cel);
        }

        if (lsk.getContainingPanel() == null) {
            addComponent(lsk);
        }

        lsk.setText(text);
        lsk.y = (iHeight - lsk.getHeight()) / 2;
        lsk.x = softKeyPadding;
        lsk.setId(id);
    }

    public void setRsk(String text, int id) {
        if (this.rsk == null) {
            rsk = new MenuBarItem(bg.getHeight(), false);
            rsk.setEventListener(cel);
        }

        if (rsk.getContainingPanel() == null) {
            addComponent(rsk);
        }

        rsk.setText(text);
        rsk.y = (iHeight - rsk.getHeight()) / 2;
        rsk.x = iWidth - rsk.getWidth() - softKeyPadding;
        rsk.setId(id);
    }

    private void initResources() {
                //#if WQVGA || WQVGA_ADS
        this.bg = Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG);
        //#else
//#         this.bg = Resources.getInstance().getThemeImage(GraphicsResources.MENU_BAR);
        //#endif
        this.softKeyPadding = 3 * UiKitDisplay.getWidth() / 100;
        this.offset = 1 * UiKitDisplay.getWidth() / 100;
    }

    public void enter(int targetY) {
        MotionLinear mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, x, targetY, 10, 0.0f, 0.0f);
        this.motionFX = mfx_slide;
    }

    public boolean onKeyPressed(int iKeyCode) {
        if (iKeyCode == Utils.LSK) {
            if (this.lsk != null) {
                this.lsk.x += offset;
                this.lsk.y += offset;
            }
        } else if (iKeyCode == Utils.RSK) {
            if (this.rsk != null) {
                this.rsk.x += offset;
                this.rsk.y += offset;
            }
        }
        return false;
    }

    public boolean onKeyReleased(int iKeyCode) {
        if (iKeyCode == Utils.LSK) {
            if (this.lsk != null) {
                this.lsk.x -= offset;
                this.lsk.y -= offset;

                if (cel != null) {
                    cel.onComponentEvent(this, this.lsk.getId(), null, -1);
                }
            }
        } else if (iKeyCode == Utils.RSK) {
            if (this.rsk != null) {
                this.rsk.x -= offset;
                this.rsk.y -= offset;

                if (cel != null) {
                    cel.onComponentEvent(this, this.rsk.getId(), null, -1);
                }
            }
        }
        return false;
    }

    public boolean onKeyRepeated(int iKeyCode) {
        return false;
    }
}
