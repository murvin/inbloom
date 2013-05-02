package com.uikit.animations;

import com.uikit.coreElements.Component;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.Style;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class UikitImageBox extends Component {

    private Image image;

    public UikitImageBox(Image image) {
        this(0, 0, image);
    }

    public UikitImageBox(InputStream is) throws IOException {
        super(0, 0);
        setFormat(is);
    }

    private void setFormat(InputStream is) throws IOException {
        image = Image.createImage(is);
        setSize(image.getWidth(), image.getHeight());

    }

    public UikitImageBox(int iX, int iY, Image image) {
        this(iX, iY, image, null);
    }

    public UikitImageBox(int iX, int iY, Image image, ComponentStyle style) {
        super(iX, iY, 0, 0, style);
        if (image == null) {
            throw new IllegalArgumentException();
        }
        this.image = image;
        if (style != null && style.hasPadding()) {
            super.setSize(image.getWidth() + style.getPadding(ComponentStyle.LEFT) + style.getPadding(ComponentStyle.RIGHT), image.getHeight() + style.getPadding(ComponentStyle.TOP) + style.getPadding(ComponentStyle.BOTTOM));
        } else {
            super.setSize(image.getWidth(), image.getHeight());
        }
    }

    public void setImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        this.image = image;

        if (getStyle() != null && getStyle().hasPadding()) {
            super.setSize(image.getWidth() + getStyle().getPadding(ComponentStyle.LEFT) + getStyle().getPadding(ComponentStyle.RIGHT), image.getHeight() + getStyle().getPadding(ComponentStyle.TOP) + getStyle().getPadding(ComponentStyle.BOTTOM));
        } else {
            super.setSize(image.getWidth(), image.getHeight());
        }
    }

    protected void animationRestarted(boolean bNeverStartedYet) {
    }

    protected void drawCurrentFrame(Graphics g) {
        if (getStyle() == null) {
            g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
        } else {
            g.drawImage(image, getStyle().getPadding(ComponentStyle.LEFT), getStyle().getPadding(ComponentStyle.TOP), 20);
        }
    }

    protected boolean animate() {
        return false;
    }

    public void setStyle(ComponentStyle style) {
        if (style != null && style.hasPadding()) {
            if (iWidth != image.getWidth() + style.getPadding(ComponentStyle.LEFT) + style.getPadding(ComponentStyle.RIGHT)
                    || iHeight != image.getHeight() + style.getPadding(ComponentStyle.TOP) + style.getPadding(ComponentStyle.BOTTOM)) {
                super.setSize(image.getWidth() + style.getPadding(ComponentStyle.LEFT) + style.getPadding(ComponentStyle.RIGHT),
                        image.getHeight() + style.getPadding(ComponentStyle.TOP) + style.getPadding(ComponentStyle.BOTTOM));
            }
        } else {
            if (iWidth != image.getWidth() || iHeight != image.getHeight()) {
                super.setSize(image.getWidth(), image.getHeight());
            }
        }
        super.setStyle(style);
    }

    public void notifyStyleChanged(Style style) {
        if (style instanceof ComponentStyle) {
            if (getStyle() != null && getStyle().hasPadding()) {
                super.setSize(image.getWidth() + getStyle().getPadding(ComponentStyle.LEFT) + getStyle().getPadding(ComponentStyle.RIGHT), image.getHeight() + getStyle().getPadding(ComponentStyle.TOP) + getStyle().getPadding(ComponentStyle.BOTTOM));
            } else {
                super.setSize(image.getWidth(), image.getHeight());
            }
        }
    }

    public Image getImage() {
        return image;
    }
}
