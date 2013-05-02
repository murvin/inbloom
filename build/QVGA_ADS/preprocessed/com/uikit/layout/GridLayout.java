package com.uikit.layout;

import com.uikit.datastructures.LinkedList;
import com.uikit.styles.ComponentStyle;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.Panel;
import java.util.Enumeration;

public class GridLayout implements ILayout {

    private int rows, cols;
    private int alignment;
    private int width, height;
    private int hGap, vGap;

    public GridLayout(int rows, int cols, int width, int height, int hGap, int vGap, int alignment) {
        setRows(rows);
        setColumns(cols);
        setCellWidth(width);
        setCellHeight(height);
        setAlignment(alignment);
        setGaps(hGap, vGap);
    }

    public GridLayout(int rows, int cols, int width, int height, int alignment) {
        setRows(rows);
        setColumns(cols);
        setCellWidth(width);
        setCellHeight(height);
        setAlignment(alignment);
        setGaps(0, 0);
    }

    public void layout(Panel container, LinkedList layoutComponents) {
        if (container == null || layoutComponents == null) {
            throw new IllegalArgumentException();
        }

        if (container.getComponentCount() == 0) {
            return;
        }

        if (layoutComponents.isEmpty()) {
            return;
        }

        resetContainerDimensions(container, container.getStyle() == null ? 0 : container.getStyle().getCompoundPadding());

        layComponents(layoutComponents, container.getStyle() == null ? 0 : container.getStyle().getCompoundPadding());

    }

    public void setGaps(int hGap, int vGap) {
        if (hGap < 0 || vGap < 0) {
            throw new IllegalArgumentException();
        }
        this.hGap = hGap;
        this.vGap = vGap;
    }

    private void alignY(Component c, int row, int cellHeight, int vGap) {
        if ((alignment & UikitConstant.VCENTER) == UikitConstant.VCENTER) {
            c.y = (row * cellHeight) + (row * vGap) + ((cellHeight - c.getHeight()) / 2);
        } else if ((alignment & UikitConstant.TOP) == UikitConstant.TOP) {
            c.y = row * cellHeight + (row * vGap);
        } else if ((alignment & UikitConstant.BOTTOM) == UikitConstant.BOTTOM) {
            c.y = (row * cellHeight) + (row * vGap) + (cellHeight - c.getHeight());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void alignX(Component c, int col, int cellWidth, int hGap) {
        if ((alignment & UikitConstant.HCENTER) == UikitConstant.HCENTER) {
            c.x = (col * cellWidth) + (col * hGap) + ((cellWidth - c.getWidth()) / 2);
        } else if ((alignment & UikitConstant.LEFT) == UikitConstant.LEFT) {
            c.x = col * cellWidth + (col * hGap);
        } else if ((alignment & UikitConstant.RIGHT) == UikitConstant.RIGHT) {
            c.x = (col * cellWidth) + (col * hGap) + (cellWidth - c.getWidth());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void setComponentSize(Component c, int cellWidth, int cellHeight) {
        int w = c.getWidth();
        int h = c.getHeight();
        if ((c.getFillParent() & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {
            w = cellWidth;
            if (w < c.getMinWidth()) {
                w = c.getMinWidth();
            }
        }
        if ((c.getFillParent() & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
            h = cellHeight;
            if (h < c.getMinHeight()) {
                h = c.getMinHeight();
            }
        }
        c.setSize(w, h);
    }

    private void resetContainerDimensions(Panel container, int padding) {
        int w = (cols * width) + ((padding >>> ComponentStyle.LEFT) & 0xff) + ((padding >>> ComponentStyle.RIGHT) & 0xff) + hGap * (cols - 1);
        int h = (rows * height) + ((padding >>> ComponentStyle.TOP) & 0xff) + ((padding >>> ComponentStyle.BOTTOM) & 0xff) + vGap * (rows - 1);

        container.setSize(w, h, false);
    }

    private void layComponents(LinkedList layoutComponents, int padding) {
        int row;
        Enumeration enu = layoutComponents.elements();
        Component c = null;

        for (row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (enu.hasMoreElements()) {
                    c = (Component) enu.nextElement();
                    setComponentSize(c, width, height);
                    alignY(c, row, height, (row == 0 ? 0 : vGap));
                    alignX(c, col, width, (col == 0 ? 0 : hGap));
                    c.x += ((padding >>> ComponentStyle.LEFT) & 0xff);
                    c.y += ((padding >>> ComponentStyle.TOP) & 0xff);
                }
            }
        }
    }

    public void setRows(int rows) {
        if (rows <= 0) {
            throw new IllegalArgumentException();
        }

        this.rows = rows;
    }

    public void setColumns(int cols) {
        if (cols <= 0) {
            throw new IllegalArgumentException();
        }

        this.cols = cols;
    }

    public void setCellWidth(int width) {

        if (width <= 0) {
            throw new IllegalArgumentException();
        }

        this.width = width;
    }

    public void setCellHeight(int height) {

        if (height <= 0) {
            throw new IllegalArgumentException();
        }

        this.height = height;
    }

    public void setAlignment(int alignment) {

        if (alignment != (UikitConstant.LEFT | UikitConstant.TOP)
                && alignment != (UikitConstant.LEFT | UikitConstant.BOTTOM)
                && alignment != (UikitConstant.LEFT | UikitConstant.VCENTER)
                && alignment != (UikitConstant.HCENTER | UikitConstant.TOP)
                && alignment != (UikitConstant.HCENTER | UikitConstant.BOTTOM)
                && alignment != (UikitConstant.HCENTER | UikitConstant.VCENTER)
                && alignment != (UikitConstant.RIGHT | UikitConstant.TOP)
                && alignment != (UikitConstant.RIGHT | UikitConstant.BOTTOM)
                && alignment != (UikitConstant.RIGHT | UikitConstant.VCENTER)) {
            throw new IllegalArgumentException();
        }

        this.alignment = alignment;
    }
}
