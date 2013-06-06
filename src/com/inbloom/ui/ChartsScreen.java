package com.inbloom.ui;

import com.inbloom.InBloomController;
import com.inbloom.model.Coordinates;
//#if ADS
//# import com.inbloom.ui.components.AdvertComponent;
//#endif
import com.inbloom.ui.components.Chart;
import com.inbloom.ui.components.ChartKeysPanel;
import com.inbloom.ui.components.MonthSelector;
import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;
import com.inbloom.utils.Utils;

import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.ITouchEventListener;

//#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//# import com.uikit.coreElements.BitmapFont;
//#elif WVGA
//# import com.uikit.coreElements.SystemFont;
//#endif 
import com.uikit.layout.BoxLayout;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class ChartsScreen extends InBloomScreen {

    private MonthSelector monthSelector;
    private int mnth;
    private int year;
    private ChartKeysPanel chartKeys;
    private Chart chart;
    private int leftYAxisColour;
    private int rightYAxisColour;
    private int xAxisColour;
    private int xAxisQty;
    private String leftYAxisLabel;
    private String rightYAxisLabel;
    private Coordinates[] leftYAxisValues;
    private Coordinates[] rightYAxisValues;
    private int chartLineColour;
    private int chartWeekFillColour;
    
    //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#     private BitmapFont
    //#elif WVGA
//#     private SystemFont
    //#endif 
     valueFont;
    
    //#if WVGA 
//#     private int fontColour;
    //#endif
    
    //#if ADS
//#     private final int ADVERT_H = 40;
//#     private AdvertComponent ad;
    //#endif 

    public ChartsScreen() {
        setIsScrollable(true);
        initResources();
        initComponents();

    }

    private void initResources() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        mnth = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        leftYAxisColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CHARTKEY_TEMP));
        rightYAxisColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CHARTKEY_WEIGHT));
        xAxisColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CHART_XAXIS_COLOUR));

        leftYAxisLabel = Utils.getCurrentTempUnit();
        rightYAxisLabel = Utils.getCurrentWeightUnit();

        chartLineColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_BORDER_COLOR));
        chartWeekFillColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CHARTK_WEEK_FILL_COLOUR));

        //#if QVGA || WQVGA || QVGA_ADS || WQVGA_ADS
//#         Image imgFontSmall = Resources.getInstance().getThemeImage(GraphicsResources.FONT_THEME_SMALL);
//#         valueFont = new BitmapFont(imgFontSmall, Utils.FONT_CHARS, Font.STYLE_PLAIN, Font.SIZE_SMALL, 0);
        //#elif WVGA
//#         fontColour = Integer.parseInt(Resources.getInstance().getThemeStr(GraphicsResources.TXT_CAL_HEADER_TEXT_COLOR));
//#         valueFont = new SystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        //#endif 
        
        
        
        padding = 4 * UiKitDisplay.getWidth() / 100;
    }

    private void initChart() {
        int chartWidth = iWidth;
        int chartHeight = 65 * chartWidth / 100;
        xAxisQty = Utils.getMonthLength(year, mnth);
        leftYAxisValues = Utils.getCoordinates(Utils.ENTRY_TEMP, mnth, year);
        rightYAxisValues = Utils.getCoordinates(Utils.ENTRY_WEIGHT, mnth, year);
        chart = new Chart(leftYAxisColour, rightYAxisColour, xAxisColour, xAxisQty, leftYAxisLabel, rightYAxisLabel, leftYAxisValues, rightYAxisValues, chartLineColour, chartWidth, chartHeight, chartWeekFillColour, valueFont);
    }

    private void initComponents() {
        setLayout(new BoxLayout(UikitConstant.VERTICAL, 0));
        //#if ADS
//#         ad = new AdvertComponent(iWidth - (padding * 2), ADVERT_H, InBloomController.myMidlet, null);
//#         ad.downloadAd();
//#         addComponent(ad);
        //#endif
        
        
        this.monthSelector = new MonthSelector(iWidth, 9 * UiKitDisplay.getHeight() / 100, false);
        this.monthSelector.setCurrent(mnth, year);
        this.monthSelector.setEventListener(this);
        addComponent(monthSelector);


        initChart();
        chart.setEventListener(this);
        addComponent(chart);
        chart.x = UiKitDisplay.getWidth();
        chart.enter();


        chartKeys = new ChartKeysPanel(iWidth, 30);
        //#if WVGA 
//#         chartKeys.expandToFitContent();
        //#endif 
        addComponent(chartKeys);
        updateBottomOffset();

        int topPadding = 0;
        //#if ADS
//#         topPadding = padding;
        //#endif
        getStyle(true).setPadding(topPadding, padding, bottomPadding, padding);
    }

    private void enterChart(boolean isNext) {
        Chart temp = chart;
        initChart();
        chart.x = isNext ? UiKitDisplay.getWidth() : -UiKitDisplay.getWidth();
        chart.y = temp.y;
        chart.isLayoutable = false;
        chart.setEventListener(this);
        insertComponentAt(chart, 1, false);

        if (isNext) {
            temp.exit();
        } else {
            temp.exitToRight();
        }

        chart.enter();

    }

    public void onComponentEvent(Component c, int e, Object o, int p) {
        super.onComponentEvent(c, e, o, p);
        if (c == monthSelector) {
            mnth = e;
            year = p;
            enterChart(((Boolean) o).booleanValue());
        } else if (c instanceof Chart) {
            if (e == Chart.EXIT_FINISHED) {
                c.isLayoutable = false;
                removeComponent(c, false);
            } else if (e == Chart.ENTER_FINISHED) {
                chart.isLayoutable = true;
                chart.setEventListener(this);
            }
        }
    }

    public boolean onDrag(int type, int iStartX, int iStartY, int iDeltaX, int iDeltaY) {
        boolean isHandled = super.onDrag(type, iStartX, iStartY, iDeltaX, iDeltaY);
        if (type == ITouchEventListener.DRAG_RELEASE) {
            if (iDeltaX < 0) {
                monthSelector.next();
            } else if (iDeltaX > 0) {
                monthSelector.previous();
            }
        }

        return isHandled;
    }
}
