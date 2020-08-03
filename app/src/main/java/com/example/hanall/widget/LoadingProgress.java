package com.example.hanall.widget;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 横向的加载进度框，没有具体的百分比，只是从左到右循环走的View
 * 如果其他地方也需要使用到该View，则再自定义颜色属性进行扩展
 */

@SuppressWarnings("unused")
public class LoadingProgress extends View {

    private float progressStart = 0f;
    private float progressEnd = 0f;
    private Paint mPaint;
    private float mWidth;
    private float mHeight;
    private ObjectAnimator objectAnimator;

    public LoadingProgress(Context context) {
        this(context, null);
    }

    public LoadingProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        startAnimator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mPaint.setStrokeWidth(mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mPaint.setColor(Color.parseColor("#55125652"));
        canvas.drawLine(0, mHeight, mWidth, mHeight , mPaint);

        mPaint.setColor(Color.parseColor("#c8102E"));
        canvas.drawLine(mWidth * progressStart, mHeight , mWidth * (progressEnd + 0.2f), mHeight , mPaint);
        canvas.restore();
    }

    private void startAnimator() {
        Keyframe keyframeStart1 = Keyframe.ofFloat(0f, 0f);
        Keyframe keyframeStart2 = Keyframe.ofFloat(0.2f, 0.2f);
        Keyframe keyframeStart3 = Keyframe.ofFloat(0.8f, 0.9f);
        Keyframe keyframeStart4 = Keyframe.ofFloat(1f, 1f);

        PropertyValuesHolder startHolder = PropertyValuesHolder.ofKeyframe("progressStart",
                keyframeStart1, keyframeStart2, keyframeStart3, keyframeStart4);

        Keyframe keyframeEnd1 = Keyframe.ofFloat(0f, 0f);
        Keyframe keyframeEnd2 = Keyframe.ofFloat(0.2f, 0.5f);
        Keyframe keyframeEnd3 = Keyframe.ofFloat(0.8f, 0.8f);
        Keyframe keyframeEnd4 = Keyframe.ofFloat(1f, 1f);

        PropertyValuesHolder endHolder = PropertyValuesHolder.ofKeyframe("progressEnd", keyframeEnd1, keyframeEnd2, keyframeEnd3, keyframeEnd4);

        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, startHolder, endHolder);
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.start();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (objectAnimator == null) {
            return;
        }
        if (visibility == VISIBLE) {
            Log.d("laoding", "isVisible-->start");
            objectAnimator.start();
        } else {
            Log.d("laoding", "isVisible-->pause");
            objectAnimator.pause();
        }
    }

    /**
     * 下面这个几个set get 方法，是对应属性progressStart 和 progressEnd的 方法，是为了 动画里动态设置这个属性值所必需的
     * @return
     */
    public float getProgressStart() {
        return progressStart;
    }

    public void setProgressStart(float progressStart) {
        this.progressStart = progressStart;
        invalidate();
    }

    public float getProgressEnd() {
        return progressEnd;
    }

    public void setProgressEnd(float progressEnd) {
        this.progressEnd = progressEnd;
        invalidate();
    }

}
