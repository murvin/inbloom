package com.uikit.motion;

public class MotionLinear extends Motion {

    public static final int MOFX_LINEAR_CSPEED = 0;
    public static final int MOFX_LINEAR_CACCEL = 1;
    public static final int MOFX_LINEAR_PULLBACK = 2; 
    public static final int MOFX_LINEAR_CSPEED_INERTIA = 3;
    private int motionType;
    private float i1, i2;
    private float[] i3, i4;
    private float i5, i6;
    private int i7;
    private boolean b0;

    public MotionLinear(int iMotionTickCount, int motionType) {
        super(iMotionTickCount);

        if (motionType != MOFX_LINEAR_CSPEED
                && motionType != MOFX_LINEAR_CACCEL
                && motionType != MOFX_LINEAR_PULLBACK
                && motionType != MOFX_LINEAR_CSPEED_INERTIA) {
            throw new IllegalArgumentException();
        }

        this.motionType = motionType;
        i3 = new float[2];
        i4 = new float[2];

        i5 = 0.7f;
        i6 = 0.3f;
        i7 = 0;
    }

    public void init(int startPosX, int startPosY, int endPosX, int endPosY, int nTotalStep, float startSpeedX, float startSpeedY) {
        super.init(startPosX, startPosY, endPosX, endPosY, nTotalStep);

        switch (motionType) {
            case MOFX_LINEAR_CSPEED:
                this.i1 = 1.0f * (endPosX - startPosX) / nTotalStep;
                this.i2 = 1.0f * (endPosY - startPosY) / nTotalStep;
                i3[0] = i3[1] = 0;
                i4[0] = i4[1] = 0;
                break;
            case MOFX_LINEAR_CACCEL:
                this.i1 = startSpeedX;
                this.i2 = startSpeedY;
                i3[0] = 2.0f * (endPosX - startPosX - startSpeedX * nTotalStep) / (nTotalStep * nTotalStep);
                i3[1] = 0.0f;
                i4[0] = 2.0f * (endPosY - startPosY - startSpeedY * nTotalStep) / (nTotalStep * nTotalStep);
                i4[1] = 0.0f;
                break;
            case MOFX_LINEAR_PULLBACK:
                this.i1 = startSpeedX;
                this.i2 = startSpeedY;
                i3[0] = 6.0f * (endPosX - startPosX) / (nTotalStep * nTotalStep) - 4.0f * startSpeedX / nTotalStep;
                i3[1] = 6.0f * startSpeedX / (nTotalStep * nTotalStep) - 12.0f * (endPosX - startPosX) / (nTotalStep * nTotalStep * nTotalStep);
                i4[0] = 6.0f * (endPosY - startPosY) / (nTotalStep * nTotalStep) - 4.0f * startSpeedY / nTotalStep;
                i4[1] = 6.0f * startSpeedY / (nTotalStep * nTotalStep) - 12.0f * (endPosY - startPosY) / (nTotalStep * nTotalStep * nTotalStep);
                ;
                break;
            case MOFX_LINEAR_CSPEED_INERTIA:
                i7 = (int) (nTotalStep * i5);
                if (i7 <= 0 || i7 >= nTotalStep) {
                    throw new IllegalArgumentException();
                }
                this.i1 = 1.0f * ((endPosX - startPosX) * (1.0f - i6)) / (nTotalStep - i7);
                this.i2 = 1.0f * ((endPosY - startPosY) * (1.0f - i6)) / (nTotalStep - i7);
                i3[0] = i3[1] = 0;
                i4[0] = i4[1] = 0;
                b0 = false;
                break;
            default:
                break;
        }
    }

    public void setInertiaIntensity(float fIneStepPer, float fIneDistPer) {
        if (motionType == MOFX_LINEAR_CSPEED_INERTIA) {
            this.i5 = fIneStepPer;
            this.i6 = fIneDistPer;
        }
    }

    protected void move() {
        double x, y;

        if (motionType == MOFX_LINEAR_CSPEED_INERTIA) {
            x = startPosX + i1 * (curStep - (b0 ? (totalStep - i7) : 0));
            y = startPosY + i2 * (curStep - (b0 ? (totalStep - i7) : 0));
            curPosX = (int) (x >= 0 ? (x + 0.5f) : (x - 0.5f));
            curPosY = (int) (y >= 0 ? (y + 0.5f) : (y - 0.5f));

            if (curStep >= (totalStep - i7)) {
                if (b0 == false) {
                    b0 = true;
                    this.i1 = 1.0f * (endPosX - curPosX) / i7;
                    this.i2 = 1.0f * (endPosY - curPosY) / i7;
                    this.startPosX = curPosX;
                    this.startPosY = curPosY;
                }
            }
        } else {
            x = startPosX + i1 * curStep + 0.5f * i3[0] * curStep * curStep + 0.1666667 * i3[1] * curStep * curStep * curStep;
            y = startPosY + i2 * curStep + 0.5f * i4[0] * curStep * curStep + 0.1666667 * i4[1] * curStep * curStep * curStep;
            curPosX = (int) (x >= 0 ? (x + 0.5f) : (x - 0.5f));
            curPosY = (int) (y >= 0 ? (y + 0.5f) : (y - 0.5f));
        }
    }
}
