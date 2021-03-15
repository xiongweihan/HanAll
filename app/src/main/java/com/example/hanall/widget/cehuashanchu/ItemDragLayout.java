package com.example.hanall.widget.cehuashanchu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * recyclerview 列表侧滑删除的辅助类
 */
public class ItemDragLayout extends FrameLayout {

    //是一个用于编写自定义视图组的工具类。
    private final ViewDragHelper mDragHelper;
    private final ViewConfiguration mViewConfiguration;

    private View mLeftLayout, mRightLayout;
    private int mLeftWidth;
    private int mLeftHeight;
    private int mRightWidth;
    private int mRightHeight;
    private State mState = State.CLOSE;
    private float mDownX, mDownY;

    public enum State {
        // 打开状态，即向左滑动使右侧部分完全显示出来
        OPEN,
        // 关闭状态，无滑动时的正常显示状态
        CLOSE
    }

    public ItemDragLayout(@NonNull Context context) {
        this(context, null);
    }

    public ItemDragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mViewConfiguration = ViewConfiguration.get(context);
        mDragHelper = ViewDragHelper.create(this, callBack);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftLayout = getChildAt(0);
        mRightLayout = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取左右两部分布局的宽高
        mLeftWidth = mLeftLayout.getMeasuredWidth();
        mLeftHeight = mLeftLayout.getMeasuredHeight();
        mRightWidth = mRightLayout.getMeasuredWidth();
        mRightHeight = mRightLayout.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //摆放两个布局的初始化位置
        mLeftLayout.layout(0, 0, mLeftWidth, mLeftHeight);
        mRightLayout.layout(mLeftWidth, 0, mLeftWidth + mRightWidth, mRightHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //拦截事件，如果当前已经有一个item处于open状态，则拦截事件
        boolean intercept = mDragHelper.shouldInterceptTouchEvent(ev);
        if (!DragItemHelper.getInstance().canDrag(ItemDragLayout.this)) {
            DragItemHelper.getInstance().closeItemDragLayout();
            intercept = true;
        }
        //只要滑动item，不管滑动的是哪个，都将处于open状态的item关闭
        //如果想要达到点击item或者右侧画出来的布局使整个item关闭，则直接去掉下面判断中的intercept即可
        if (intercept && DragItemHelper.getInstance().getOpenedItemLayout() != null) {
            DragItemHelper.getInstance().closeItemDragLayout();
        }
        return intercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                //如果横向滑动，则不让RecyclerView/ListView 等滑动控件拦截事件，自己来处理
                if (Math.abs(moveX - mDownX) > Math.abs(moveY - mDownY)) {
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
        }

        return true;
    }

    ViewDragHelper.Callback callBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == mLeftLayout || child == mRightLayout;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return mRightWidth;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            //如果有item处于OPEN 状态，则滑动其他item时，该item无法滑动
            if (!DragItemHelper.getInstance().canDrag(ItemDragLayout.this)) {
                return -1;
            }
            //根据滑动的对象view指定当前view的左右位置限制条件
            if (child == mLeftLayout) {
                if (left > 0) {
                    left = 0;
                }
                if (left < -mRightWidth) {
                    left = -mRightWidth;
                }
            } else {
                if (left < mLeftWidth - mRightWidth) {
                    left = mLeftWidth - mRightWidth;
                }
                if (left > mLeftWidth) {
                    left = mLeftWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            //处理伴随滑动
            if (changedView == mLeftLayout) {
                int rightLayoutLeft = mRightLayout.getLeft() + dx;
                mRightLayout.layout(rightLayoutLeft, top, rightLayoutLeft + mRightWidth, top + mRightHeight);
            } else {
                int leftLayoutLeft = mLeftLayout.getLeft() + dx;
                mLeftLayout.layout(leftLayoutLeft, top, leftLayoutLeft + mLeftWidth, mLeftHeight);
            }

            Log.e("aaa","mLeftLayout.getLeft() ==" + mLeftLayout.getLeft()+"///-mRightWidth == "+ (-mRightWidth) + "//mState==" + mState);
            if (mLeftLayout.getLeft() == -mRightWidth && mState == State.CLOSE) {
                mState = State.OPEN;
                DragItemHelper.getInstance().setOpenedItemLayout(ItemDragLayout.this);
            } else if (mLeftLayout.getLeft() == 0 && mState == State.OPEN) {
                mState = State.CLOSE;
                DragItemHelper.getInstance().removeItemDragLayout();
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (Math.abs(xvel) > mViewConfiguration.getScaledMinimumFlingVelocity()) {
                if (xvel > 0) {
                    close();
                } else {
                    open();
                }
            } else {
                if (mLeftLayout.getLeft() < -mRightWidth / 2) {
                    open();
                } else {
                    close();
                }
            }
        }
    };

    private void open() {
        mDragHelper.smoothSlideViewTo(mLeftLayout, -mRightWidth, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void close() {
        mDragHelper.smoothSlideViewTo(mLeftLayout, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        //当前view不可见时，清空item，防止试图切换时造成item存储错误导致左右页面的item不可左右滑动，例如切换activity
        if (visibility != View.VISIBLE) {
            DragItemHelper.getInstance().removeItemDragLayout();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //当侧滑删除item时需要回复状态
        DragItemHelper.getInstance().removeItemDragLayout();
    }

    public State getState() {
        return mState;
    }


}
