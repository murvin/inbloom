package com.uikit.coreElements;

import com.uikit.datastructures.LinkedList;
import com.uikit.datastructures.Stack;
import com.uikit.layout.ILayout;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.Style;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class Panel extends Component implements IContainer {

    private boolean hasClippingMode;
    protected Vector components;
    private ILayout currentLayout;
    private int absX;
    private int absY;

    public Panel(int iX, int iY, int iWidth, int iHeight) {
        super(iX, iY, iWidth, iHeight);

        components = new Vector();
        hasClippingMode = true;
    }

    public Panel(int iWidth, int iHeight) {
        this(0, 0, iWidth, iHeight);
    }

    protected boolean tick() {
        super.tick();
        if (isAnimationStopped()) {
            return false;
        }

        for (int i = 0; i < components.size(); i++) {
            Component c = (Component) components.elementAt(i);
            if (c.isVisible) {
                c.tick();
            }
        }

        return true;
    }

    protected void animationRestarted(boolean bNeverStartedYet) {
    }

    protected void drawCurrentFrame(Graphics g) { //to be optionally overriden by subclasses
        int iPrevClipX = g.getClipX();
        int iPrevClipY = g.getClipY();
        int iPrevClipWidth = g.getClipWidth();
        int iPrevClipHeight = g.getClipHeight();

        if (hasClippingMode) {
            g.clipRect(0, 0, iWidth, iHeight);
        }

        drawAllChildComponents(g);

        if (hasClippingMode) {
            g.setClip(iPrevClipX, iPrevClipY, iPrevClipWidth, iPrevClipHeight);
        }
    }

    protected boolean animate() {
        return true;
    }

    protected final void drawAllChildComponents(Graphics g) {
        drawChildComponents(g, components);
    }

    protected final void drawChildComponents(Graphics g, Vector subsetComponents) {
        try {

            absX = getAbsoluteX();
            absY = getAbsoluteY();

            Vector components = subsetComponents;

            for (int i = 0; i < components.size(); i++) {
                Component c = (Component) components.elementAt(i);
                if (subsetComponents != components && indexOfComponent(c) == -1) {
                    throw new IllegalArgumentException();
                }
                if (c.isVisible
                        && absX + c.x + c.iWidth >= 0
                        && absX + c.x < UiKitDisplay.getWidth()
                        && absY + c.y + c.iHeight >= 0
                        && absY + c.y < UiKitDisplay.getHeight()) {

                    final int iX = c.x;
                    final int iY = c.y;
                    g.translate(iX, iY);
                    c.drawCurrentFrameEx(g);
                    g.translate(-iX, -iY);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
        }
    }

    public void componentsAtPosition(int iX, int iY, Stack stack) {

        for (int i = 0; i < components.size(); i++) {
            Component c = (Component) components.elementAt(i);
            if (!c.isVisible) {
                continue;
            }
            if ((c instanceof ITouchEventListener) && (c.getContainingCanvas() != null)) {
                final int x = c.getAbsoluteX();
                final int y = c.getAbsoluteY();
                if (x <= iX && x + c.iWidth > iX
                        && y <= iY && y + c.iHeight > iY) {
                    stack.push(c);
                }
            } else if (c instanceof Panel) {
                Panel p = (Panel) c;
                p.componentsAtPosition(iX, iY, stack);
            }
        }
    }

    public synchronized void addComponent(Component c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        if (components.indexOf(c) != -1) {
            return;
        }
        UikitCanvas.setAttachedCanvasRecursively(c, theAttachedCanvas);
        components.addElement(c);
        c.theAttachedPanel = this;
        c.notifyContainerSizeChanged();

        if (!c.hasAnimationStarted()) {
            c.startStopAnimation();
        }

        Panel panel = this;
        while (panel != null) {
            if (panel instanceof SmartPanel) {
                ((SmartPanel) panel).registerFocusables(c);
                break;
            }
            panel = panel.theAttachedPanel;
        }

        relayout();
    }

    public int indexOfComponent(Component c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }

        return components.indexOf(c);
    }

    public Component componentAt(int index) {
        if (index < 0 || index > components.size() - 1) {
            throw new IndexOutOfBoundsException();
        }

        return (Component) components.elementAt(index);
    }

    public synchronized void insertComponentAt(Component c, int index) {
        if (c == null) {
            throw new IllegalArgumentException();
        }

        if (index < 0 || index > components.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (c.isAnimationStopped()) {
            c.startStopAnimation();
        }
        UikitCanvas.setAttachedCanvasRecursively(c, theAttachedCanvas);
        c.theAttachedPanel = this;
        components.insertElementAt(c, index);
        c.notifyContainerSizeChanged();

        Panel panel = this;
        while (panel != null) {
            if (panel instanceof SmartPanel) {
                ((SmartPanel) panel).registerFocusables(c);
                break;
            }
            panel = panel.theAttachedPanel;
        }

        relayout();
    }

    public synchronized void insertComponentAt(Component c, int index, boolean isRelayout) {
        if (c == null) {
            throw new IllegalArgumentException();
        }

        if (index < 0 || index > components.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (c.isAnimationStopped()) {
            c.startStopAnimation();
        }
        UikitCanvas.setAttachedCanvasRecursively(c, theAttachedCanvas);
        c.theAttachedPanel = this;
        components.insertElementAt(c, index);
        c.notifyContainerSizeChanged();

        Panel panel = this;
        while (panel != null) {
            if (panel instanceof SmartPanel) {
                ((SmartPanel) panel).registerFocusables(c);
                break;
            }
            panel = panel.theAttachedPanel;
        }

        if (isRelayout) {
            relayout();
        }
    }

    public synchronized void replaceComponent(Component oldC, Component newC) {
        if (oldC == null || newC == null) {
            throw new IllegalArgumentException();
        }
        final int iIndex = components.indexOf(oldC);
        if (iIndex == -1) {
            throw new IllegalArgumentException();
        }
        if (components.indexOf(newC) != -1) {
            throw new IllegalArgumentException();
        }
        UikitCanvas.setAttachedCanvasRecursively(newC, theAttachedCanvas);
        newC.theAttachedPanel = this;
        components.setElementAt(newC, iIndex);
        oldC.theAttachedPanel = null;
        UikitCanvas.setAttachedCanvasRecursively(oldC, null);
        relayout();
    }

    public synchronized void removeComponent(Component c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        final int iIndex;
        if ((iIndex = components.indexOf(c)) == -1) {
            return;
        }
        Panel panel = this;
        while (panel != null) {
            if (panel instanceof SmartPanel) {
                ((SmartPanel) panel).unregisterFocusables(c);
                break;
            }
            panel = panel.theAttachedPanel;
        }

        components.removeElementAt(iIndex);
        c.theAttachedPanel = null;
        UikitCanvas.setAttachedCanvasRecursively(c, null);
        relayout();
    }

    public synchronized void removeComponent(Component c, boolean isRelayout) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        final int iIndex;
        if ((iIndex = components.indexOf(c)) == -1) {
            return;
        }
        Panel panel = this;
        while (panel != null) {
            if (panel instanceof SmartPanel) {
                ((SmartPanel) panel).unregisterFocusables(c);
                break;
            }
            panel = panel.theAttachedPanel;
        }

        components.removeElementAt(iIndex);
        c.theAttachedPanel = null;
        UikitCanvas.setAttachedCanvasRecursively(c, null);
        if (isRelayout) {
            relayout();
        }
    }

    public synchronized void removeComponentAt(int index) {
        if (index >= getComponentCount()) {
            return;
        }

        Panel panel = this;
        while (panel != null) {
            if (panel instanceof SmartPanel) {
                ((SmartPanel) panel).unregisterFocusables(componentAt(index));
                break;
            }
            panel = panel.theAttachedPanel;
        }
        Component c = (Component) components.elementAt(index);
        components.removeElementAt(index);
        c.theAttachedPanel = null;
        UikitCanvas.setAttachedCanvasRecursively(c, null);
        relayout();
    }

    public synchronized void removeAllComponents() {
        while (!components.isEmpty()) {
            removeComponent((Component) components.elementAt(0));
        }
    }

    public int getComponentCount() {
        return components.size();
    }

    public void setLayout(ILayout layout) {
        this.currentLayout = layout;
        relayout();

    }

    public void relayout() {
        if (currentLayout != null) {
            currentLayout.layout(this, filterLayoutComponents(this));
        }
    }

    public final int getDescendantCount() {
        int iDescendantCount = 0;

        final int iImmCount = components.size();
        iDescendantCount += iImmCount;
        for (int i = 0; i < iImmCount; i++) {
            Component c = (Component) components.elementAt(i);
            if (c instanceof Panel) {
                iDescendantCount += ((Panel) c).getDescendantCount();
            }
        }

        return iDescendantCount;
    }

    public final void useClippingMode(boolean bUseClippingMode) {
        this.hasClippingMode = bUseClippingMode;
    }

    public final boolean isUsingClippingMode() {
        return hasClippingMode;
    }

    public ILayout getLayout() {
        return this.currentLayout;
    }

    protected LinkedList filterLayoutComponents(Panel container) {
        LinkedList layoutComponents = new LinkedList();

        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.componentAt(i).isLayoutable) {
                layoutComponents.addElement(container.componentAt(i));
            }
        }

        return layoutComponents;
    }

    public void expandToFitContent() {
        int paddingLeft = 0;
        int paddingRight = 0;
        int paddingTop = 0;
        int paddingBottom = 0;
        if (getStyle() != null) {
            paddingLeft = getStyle().getPadding(ComponentStyle.LEFT);
            paddingRight = getStyle().getPadding(ComponentStyle.RIGHT);
            paddingTop = getStyle().getPadding(ComponentStyle.TOP);
            paddingBottom = getStyle().getPadding(ComponentStyle.BOTTOM);
        }
        int xOffset = 0;
        int yOffset = 0;
        int minWidth = 0;
        int minHeight = 0;
        Component c;
        for (int i = 0; i < components.size(); i++) {
            c = (Component) components.elementAt(i);
            if (c.x < paddingLeft - xOffset) {
                xOffset = paddingLeft - c.x;
            }
            if (c.y < paddingTop - yOffset) {
                yOffset = paddingTop - c.y;
            }
        }
        for (int i = 0; i < components.size(); i++) {
            c = (Component) components.elementAt(i);
            c.x += xOffset;
            c.y += yOffset;
            if (c.x + c.getWidth() + paddingRight > minWidth) {
                minWidth = c.x + c.getWidth() + paddingRight;
            }
            if (c.y + c.getHeight() + paddingBottom > minHeight) {
                minHeight = c.y + c.getHeight() + paddingBottom;
            }
        }
        x -= xOffset;
        y -= yOffset;
        if (minWidth > getWidth() || minHeight > getHeight()) {
            setSize(minWidth > getWidth() ? minWidth : getWidth(), minHeight > getHeight() ? minHeight : getHeight(), false);
            for (int i = 0; i < components.size(); i++) {
                ((Component) components.elementAt(i)).notifyContainerSizeChanged();
            }
        }
    }

    public void setStyle(ComponentStyle style) {
        if (getStyle() != null && style != null) {
            if (getStyle().getCompoundPadding() != style.getCompoundPadding()) {
                super.setStyle(style);
                relayout();
                return;
            }
        } else if (style != null && style.getCompoundPadding() != 0) {
            super.setStyle(style);
            relayout();
        }
        super.setStyle(style);
    }

    public void setSize(int iWidth, int iHeight) {
        setSize(iWidth, iHeight, true);
    }

    public void setSize(int iWidth, int iHeight, boolean relayout) {
        if (this.iWidth == iWidth && this.iHeight == iHeight) {
            return;
        }
        if (iWidth < 0 || iHeight < 0) {
            throw new IllegalArgumentException();
        }
        this.iWidth = iWidth;
        this.iHeight = iHeight;
        for (int i = 0; i < components.size(); i++) {
            ((Component) components.elementAt(i)).notifyContainerSizeChanged();
        }
        if (relayout) {
            relayout();
        }
    }

    protected void notifyContainerSizeChanged() {
        if (relPosition != 0) {
            reposition();
        }
        if (relSize != 0) {
            resize();
            relayout();
        }
    }

    public void notifyStyleChanged(Style style) {
        if (style instanceof ComponentStyle) {
            relayout();
        }
    }
}
