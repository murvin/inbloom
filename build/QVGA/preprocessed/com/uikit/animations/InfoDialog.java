
package com.uikit.animations;

import com.uikit.utils.ImageUtil;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.Panel;
import com.uikit.layout.FlowLayout;
import com.uikit.styles.TextStyle;
import javax.microedition.lcdui.Image;


public class InfoDialog extends Dialog {

    private int dialogWidth;
    protected String title = null;
    protected UikitTextBox titleTBox = null;
    protected Image icon = null;
    protected UikitImageBox iconImage = null;
    protected Panel btnPanel = null;

    public InfoDialog(int iX, int iY, int iWidth, int iHeight) {
        super(iX, iY, iWidth, iHeight, false);
        containerPanel.setSize(iWidth, iHeight);
        dialogWidth = iWidth;

        setIcon(ImageUtil.generateTransparentImage(22, 22, (byte) 100, 0x0));
    }

    public void setTitle(String str) {
        if (str == null) {
            return;
        }

        title = str;
        if (titleTBox == null) {
            titleTBox = new UikitTextBox(iWidth * 90 / 100, str);
            containerPanel.addComponent(titleTBox);
        } else {
            titleTBox.setText(title);
        }
        
        titleTBox.x = (containerPanel.getWidth() - titleTBox.getWidth()) / 2;
        titleTBox.y = iconImage.y;

    }

    public void setIcon(Image img) {
        if (img == null) {
            return;
        }

        icon = img;

        if (iconImage == null) {
            iconImage = new UikitImageBox(img);
            containerPanel.addComponent(iconImage);
        }
        iconImage.setImage(img);

        iconImage.x = iconImage.getWidth() / 3;
        iconImage.y = iconImage.x;

    }

    
    public void setTitleTextStyle(TextStyle style) {
        if (style == null) {
            return;
        }
        titleTBox.setTextStyle(style);
    }

    public TextStyle getTitleTextStyle() {
        return titleTBox.getTextStyle();
    }

    public void addButton(UikitButton btn) {

        if (btn == null) {
            return;
        }
        
        if (btnPanel == null) {
            btnPanel = new Panel(dialogWidth, 0);
            btnPanel.setSize(dialogWidth, 5 * btn.getHeight() / 4);
            FlowLayout layout = new FlowLayout(UikitConstant.HORIZONTAL,
                    UikitConstant.HCENTER | UikitConstant.VCENTER,
                    btn.getWidth() / 10, btn.getHeight() / 10);
            btnPanel.setLayout(layout);
            btnPanel.y = containerPanel.getHeight() - btnPanel.getHeight() - 10;
            containerPanel.addComponent(btnPanel);
        }

        btnPanel.addComponent(btn);
        btnPanel.y = containerPanel.getHeight() - btnPanel.getHeight() - 10;
    }

    public void removeButton(UikitButton btn) {
        if (btnPanel != null && btn != null) {
            btnPanel.removeComponent(btn);
            btnPanel.y = containerPanel.getHeight() - 10 * btnPanel.getHeight() / 9;
        }
    }
}
