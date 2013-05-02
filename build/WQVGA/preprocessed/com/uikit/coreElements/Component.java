package com.uikit.coreElements;

import com.uikit.motion.Motion;
import com.uikit.painters.BorderPainter;
import com.uikit.painters.IPainter;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.Style;
import com.uikit.utils.UikitConstant;
import javax.microedition.lcdui.*;

public class Component {

    public int x;
    public int y;
    protected int iWidth;
    protected int iHeight;
    protected int minWidth = 100;
    protected int minHeight = 100;
    protected int relWidth;
    protected int relHeight;
    
    protected int relX;
    protected int relY;
    protected int relSize;
    protected int relPosition;
    protected int fillParent;
    protected UikitCanvas theAttachedCanvas;
    protected Panel theAttachedPanel;
    protected boolean isVisible = true;
    protected IComponentEventListener cel;
    public Motion motionFX;
    private boolean hasNeverStartedYet = true;
    private boolean isAnimationStopped = true;
    private ComponentStyle style;
    public boolean isLayoutable = true;

    public Component(int iX, int iY, int iWidth, int iHeight, ComponentStyle style) {
        this(iX, iY, iWidth, iHeight);
        this.style = style;
    }

    public Component(int iX, int iY, int iWidth, int iHeight) {
        this.x = iX;
        this.y = iY;
        this.iWidth = iWidth;
        this.iHeight = iHeight;
    }

    public Component(int iWidth, int iHeight) {
        this(0, 0, iWidth, iHeight);
    }

    protected boolean tick() {
        if (isAnimationStopped) {
            return false;
        }

        if (motionFX != null) {
            motionFX.tick();
            x = motionFX.getCurPositionX();
            y = motionFX.getCurPositionY();

            if (motionFX.getProgress() == Motion.MOTIONFX_FINISHED) {
                motionFX = null;
            }
        }


        boolean bFrameAdvanced = animate();

        return true;

    }

    final void drawCurrentFrameEx(Graphics g) {
        if (g == null) {
            throw new IllegalArgumentException();
        }

        if (style != null) {
            if (style.getRenderers() != null && !style.getRenderers().isEmpty()) {
                for (int i = 0; i < style.getRenderers().size(); i++) {
                    if (!(((IPainter) style.getRenderers().elementAt(i)) instanceof BorderPainter)) {
                        ((IPainter) style.getRenderers().elementAt(i)).paint(this, g);
                    }
                }
            }
        }
        drawCurrentFrame(g);
        if (style != null) {
            if (style.getRenderers() != null && !style.getRenderers().isEmpty()) {
                for (int i = 0; i < style.getRenderers().size(); i++) {
                    if ((((IPainter) style.getRenderers().elementAt(i)) instanceof BorderPainter)) {
                        ((IPainter) style.getRenderers().elementAt(i)).paint(this, g);
                    }
                }
            }
        }
    }

    public final boolean hasAnimationStarted() {
        return !hasNeverStartedYet;
    }

    public final void setEventListener(IComponentEventListener cel) {
        this.cel = cel;
    }

    public final IComponentEventListener getEventListener() {
        return cel;
    }

    public final void invalidate() {
        invalidate(0, 0, iWidth, iHeight);
    }

    public final void invalidate(int iX, int iY, int iWidth, int iHeight) {
        if (theAttachedCanvas != null && theAttachedCanvas.isShown()) {
            theAttachedCanvas.updateScreen();
        }
    }

    public final Image getCurrentFrameImage() {
        Image retImg;

        Image tmpImg = Image.createImage(iWidth, iHeight);
        Graphics g = tmpImg.getGraphics();
        drawCurrentFrameEx(g);
        retImg = tmpImg;


        return retImg;
    }

    public final void paintCurrentFrame(Graphics g, int iX, int iY) {
        g.translate(iX, iY);
        drawCurrentFrameEx(g);
        g.translate(-iX, -iY);
    }

    public final void setVisibleMode(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public final boolean isVisible() {
        return isVisible;
    }

    public int getHeight() {
        return iHeight;
    }

    public int getWidth() {
        return iWidth;
    }

    public final void startStopAnimation() {
        isAnimationStopped = !isAnimationStopped;
        if (!isAnimationStopped) {
            animationRestarted(hasNeverStartedYet);
            hasNeverStartedYet = false;
        }
    }

    public final boolean isAnimationStopped() {
        return isAnimationStopped;
    }

    public void setSize(int iWidth, int iHeight) {
        if (this.iWidth == iWidth && this.iHeight == iHeight) {
            return;
        }

        if (iWidth < 0 || iHeight < 0) {
            throw new IllegalArgumentException();
        }

        this.iWidth = iWidth;
        this.iHeight = iHeight;

    }

    public final UikitCanvas getContainingCanvas() {
        return theAttachedCanvas;
    }

    public final Panel getContainingPanel() {
        return theAttachedPanel;
    }

    public final void setContainingPanel(Panel theAttachedPanel) {
        this.theAttachedPanel = theAttachedPanel;
    }

    public int getAbsoluteX() {
        if (theAttachedCanvas == null) {
            throw new IllegalArgumentException();
        }
        if (theAttachedPanel != null) {
            return theAttachedPanel.getAbsoluteX() + x;
        } else {
            return x;
        }
    }

    public int getAbsoluteY() {
        if (theAttachedCanvas == null) {
            throw new IllegalArgumentException();
        }

        if (theAttachedPanel != null) {
            return theAttachedPanel.getAbsoluteY() + y;
        } else {
            return y;
        }
    }

    public void setPreferredSize(int iWidth, int iHeight) {
        if (iWidth <= 0 || iHeight <= 0) {
            throw new IllegalArgumentException();
        }

        setSize(iWidth, iHeight);
    }

    public void setStyle(ComponentStyle style) {
        if (style == this.style) {
            return;
        }
        if (this.style != null) {
            this.style.removeComponent(this);
        }
        this.style = style;
        if (style != null) {
            style.addComponent(this);
        }
    }

    public void setStyle(Style style) {
        if (style instanceof ComponentStyle) {
            setStyle((ComponentStyle) style);
        }
    }

    public ComponentStyle getStyle() {
        return style;
    }

    public ComponentStyle getStyle(boolean createNew) {
        if (style != null) {
            return style;
        }
        if (createNew) {
            ComponentStyle style = new ComponentStyle();
            setStyle(style);
            return style;
        }
        return null;
    }

    public void useRelativeSize(int relativeSize) {
        if (relativeSize == this.relSize) {
            return;
        }
        if ((relativeSize & UikitConstant.HORIZONTAL) != UikitConstant.HORIZONTAL
                && (relativeSize & UikitConstant.VERTICAL) != UikitConstant.VERTICAL
                && relativeSize != 0) {
            throw new IllegalArgumentException();
        }
        this.relSize = relativeSize;
        resize();
    }

    public void setRelWidth(int relWidth) {
        if (relWidth < 0) {
            throw new IllegalArgumentException();
        }
        if (this.relWidth == relWidth) {
            return;
        }
        this.relWidth = relWidth;
        resize();
    }


    public int getMinWidth() {
        return minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getFillParent() {
        return fillParent;
    }

    protected void reposition() {
        if (getContainingPanel() == null) {
            return;
        }

        int size = getContainingPanel().getWidth();
        int offset = 0;

        if (getContainingPanel().getStyle() != null) {
            size -= getContainingPanel().getStyle().getPadding(ComponentStyle.LEFT) + getContainingPanel().getStyle().getPadding(ComponentStyle.RIGHT);
            offset = getContainingPanel().getStyle().getPadding(ComponentStyle.LEFT);
        }

        if ((relPosition & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {
            x = (relX * size) / 100 + offset;
        }

        size = getContainingPanel().getHeight();
        offset = 0;

        if (getContainingPanel().getStyle() != null) {
            size -= getContainingPanel().getStyle().getPadding(ComponentStyle.TOP) + getContainingPanel().getStyle().getPadding(ComponentStyle.BOTTOM);
            offset = getContainingPanel().getStyle().getPadding(ComponentStyle.TOP);
        }

        if ((relPosition & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
            y = (relY * size) / 100 + offset;
        }
    }

    protected void resize() {
        if (getContainingPanel() == null) {
            return;
        }
        int newWidth = iWidth;
        int newHeight = iHeight;

        int size = getContainingPanel().getWidth();

        if (getContainingPanel().getStyle() != null) {
            size -= getContainingPanel().getStyle().getPadding(ComponentStyle.LEFT) + getContainingPanel().getStyle().getPadding(ComponentStyle.RIGHT);
        }

        if ((relSize & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {
            newWidth = (relWidth * size) / 100;
        }

        size = getContainingPanel().getHeight();

        if (getContainingPanel().getStyle() != null) {
            size -= getContainingPanel().getStyle().getPadding(ComponentStyle.TOP) + getContainingPanel().getStyle().getPadding(ComponentStyle.BOTTOM);
        }

        if ((relSize & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
            newHeight = (relHeight * size) / 100;
        }


        if (newWidth != iWidth || newHeight != iHeight) {
            setSize(newWidth, newHeight);
        }
    }

    protected void notifyContainerSizeChanged() {
        if (relPosition != 0) {
            reposition();
        }
        if (relSize != 0) {
            resize();
        }
    }

    public void notifyStyleChanged(Style style) {
    }

    protected void animationRestarted(boolean bNeverStartedYet) {
    }

    protected void drawCurrentFrame(Graphics g) {
    }

    protected boolean animate() {
        return false;
    }

    public void setPosition(int position) {
        if (theAttachedPanel != null) {
            int padding = 0;
            if (theAttachedPanel.getStyle() != null) {
                padding = theAttachedPanel.getStyle().getCompoundPadding();
            }
            if ((position & UikitConstant.LEFT) == UikitConstant.LEFT) {
                x = (padding >>> ComponentStyle.LEFT) & 0xff;
            } else if ((position & UikitConstant.RIGHT) == UikitConstant.RIGHT) {
                x = theAttachedPanel.getWidth() - iWidth - (padding >>> ComponentStyle.RIGHT) & 0xff;
            } else if ((position & UikitConstant.HCENTER) == UikitConstant.HCENTER) {
                x = (theAttachedPanel.getWidth() - ((padding >>> ComponentStyle.RIGHT) & 0xff) - ((padding >>> ComponentStyle.LEFT) & 0xff) - iWidth) / 2 + ((padding >>> ComponentStyle.LEFT) & 0xff);
            }

            if ((position & UikitConstant.TOP) == UikitConstant.TOP) {
                y = (padding >>> ComponentStyle.TOP) & 0xff;
            } else if ((position & UikitConstant.BOTTOM) == UikitConstant.BOTTOM) {
                y = theAttachedPanel.getHeight() - iHeight - ((padding >>> ComponentStyle.BOTTOM) & 0xff);
            } else if ((position & UikitConstant.VCENTER) == UikitConstant.VCENTER) {
                y = (theAttachedPanel.getHeight() - ((padding >>> ComponentStyle.BOTTOM) & 0xff) - ((padding >>> ComponentStyle.TOP) & 0xff) - iHeight) / 2 + ((padding >>> ComponentStyle.TOP) & 0xff);
            }
        } else if (theAttachedCanvas != null) {
            if ((position & UikitConstant.LEFT) == UikitConstant.LEFT) {
                x = 0;
            } else if ((position & UikitConstant.RIGHT) == UikitConstant.RIGHT) {
                x = theAttachedCanvas.getWidth() - iWidth;
            } else if ((position & UikitConstant.HCENTER) == UikitConstant.HCENTER) {
                x = (theAttachedCanvas.getWidth() - iWidth) / 2;
            }

            if ((position & UikitConstant.TOP) == UikitConstant.TOP) {
                y = 0;
            } else if ((position & UikitConstant.BOTTOM) == UikitConstant.BOTTOM) {
                y = theAttachedCanvas.getHeight() - iHeight;
            } else if ((position & UikitConstant.VCENTER) == UikitConstant.VCENTER) {
                y = (theAttachedCanvas.getHeight() - iHeight) / 2;
            }
        }
    }
}
