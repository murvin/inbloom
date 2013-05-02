package com.uikit.animations;

import com.uikit.utils.UikitConstant;
import com.uikit.styles.Style;
import com.uikit.styles.TextStyle;

public class AlertDialog extends InfoDialog {

    private String alertText;
    protected UikitTextBox alertTextbox;
    public static final int COMP_TEXT = 0x201;

    public AlertDialog(int iX, int iY, int iWidth, int iHeight, String txt) {
        super(iX, iY, iWidth, iHeight);

        /* */
        if (txt != null) {
            alertText = txt;
        } else {
            alertText = "";
        }

        alertTextbox = new UikitTextBox(9 * iWidth / 10, alertText);
        containerPanel.addComponent(alertTextbox);

        alertTextbox.x = (containerPanel.getWidth() - alertTextbox.getWidth()) / 2;
        alertTextbox.y = (containerPanel.getHeight() - alertTextbox.getHeight()) / 2;

        initDefaultStyles();
    }

    private void initDefaultStyles() {
        TextStyle txtStyle = new TextStyle();
        txtStyle.setAlign(UikitConstant.HCENTER);
        setStyle(COMP_TEXT, txtStyle);
    }

    public void setStyle(int component, Style style) {
        if (style != null) {
            switch (component) {
                case COMP_TEXT: {
                    alertTextbox.setTextStyle((TextStyle) style);
                    break;
                }
            }
        }
    }

    public void setAlertText(String txt) {
        if (txt == null) {
            return;
        }

        alertText = txt;
        alertTextbox.setText(alertText);

        alertTextbox.y = (containerPanel.getHeight() - alertTextbox.getHeight()) / 2;
    }
}
