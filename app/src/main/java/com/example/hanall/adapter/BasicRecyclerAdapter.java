package com.example.hanall.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.example.hanall.R;
import com.example.hanall.activity.MaterialDesignActivity;

import java.util.List;

public class BasicRecyclerAdapter extends BaseRecyclerAdapter<String> {

    public BasicRecyclerAdapter(List<String> list, Activity context){
        this.mList = list;
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_basic_recycler_layout;
    }

    @Override
    protected void bindView(BaseVH holder, int position) {
        TextView tvBasic = holder.getTextView(R.id.tv_basic_view);
        tvBasic.setText("BaSic" + position);
    }
}
