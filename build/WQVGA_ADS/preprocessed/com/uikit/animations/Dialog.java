package com.uikit.animations;

import com.uikit.painters.BgColorPainter;
import com.uikit.painters.BgImagePainter;
import com.uikit.painters.BorderPainter;
import com.uikit.utils.ImageUtil;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.SmartPanel;
import com.uikit.coreElements.Panel;
import com.uikit.coreElements.SystemFont;
import com.uikit.layout.BoxLayout;
import com.uikit.styles.ComponentStyle;
import com.uikit.styles.TextStyle;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public abstract class Dialog extends SmartPanel {

    public static final int COMP_SELF = 0;
    public static final int STATE_NORMAL = 0;
    public static final int EVENT_CANCELED = 0;
    protected boolean bSelected = false;
    private static final int MINIMUM_CONTAINER_HEIGHT = 30;

    public class ContainerPanel extends Panel {

        public static final int COMP_SELF = 0;
        public static final int STATE_NORMAL = 0;
        protected SmartPanel innerFocusManagerPanel;
        private int iMinHeight = 0;
        private int iMaxHeight = 0;

        public SmartPanel getInnerFocusManagerPanel() {
            return this.innerFocusManagerPanel;
        }

        ContainerPanel(int iX, int iY, int iWidth, int iMinHeight, int iMaxHeight) {
            this(iX, iY, iWidth, iMinHeight);
            this.iMinHeight = iMinHeight;
            this.iMaxHeight = iMaxHeight;
        }

        ContainerPanel(int iX, int iY, int iWidth, int iHeight) {
            super(iX, iY, iWidth, iHeight);
            initDefaultStyles();
        }

        public void initInnerPanel(SmartPanel innerPanel) {
            this.innerFocusManagerPanel = innerPanel;
            this.addComponent(innerFocusManagerPanel);
        }

        private void initDefaultStyles() {
            ComponentStyle containerStyle = new ComponentStyle();
            containerStyle.setPadding(3);
            BorderPainter border = new BorderPainter();
            border.setBorderSize(2);
            border.setBorderColor(0x333333);
            containerStyle.addRenderer(border);
            containerStyle.addRenderer(new BgColorPainter(0xffffff));
            setStyle(containerStyle);
        }

        public void setContainerHeight(int iHeight) {
            if ((iMinHeight != 0) || (iMaxHeight != 0)) {
                if (iHeight < iMinHeight) {
                    iHeight = iMinHeight;
                }
                if (iHeight > iMaxHeight) {
                    iHeight = iMaxHeight;
                }
                this.setSize(this.getWidth(), iHeight);
            }
        }
    }
    public ContainerPanel containerPanel;
    public UikitTextBox softKeyButton;
    private Panel softKeyPanel;
    public boolean bShowSoftKey = true;

    public ContainerPanel getContainerPanel() {
        return this.containerPanel;
    }

    public Dialog(int iX, int iY, int iWidth, int iHeight, boolean bShowSoftKey) {
        super(0, 0, UiKitDisplay.getCurrent().getWidth(), UiKitDisplay.getCurrent().getHeight());
        this.bShowSoftKey = bShowSoftKey;
        setLayout(new BoxLayout(UikitConstant.VERTICAL, 0));
        initDefaultStyles();
        int softKeyPanelHeight = 0;
        if (bShowSoftKey == true) {
            softKeyPanelHeight = addSoftKey("Cancel", UikitConstant.RIGHT | UikitConstant.BOTTOM);
            super.setSize(super.getWidth(), super.getHeight() - softKeyPanelHeight);

        }
        //Inner bounds of dialog should take into consideration the padding of the outer bounds
        // and not tresspass into these padding margins.
        int leftPadding = getStyle().getPadding(ComponentStyle.LEFT);
        int rightPadding = getStyle().getPadding(ComponentStyle.RIGHT);
        int topPadding = getStyle().getPadding(ComponentStyle.TOP);
        int botPadding = getStyle().getPadding(ComponentStyle.BOTTOM);
        if (bShowSoftKey == true) {
            botPadding += softKeyPanelHeight;
        }
        if (iX < leftPadding) {
            iX = leftPadding;
        }
        if (iY < topPadding) {
            iY = topPadding;
        }
        if ((iX + iWidth) > (UiKitDisplay.getCurrent().getWidth() - rightPadding)) {
            iWidth = UiKitDisplay.getCurrent().getWidth() - rightPadding - iX;
        }
        if ((iY + iHeight) > (UiKitDisplay.getCurrent().getHeight() - botPadding)) {
            iHeight = UiKitDisplay.getCurrent().getHeight() - botPadding - iY;
        }
        initAutoStretchContainer(iX, iY, iWidth, MINIMUM_CONTAINER_HEIGHT, iHeight);
    }

    protected void initContainer(int iX, int iY, int iWidth, int iHeight) {
        this.containerPanel = new ContainerPanel(iX, iY, iWidth, iHeight);
        super.addComponent(this.containerPanel);
    }

    protected void initAutoStretchContainer(int iX, int iY, int iWidth, int iMinHeight, int iMaxHeight) {
        this.containerPanel = new ContainerPanel(iX, iY, iWidth, iMinHeight, iMaxHeight);
        super.addComponent(this.containerPanel);
    }

    private void initDefaultStyles() {
        ComponentStyle containerStyle = new ComponentStyle();
        containerStyle.setPadding(20);
        BorderPainter br = new BorderPainter();
        br.setBorderColor(0x333333);
        br.setBorderSize(2);
        containerStyle.addRenderer(br);
        Image bg = ImageUtil.generateTransparentImage(60, 60, (byte) 75, 0x555555);
        containerStyle.addRenderer(new BgImagePainter(bg, UikitConstant.REPEAT));
        setStyle(containerStyle);
    }

    public void show() {
        //make sure that this component is the top layer in the TWUIKDisplay.
        UiKitDisplay.getCurrent().removeComponent(this);
        UiKitDisplay.getCurrent().addComponent(this);
        this.setVisibleMode(true);
        //Take focus away from the previous focus manager and give it to this dialog
        UiKitDisplay.getCurrent().setCurrentFocusManager(this);
        if (this.softKeyPanel != null) {
            UiKitDisplay.getCurrent().removeComponent(this.softKeyPanel);
            UiKitDisplay.getCurrent().addComponent(this.softKeyPanel);
            this.softKeyPanel.setVisibleMode(true);
        }
        bSelected = false;
    }

    public void hide() {
        UiKitDisplay.getCurrent().removeComponent(this);
        this.setVisibleMode(false);
        if (this.softKeyPanel != null) {
            UiKitDisplay.getCurrent().removeComponent(this.softKeyPanel);
            this.softKeyPanel.setVisibleMode(false);
        }
    }

    private int addSoftKey(String label, int alignment) {
        SystemFont font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
        TextStyle ts = new TextStyle(font, UikitConstant.HCENTER, 0xffffff, -2);
        //Assuming a panel height of 1.5 times the font height used for the soft key;
        int panelHeight = (ts.getFont().getHeight() * 3) / 2;
        this.softKeyPanel = new Panel(0, UiKitDisplay.getCurrent().getHeight() - panelHeight, iWidth, panelHeight);
        ComponentStyle panelStyle = new ComponentStyle();
        panelStyle.setPadding(3);
        BorderPainter border = new BorderPainter();
        border.setBorderSize(2);
        border.setBorderColor(0x333333);
        panelStyle.addRenderer(border);
        panelStyle.addRenderer(new BgColorPainter(0));
        this.softKeyPanel.setStyle(panelStyle);


        this.softKeyButton = new UikitTextBox(label, ts, null);
        this.softKeyButton.isLayoutable = false;
        this.softKeyPanel.addComponent(this.softKeyButton);
        this.softKeyButton.setPosition(alignment);

        this.softKeyPanel.addComponent(this.softKeyButton);
        this.softKeyPanel.setPosition(UikitConstant.BOTTOM);
        this.softKeyPanel.setVisibleMode(false);
        return panelHeight;
    }

    public void unregisterFocusables(Component c) {
        if (this.containerPanel.innerFocusManagerPanel != null) {
            this.containerPanel.innerFocusManagerPanel.unregisterFocusables(c);
        }
    }

    /**
     * Used to unregister an individual focusable component
     *
     * @param c                 the component to unregister as a focusable element
     */
    public void unregisterFocusable(Component c) {
        if (this.containerPanel.innerFocusManagerPanel != null) {
            this.containerPanel.innerFocusManagerPanel.unregisterFocusable(c);
        }
    }

    public synchronized void removeComponent(Component c) {
        if (this.containerPanel.innerFocusManagerPanel != null) {
            this.containerPanel.innerFocusManagerPanel.removeComponent(c);
        }
    }

    public int indexOfComponent(Component c) {
        return this.containerPanel.innerFocusManagerPanel.indexOfComponent(c);
    }

    public Component componentAt(int index) {
        return this.containerPanel.innerFocusManagerPanel.componentAt(index);
    }

    public void insertComponentAt(Component c, int index) {
        this.containerPanel.innerFocusManagerPanel.insertComponentAt(c, index);
    }

    public void replaceComponent(Component oldC, Component newC) {
        this.containerPanel.innerFocusManagerPanel.replaceComponent(oldC, newC);
    }

    public void removeComponentAt(int index) {
        this.containerPanel.innerFocusManagerPanel.removeComponentAt(index);
    }

    public synchronized void removeAllComponents() {
        this.containerPanel.innerFocusManagerPanel.removeAllComponents();
    }

    public int getComponentCount() {
        if (containerPanel == null) {
            //if there's no Container Panel then there can't be any added components
            return 0;
        }
        if (containerPanel.innerFocusManagerPanel == null) {
            //if there's no inner FocusManagerPanel in the Container Panel then there can't be any added components
            return 0;
        }
        return this.containerPanel.innerFocusManagerPanel.getComponentCount();

    }

    public boolean onKeyPressed(int iKeyCode) {
        boolean isHandled = super.onKeyPressed(iKeyCode);
        if (!isHandled) {
            int key = UiKitDisplay.getGameAction(iKeyCode);
            if (key == Canvas.UP || key == Canvas.DOWN) {
                isHandled = true;
            }
        }
        return isHandled;
    }
}
