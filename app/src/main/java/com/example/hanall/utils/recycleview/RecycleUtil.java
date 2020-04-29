package com.example.hanall.utils.recycleview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecycleView 的工具类
 */
public class RecycleUtil {

    private static RecycleUtil util;
    private Context mContext;
    private RecyclerView view;

    public RecycleUtil(Context mContext, RecyclerView view) {
        this.mContext = mContext;
        this.view = view;
    }

    public static RecycleUtil getInstance(Context context, RecyclerView view) {
        util = new RecycleUtil(context, view);
        return util;
    }

    /**
     * @param itemDecoration 分割线
     * @return 设置分割线
     */
    public RecycleUtil setDivder(RecyclerView.ItemDecoration itemDecoration) {
        if ((view != null) && (itemDecoration != null)) {
            view.addItemDecoration(itemDecoration);
        }
        return util;
    }

    /**
     * @return 设置竖向的线性布局
     */
    public RecycleUtil setVertical() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        if (view != null) {
            view.setLayoutManager(manager);
        }
        return util;
    }

    /**
     * @return 设置横向的线性布局
     */
    public RecycleUtil setHorizontal() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        if (view != null) {
            view.setLayoutManager(manager);
        }
        return util;
    }

    /**
     * 设置Adaptrer
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null && view != null) {
            view.setAdapter(adapter);
        }
    }

    /**
     * @return 解决数据加载完成后, 没有停留在顶部的问题
     */
    public RecycleUtil setFocusable() {
        //解决数据加载完成后, 没有停留在顶部的问题
        if (view != null) {
            view.setFocusable(false);
        }
        return util;
    }

    public RecycleUtil setDataHeight() {
        //解决数据加载不完的问题
        if (view != null) {
            view.setNestedScrollingEnabled(false);
            view.setHasFixedSize(true);
        }
        return util;
    }

    /**
     * 销毁内存
     */
    public static void clear() {
        if (util != null) {
            util = null;
        }
    }
}
