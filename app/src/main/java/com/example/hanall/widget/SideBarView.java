package com.example.hanall.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;


import com.example.hanall.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SideBarView extends View {

    private List<String> mContentDataList = new ArrayList<>();
    private int mBackgroundColor = Color.TRANSPARENT;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private float mTextSize = dip2px(12);
    private int mTextColor = Color.BLACK;
    private int mItemSpace = 15;                    //自定义Item的间距
    private boolean mIsEqualItemSpace = true;     //是否按照view高度均分item高度间隔
    private Paint mPaint, mCirclePaint;
    private int mWidth = 0;
    private int mHeight = 0;
    private Point mOneItemPoint = new Point();       //第一个item的坐标
    private int mItemHeightSize = 0;                //单个item的高度尺寸
    private onItemClickListener mListener;
    private int upPosition;                         //手指点击抬起的位置下标

    //按下后，在该字母上覆盖的红色背景圆的半径
    private float bgRadius;

    private boolean isClick;//是否点击了该字母。默认是false


    public SideBarView(Context context) {
        super(context);
        initPaint();
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);//对齐样式

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);
        mCirclePaint.setColor(getContext().getResources().getColor(R.color.colorAccent));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mIsEqualItemSpace) {
            //均分item  间距模式
            mItemHeightSize = (mHeight - (mPaddingTop + mPaddingBottom)) / mContentDataList.size();//高度 - 上下边距 / Item的数量 = 一个Item的高度
        } else {
            //自定义item间距模式
            mItemHeightSize = ((mHeight - (mPaddingTop + mPaddingBottom)) / mContentDataList.size()) + mItemSpace;//
        }
        mOneItemPoint.x = (mWidth - (mPaddingLeft + mPaddingRight)) / 2;// 宽度 - 左右边距 / 2 = 第一个Item的X坐标值
        mOneItemPoint.y = mPaddingTop + mItemHeightSize;            //上边距 + Item高度 = 第一个Item的Y坐标值
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO: 2020/10/19 未完成
        int action = event.getAction();
        int itemAllHeight = mHeight - (mPaddingTop + mPaddingBottom);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //根据当前点击的位置与整体item的全部高度除比，在将除得的的数字四舍五入获取对应集合的位置
                int downPosition = Math.round(mContentDataList.size() / (itemAllHeight / event.getY()));
                downPosition = Math.max(downPosition, 1);                        //不允许有小于1的值
                downPosition = Math.min(downPosition, mContentDataList.size());  //不允许有大于集合长度的值
                downPosition = downPosition - 1;
                if (mListener != null) {
                    mListener.onItemDown(downPosition, mContentDataList.get(downPosition));
                }

                return true;
            case MotionEvent.ACTION_MOVE:
                int movePosition = Math.round(mContentDataList.size() / (itemAllHeight / event.getY()));
                movePosition = Math.max(movePosition, 1);
                movePosition = Math.min(movePosition, mContentDataList.size());
                movePosition = movePosition - 1;
                if (mListener != null) {
                    mListener.onItemMove(movePosition, mContentDataList.get(movePosition));
                }

                return true;
            case MotionEvent.ACTION_UP:
                upPosition = Math.round(mContentDataList.size() / (itemAllHeight / event.getY()));
                upPosition = Math.max(upPosition, 1);
                upPosition = Math.min(upPosition, mContentDataList.size());
                upPosition = upPosition - 1;
                float upY = event.getY();
                if (mListener != null) {
                    mListener.inItemUp(upPosition, mContentDataList.get(upPosition));
                    isClick = true;
                    beginAnim();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始动画
     */
    private void beginAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "bgRadius", 0, mWidth / 2);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animator.getAnimatedValue();
            if (animatedValue >= mWidth / 2) {
                isClick = false;
                upPosition = 0;

            }
        });
        animator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        drawContent(canvas);
    }

    private void drawContent(Canvas canvas) {
        if (mContentDataList.isEmpty()) {
            return;
        }
        Log.e("---->", "mContentDataList-----" + mContentDataList.size());

        for (int i = 0; i < mContentDataList.size(); i++) {
            String itemContent = mContentDataList.get(i);

            int y = mOneItemPoint.y + (mItemHeightSize * i);
            canvas.drawText(itemContent, mOneItemPoint.x, y, mPaint);
            if (isClick && upPosition == i) {
                canvas.drawCircle(mOneItemPoint.x, y - dip2px(5), bgRadius, mCirclePaint);
            }
        }
    }


    public void setContentDataList(List<String> list) {
        mContentDataList.clear();
        mContentDataList.addAll(list);
        postInvalidate();
    }

    /**
     * 设置文字大小
     *
     * @param spValue 单位sp
     */
    public void setTextSize(float spValue) {
        mTextSize = sp2px(spValue);
        mPaint.setTextSize(mTextSize);
        postInvalidate();
    }

    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        mPaint.setColor(mTextColor);
        postInvalidate();
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        postInvalidate();
    }

    /**
     * 设置是否根据View的高度均分item的间距
     *
     * @param isEqualItemSpace true--使用均分，   false---不均分
     */
    public void setEqualItemSpace(boolean isEqualItemSpace) {
        mIsEqualItemSpace = isEqualItemSpace;
        postInvalidate();
    }

    public void setItemSpace(int itemSpace) {
        this.mItemSpace = itemSpace;
        mIsEqualItemSpace = false;
        postInvalidate();
    }

    /**
     * 设置内边距
     *
     * @param top    上边距 单位dp
     * @param bottom 下边距，单位dp
     * @param left   左边距，单位dp
     * @param right  右边距，单位dp
     */
    public void setPadding(int top, int bottom, int left, int right) {
        mPaddingTop = dip2px(top);
        mPaddingBottom = dip2px(bottom);
        mPaddingLeft = dip2px(left);
        mPaddingRight = dip2px(right);
        postInvalidate();
    }

    public void setBgRadius(float bgRadius) {
        this.bgRadius = bgRadius;
        postInvalidate();
    }


    public int dip2px(float dpValue) {
        float space = getResources().getDisplayMetrics().density;
        return (int) (dpValue * space + 0.5f);
    }

    public int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface onItemClickListener {
        void onItemDown(int position, String itemContent);

        void onItemMove(int position, String itemContent);

        void inItemUp(int position, String itemContent);
    }

    public void setOnItemClickListener(onItemClickListener mListener) {
        this.mListener = mListener;
    }


}
