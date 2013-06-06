
package com.uikit.painters;

import com.uikit.coreElements.Component;
import com.uikit.styles.ComponentStyle;
import javax.microedition.lcdui.Graphics;

public class BorderPainter implements IPainter {

    private int border_size;

    private long border_colorLR;

    private long border_colorTB;

    private int i;

 
    public BorderPainter() {
    }

    
    private void setBorderSize(int borderSize, int alignment) {
        if (alignment == ComponentStyle.TOP || alignment == ComponentStyle.BOTTOM || alignment == ComponentStyle.LEFT || alignment == ComponentStyle.RIGHT) {
            borderSize <<= alignment;
        } else {
            throw new IllegalArgumentException();
        }

        this.border_size &= ~((0x000000ff << alignment));
        this.border_size |= borderSize;
    }

    private void setBorderColor(long borderColor, int alignment) {
        if (alignment == ComponentStyle.TOP) {
            borderColor <<= 32;
            this.border_colorTB &= 0x00000000ffffffffl;
            this.border_colorTB |= borderColor;
        } else if (alignment == ComponentStyle.BOTTOM) {
            this.border_colorTB &= 0xffffffff00000000l;
            this.border_colorTB |= borderColor;
        } else if (alignment == ComponentStyle.LEFT) {
            borderColor <<= 32;
            this.border_colorLR &= 0x00000000ffffffffl;
            this.border_colorLR |= borderColor;
        } else if (alignment == ComponentStyle.RIGHT) {
            this.border_colorLR &= 0xffffffff00000000l;
            this.border_colorLR |= borderColor;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getBorderSize(int alignment) {
        if (alignment == ComponentStyle.TOP || alignment == ComponentStyle.BOTTOM || alignment == ComponentStyle.LEFT || alignment == ComponentStyle.RIGHT) {
            return (border_size >>> alignment) & 0xff;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getBorderColor(int alignment) {
        if (alignment == ComponentStyle.TOP) {
            return (int) (border_colorTB >>> 32) & 0xffffffff;
        } else if (alignment == ComponentStyle.BOTTOM) {
            return (int) (border_colorTB >>> 0) & 0xffffffff;
        } else if (alignment == ComponentStyle.LEFT) {
            return (int) (border_colorLR >>> 32) & 0xffffffff;
        } else if (alignment == ComponentStyle.RIGHT) {
            return (int) (border_colorLR >>> 0) & 0xffffffff;
        } else {
            throw new IllegalArgumentException();
        }
    }

   
    public void setBorderSize(int borderSize) {
        setBorderSize(new int[]{borderSize, borderSize, borderSize, borderSize});
    }

    public void setBorderSize(int top, int right, int bottom, int left) {
        setBorderSize(new int[]{top, right, bottom, left});
    }

    public void setBorderSize(int[] borderSize) {
        if (borderSize == null || borderSize.length != 4) {
            throw new IllegalArgumentException();
        }
        for (i = 0; i < borderSize.length; i++) {
            if (borderSize[i] < 0) {
                throw new IllegalArgumentException();
            }
        }
        if (borderSize[0] >= 0 || borderSize[1] >= 0 || borderSize[2] >= 0 || borderSize[3] >= 0) {
            setBorderSize(borderSize[0], ComponentStyle.TOP);
            setBorderSize(borderSize[1], ComponentStyle.RIGHT);
            setBorderSize(borderSize[2], ComponentStyle.BOTTOM);
            setBorderSize(borderSize[3], ComponentStyle.LEFT);
        }
    }

    public boolean hasBorder() {
        return border_size != 0;
    }

    public void setBorderColor(int borderColor) {
        setBorderColor(new int[]{borderColor, borderColor, borderColor, borderColor});
    }

    public void setBorderColor(int top, int right, int bottom, int left) {
        setBorderColor(new int[]{top, right, bottom, left});
    }

    public void setBorderColor(int[] borderColor) {
        if (borderColor == null || borderColor.length != 4) {
            throw new IllegalArgumentException();
        }
        setBorderColor(borderColor[0], ComponentStyle.TOP);
        setBorderColor(borderColor[1], ComponentStyle.RIGHT);
        setBorderColor(borderColor[2], ComponentStyle.BOTTOM);
        setBorderColor(borderColor[3], ComponentStyle.LEFT);
    }

    int getCompoundBorderSize() {
        return this.border_size;
    }

    
    long getBorderColorTB() {
        return this.border_colorTB;
    }

    long getBorderColorLR() {
        return this.border_colorLR;
    }

    public void paint(Component c, Graphics g) {
        int size = ((border_size >>> 24) & 0xff);
        g.setColor((int) (border_colorTB >>> 32) & 0xffffffff);
        for (i = 0; i < size; i++) {
            g.drawLine(0, i, c.getWidth() - 1, i);
        }

        size = ((border_size >>> 16) & 0xff);
        g.setColor((int) border_colorTB);
        for (i = 0; i < size; i++) {
            g.drawLine(0, c.getHeight() - i - 1, c.getWidth() - 1, c.getHeight() - i - 1);
        }

        size = ((border_size >>> 0) & 0xff);
        g.setColor((int) border_colorLR);
        for (i = 0; i < size; i++) {
            g.drawLine(c.getWidth() - i - 1, 0, c.getWidth() - i - 1, c.getHeight() - 1);
        }

        size = ((border_size >>> 8) & 0xff);
        g.setColor((int) (border_colorLR >>> 32) & 0xffffffff);
        for (i = 0; i < size; i++) {
            g.drawLine(i, 0, i, c.getHeight() - 1);
        }
    }
    
}
