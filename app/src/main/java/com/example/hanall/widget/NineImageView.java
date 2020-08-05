package com.example.hanall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hanall.R;
import com.example.hanall.utils.ConvertUtil;

public class NineImageView extends ViewGroup {

    //一张图片允许的最大  宽高范围
    private int singleImageMaxWidth = 200;
    //图片之间间隙
    private int itemMargin = 5;
    //控件宽度
    private int width = 300;
    //单个图片的宽度和高度
    private int itemWidth;

    //单张图片时的宽高
    private int singleViewWidth = 0;
    private int singleViewHeight = 0;


    public NineImageView(Context context) {
        this(context, null);
    }

    public NineImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineImageView);
        singleImageMaxWidth = typedArray.getDimensionPixelSize(R.styleable.NineImageView_nine_singleImagMaxWidth, ConvertUtil.dip2Px(getContext(), singleImageMaxWidth));
        itemMargin = typedArray.getDimensionPixelSize(R.styleable.NineImageView_nine_iamge_gap, ConvertUtil.dip2Px(getContext(), itemMargin));
        width = typedArray.getDimensionPixelSize(R.styleable.NineImageView_nine_layoutWidth, ConvertUtil.dip2Px(getContext(), width));
        typedArray.recycle();

        itemWidth = (width - (itemMargin * 2)) / 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件的宽高
        int viewWidth = 0;
        int viewHeight = 0;
        int count = getChildCount();
        if (count == 1) {
            setMeasuredDimension(singleViewWidth, singleViewHeight);
            return;
        } else if (count <= 3) {
            viewHeight = itemWidth;
            if (count == 2) {
                viewWidth = itemWidth * 2 + itemMargin;
            } else if (count == 3) {
                viewWidth = width;
            }
        } else if (count <= 6) {
            viewHeight = itemWidth * 2 + itemMargin;
            if (count == 4) {
                viewWidth = itemWidth * 2 + itemMargin;
            } else {
                viewWidth = width;
            }
        } else if (count <= 9) {
            viewWidth = width;
            viewHeight = width;
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < count; i++) {
            View childAt = getChildAt(i);
            switch (i) {
                case 0:
                    left = 0;
                    top = 0;
                    if (count == 1) {
                        //单独处理
                        right = left + singleViewWidth;
                        bottom = top + singleViewHeight;
                    } else {
                        right = left + itemWidth;
                        bottom = top + itemWidth;
                    }
                    break;
                case 1:
                    left = itemWidth + itemMargin;
                    top = 0;
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                    break;
                case 2:
                    if (count == 4) {
                        left = 0;
                        top = itemWidth + itemMargin;
                        right = left + itemWidth;
                        bottom = top + itemWidth;
                    } else {
                        left = itemWidth * 2 + itemMargin * 2;
                        top = 0;
                        right = width;
                        bottom = top + itemWidth;
                    }
                    break;
                case 3:
                    if (count == 4) {
                        left = itemWidth + itemMargin;
                        top = itemWidth + itemMargin;
                        right = left + itemWidth;
                        bottom = top + itemWidth;
                    } else {
                        left = 0;
                        top = itemWidth + itemMargin;
                        right = left + itemWidth;
                        bottom = top + itemWidth;
                    }
                    break;
                case 4:
                    left = itemWidth + itemMargin;
                    top = itemWidth + itemMargin;
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                    break;
                case 5:
                    left = itemWidth * 2 + itemMargin * 2;
                    top = itemWidth + itemMargin;
                    right = width;
                    bottom = top + itemWidth;
                    break;
                case 6:
                    left = 0;
                    top = itemWidth * 2 + itemMargin * 2;
                    right = left + itemWidth;
                    bottom = width;
                    break;
                case 7:
                    left = itemWidth + itemMargin;
                    top = itemWidth * 2 + itemMargin * 2;
                    right = left + itemWidth;
                    bottom = width;
                    break;
                case 8:
                    left = itemWidth * 2 + itemMargin * 2;
                    top = itemWidth * 2 + itemMargin * 2;
                    right = width;
                    bottom = width;
                    break;
                default:
                    break;
            }
            childAt.layout(left,top,right,bottom);
        }
    }

    public void setSingleImage(int width, int height, View view){
        Log.e("---->","getChildCount=="+getChildCount());

        if(getChildCount() != 1){
            removeAllViews();
            addView(view);
        }
        Log.e("---->","width=="+width+"//height" + height);

        if(width >= height){
            singleViewWidth = singleImageMaxWidth;
            singleViewHeight = (int) (singleImageMaxWidth * ((double)height /(double) width));
        } else {
            singleViewHeight = singleImageMaxWidth;
            singleViewWidth = (int) (singleImageMaxWidth * ((double)width /(double) height));
        }
        getChildAt(0).layout(0,0,singleViewWidth, singleViewHeight);
        setMeasuredDimension(singleViewWidth,singleViewHeight);
    }

    /**
     * 设置数据源
     * @param adapter
     */
    public void setAdapter(NineImageAdapter adapter){
        removeAllViews();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            View view = adapter.createView(LayoutInflater.from(getContext()), this, i);
            adapter.bindView(view,i);
            removeView(view);
            addView(view);
            bindClickEvent(view,i,adapter);

        }

    }

    /**
     * 绑定点击事件
     * @param view
     * @param posiiton
     * @param adapter
     */
    private void bindClickEvent(View view, int posiiton, NineImageAdapter adapter) {
        if(adapter == null){
            return;
        }
        view.setOnClickListener(v -> adapter.OnItemClick(view,posiiton));
    }

    /**
     * 获取MarginLayoutParams必须要重写这几个方法
     * @return
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

   public abstract static class NineImageAdapter{

       public  abstract int getItemCount();

       public  abstract View createView(LayoutInflater inflater, ViewGroup parent, int position);

       public  abstract void bindView(View view, int position);

       public  void OnItemClick(View view, int position){

        }

    }
}
