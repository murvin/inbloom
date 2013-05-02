package com.inbloom.ui.components;

import com.inbloom.model.Coordinates;

import com.uikit.motion.IMotionListener;
import com.uikit.motion.MotionEaseOutExpo;
import com.uikit.motion.Motion;
import com.uikit.coreElements.UiKitDisplay;
import com.uikit.coreElements.Component;
import com.uikit.coreElements.BitmapFont;
import com.uikit.coreElements.SystemFont;
import com.uikit.coreElements.UikitFont;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Chart extends Component implements IMotionListener {

    int leftYAxisColour;
    int rightYAxisColour;
    int xAxisColour;
    int xAxisQty;
    String leftYAxisLabel;
    String rightYAxisLabel;
    Coordinates[] leftYAxisValues;
    Coordinates[] rightYAxisValues;
    int chartLineColour;
    int chartWeekFillColour;
    // working variables
    private int chartWidth;
    private int chartHeight;
    private int xChart;
    private int yChart;
    private MotionEaseOutExpo mfx_slide;
    public static final int EXIT_FINISHED = 0x1101;
    public static final int ENTER_FINISHED = 0x1102;
    private int chartLineInterval;
    private int chartLineYStart;
    private int dayPxLength;
    private int xWeekDelimiter;
    private UikitFont labelFont;
    private BitmapFont valueFont;
    private String[] dayValues;
    private float leftYAxisMin, leftYAxisMax;
    private float rightYAxisMin, rightYAxisMax;
    private String leftYAxisMinVal, leftYAxisMidVal, leftYAxisMaxVal;
    private String rightYAxisMinVal, rightYAxisMidVal, rightYAxisMaxVal;
    final int YSTARTOFFSET = 3;

    public Chart(int leftYAxisColour,
            int rightYAxisColour,
            int xAxisColour,
            int xAxisQty,
            String leftYAxisLabel,
            String rightYAxisLabel,
            Coordinates[] leftYAxisValues,
            Coordinates[] rightYAxisValues,
            int chartLineColour,
            int width,
            int height,
            int chartWeekFillColour,
            BitmapFont valueFont) {
        super(width, height);
        this.leftYAxisColour = leftYAxisColour;
        this.rightYAxisColour = rightYAxisColour;
        this.xAxisColour = xAxisColour;
        this.xAxisQty = xAxisQty;
        this.leftYAxisLabel = leftYAxisLabel;
        this.rightYAxisLabel = rightYAxisLabel;
        this.leftYAxisValues = leftYAxisValues;
        this.rightYAxisValues = rightYAxisValues;
        this.chartLineColour = chartLineColour;
        this.chartWeekFillColour = chartWeekFillColour;
        this.valueFont = valueFont;

        initVariables();
    }

    private void initVariables() {
        chartWidth = iWidth * 76 / 100;
        chartHeight = iHeight * 88 / 100;

        xChart = (iWidth - chartWidth) / 2;
        yChart = 0;

        chartLineYStart = yChart + YSTARTOFFSET;
        chartLineInterval = (chartHeight - chartLineYStart) / 6;

        dayPxLength = (chartWidth / xAxisQty) + (xAxisQty == 31 ? 1 : 0);

        labelFont = new SystemFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        dayValues = new String[]{"0", "7", "14", "21", "28"};

        if (leftYAxisValues != null && leftYAxisValues.length > 0) {
            leftYAxisMin = leftYAxisValues[0].getY();
            leftYAxisMax = leftYAxisMin;
            for (int i = 0; i < leftYAxisValues.length; i++) {
                Coordinates coordinates = leftYAxisValues[i];
                if (coordinates.getY() < leftYAxisMin) {
                    leftYAxisMin = coordinates.getY();
                } else if (coordinates.getY() > leftYAxisMax) {
                    leftYAxisMax = coordinates.getY();
                }
            }
            float leftYAxisMid = (leftYAxisMax + leftYAxisMin) / 2;

            leftYAxisMinVal = formatTo1Decimal(String.valueOf(leftYAxisMin));
            leftYAxisMidVal = formatTo1Decimal(String.valueOf(leftYAxisMid));
            leftYAxisMaxVal = formatTo1Decimal(String.valueOf(leftYAxisMax));
        }

        if (rightYAxisValues != null && rightYAxisValues.length > 0) {
            rightYAxisMin = rightYAxisValues[0].getY();
            rightYAxisMax = rightYAxisMin;
            for (int i = 0; i < rightYAxisValues.length; i++) {
                Coordinates coordinates = rightYAxisValues[i];
                if (coordinates.getY() < rightYAxisMin) {
                    rightYAxisMin = coordinates.getY();
                } else if (coordinates.getY() > rightYAxisMax) {
                    rightYAxisMax = coordinates.getY();
                }
            }
            float rightYAxisMid = (rightYAxisMax + rightYAxisMin) / 2;

            rightYAxisMinVal = formatTo1Decimal(String.valueOf(rightYAxisMin));
            rightYAxisMidVal = formatTo1Decimal(String.valueOf(rightYAxisMid));
            rightYAxisMaxVal = formatTo1Decimal(String.valueOf(rightYAxisMax));
        }
    }

    private String formatTo1Decimal(String str) {
        String s = str;
        int idx = str.indexOf(".");
        if (idx != -1) {
            try {
                s = str.substring(0, idx + 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    protected void drawCurrentFrame(Graphics g) {

        // fill weeks rectangles
        g.setColor(chartWeekFillColour);
        xWeekDelimiter = 0;
        for (int i = 0; i < 2; i++) {
            g.fillRect(xChart + xWeekDelimiter, yChart + YSTARTOFFSET, (dayPxLength * 7), chartHeight - 3);
            xWeekDelimiter += (dayPxLength * 14);
        }


        // draw chart axes
        g.setColor(leftYAxisColour);
        g.drawLine(xChart, yChart, xChart, yChart + chartHeight);
        g.setColor(xAxisColour);
        g.drawLine(xChart, yChart + chartHeight, xChart + chartWidth, yChart + chartHeight);
        g.setColor(rightYAxisColour);
        g.drawLine(xChart + chartWidth, yChart + chartHeight, xChart + chartWidth, yChart);


        // draw chart decorative horz line
        chartLineYStart = yChart + YSTARTOFFSET;
        g.setColor(chartLineColour);
        for (int i = 0; i < 6; i++, chartLineYStart += chartLineInterval) {
            g.drawLine(xChart, chartLineYStart, xChart + chartWidth, chartLineYStart);
        }

        // draw weeks x-axis little delimiters
        g.setColor(xAxisColour);
        xWeekDelimiter = 0;
        for (int i = 0; i < 5; i++) {
            g.drawLine(xChart + xWeekDelimiter, yChart + chartHeight - YSTARTOFFSET, xChart + xWeekDelimiter, yChart + chartHeight);
            xWeekDelimiter += (dayPxLength * 7);
        }

        // draw axes labels
        g.setColor(leftYAxisColour);
        labelFont.drawString(g, leftYAxisLabel, xChart - YSTARTOFFSET - labelFont.stringWidth(leftYAxisLabel), 0, 20);

        g.setColor(rightYAxisColour);
        labelFont.drawString(g, rightYAxisLabel, xChart + chartWidth + YSTARTOFFSET, 0, 20);

        // draw x axis values
        xWeekDelimiter = 0;
        for (int i = 0; i < 5; i++) {
            valueFont.drawString(g, dayValues[i], xChart + xWeekDelimiter - (valueFont.stringWidth(dayValues[i])) / 2, yChart + chartHeight, 20);
            xWeekDelimiter += (dayPxLength * 7);
        }

        if (leftYAxisValues != null) {
            drawLeftYAxisLabelValues(g);
            drawLeftGraph(g, leftYAxisColour, leftYAxisValues);
        }

        if (rightYAxisValues != null) {
            drawRightYAxisLabelValues(g);
            drawRightGraph(g, rightYAxisColour, rightYAxisValues);
        }
    }

    private void drawLeftYAxisLabelValues(Graphics g) {
        g.translate(0, yChart);
        chartLineYStart = YSTARTOFFSET + chartLineInterval;
        if (leftYAxisMin != leftYAxisMax) {
            valueFont.drawString(g, leftYAxisMaxVal, xChart - YSTARTOFFSET - valueFont.stringWidth(leftYAxisMaxVal),
                    chartLineYStart - (valueFont.getHeight() / 2), 20);
            chartLineYStart += (chartLineInterval * 2);
            valueFont.drawString(g, leftYAxisMidVal, xChart - YSTARTOFFSET - valueFont.stringWidth(leftYAxisMidVal),
                    chartLineYStart - (valueFont.getHeight() / 2), 20);
            chartLineYStart += (chartLineInterval * 2);
            valueFont.drawString(g, leftYAxisMinVal, xChart - YSTARTOFFSET - valueFont.stringWidth(leftYAxisMinVal),
                    chartLineYStart - (valueFont.getHeight() / 2), 20);
        }
        g.translate(0, -yChart);
    }

    private void drawRightYAxisLabelValues(Graphics g) {
        g.translate((xChart + chartWidth), yChart);
        chartLineYStart = YSTARTOFFSET + chartLineInterval;
        if (rightYAxisMin != rightYAxisMax) {
            valueFont.drawString(g, rightYAxisMaxVal, YSTARTOFFSET, chartLineYStart - (valueFont.getHeight() / 2), 20);
            chartLineYStart += (chartLineInterval * 2);
            valueFont.drawString(g, rightYAxisMidVal, YSTARTOFFSET, chartLineYStart - (valueFont.getHeight() / 2), 20);
            chartLineYStart += (chartLineInterval * 2);
            valueFont.drawString(g, rightYAxisMinVal, YSTARTOFFSET, chartLineYStart - (valueFont.getHeight() / 2), 20);
        }
        g.translate(-(xChart + chartWidth), -yChart);
    }
    private int xStart, xEnd, yStart, yEnd;

    private void drawLeftGraph(Graphics g, int colour, Coordinates[] coors) {
        if (coors.length > 1) {
            g.translate(xChart, (yChart + YSTARTOFFSET + chartLineInterval));
            g.setColor(colour);
            for (int i = 0; i < coors.length - 1; i++) {
                xStart = (int) coors[i].getX();
                xEnd = (int) coors[i + 1].getX();
                xStart *= dayPxLength;
                xEnd *= dayPxLength;

                yStart = (int) (((leftYAxisMax - coors[i].getY()) / (leftYAxisMax - leftYAxisMin)) * (chartLineInterval * 4));
                yEnd = (int) (((leftYAxisMax - coors[i + 1].getY()) / (leftYAxisMax - leftYAxisMin)) * (chartLineInterval * 4));

                g.drawLine(xStart, yStart, xEnd, yEnd);
                if (yEnd == yStart) {
                    g.drawLine(xStart, yStart - 1, xEnd, yEnd - 1);
                    g.drawLine(xStart, yStart + 1, xEnd, yEnd + 1);
                } else {
                    g.drawLine(xStart + 1, yStart, xEnd + 1, yEnd);
                    g.drawLine(xStart - 1, yStart, xEnd - 1, yEnd);
                }
            }
            g.translate(-xChart, -(yChart + YSTARTOFFSET + chartLineInterval));
        }
    }

    private void drawRightGraph(Graphics g, int colour, Coordinates[] coors) {
        if (coors.length > 1) {
            g.translate(xChart, (yChart + YSTARTOFFSET + chartLineInterval));
            g.setColor(colour);
            for (int i = 0; i < coors.length - 1; i++) {
                xStart = (int) coors[i].getX();
                xEnd = (int) coors[i + 1].getX();
                xStart *= dayPxLength;
                xEnd *= dayPxLength;

                yStart = (int) (((rightYAxisMax - coors[i].getY()) / (rightYAxisMax - rightYAxisMin)) * (chartLineInterval * 4));
                yEnd = (int) (((rightYAxisMax - coors[i + 1].getY()) / (rightYAxisMax - rightYAxisMin)) * (chartLineInterval * 4));

                g.drawLine(xStart, yStart, xEnd, yEnd);
                if (yEnd == yStart) {
                    g.drawLine(xStart, yStart - 1, xEnd, yEnd - 1);
                    g.drawLine(xStart, yStart + 1, xEnd, yEnd + 1);
                } else {
                    g.drawLine(xStart + 1, yStart, xEnd + 1, yEnd);
                    g.drawLine(xStart - 1, yStart, xEnd - 1, yEnd);
                }
            }
            g.translate(-xChart, -(yChart + YSTARTOFFSET + chartLineInterval));
        }
    }

    public void exit() {
        mfx_slide = new MotionEaseOutExpo(1, 4);
        mfx_slide.init(x, y, -UiKitDisplay.getWidth(), y, 15);
        mfx_slide.setMotionFXListener(this);
        this.motionFX = mfx_slide;
    }

    public void exitToRight() {
        mfx_slide = new MotionEaseOutExpo(1, 4);
        mfx_slide.init(x, y, UiKitDisplay.getWidth(), y, 15);
        mfx_slide.setMotionFXListener(this);
        this.motionFX = mfx_slide;
    }

    public void enter() {
        mfx_slide = new MotionEaseOutExpo(1, 4);
        mfx_slide.init(x, y, 0, y, 15);
        mfx_slide.setMotionFXListener(this);
        this.motionFX = mfx_slide;
    }

    public void onMotionStarted(Motion mfx) {
    }

    public void onMotionProgressed(Motion mfx, int progress) {
    }

    public void onMotionFinished(Motion mfx) {
        if (x != 0) {
            if (cel != null) {
                cel.onComponentEvent(this, EXIT_FINISHED, null, -1);
            }
        } else {
            if (cel != null) {
                cel.onComponentEvent(this, ENTER_FINISHED, null, -1);
            }
        }
    }
}
