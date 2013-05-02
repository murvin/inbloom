package com.uikit.motion;

public abstract class Motion {

    public static final int MOTIONFX_UNINITIALIZED = -1;
    public static final int MOTIONFX_FINISHED = 0x1002;
    protected int startPosX;
    protected int startPosY;
    protected int endPosX;
    protected int endPosY;
    protected int curPosX;
    protected int curPosY;
    protected int totalStep;
    protected int curStep;
    private int tickCountCounter;
    private int motionTickCount;
    private IMotionListener elMOFX;

    protected Motion(int motionTickCount) {
        if (motionTickCount <= 0) {
            throw new IllegalArgumentException();
        }

        this.motionTickCount = motionTickCount;

        curStep = -1;
    }

    public void setMotionFXListener(IMotionListener elMOFX) {
        this.elMOFX = elMOFX;
    }

    protected void init(int startPosX, int startPosY, int endPosX, int endPosY, int nTotalStep) {
        if (nTotalStep <= 0) {
            throw new IllegalArgumentException();
        }

        this.totalStep = nTotalStep;

        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.endPosX = endPosX;
        this.endPosY = endPosY;

        this.curPosX = this.startPosX;
        this.curPosY = this.startPosY;

        curStep = 0;
        tickCountCounter = 0;

        if (elMOFX != null) {
            elMOFX.onMotionStarted(this);
        }
    }

    public int getProgress() {
        if (totalStep <= 0) {
            return MOTIONFX_UNINITIALIZED;
        } else {
            if (curStep <= totalStep) {
                return curStep * 100 / totalStep;
            } else {
                return MOTIONFX_FINISHED;
            }
        }
    }

    public void tick() {
        if (curStep == -1 || totalStep <= 0) {
            return;
        }

        if (curStep > totalStep) {

            return;
        }

        if (++tickCountCounter == motionTickCount) {
            tickCountCounter = 0;
            curStep++;
            if (curStep < totalStep) {
                move();
                if (elMOFX != null) {
                    elMOFX.onMotionProgressed(this, curStep * 100 / totalStep);
                }
            } else if (curStep == totalStep) {
                curPosX = endPosX;
                curPosY = endPosY;
                if (elMOFX != null) {
                    elMOFX.onMotionProgressed(this, curStep * 100 / totalStep);
                }
            } else {
                if (elMOFX != null) {
                    elMOFX.onMotionFinished(this);
                }
            }
        }
    }

    public int getCurPositionX() {
        return curPosX;
    }

    public int getCurPositionY() {
        return curPosY;
    }

    protected abstract void move();
}
