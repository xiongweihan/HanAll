package com.example.hanall.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hanall.R;
import com.example.hanall.utils.ConvertUtil;

/**
 * 华为loadingView
 * https://blog.csdn.net/chuhe1989/article/details/104321103#comments_15207267
 */

public class HuaWeiLoadingView extends View {

    private static final int CIRCLE_COUNT = 12;//小圆总数
    private static final int DEGREE_PER_CIRCLE = 360 / CIRCLE_COUNT;//小圆圆心之间间隔角度差
    private float[] mWholeCircleRadius = new float[CIRCLE_COUNT];//记录所有小圆的半径
    private int[] mWholeCircleColors = new int[CIRCLE_COUNT];//记录所有小圆的颜色
    private float mMaxCircleRadius;//小圆的最大半径
    private Context mContext;
    private Paint mPaint;
    /**
     * 控件大小
     */
    private int mSize;
    /**
     * 小圆的颜色
     */
    private int mColor;
    /**
     * 动画时长
     */
    private long mDuration;
    private ValueAnimator mAnimator;
    private int mAnimationValue = 0;

    public HuaWeiLoadingView(Context context) {
        this(context, null);
    }

    public HuaWeiLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HuaWeiLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
        initValue();
    }

    /**
     * 初始化小圆半径和颜色
     */
    private void initValue() {
        float minCircleRadius = mSize / 24;
        for (int i = 0; i < CIRCLE_COUNT; i++) {
            switch (i) {
                case 7:
                    mWholeCircleRadius[i] = minCircleRadius * 1.25f;
                    mWholeCircleColors[i] = (int) (255 * 0.7f);
                    break;
                case 8:
                    mWholeCircleRadius[i] = minCircleRadius * 1.5f;
                    mWholeCircleColors[i] = (int) (255 * 0.8f);
                    break;
                case 9:
                case 11:
                    mWholeCircleRadius[i] = minCircleRadius * 1.75f;
                    mWholeCircleColors[i] = (int) (255 * 0.9f);
                    break;
                case 10:
                    mWholeCircleRadius[i] = minCircleRadius * 2f;
                    mWholeCircleColors[i] = (int) (255 * 1f);
                    break;
                default:
                    mWholeCircleRadius[i] = minCircleRadius;
                    mWholeCircleColors[i] = (int) (255 * 0.5f);
                    break;
            }
        }
        mMaxCircleRadius = minCircleRadius * 2;

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
    }

    /**
     * 初始化自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        this.mContext = context;
        @SuppressLint("Recycle")
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.HuaWeiLoadingView);

        mSize = (int) array.getDimension(R.styleable.HuaWeiLoadingView_hw_size, ConvertUtil.dip2Px(mContext, 100));
        setSize(mSize);

        mColor = array.getColor(R.styleable.HuaWeiLoadingView_hw_color, Color.parseColor("#333333"));
        setColor(mColor);

        mDuration = array.getInt(R.styleable.HuaWeiLoadingView_hw_duration, 1500);

        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //去除warp_content,Match_Parent 对空间的影响
        setMeasuredDimension(mSize, mSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mSize > 0) {
            //每隔DEFREE_PRE_CICLE * mAnimationValue 角度，绘制所有小圆
            canvas.rotate(DEGREE_PER_CIRCLE * mAnimationValue, (float) (mSize / 2), (float) (mSize / 2));
            for (int i = 0; i < CIRCLE_COUNT; i++) {
                //设置小圆的颜色
//                mPaint.setColor(mWholeCircleColors[i]);
                mPaint.setAlpha(mWholeCircleColors[i]);
                //每隔DEFREE_PRE_CICLE 角度，绘制一个圆
                canvas.drawCircle((float) (mSize / 2), mMaxCircleRadius, mWholeCircleRadius[i], mPaint);
                canvas.rotate(DEGREE_PER_CIRCLE, (float) (mSize / 2), (float) (mSize / 2));
            }
        }
    }

    /**
     * view依附window时开启动画
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    /**
     * view脱离window时停止动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    /**
     * 根据view的可见性进行开始/停止动画
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAnim();
        } else {
            stopAnim();
        }
    }

    private void startAnim() {
        if(mAnimator == null){
            mAnimator = ValueAnimator.ofInt(0,CIRCLE_COUNT -1);
            mAnimator.addUpdateListener(updateListener);
            mAnimator.setDuration(mDuration);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();

        }else if(!mAnimator.isStarted()){
            mAnimator.start();
        }

    }

    private void stopAnim() {
        if(mAnimator !=null){
            mAnimator.removeUpdateListener(updateListener);
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }

    }

    public ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener(){
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mAnimationValue = (int) animation.getAnimatedValue();
            invalidate();
        }
    };

    /**
     * 设置小圆的颜色
     */
    private void setColor(int color) {
        mColor = color;
        invalidate();

    }

    /**
     * 设置控件大小
     */
    private void setSize(int size) {
        mSize = size;
        invalidate();
    }
}
