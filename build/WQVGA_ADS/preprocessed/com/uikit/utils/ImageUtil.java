package com.uikit.utils;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import com.uikit.coreElements.UikitFont;
import com.uikit.coreElements.UiKitDisplay;

public class ImageUtil {

    public static void drawTiledRectangle(Graphics g,
            int x, int y, int iWidth, int iHeight,
            Image tileImg) {

        if (g == null) {
            throw new IllegalArgumentException();
        }

        if (iWidth <= 0 || iHeight <= 0) {
            throw new IllegalArgumentException();
        }

        if (tileImg == null) {
            throw new IllegalArgumentException();
        }
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight();

        g.clipRect(x, y, iWidth, iHeight);

        int imgW = tileImg.getWidth();
        int imgH = tileImg.getHeight();

        int widthLimit = iWidth / imgW + (iWidth % imgW == 0 ? 0 : 1);
        int heightLimit = iHeight / imgH + (iHeight % imgH == 0 ? 0 : 1);

        for (int c = 0; c < widthLimit; c++) {
            for (int r = 0; r < heightLimit; r++) {
                g.drawImage(tileImg, x + c * imgW, y + r * imgH, Graphics.LEFT | Graphics.TOP);
            }
        }
        g.setClip(clipX, clipY, clipW, clipH);
    }

    public static Image generateTextImage(String[] strs, UikitFont font, int iBgColor, int iTextColor, int iLineSpacing) {
        if (strs == null) {
            throw new IllegalArgumentException();
        }
        if (font == null) {
            throw new IllegalArgumentException();
        }

        final int iFontHeight = font.getHeight();

        int iWidth = 0;
        for (int i = 0; i < strs.length; i++) {
            final int iCurWidth = font.stringWidth(strs[i]);
            if (iCurWidth > iWidth) {
                iWidth = iCurWidth;
            }
        }

        int iHeight = strs.length * (iFontHeight + iLineSpacing) - iLineSpacing;

        Image tmpImg = Image.createImage(iWidth, iHeight);
        Graphics tmpG = tmpImg.getGraphics();
        tmpG.setColor(iBgColor);
        tmpG.fillRect(0, 0, iWidth, iHeight);
        tmpG.setColor(iTextColor);

        for (int i = 0; i < strs.length; i++) {
            font.drawString(tmpG, strs[i], 0, i * (iFontHeight + iLineSpacing), Graphics.LEFT | Graphics.TOP);
        }

        return tmpImg;
    }

    public static Image generateTransparentImage(int iWidth, int iHeight,
            byte btOpacityPercentage, int iColor) {
        if (iWidth < 0 || iHeight < 0) {
            throw new IllegalArgumentException();
        }

        /* Correct the input opacity value */
        if (btOpacityPercentage < 0) {
            btOpacityPercentage = 0;
        }

        if (btOpacityPercentage > 100) {
            btOpacityPercentage = 100;
        }


        int btAlphaValue = (btOpacityPercentage * UiKitDisplay.getDisplay().numAlphaLevels() / 100);
        if (btAlphaValue >= UiKitDisplay.getDisplay().numAlphaLevels() - 1) {
            btAlphaValue = 0xFF;
        } else if (btAlphaValue > 0) {
            btAlphaValue = 255 * btAlphaValue / (UiKitDisplay.getDisplay().numAlphaLevels() - 1);
        }
        final int iArgbColor = (btAlphaValue << 24) | iColor;

        int[] argb = new int[iWidth * iHeight];
        for (int i = 0; i < argb.length; i++) {
            argb[i] = iArgbColor;
        }

        Image img = Image.createRGBImage(argb, iWidth, iHeight, true);
        argb = null;
        return img;
    }

    public static Image replaceColor(Image img, int iReplacementArgb) {
        if (img == null) {
            throw new IllegalArgumentException();
        }

        int iWidth = img.getWidth();
        int iHeight = img.getHeight();

        int[] argb = new int[iWidth * iHeight];

        img.getRGB(argb, 0, iWidth, 0, 0, iWidth, iHeight);

        for (int i = 0; i < argb.length; i++) {
            argb[i] = (0x00ffffff & iReplacementArgb) | (argb[i] & 0xff000000);
        }
        Image img2 = Image.createRGBImage(argb, iWidth, iHeight, true);
        argb = null;
        return img2;
    }
    private static int[] rgbSize1, rgbSize2;
    private static int sw = 0, sh = 0, dw = 0, dh = 0;

    public static Image resizeImage(Image img, int width, int height) {

        if (img == null) {
            throw new IllegalArgumentException();
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }

        if (width == img.getWidth() && height == img.getHeight()) {
            return img;
        }

        if (sw != img.getWidth() || sh != img.getHeight()) {
            sw = img.getWidth();
            sh = img.getHeight();
            rgbSize1 = null;
            rgbSize1 = new int[sw * sh];
        }
        if (dw != width || dh != height) {
            dw = width;
            dh = height;
            rgbSize2 = null;
            rgbSize2 = new int[dw * dh];
        }

        int x, y, srcline, destline;
        int[] pixels = new int[dw];
        for (int i = 0; i < dw; i++) {
            pixels[i] = (((i * sw + 2) << 2) / dw) >>> 2;
        }
        img.getRGB(rgbSize1, 0, sw, 0, 0, sw, sh);
        for (y = 0; y < dh; y++) {
            srcline = ((((y * sh + 2) << 2) / dh) >>> 2) * sw;
            destline = y * dw;
            for (x = 0; x < dw; x++) {
                rgbSize2[destline + x] = rgbSize1[srcline + pixels[x]];
            }
        }

        return Image.createRGBImage(rgbSize2, dw, dh, true);
    }

    public static void copyRGB(int[] dest, int[] src, int dx, int dy, int dw, int dh,
            int sx, int sy, int sw, int sh, int so) {

        if (dx == 0 && sx == 0 && dw == sw && so == dw) {
            final int soff = sy * sw;
            final int doff = dy * dw;
            System.arraycopy(src, soff, dest, doff, Math.min(dh - dy, sh - sy) * dw);
        } else {
            int w = Math.min(dw - dx, sw);
            int h = Math.min(dh - dy, sh);
            int soff = sy * so + sx;
            int doff = dy * dw + dx;
            for (int i = 0; i < h; i++) {
                System.arraycopy(src, soff, dest, doff, w);
                soff += so;
                doff += dw;
            }
        }
    }
}
