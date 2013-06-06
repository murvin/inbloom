package com.uikit.utils;

import javax.microedition.lcdui.Image;
import com.uikit.coreElements.UikitFont;

public class TextUtil {

    public static Image generateTransparentTextImage(String string, UikitFont font, int colour) throws IllegalArgumentException {
        if (string == null || string.equals("")) {
            throw new IllegalArgumentException();
        }
        if (font == null) {
            throw new IllegalArgumentException();
        }
        Image image = ImageUtil.generateTextImage(new String[]{string}, font, 0x000000, 0xff0000, 0);
        int iW = image.getWidth();
        int iH = image.getHeight();
        int[] rgbData = new int[iW * iH];
        image.getRGB(rgbData, 0, iW, 0, 0, iW, iH);
        for (int i = 0; i < rgbData.length; i++) {
            rgbData[i] = ((rgbData[i] & 0xff0000) << 8) + colour;
        }
        return Image.createRGBImage(rgbData, iW, iH, true);
    }
}
