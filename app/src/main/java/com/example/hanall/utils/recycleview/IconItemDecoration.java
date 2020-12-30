package com.example.hanall.utils.recycleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;

/**
 * 左侧带有icon的分割线
 */
public class IconItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint = new Paint();
    private Paint dividerPaint;
    private Context mContext;
    private Bitmap iconBitmap;

    public IconItemDecoration(Context context) {
        this.mContext = context;
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        dividerPaint.setStyle(Paint.Style.FILL);

        iconBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.leaf);
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = 100;
        outRect.bottom = 5;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //通过 LayoutManager 来获取 getItemOffsets() 中设置的 outRect 的值。
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            int leftDecorationWidth = layoutManager.getLeftDecorationWidth(childView);
            int topDecorationHeight = layoutManager.getTopDecorationHeight(childView);
            int rightDecorationWidth = layoutManager.getRightDecorationWidth(childView);
            int bottomDecorationHeight = layoutManager.getBottomDecorationHeight(childView);

//            //画圆
//            c.drawCircle((leftDecorationWidth), childView.getTop() + (childView.getHeight() / 2), 20, mPaint);

            //            // getItemOffsets()中的设置的是 bottom = 5px;
            //            所以在 drawRect 时，top 为 childView.getBottom,bottom为top+bottomDecorationHeight
            //画下划线
            c.drawRect(new Rect(leftDecorationWidth,
                    childView.getBottom(),
                    childView.getWidth()+leftDecorationWidth,
                    childView.getBottom() + bottomDecorationHeight
            ), dividerPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c,
                           @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            int leftDecorationWidth = layoutManager.getLeftDecorationWidth(childView);
            c.drawBitmap(iconBitmap,leftDecorationWidth - iconBitmap.getWidth() / 2,childView.getTop() + childView.getHeight()/ 2 - iconBitmap.getHeight() / 2,mPaint);
        }
    }

}
