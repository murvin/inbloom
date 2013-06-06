package com.inbloom.ui.components;

import com.inbloom.utils.GlobalResources;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;

//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//# import com.inbloom.utils.Utils;
//#endif 
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.Panel;
//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.coreElements.UikitFont;
import com.uikit.layout.GridLayout;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//# import javax.microedition.lcdui.Image;
//#endif 
public class MapKeys extends Panel {

    private int padding;
    private int cellWidth, cellHeight;
    private UikitFont font;
    private int txtLabelColour;
    private int gap;
    
    public MapKeys(int width, int height) {
        super(width, height);
        initResources();

        setLayout(new GridLayout(2, 2, cellWidth, cellHeight, UikitConstant.HCENTER | UikitConstant.VCENTER));

        getStyle(true).setPadding(padding / 4, padding, padding / 2, padding);

        initComponents();
    }

    private void initResources() {
        
        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#         Image imgFont = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_SMALL);
//#         font = new BitmapFont(imgFont, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_SMALL, 0);
        //#elif WVGA
//#         font = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        //#endif 
        
        padding = 6 * UiKitDisplay.getWidth() / 100;
        cellWidth = (iWidth - (padding * 2)) / 2;
        cellHeight = iHeight / 2;
        gap = 2 * UiKitDisplay.getWidth() / 100;
        txtLabelColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MAPKEY_TEXT_COLOR));
    }

    private void initComponents() {
        MapKey mostFertile = new MapKey(cellWidth, cellHeight, Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MAPKEY_FERTILE_COLOR)), txtLabelColour, Resources.getInstance().getText(GlobalResources.TXT_MAPKEY_MOSTFERTILE));
        setCommonAttrs(mostFertile);
        addComponent(mostFertile);

        MapKey today = new MapKey(cellWidth, cellHeight, Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MAPKEY_TODAY_COLOR)), txtLabelColour, Resources.getInstance().getText(GlobalResources.TXT_MAPKEY_TODAY));
        setCommonAttrs(today);
        addComponent(today);

        MapKey ovulation = new MapKey(cellWidth, cellHeight, Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MAPKEY_OVULATION_COLOR)), txtLabelColour, Resources.getInstance().getText(GlobalResources.TXT_MAPKEY_OVULATION));
        setCommonAttrs(ovulation);
        addComponent(ovulation);

        MapKey period = new MapKey(cellWidth, cellHeight, Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_MAPKEY_PERIOD_COLOR)), txtLabelColour, Resources.getInstance().getText(GlobalResources.TXT_MAPKEY_PERIOD));
        setCommonAttrs(period);
        addComponent(period);
    }

    private void setCommonAttrs(MapKey key) {
        key.setFont(font);
        key.setGap(gap);
    }

    class MapKey extends Component {

        private int colour;
        private int textColour;
        private String label;
        private UikitFont font;
        /** Gap between text and box */
        private int gap;

        public MapKey(int width, int height, int colour, int textColour, String label) {
            super(width, height);
            this.colour = colour;
            this.textColour = textColour;
            this.label = label;
        }

        public void setFont(UikitFont font) {
            this.font = font;
        }

        public void setGap(int gap) {
            this.gap = gap;
        }

        protected void drawCurrentFrame(Graphics g) {
            g.setColor(colour);
            g.fillRect(gap, gap, iHeight - (gap * 2), iHeight - (gap * 2));
            g.setColor(textColour);
            if (font != null) {
                font.drawString(g, label, iHeight, (iHeight - font.getHeight()) / 2, 20);
            }
        }
    }
}
