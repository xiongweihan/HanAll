package com.example.hanall.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;
import com.example.hanall.utils.ScreenUtil;

/**
 * 通讯录列表悬停专用的 Decoration
 */
public class SectionDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Context mContext;
    private TextPaint textPaint;
    private DecorationCallBack callBack;
    private int topGap;
    private Rect textRect;

    public SectionDecoration(Context context, DecorationCallBack callBack) {
        Resources resources = context.getResources();
        this.mContext = context;
        this.callBack = callBack;
        //设置悬浮栏画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(resources.getColor(R.color.color_D6DDEA));

        //设置悬浮栏中的文字的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(ScreenUtil.dip2px(mContext, 14));
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextAlign(Paint.Align.LEFT);

        //决定文本显示位置
        topGap = ScreenUtil.dip2px(mContext, 40);
        textRect = new Rect();


    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        //只有是同一组第第一个才会显示悬浮栏
        if (pos == 0 || isFirstGroup(pos)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String textLine = callBack.getGroupName(position).toUpperCase();

            if (position == 0 || isFirstGroup(position)) {
                int top = view.getTop() - topGap;
                int bottom = view.getTop();
                //绘制悬浮栏
                c.drawRect(left, top, right, bottom, mPaint);
                //绘制文字
                textPaint.getTextBounds(textLine, 0, textLine.length(), textRect);
                c.drawText(textLine, left + 20, bottom - topGap / 2 + textRect.height() / 2, textPaint);
            } else {
                //绘制悬浮栏
                c.drawRect(left, view.getTop() - 1, right, view.getTop(), mPaint);
            }

        }

    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();//40 --总的
        int childCount = parent.getChildCount();//13 --当前显示页

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        String preGroupId;
        String groupId = "-1";
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);

            preGroupId = groupId;
            groupId = callBack.getGroupName(position);
            if (groupId.equals(preGroupId)) continue;

            String textLine = callBack.getGroupName(position).toUpperCase();

            int viewBottom = childView.getBottom();

            Log.e("--->", "childView.getTop()==" + childView.getTop());
            Log.e("--->", "childView.getBottom()==" + childView.getBottom());

            float textY = Math.max(topGap, childView.getTop());
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                String nextGroupId = callBack.getGroupName(position + 1);
                //组内最后一个view进入header
                if (!nextGroupId.equals(groupId) && viewBottom < textY) {
                    textY = viewBottom;
                }
            }

            //textY-topGap 决定了悬浮栏绘制的高度和位置
            c.drawRect(left, textY - topGap, right, textY, mPaint);

            //left+2*alignBottom 决定了文本往左偏移的多少（加-->向左移）
            //textY-alignBottom  决定了文本往右偏移的多少  (减-->向上移)
            textPaint.getTextBounds(textLine, 0, textLine.length(), textRect);
            c.drawText(textLine, left + 20, textY - topGap / 2 + textRect.height() / 2, textPaint);
        }
    }

    private boolean isFirstGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            // 因为是根据 字符串内容的相同与否 来判断是不是同意组的，所以此处的标记id 要是String类型
            // 如果你只是做联系人列表，悬浮框里显示的只是一个字母，则标记id直接用 int 类型就行了
            String prevGroupId = callBack.getGroupName(pos - 1);
            String groupId = callBack.getGroupName(pos);
            //判断前一个字母与当前字符串是否相同
            return !prevGroupId.equals(groupId);
        }
    }


    public interface DecorationCallBack {
        String getGroupName(int position);
    }
}
