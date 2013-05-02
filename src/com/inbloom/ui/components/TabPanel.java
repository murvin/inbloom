package com.inbloom.ui.components;

import com.inbloom.utils.GraphicsResources;
import com.inbloom.utils.Resources;

import com.uikit.motion.MotionLinear;
import com.uikit.utils.UikitConstant;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.SmartPanel;
import com.uikit.coreElements.IComponentEventListener;
import com.uikit.layout.BoxLayout;

import com.uikit.painters.PatchPainter;
import javax.microedition.lcdui.Image;

public class TabPanel extends SmartPanel {

    private Image bg;
    private Image imgOnFocusBg;
    private Image imgCal, imgChart, imgList, imgSettings;
    private Image imgCalLabel, imgChartLabel, imgListLabel, imgSettingsLabel;
    public static final int TAB_CAL = 0x001;
    public static final int TAB_LIST = 0x002;
    public static final int TAB_CHARTS = 0x003;
    public static final int TAB_SETTINGS = 0x004;
    private Tab tabCal, tabList, tabCharts, tabSettings;

    public TabPanel(IComponentEventListener cel) {
        super(UiKitDisplay.getWidth(), 0);
        this.cel = cel;
        initResources();

        getStyle(true).addRenderer(new PatchPainter(bg, 0, 5, 0, 5));
        setSize(iWidth, bg.getHeight());

        setLayout(new BoxLayout(UikitConstant.HORIZONTAL, 0));
        addTabs();
        setIsScrollable(false);

    }

    private void initResources() {
        this.bg = Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG);
        this.imgCal = Resources.getInstance().getThemeImage(GraphicsResources.TAB_CAL);
        this.imgList = Resources.getInstance().getThemeImage(GraphicsResources.TAB_LIST);
        this.imgChart = Resources.getInstance().getThemeImage(GraphicsResources.TAB_CHARTS);
        this.imgSettings = Resources.getInstance().getThemeImage(GraphicsResources.TAB_SETTINGS);
        this.imgOnFocusBg = Resources.getInstance().getThemeImage(GraphicsResources.IMG_SELECTED_TAB);
    }

    public void loadTabLabel(String locale) {
        int calLabelId = -1;
        int listLabelId = -1;
        int chartsLabelId = -1;
        int settingsLabelId = -1;

        if (locale.equals("en-GB")) {
            calLabelId = GraphicsResources.IMG_TABLABEL_CAL_EN;
            listLabelId = GraphicsResources.IMG_TABLABEL_LIST_EN;
            chartsLabelId = GraphicsResources.IMG_TABLABEL_CHARTS_EN;
            settingsLabelId = GraphicsResources.IMG_TABLABEL_SETTINGS_EN;
        } else if (locale.equals("fr-FR")) {
            calLabelId = GraphicsResources.IMG_TABLABEL_CAL_FR;
            listLabelId = GraphicsResources.IMG_TABLABEL_LIST_FR;
            chartsLabelId = GraphicsResources.IMG_TABLABEL_CHARTS_FR;
            settingsLabelId = GraphicsResources.IMG_TABLABEL_SETTINGS_FR;
        } else if (locale.equals("es-ES")) {
            calLabelId = GraphicsResources.IMG_TABLABEL_CAL_ES;
            listLabelId = GraphicsResources.IMG_TABLABEL_LIST_ES;
            chartsLabelId = GraphicsResources.IMG_TABLABEL_CHARTS_ES;
            settingsLabelId = GraphicsResources.IMG_TABLABEL_SETTINGS_ES;
        } else if (locale.equals("de-DE")) {
            calLabelId = GraphicsResources.IMG_TABLABEL_CAL_DE;
            listLabelId = GraphicsResources.IMG_TABLABEL_LIST_DE;
            chartsLabelId = GraphicsResources.IMG_TABLABEL_CHARTS_DE;
            settingsLabelId = GraphicsResources.IMG_TABLABEL_SETTINGS_DE;
        } else if (locale.equals("it-IT")) {
            calLabelId = GraphicsResources.IMG_TABLABEL_CAL_IT;
            listLabelId = GraphicsResources.IMG_TABLABEL_LIST_IT;
            chartsLabelId = GraphicsResources.IMG_TABLABEL_CHARTS_IT;
            settingsLabelId = GraphicsResources.IMG_TABLABEL_SETTINGS_IT;
        }

        this.imgCalLabel = Resources.getInstance().getThemeImage(calLabelId);
        this.imgListLabel = Resources.getInstance().getThemeImage(listLabelId);
        this.imgChartLabel = Resources.getInstance().getThemeImage(chartsLabelId);
        this.imgSettingsLabel = Resources.getInstance().getThemeImage(settingsLabelId);
        updateAllLabels();
    }

    private void updateAllLabels() {
        tabCal.setImgLabel(imgCalLabel);
        tabList.setImgLabel(imgListLabel);
        tabCharts.setImgLabel(imgChartLabel);
        tabSettings.setImgLabel(imgSettingsLabel);
    }

    public void updateBg() {
        this.bg = Resources.getInstance().getThemeImage(GraphicsResources.TAB_BG);
        getStyle().clearAllRenderers();
        getStyle().addRenderer(new PatchPainter(bg, 0, 5, 0, 5));
    }

    private void addTabs() {
        int tabWidth = iWidth / 4;
        int tabHeight = iHeight * 90 / 100;
        addComponent(tabCal = new Tab(imgCal, TAB_CAL, tabWidth, tabHeight, imgCalLabel));
        tabCal.setEventListener(cel);
        tabCal.setImgOnFocusBg(imgOnFocusBg);
        addComponent(tabList = new Tab(imgList, TAB_LIST, tabWidth, tabHeight, imgListLabel));
        tabList.setEventListener(cel);
        tabList.setImgOnFocusBg(imgOnFocusBg);
        addComponent(tabCharts = new Tab(imgChart, TAB_CHARTS, tabWidth, tabHeight, imgChartLabel));
        tabCharts.setEventListener(cel);
        tabCharts.setImgOnFocusBg(imgOnFocusBg);
        addComponent(tabSettings = new Tab(imgSettings, TAB_SETTINGS, tabWidth, tabHeight, imgSettingsLabel));
        tabSettings.setEventListener(cel);
        tabSettings.setImgOnFocusBg(imgOnFocusBg);
    }

    public void enter() {
        MotionLinear mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, x, 0, 10, 0.0f, 0.0f);
        this.motionFX = mfx_slide;
    }

    public void exit() {
        MotionLinear mfx_slide = new MotionLinear(1, MotionLinear.MOFX_LINEAR_PULLBACK);
        ((MotionLinear) mfx_slide).init(x, y, x, -iHeight, 10, 0.0f, 0.0f);
        this.motionFX = mfx_slide;
    }
}
