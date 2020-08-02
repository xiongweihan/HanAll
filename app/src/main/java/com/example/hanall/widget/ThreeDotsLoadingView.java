package com.example.hanall.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.hanall.R;
import com.example.hanall.utils.ConvertUtil;

/**
 * 三个圆点的loading图
 */
public class ThreeDotsLoadingView extends View {

    //圆的半径
    private float mRadius = ConvertUtil.dip2Px(8f);
    //两个圆的之间间距
    private static final int PADDING = ConvertUtil.dip2Px(2f);

    //画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //圆的颜色
    private int color = Color.parseColor("#c8102e");

    private AnimatorSet mAnimatorSet;

    private float mF1, mF2, mF3;

    public ThreeDotsLoadingView(Context context) {
        this(context, null);
    }

    public ThreeDotsLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        startAnim();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThreeDotsLoadingView);
        color = typedArray.getColor(R.styleable.ThreeDotsLoadingView_dot_color, this.color);
        mRadius = typedArray.getFloat(R.styleable.ThreeDotsLoadingView_dot_radius, mRadius);
        mPaint.setColor(color);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final float targetWidth = getPaddingLeft() + getPaddingEnd() + mRadius * 6 + PADDING * 2;
        final float targetHeight = getPaddingTop() + getPaddingBottom() + mRadius * 2;
        final int realWidth = resolveSize((int) targetWidth, widthMeasureSpec);
        final int realHeight = resolveSize((int) targetHeight, heightMeasureSpec);
        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawCircle(getWidth() / 2 - PADDING - mRadius * 2, getHeight() / 2, mRadius * mF1, mPaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius * mF2, mPaint);
        canvas.drawCircle(getWidth() / 2 + PADDING + mRadius * 2, getHeight() / 2, mRadius * mF3, mPaint);
        canvas.restore();
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    public void onVisibilityAggregated(boolean isVisible) {
//        super.onVisibilityAggregated(isVisible);
//        if (isVisible) {
//            Log.d("laoding","isVisible-->resume" );
//            mAnimatorSet.resume();
//        } else {
//            Log.d("laoding","isVisible-->pause" );
//            mAnimatorSet.pause();
//        }
//    }

    private void startAnim() {
        mAnimatorSet = new AnimatorSet();
        ValueAnimator animator1 = ValueAnimator.ofFloat(0f, 1f, 0f);
        animator1.setRepeatCount(ValueAnimator.INFINITE);//INFINITE--一直重复
        animator1.setRepeatMode(ValueAnimator.RESTART);//;//RESTART表示从头开始，REVERSE表示从末尾倒播
        animator1.addUpdateListener(animation -> {
            mF1 = (Float) animation.getAnimatedValue();
            invalidate();
        });
        ValueAnimator animator2 = ValueAnimator.ofFloat(0f, 1f, 0f);
        animator2.setStartDelay(300);//延迟播放
        animator2.setRepeatCount(ValueAnimator.INFINITE);//INFINITE--一直重复
        animator2.setRepeatMode(ValueAnimator.RESTART);//;//RESTART表示从头开始，REVERSE表示从末尾倒播
        animator2.addUpdateListener(animation -> {
            mF2 = (Float) animation.getAnimatedValue();
        });
        ValueAnimator animator3 = ValueAnimator.ofFloat(0f, 1f, 0f);
        animator3.setStartDelay(600);//延迟播放
        animator3.setRepeatCount(ValueAnimator.INFINITE);//INFINITE--一直重复
        animator3.setRepeatMode(ValueAnimator.RESTART);//;//RESTART表示从头开始，REVERSE表示从末尾倒播
        animator3.addUpdateListener(animation -> {
            mF3 = (Float) animation.getAnimatedValue();
        });

        mAnimatorSet.playTogether(animator1, animator2, animator3);
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.start();

    }
}
