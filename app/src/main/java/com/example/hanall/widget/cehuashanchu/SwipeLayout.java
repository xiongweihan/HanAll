package com.example.hanall.widget.cehuashanchu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.hanall.utils.ScreenUtil;

/**
 * 侧滑删除的自定义View
 * todo 这个是未完成品，要用的话，需要使用 SwipeMenuLayout 这个（就在同一个包里）
 */
public class SwipeLayout extends ViewGroup {
    private static final String TAG = "SwipeLayout";
    private int mRightMenuWidth;//右侧菜单宽度总和(最大滑动距离)
    private int mHeight;//自己的高度
    private int mScreenW;//屏幕宽度
    //滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
    private int mLimit;
    private boolean isSwipeEnable;
    private VelocityTracker mVelocityTracker;//滑动速度变量

    //防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
    private boolean isTouching;
    //上一次的xy
    private final PointF mLastP = new PointF();

    //存储的是当前正在展开的View
    private static SwipeLayout mViewCache;
    private int mPointerId;//多点触摸只算第一根手指的速度
    private int mMaxVelocity;//计算滑动速度用
    private int mScaleTouchSlop;//为了处理单击事件的冲突


    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenW = ScreenUtil.getScreenWidth(context);
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //右滑删除功能的开关,默认开
        isSwipeEnable = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);//令自己可点击，从而获取触摸事件

        mRightMenuWidth = 0;//由于ViewHolder的复用机制，每次这里都要手动恢复初始值
        int childCount = getChildCount();
        boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildren = false;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                //后续计划加入上滑、下滑，则将不再支持Item的margin
//                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight() /*+ lp.topMargin + lp.bottomMargin*/);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildren = true;
                }
                if (i > 0) {//第一个布局是left item,从第二个开始才是RightMenu
                    mRightMenuWidth += childView.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(mScreenW, mHeight);
        mLimit = mRightMenuWidth * 4 / 10;//滑动距离的临界值
        if (isNeedMeasureChildren) {//如果子view的height有MatchParent 属性的，设置子view高度
            forceUniformHeight(childCount, widthMeasureSpec);

        }
    }

    /**
     * 给MatchParent 的子view设置高度
     */
    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);//以父布局高度构建一个Exactly的测量参数
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            if (lp.height == LayoutParams.MATCH_PARENT) {
                int oldWidth = child.getMeasuredWidth();
                measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                lp.width = oldWidth;
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 1;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) {//第一个子view是内容，宽度设为全屏
                    childView.layout(left, getPaddingTop(), left + mScreenW,
                            getPaddingTop() + childView.getMeasuredHeight());
                    left = left + mScreenW;
                } else {
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(),
                            getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();

                }
            }

        }
    }

    /**
     * 我们希望子View的LayoutParams是MarginLayoutParams，
     * 需要如下重写generateLayoutParams（）这个方法。
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
            acquireVelocityTracker(ev);
            final VelocityTracker verTracker = mVelocityTracker;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isTouching) {//如果有别的指头摸过了，那么就return false，这样后续的move..等事件也不会来找这个View了
                        return false;
                    } else {
                        isTouching = true;//第一个摸的指头，赶紧改变标志，宣誓主权，
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());

                    //如果down,view和cacheview 不一样，则立马让它还原，且把它置为null
                    if (mViewCache != null) {
                        if (mViewCache != this) {
                            mViewCache.smoothClose();
                            mViewCache = null;
                        }

                        //只要有一个侧滑菜单处于打开状态，就不给外层布局上下滑动了
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                    //求第一个出点的id，此时可能有多个触点，但至少一个，计算滑动速率用
                    mPointerId = ev.getPointerId(0);

                    break;
                case MotionEvent.ACTION_MOVE:
                    float gap = mLastP.x - ev.getRawX();
                    //为了在水平滑动中禁止父类ListView 等再竖直滑动
                    if (gap > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
//                    //修改此处，使屏蔽父布局滑动更加灵敏，
//                    if (Math.abs(gap) > 10 || Math.abs(getScaleX()) > 10) {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
                    scrollBy((int) gap, 0);//滑动使用scrollBy
                    //修正
                    if (getScaleX() <= 0) {
                        scrollTo(0, 0);
                    }
                    if (getScaleX() >= mRightMenuWidth) {
                        scrollTo(mRightMenuWidth, 0);
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //求伪瞬时速度
                    verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    float velocityX = verTracker.getXVelocity(mPointerId);
                    if (Math.abs(velocityX) > 1000) {//滑动速度超过阙值
                        if (velocityX < -1000) {
                            //平滑展开menu
                            smoothExpand();
                            //展开就加入mViewCache
                            mViewCache = this;
                        } else {
                            //平滑关闭menu
                            smoothClose();
                        }
                    } else {
                        if (getScaleX() > mLimit) {//否则就判断滑动距离
                            //平滑展开menu
                            smoothExpand();
                            //展开就加入viewCache
                            mViewCache = this;
                        } else {
                            //平滑关闭menu
                            smoothClose();
                        }
                    }
                    //释放
                    releaseVelocityTracker();
                    isTouching = false;//没有手指再摸我了
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //为了再侧滑时，屏蔽子view的点击事件
                if (getScaleX() > mScaleTouchSlop) {
                    //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                    if (ev.getX() < getWidth() - getScrollX()) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 平滑关闭
     */
    private void smoothClose() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        valueAnimator.addUpdateListener(animation -> scrollTo((Integer) animation.getAnimatedValue(), 0));
        valueAnimator.setInterpolator(new AnticipateInterpolator());
        valueAnimator.setDuration(300).start();
    }


    /**
     * 平滑展开
     */
    private void smoothExpand() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollX(), mRightMenuWidth);
        valueAnimator.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();
            scrollTo(value, 0);
        });
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(300).start();
    }

    /**
     * 向 VelocityTracker 添加MotionEvent
     */
    private void acquireVelocityTracker(MotionEvent ev) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    /**
     * 释放VelocityTracker
     */
    private void releaseVelocityTracker() {

        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 展开时，禁止长按
     */
    @Override
    public boolean performLongClick() {
        if (getScaleX() > 0) {
            return false;
        }
        return super.performLongClick();
    }
}
