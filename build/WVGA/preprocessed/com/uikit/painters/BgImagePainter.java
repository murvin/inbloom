package com.uikit.painters;

import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Component;
import com.uikit.utils.ImageUtil;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class BgImagePainter implements IPainter {

    private Image imgBg;
    private int repeatStyle;

    public BgImagePainter(Image image, int repeatStyle) {
        setRepeatStyle(repeatStyle);
        setImage(image);
    }

    private void setImage(Image imgBg) {
        if (imgBg == null) {
            throw new NullPointerException();
        }
        this.imgBg = imgBg;
    }

    public Image getImgBg() {
        return imgBg;
    }

    private void setRepeatStyle(int repeatStyle) {
        if (repeatStyle != UikitConstant.NOREPEAT && repeatStyle != UikitConstant.REPEAT
                && repeatStyle != UikitConstant.XREPEAT && repeatStyle != UikitConstant.YREPEAT) {
            throw new IllegalArgumentException();
        }
        this.repeatStyle = repeatStyle;
    }

    public int getRepeatStyle() {
        return repeatStyle;
    }

    public void paint(Component c, Graphics g) {
        if (repeatStyle == UikitConstant.REPEAT) {
            ImageUtil.drawTiledRectangle(g, 0, 0, c.getWidth(), c.getHeight(), imgBg);
        } else if (repeatStyle == UikitConstant.XREPEAT) {
            ImageUtil.drawTiledRectangle(g, 0, 0, c.getWidth(), Math.min(imgBg.getHeight(), c.getHeight()), imgBg);
        } else if (repeatStyle == UikitConstant.YREPEAT) {
            ImageUtil.drawTiledRectangle(g, 0, 0, Math.min(imgBg.getWidth(), c.getWidth()), c.getHeight(), imgBg);
        } else {
            int cx = g.getClipX();
            int cy = g.getClipY();
            int cw = g.getClipWidth();
            int ch = g.getClipHeight();
            g.clipRect(0, 0, c.getWidth(), c.getHeight());
            g.drawImage(imgBg, 0, 0, 20);
            g.setClip(cx, cy, cw, ch);
        }
    }
}
