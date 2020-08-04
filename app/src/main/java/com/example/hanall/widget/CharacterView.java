package com.example.hanall.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.example.hanall.R;
import com.example.hanall.utils.ConvertUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CharacterView extends View {

    private static final int TEXT_SIZE = ConvertUtil.dip2Px(12);
    private static final int MARGIN = ConvertUtil.dip2Px(5);
    private static final int VIEW_WIDTH = ConvertUtil.dip2Px(24);

    //控件的高度
    private int mViewHeight = TEXT_SIZE * 27 + MARGIN * 27 * 2;
    //拼音列表集合
    private List<String> mCharacters;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //FontMetrics 字体度量，该类是Paint的内部类，通过getFontMetrics()方法可获取字体相关属性。
    //字体的几个属性
    //baseline　　Android文本绘制是以baseline为基准的
    //ascent　　　baseline之上至字符最高处的距离，以baseline为基准，负值
    //descent　　 baseline之下至字符最低处的距离，以baseline为基准，正值
    //leading　　 上一行文字的descent到当前行文字的ascent称为行距
    //top 最高字符到baseline的值，以baseline为基准，负值
    //bottom 最下字符到baseline的值，以baseline为基准，正值
    private Paint.FontMetrics metrics = new Paint.FontMetrics();
    //字母基准线的集合
    private List<Float> mY = new ArrayList<>();
    //是否点击某个字母
    private boolean isClick;
    //点击该字母后，该字母在列表的位置下标
    private int mCurrentClickIndex = -1;

    //按下的坐标
    private float mDownX, mDownY;

    //按下时的时间
    private long mDownTime;

    //按下后，在该字母上覆盖的红色背景圆的半径
    private float bgRadius;

    public CharacterView(Context context) {
        this(context, null);
    }

    public CharacterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.parseColor("#C8102E"));
        mPaint.setTextSize(TEXT_SIZE);
        mPaint.getFontMetrics(metrics);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式
        //设置默认文字字母列表i
        mCharacters = Arrays.asList(getResources().getStringArray(R.array.characters));
        for (int i = 0; i < mCharacters.size(); i++) {
            mY.add(i * TEXT_SIZE + 0.5f * TEXT_SIZE + MARGIN + 2 * i * MARGIN - (metrics.ascent + metrics.descent) / 2);
        }
    }

    /**
     * 设置字母列表
     *
     * @param list--字母列表
     */
    public void setmCharacters(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        int size = list.size();
        mViewHeight = TEXT_SIZE * size + MARGIN * size * 2;
        mCharacters = list;
        for (int i = 0; i < mCharacters.size(); i++) {
            mY.add(i * TEXT_SIZE + 0.5f * TEXT_SIZE + MARGIN + 2 * i * MARGIN - (metrics.ascent + metrics.descent) / 2);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(VIEW_WIDTH, widthMeasureSpec), resolveSize(mViewHeight, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < mCharacters.size(); i++) {
            mPaint.setColor(Color.parseColor("#8c8c8c"));
            canvas.drawText(mCharacters.get(i), VIEW_WIDTH / 2, mY.get(i), mPaint);
            if (isClick && mCurrentClickIndex != -1) {
                mPaint.setColor(Color.parseColor("#11FF0000"));
                canvas.drawCircle(VIEW_WIDTH / 2, mY.get(mCurrentClickIndex) - ConvertUtil.dip2Px(5), bgRadius, mPaint);// TODO: 2020/8/3  这里纵坐标，有出入
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handle = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handle = true;
                mDownX = event.getX();
                mDownY = event.getY();
                mDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long upTime = System.currentTimeMillis();
                float upX = event.getX();
                float upY = event.getY();
                float instanceX = Math.abs(mDownX - upX);
                float instanceY = Math.abs(mDownY - upY);
                // 如果滑动时间小于400毫秒并且距离小于系统认为的点击事件的距离，则认为该次触摸事件为点击事件
                if ((upTime - mDownTime) < 400 && Math.sqrt(instanceX * instanceX + instanceY * instanceY) <=
                        ViewConfiguration.get(getContext()).getScaledDoubleTapSlop()) {

                    isClick = true;
                    // 找该次点击是点击的哪个字母的位置
                    for (int i = 0; i < mY.size(); i++) {
                        if (upY >= mY.get(i) - ConvertUtil.dip2Px(11) && upY <= mY.get(i) + ConvertUtil.dip2Px(11)) {
                            mCurrentClickIndex = i;
                            break;
                        }
                    }
                    if (onCharacterClickListener != null && mCurrentClickIndex != -1) {
                        onCharacterClickListener.onCharacterClick(mCharacters.get(mCurrentClickIndex), mCurrentClickIndex);
                    }
                    //开启动画
                    startAnim();
                }
                break;
        }

        return handle;
    }

    private void startAnim() {
        float radius = ConvertUtil.dip2Px(10);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "bgRadius", radius * 0.5f, radius);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value == radius) {
                    isClick = false;
                    mCurrentClickIndex = -1;
                }
            }
        });
        objectAnimator.start();
    }

    public void setBgRadius(float bgRadius) {
        this.bgRadius = bgRadius;
        invalidate();
    }

    //接口，
    public interface onCharacterClickListener {
        void onCharacterClick(String character, int position);
    }

    public onCharacterClickListener onCharacterClickListener;

    public void setOnCharacterClickListener(onCharacterClickListener onCharacterClickListener) {
        this.onCharacterClickListener = onCharacterClickListener;

    }
}
