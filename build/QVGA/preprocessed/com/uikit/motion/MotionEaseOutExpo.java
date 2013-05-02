package com.uikit.motion;

import com.uikit.utils.MathUtils;

public class MotionEaseOutExpo extends Motion {

    private double delta_x;
    private int delta_y;
    private double progression;
    private int amplitude;
    private MathUtils mathUtils;

    public MotionEaseOutExpo(int motionTickCount, int amplitude) {
        super(motionTickCount);
        this.amplitude = amplitude;
        mathUtils = new MathUtils();
    }

    public void init(int startPosX, int startPosY, int endPosX, int endPosY, int totalSteps) {
        super.init(startPosX, startPosY, endPosX, endPosY, totalSteps);
        delta_x = endPosX - startPosX;
        delta_y = endPosY - startPosY;
    }

    protected void move() {
        progression = (curStep + 1) * (float) amplitude / (totalStep);

        curPosX = (int) (startPosX + ((amplitude - (amplitude / mathUtils.exp(progression))) / amplitude * delta_x));
        curPosY = (int) (startPosY + ((amplitude - (amplitude / mathUtils.exp(progression))) / amplitude * delta_y));

    }
}
