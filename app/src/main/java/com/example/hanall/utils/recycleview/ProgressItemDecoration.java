package com.example.hanall.utils.recycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;

import java.util.IllformedLocaleException;

public class ProgressItemDecoration extends RecyclerView.ItemDecoration {
    private Paint linePaint;
    private Paint circlePaint;
    private int radius;
    private int currentPosition = 0;//当前进行中的位置下标
    private Context mContext;


    public ProgressItemDecoration(Context context) {
        this.mContext = context;
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeWidth(dp2Px(2));

        radius = dp2Px(8);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        linePaint.setStrokeWidth(dp2Px(2));

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = dp2Px(20);
        outRect.left = dp2Px(50);
        outRect.right = dp2Px(20);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int leftDecorationWidth = layoutManager.getLeftDecorationWidth(childView);
            int topDecorationHeight = layoutManager.getTopDecorationHeight(childView);
            //获取当前item是recyclerview 的第几个childView
            int childLayoutPosition = parent.getChildLayoutPosition(childView);
            float startX = leftDecorationWidth / 2;
            float stopX = startX;
            //圆顶部部分竖线， 起点Y
            float topStartY = childView.getTop() - topDecorationHeight;
            //圆顶部部分竖线，终点 Y
            float topStopY = (childView.getTop() + childView.getBottom()) / 2 - radius;
            //圆底部部分竖线，起点 Y；
            float bottomStartY = (childView.getTop() + childView.getBottom()) / 2 + radius;
            //圆底部部分竖线，终点 Y；
            float bottomStopY = childView.getBottom();

            //当位置超过curPosition时，竖线颜色--红色
            if (childLayoutPosition > currentPosition) {
                linePaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
                circlePaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
                circlePaint.setStyle(Paint.Style.STROKE);
            } else {
                linePaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
                circlePaint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
                circlePaint.setStyle(Paint.Style.FILL);
            }

            //绘制圆
            if (childLayoutPosition == currentPosition) {
                circlePaint.setStyle(Paint.Style.STROKE);
                c.drawCircle(leftDecorationWidth / 2, (childView.getTop() + childView.getBottom()) / 2, dp2Px(2), circlePaint);
            }
            c.drawCircle(leftDecorationWidth / 2, (childView.getTop() + childView.getBottom()) / 2, radius, circlePaint);

            //绘制竖线
            //第 0 个位置只需绘制下半部分竖线
            if (childLayoutPosition == 0) {
                if (childLayoutPosition == currentPosition) {
                    //当前item的position == currentPosition 时，绘制下半部分竖线时，颜色改为红色
                    linePaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
                }
                c.drawLine(startX, bottomStartY, startX, bottomStopY, linePaint);
            } else if (childLayoutPosition == childCount - 1) {
                //这时最后位置时，只需绘制上半部分
                c.drawLine(startX, topStartY, startX, topStopY, linePaint);
            } else {
                //其他都要绘制
                c.drawLine(startX, topStartY, startX, topStopY, linePaint);
                //当前item position == currentPosition 时，下半部分绘制时，颜色设为红色
                if (childLayoutPosition == currentPosition) {
                    linePaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
                }
                c.drawLine(startX, bottomStartY, startX, bottomStopY, linePaint);
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    public void setDoingPosition(RecyclerView recyclerView, int position) {
        if (recyclerView == null) {
            throw new IllegalArgumentException("RecyclerView can't be null");
        }
        if (recyclerView.getAdapter() == null) {
            throw new IllegalArgumentException("RecyclerView adapter can't be null");
        }

        if (position < 0) {
            throw new IllegalArgumentException("position can't be be less than 0");
        }
        int childCount = recyclerView.getLayoutManager().getChildCount();
        if (position > recyclerView.getAdapter().getItemCount() - 1) {
            throw new IllegalArgumentException("position can't be greter than item count");
        }
        this.currentPosition = position;
    }


    private int dp2Px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
    }
}
