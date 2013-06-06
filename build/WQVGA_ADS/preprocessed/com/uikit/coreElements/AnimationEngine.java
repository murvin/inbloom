package com.uikit.coreElements;

import java.util.Random;

public class AnimationEngine {

    public static boolean bSyncAnimation = false;
    public static final int DEFAULT_FRAMEDELAY = 40;
    public static final int DELAY_TIMEBASED = 0x01;
    public static final int DELAY_FRAMEBASED = 0x02;
    private static int delay;
    private static Engine engine;


    private static class Engine extends Thread {

        public void run() {

            long start = 0;
            long elapsed = 0;
            Random rand = new Random();

            while (true) {
                start = System.currentTimeMillis();

                UiKitDisplay.refreshScreen();


                elapsed = System.currentTimeMillis() - start;

                try {
                    if (delay - elapsed > 1) {
                        Thread.sleep(delay - elapsed);
                    } else {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException ex) {
                }

                if (rand.nextInt(60) == 0) {
                    AnimationEngine.bSyncAnimation = true;
                } else {
                    AnimationEngine.bSyncAnimation = false;
                }
            }
        }
    }

    public static void init() {
        if (engine != null) {
            return;
        }

        setDelay(DEFAULT_FRAMEDELAY);
        engine = new Engine();
        engine.start();
    }

    public static final void setDelay(int iDelay) {
        AnimationEngine.delay = iDelay;
    }

    public static final int getDelay() {
        return delay;
    }
}
