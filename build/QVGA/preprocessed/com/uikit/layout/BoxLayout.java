package com.uikit.layout;

import com.uikit.datastructures.LinkedList;
import com.uikit.styles.ComponentStyle;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.Panel;
import java.util.Enumeration;

public class BoxLayout implements ILayout {

    private int orientation;
    private int gap;
    private int align = UikitConstant.VCENTER | UikitConstant.HCENTER;
    private boolean useFirstItemPos;

    public BoxLayout(int orientation, int gap) {
        if (orientation != UikitConstant.HORIZONTAL && orientation != UikitConstant.VERTICAL) {
            throw new IllegalArgumentException();
        }

        this.orientation = orientation;

        setGap(gap);
    }

    public void setAlign(int alignment) {
        if (this.orientation == UikitConstant.HORIZONTAL) {
            if ((alignment & UikitConstant.TOP) != UikitConstant.TOP && (alignment & UikitConstant.VCENTER) != UikitConstant.VCENTER && (alignment & UikitConstant.BOTTOM) != UikitConstant.BOTTOM) {
                throw new IllegalArgumentException();
            }
        } else if (this.orientation == UikitConstant.VERTICAL) {
            if ((alignment & UikitConstant.LEFT) != UikitConstant.LEFT && (alignment & UikitConstant.HCENTER) != UikitConstant.HCENTER && (alignment & UikitConstant.RIGHT) != UikitConstant.RIGHT) {
                throw new IllegalArgumentException();
            }
        }
        this.align = alignment;
    }

    public int getAlign() {
        return align;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void layout(Panel container, LinkedList layoutComponents) {
        if (container == null || layoutComponents == null) {
            throw new IllegalArgumentException();
        }

        if (container.getComponentCount() == 0) {
            return;
        }

        int combinedDimension = 0;
        int padding = container.getStyle() == null ? 0 : container.getStyle().getCompoundPadding();
        Enumeration enu;
        Component c;
        boolean isFirst = true;
        for (enu = layoutComponents.elements(); enu.hasMoreElements();) {
            c = (Component) enu.nextElement();
            int offset = (isFirst ? (useFirstItemPos ? (orientation == UikitConstant.HORIZONTAL ? c.x : c.y) : 0) : gap);
            combinedDimension += (offset + (orientation == UikitConstant.HORIZONTAL ? c.getWidth() : c.getHeight()));
            isFirst = false;
        }
        enu = null;

        if (layoutComponents.isEmpty()) {
            return;
        }

        resetContainerDimensions(container,
                padding,
                combinedDimension);

        layComponents(container, layoutComponents, padding);

    }

    public void setGap(int gap) {
        if (gap < 0) {
            throw new IllegalArgumentException();
        } else {
            this.gap = gap;
        }
    }

    public int getGap() {
        return this.gap;
    }

    public void setUseFirstItemPos(boolean useFirstItemPos) {
        this.useFirstItemPos = useFirstItemPos;
    }

    private void resetContainerDimensions(Panel container, int padding, int combinedDimension) {
        int width = container.getWidth();
        int height = container.getHeight();

        if (orientation == UikitConstant.HORIZONTAL) {
            width = ((padding >>> ComponentStyle.LEFT) & 0xff) + combinedDimension + ((padding >>> ComponentStyle.RIGHT) & 0xff);
        } else {
            height = ((padding >>> ComponentStyle.TOP) & 0xff) + combinedDimension + ((padding >>> ComponentStyle.BOTTOM) & 0xff);
        }

        container.setSize(width, height, false);
    }

    private void layComponents(Panel container, LinkedList layoutComponents, int padding) {
        Enumeration enu;
        Component component, preceding;
        for (enu = layoutComponents.elements(), preceding = null; enu.hasMoreElements(); preceding = component) {
            component = (Component) enu.nextElement();
            if (orientation == UikitConstant.VERTICAL) {
                component.y = preceding == null ? (useFirstItemPos ? Math.max(component.y, ((padding >>> ComponentStyle.TOP) & 0xff)) : ((padding >>> ComponentStyle.TOP) & 0xff))
                        : (preceding.y + preceding.getHeight() + gap);
                if ((component.getFillParent() & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {
                    int size = (container.getWidth() - ((padding >>> ComponentStyle.RIGHT) & 0xff) - ((padding >>> ComponentStyle.LEFT) & 0xff));
                    if (size < component.getMinWidth()) {
                        size = component.getMinWidth();
                    }
                    component.setSize(size, component.getHeight());
                }
                if ((align & UikitConstant.LEFT) == UikitConstant.LEFT) {
                    component.x = ((padding >>> ComponentStyle.LEFT) & 0xff);
                } else if ((align & UikitConstant.RIGHT) == UikitConstant.RIGHT) {
                    component.x = container.getWidth() - ((padding >>> ComponentStyle.RIGHT) & 0xff) - component.getWidth();
                } else {
                    component.x = (container.getWidth() - ((padding >>> ComponentStyle.RIGHT) & 0xff) - ((padding >>> ComponentStyle.LEFT) & 0xff) - component.getWidth()) / 2 + ((padding >>> ComponentStyle.LEFT) & 0xff);
                }
            } else {
                component.x = preceding == null ? (useFirstItemPos ? Math.max(component.x, ((padding >>> ComponentStyle.LEFT) & 0xff)) : ((padding >>> ComponentStyle.LEFT) & 0xff))
                        : (preceding.x + preceding.getWidth() + gap);
                if ((component.getFillParent() & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
                    int size = (container.getHeight() - ((padding >>> ComponentStyle.TOP) & 0xff) - ((padding >>> ComponentStyle.BOTTOM) & 0xff));
                    if (size < component.getMinHeight()) {
                        size = component.getMinHeight();
                    }
                    component.setSize(component.getWidth(), size);
                }
                if ((align & UikitConstant.TOP) == UikitConstant.TOP) {
                    component.y = ((padding >>> ComponentStyle.TOP) & 0xff);
                } else if ((align & UikitConstant.BOTTOM) == UikitConstant.BOTTOM) {
                    component.y = container.getHeight() - ((padding >>> ComponentStyle.BOTTOM) & 0xff) - component.getHeight();
                } else {
                    component.y = (container.getHeight() - ((padding >>> ComponentStyle.TOP) & 0xff) - ((padding >>> ComponentStyle.BOTTOM) & 0xff) - component.getHeight()) / 2 + ((padding >>> ComponentStyle.TOP) & 0xff);
                }
            }
        }
    }
}
