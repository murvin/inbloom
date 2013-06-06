
package com.uikit.motion;

public class MotionQuadOut extends Motion {

    int deltaX = 0;
    int deltaY = 0;

    public MotionQuadOut() {
        super(1);
    }

    public void init(int startPosX, int startPosY, int endPosX, int endPosY, int nTotalStep) {
        super.init(startPosX, startPosY, endPosX, endPosY, nTotalStep);
        deltaX = endPosX - startPosX;
        deltaY = endPosY - startPosY;
    }

    protected void move() {
        float progress = (float)curStep/(float)totalStep;
        curPosX = (int) (-deltaX * (progress) * (progress - 2) + startPosX);
        curPosY = (int) (-deltaY * (progress) * (progress - 2) + startPosY);
    }
    
}
