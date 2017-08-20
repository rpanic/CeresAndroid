package org.rpanic1308.recordingActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;

import rpanic1308.ceres.R;

/**
 * Created by Team_ on 20.08.2017.
 */

public class EffectFAB extends FloatingActionButton {

    ObjectAnimator pressedAnimator;
    float animationProgress;
    int pressedRingWidth;

    public EffectFAB(Context context) {
        super(context);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if(pressed){
            showAnimation();
        }else{
            hideAnimation();
        }
    }

    public void showAnimation(){
        pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
        pressedAnimator.start();
    }

    public void hideAnimation(){
        pressedAnimator.setFloatValues(pressedRingWidth, 0f);
        pressedAnimator.start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void init(){

        //pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources()
       //         .getDisplayMetrics());

        pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
        pressedAnimator.setDuration(500L);

    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
        this.invalidate();
    }
}
