package com.uikit.painters;

import com.uikit.coreElements.Component;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class PatchPainter implements IPainter {

    private Image img;
    private int top = 0;
    private int bottom = 0;
    private int left = 0;
    private int right = 0;
    private int ihm;
    private int ivm;
    private int iw;
    private int ih;
    private boolean bFillCenter = true;

    public PatchPainter(Image image, int top, int right, int bottom, int left) {
        if (image == null) {
            throw new NullPointerException();
        }
        if (top < 0 || bottom < 0 || left < 0 || right < 0) {
            throw new IllegalArgumentException();
        }
        this.img = image;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        iw = img.getWidth();
        ih = img.getHeight();
        ihm = iw - left - right;
        ivm = ih - top - bottom;
    }

    public PatchPainter(Image image, int top, int right, int bottom, int left, boolean bFillCenter) {
        this(image, top, right, bottom, left);
        this.bFillCenter = bFillCenter;
    }

    public PatchPainter() {
    }

    public void paint(Component c, Graphics g) {
        if (img == null) {
            return;
        }
        final int cx = g.getClipX();
        final int cy = g.getClipY();
        final int cw = g.getClipWidth();
        final int ch = g.getClipHeight();

        final int w = c.getWidth();
        final int h = c.getHeight();

        final int hm = w - left - right;
        final int vm = h - top - bottom;

        int cstart = 0;
        //top
        if (top > 0) {

            //top left
            if (left > 0) {
                g.clipRect(0, 0, left, top);
                g.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT);
                g.setClip(cx, cy, cw, ch);
            }

            //top right
            if (right > 0) {
                g.clipRect(w - right, 0, right, top);
                g.drawImage(img, w - iw, 0, Graphics.TOP | Graphics.LEFT);
                g.setClip(cx, cy, cw, ch);
            }

            //top middle
            if (hm > 0 && ih - left - right > 0) {
                cstart = left;
                //loop
                while (cstart < w - right - ihm) {
                    g.clipRect(cstart, 0, ihm, top);
                    g.drawImage(img, cstart - left, 0, Graphics.TOP | Graphics.LEFT);
                    cstart += ihm;
                    g.setClip(cx, cy, cw, ch);
                }
                //last piece
                if (w - right - cstart > 0) {
                    g.clipRect(cstart, 0, w - right - cstart, top);
                    g.drawImage(img, cstart - left, 0, Graphics.TOP | Graphics.LEFT);
                    g.setClip(cx, cy, cw, ch);
                }
            }
        }

        //botoom
        if (bottom > 0) {

            //bottom left
            if (left > 0) {
                g.clipRect(0, h - bottom, left, bottom);
                g.drawImage(img, 0, h - ih, Graphics.TOP | Graphics.LEFT);
                g.setClip(cx, cy, cw, ch);
            }

            //bottom right
            if (right > 0) {
                g.clipRect(w - right, h - bottom, right, bottom);
                g.drawImage(img, w - iw, h - ih, Graphics.TOP | Graphics.LEFT);
                g.setClip(cx, cy, cw, ch);
            }


            //bottom middle
            if (hm > 0 && ihm > 0) {
                cstart = left;
                //loop
                while (cstart < w - right - ihm) {
                    g.clipRect(cstart, h - bottom, ihm, bottom);
                    g.drawImage(img, cstart - left, h - ih, Graphics.TOP | Graphics.LEFT);
                    cstart += ihm;
                    g.setClip(cx, cy, cw, ch);
                }
                //last piece
                if (w - right - cstart > 0) {
                    g.clipRect(cstart, h - bottom, w - right - cstart, top);
                    g.drawImage(img, cstart - left, h - ih, Graphics.TOP | Graphics.LEFT);
                    g.setClip(cx, cy, cw, ch);
                }
            }

        }

        //left middle
        if (vm > 0 && ivm > 0) {
            cstart = top;
            //loop
            while (cstart < h - bottom - ivm) {
                if (left > 0) {
                    g.clipRect(0, cstart, left, ivm);
                    g.drawImage(img, 0, cstart - top, Graphics.TOP | Graphics.LEFT);
                    g.setClip(cx, cy, cw, ch);
                }
                if (right > 0) {
                    g.clipRect(w - right, cstart, right, ivm);
                    g.drawImage(img, w - iw, cstart - top, Graphics.TOP | Graphics.LEFT);
                    g.setClip(cx, cy, cw, ch);
                }
                if (bFillCenter && w - right - left > 0) {
                    int cxstart = left;
                    //fill horizontaly
                    while (cxstart < w - right - ihm) {
                        g.clipRect(cxstart, cstart, ihm, ivm);
                        g.drawImage(img, cxstart - left, cstart - top, Graphics.TOP | Graphics.LEFT);
                        g.setClip(cx, cy, cw, ch);
                        cxstart += ihm;
                    }
                    //last piece
                    if (w - left - cxstart > 0) {
                        g.clipRect(cxstart, cstart, w - left - cxstart, ivm);
                        g.drawImage(img, cxstart - left, cstart - top, Graphics.TOP | Graphics.LEFT);
                        g.setClip(cx, cy, cw, ch);
                    }
                }
                cstart += ivm;
            }
            //last piece
            if (h - bottom - cstart > 0) {
                if (left > 0) {
                    g.clipRect(0, cstart, left, h - bottom - cstart);
                    g.drawImage(img, 0, cstart - top, Graphics.TOP | Graphics.LEFT);
                    g.setClip(cx, cy, cw, ch);
                }
                if (right > 0) {
                    g.clipRect(w - right, cstart, right, h - bottom - cstart);
                    g.drawImage(img, w - iw, cstart - top, Graphics.TOP | Graphics.LEFT);
                    g.setClip(cx, cy, cw, ch);
                }
                //last fill row
                if (bFillCenter && w - right - left > 0) {
                    int cxstart = left;
                    //fill horizontaly
                    while (cxstart < w - right - ihm) {
                        g.clipRect(cxstart, cstart, ihm, h - bottom - cstart);
                        g.drawImage(img, cxstart - left, cstart - top, Graphics.TOP | Graphics.LEFT);
                        g.setClip(cx, cy, cw, ch);
                        cxstart += ihm;
                    }
                    //last piece
                    if (w - left - cxstart > 0) {
                        g.clipRect(cxstart, cstart, w - left - cxstart, h - bottom - cstart);
                        g.drawImage(img, cxstart - left, cstart - top, Graphics.TOP | Graphics.LEFT);
                        g.setClip(cx, cy, cw, ch);
                    }
                }
            }
        }

    }

    public Image getImage() {
        return img;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getRight() {
        return right;
    }

    public int getLeft() {
        return left;
    }
}
