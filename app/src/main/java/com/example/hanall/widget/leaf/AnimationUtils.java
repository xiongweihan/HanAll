package com.example.hanall.widget.leaf;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AnimationUtils {

    public static RotateAnimation initRotateAnimation(long duration, int fromAngle, int toAngle,
                                                      boolean isFillAfter, int repeatCount) {
        RotateAnimation mLoadingRotateAnimation = new RotateAnimation(fromAngle, toAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mLoadingRotateAnimation.setInterpolator(linearInterpolator);
        mLoadingRotateAnimation.setDuration(duration);
        mLoadingRotateAnimation.setFillAfter(isFillAfter);
        mLoadingRotateAnimation.setRepeatCount(repeatCount);
        mLoadingRotateAnimation.setRepeatMode(Animation.RESTART);

        return mLoadingRotateAnimation;
    }

    public static RotateAnimation initRotateAnimation(boolean isClickWise, long duration,
                                                      boolean isFlillAfter, int repeatCount) {
        int endAngle;
        if (isClickWise) {
            endAngle = 360;
        } else {
            endAngle = -360;
        }
        RotateAnimation mLoadingRotateAnimation = new RotateAnimation(0, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mLoadingRotateAnimation.setInterpolator(linearInterpolator);
        mLoadingRotateAnimation.setDuration(duration);
        mLoadingRotateAnimation.setFillAfter(isFlillAfter);
        mLoadingRotateAnimation.setRepeatCount(repeatCount);
        mLoadingRotateAnimation.setRepeatMode(Animation.RESTART);
        return mLoadingRotateAnimation;
    }

    public static AnimationDrawable initAnimationDrawable(Context context, int[] drawableIds,
                                                          int durationTime, boolean isOneShot) {
        AnimationDrawable mAnimationDrawable = new AnimationDrawable();
        for (int i = 0; i < drawableIds.length; i++) {
            int id = drawableIds[i];
            mAnimationDrawable.addFrame(context.getResources().getDrawable(id),durationTime);
        }
        mAnimationDrawable.setOneShot(isOneShot);
        return mAnimationDrawable;
    }


    public static Animation initAlphaAnimation(Context context, float fromAlpha, float toAlpha,
                                               long duration) {
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }
}
