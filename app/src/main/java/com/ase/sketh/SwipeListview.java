package com.ase.sketh;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by thirumal on 22-10-2016.
 */
public class SwipeListview implements View.OnTouchListener, View.OnClickListener {

    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final String logTag = "SwipeDetector";
    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;

    public boolean swipeDetected() {
        Log.i("RL", "swipeDetected");

        return mSwipeDetected != Action.None;
    }

    public Action getAction() {
        Log.i("RL", "mSwipeDetected" + mSwipeDetected);

        return mSwipeDetected;
    }

    public boolean onTouch(View v, MotionEvent event) {
        try {
            Log.d(logTag, "on touch");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    mSwipeDetected = Action.None;
                    return false; // allow other events like Click to be processed
                }
                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // horizontal swipe detection
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            Log.i("RL", "Swipe Detector LR");
                            mSwipeDetected = Action.LR;
                            return true;
                        }
                        if (deltaX > 0) {
                            Log.i("RL", "Swipe Detector RL y :" +deltaY +" x :"+deltaX);
                            if(deltaY < 500 && deltaY > -500) {
                                Log.i("RL","deltaY < 1000 && deltaY > -1000");
                                mSwipeDetected = Action.RL;
                                return true;
                            } else {
                                mSwipeDetected = Action.None;
                                return false;
                            }
                        }
                    } else

                        // vertical swipe detection
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            // top or down
                            if (deltaY < 0) {
                                mSwipeDetected = Action.TB;
                                return false;
                            }
                            if (deltaY > 0) {
                                mSwipeDetected = Action.BT;
                                return false;
                            }
                        }
                    return true;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


}
