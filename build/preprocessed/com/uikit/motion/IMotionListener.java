package com.uikit.motion;

public interface IMotionListener {

    public void onMotionStarted(Motion motion);

    public void onMotionProgressed(Motion motion, int progress);

    public void onMotionFinished(Motion motion);
}
