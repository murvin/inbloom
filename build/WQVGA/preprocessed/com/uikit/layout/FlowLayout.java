package com.uikit.layout;

import com.uikit.datastructures.LinkedList;
import com.uikit.styles.ComponentStyle;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.Panel;
import java.util.Enumeration;

public class FlowLayout implements ILayout {

    private int orientation;
    private int alignment;
    private int hGap;
    private int vGap;

    public FlowLayout(int orientation, int alignment, int hGap, int vGap) {
        setOrientation(orientation);
        setAlignment(alignment);
        setHGap(hGap);
        setVGap(vGap);
    }

    public void setHGap(int hGap) {
        if (hGap < 0) {
            throw new IllegalArgumentException();
        }

        this.hGap = hGap;
    }

    public void setVGap(int vGap) {
        if (vGap < 0) {
            throw new IllegalArgumentException();
        }

        this.vGap = vGap;
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

    public void setOrientation(int orientation) {
        if (orientation == UikitConstant.VERTICAL || orientation == UikitConstant.HORIZONTAL) {
        } else {
            throw new IllegalArgumentException();
        }
        this.orientation = orientation;
    }

    public void layout(Panel container, LinkedList layoutComponents) {

        if (container == null || layoutComponents == null) {
            throw new IllegalArgumentException();
        }
        if (container.getComponentCount() == 0) {
            return;
        }

        int dimension = 0;
        int padding = container.getStyle() == null ? 0 : container.getStyle().getCompoundPadding();

        if ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {

            dimension = container.getHeight() - (((padding >>> ComponentStyle.TOP) & 0xff) + ((padding >>> ComponentStyle.BOTTOM) & 0xff));
        } else if ((orientation & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {

            dimension = container.getWidth() - (((padding >>> ComponentStyle.LEFT) & 0xff) + ((padding >>> ComponentStyle.RIGHT) & 0xff));
        }

        layComponents(container, layoutComponents, dimension, (padding >>> ComponentStyle.LEFT) & 0xff, (padding >>> ComponentStyle.TOP) & 0xff);
    }

    private void layComponents(Panel container, LinkedList components, int dimension, int startX, int startY) {

        if (components.isEmpty()) {
            return;
        }

        Enumeration enu;
        Component temp;
        int cummuDimension, i, maxDimension, tempSize, rest = 0, fillSize = 0, sX, sY;
        boolean isFillParent = false;
        LinkedList subList = new LinkedList(), fill = new LinkedList();

        for (enu = components.elements(), cummuDimension = 0, i = 0, maxDimension = 0; enu.hasMoreElements(); i++) {

            temp = ((Component) enu.nextElement());

            //determine if component fills parent in alignment direction
            isFillParent = ((orientation & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) ? (temp.getFillParent() & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL : (temp.getFillParent() & UikitConstant.VERTICAL) == UikitConstant.VERTICAL;

            tempSize = isFillParent ? ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) ? temp.getMinHeight() : temp.getMinWidth() : ((alignment & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) ? temp.getHeight() : temp.getWidth();
            cummuDimension += (i == 0 ? tempSize
                    : ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL ? tempSize + vGap : tempSize + hGap));
            if (cummuDimension <= dimension || subList.isEmpty()) {
                subList.addElement(temp);
                if (isFillParent) {
                    rest += tempSize;
                    fill.addElement(temp);
                }
                maxDimension = Math.max(maxDimension, ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL ? temp.getWidth() : temp.getHeight()));
            } else {
                cummuDimension -= (i == 0 ? tempSize
                        : ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL ? tempSize + vGap : tempSize + hGap));
                break;
            }
        }

        rest += dimension - cummuDimension;
        boolean bCheck = true;

        if (fill.size() > 0) {
            loop:
            while (bCheck) {
                for (enu = fill.elements(); enu.hasMoreElements();) {
                    temp = (Component) enu.nextElement();
                    tempSize = ((orientation & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) ? temp.getMinWidth() : temp.getMinHeight();
                    if (rest / fill.size() < tempSize) {
                        fill.removeElement(temp);
                        rest -= tempSize;
                        continue loop;
                    }
                }
                bCheck = false;
            }
            fillSize = rest / fill.size();
        }

        maxDimension = setSizes(subList, maxDimension, fillSize);

        resetContainer(container, maxDimension, startX, startY);

        if (!fill.isEmpty()) {
            layout(subList, maxDimension, startX, startY);
        } else {
            sX = startX;
            sY = startY;
            if ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
                if ((alignment & UikitConstant.BOTTOM) == UikitConstant.BOTTOM) {
                    sY = startY + dimension - cummuDimension;
                } else if ((alignment & UikitConstant.VCENTER) == UikitConstant.VCENTER) {
                    sY = startY + (dimension - cummuDimension) / 2;
                }
            } else {
                if ((alignment & UikitConstant.RIGHT) == UikitConstant.RIGHT) {
                    sX = startX + dimension - cummuDimension;
                } else if ((alignment & UikitConstant.HCENTER) == UikitConstant.HCENTER) {
                    sX = startX + (dimension - cummuDimension) / 2;
                } else if ((alignment & UikitConstant.HCENTER) == UikitConstant.HCENTER) {
                    sX = startX + (dimension - cummuDimension) / 2;
                }
            }
            layout(subList, maxDimension, sX, sY);
        }

        if ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
            startX += (maxDimension + hGap);
        } else {
            startY += (maxDimension + vGap);
        }

        filterComponents(components, subList);

        layComponents(container, components, dimension, startX, startY);

    }

    private void filterComponents(LinkedList components, LinkedList layoutComponents) {
        for (Enumeration enu = layoutComponents.elements(); enu.hasMoreElements();) {
            components.removeElement(enu.nextElement());
        }
    }

    private void resetContainer(Panel container, int maxDimension, int startX, int startY) {
        int width = container.getWidth();
        int height = container.getHeight();
        if ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
            width = startX + maxDimension + (container.getStyle() == null ? 0 : container.getStyle().getPadding(ComponentStyle.RIGHT));
        } else {
            height = startY + maxDimension + (container.getStyle() == null ? 0 : container.getStyle().getPadding(ComponentStyle.BOTTOM));
        }
        container.setSize(width, height, false);
    }

    private int setSizes(LinkedList components, int maxDimension, int fillSize) {
        Component c;
        Enumeration enu;
        for (enu = components.elements(); enu.hasMoreElements();) {
            c = (Component) enu.nextElement();
            int w = c.getWidth();
            int h = c.getHeight();

            if ((orientation & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {

                if ((c.getFillParent() & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {
                    w = fillSize > c.getMinWidth() ? fillSize : c.getMinWidth();
                }
                if ((c.getFillParent() & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
                    h = maxDimension > c.getMinHeight() ? maxDimension : c.getMinWidth();
                }
                c.setSize(w, h);
            } else {

                if ((c.getFillParent() & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {
                    h = fillSize > c.getMinHeight() ? fillSize : c.getMinWidth();
                }
                if ((c.getFillParent() & UikitConstant.VERTICAL) == UikitConstant.VERTICAL) {
                    w = maxDimension > c.getMinWidth() ? maxDimension : c.getMinWidth();
                }
                c.setSize(w, h);
            }

        }
        for (enu = components.elements(); enu.hasMoreElements();) {
            c = (Component) enu.nextElement();
            maxDimension = Math.max(maxDimension, ((orientation & UikitConstant.VERTICAL) == UikitConstant.VERTICAL ? c.getWidth() : c.getHeight()));
        }

        return maxDimension;
    }

    private void layout(LinkedList components, int maxDimension, int startX, int startY) {
        Component prev, c;
        Enumeration enu;
        for (enu = components.elements(), prev = null; enu.hasMoreElements(); prev = c) {

            c = (Component) enu.nextElement();

            if ((orientation & UikitConstant.HORIZONTAL) == UikitConstant.HORIZONTAL) {

                if ((alignment & UikitConstant.VCENTER) == UikitConstant.VCENTER) {
                    c.y = startY + ((maxDimension - c.getHeight()) / 2);
                } else if ((alignment & UikitConstant.BOTTOM) == UikitConstant.BOTTOM) {
                    c.y = startY + (maxDimension - c.getHeight());
                } else {
                    c.y = startY;
                }
                c.x = prev == null ? startX : prev.x + prev.getWidth() + hGap;

            } else {

                if ((alignment & UikitConstant.HCENTER) == UikitConstant.HCENTER) {
                    c.x = startX + ((maxDimension - c.getWidth()) / 2);
                } else if ((alignment & UikitConstant.RIGHT) == UikitConstant.RIGHT) {
                    c.x = startX + (maxDimension - c.getWidth());
                } else {
                    c.x = startX;
                }
                c.y = prev == null ? startY : prev.y + prev.getHeight() + vGap;
            }
        }
    }
}
