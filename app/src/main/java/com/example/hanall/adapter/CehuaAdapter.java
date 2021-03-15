package com.example.hanall.adapter;

import android.content.Context;
import android.view.View;

import com.example.hanall.R;
import com.example.hanall.activity.CehuaActivity;
import com.example.hanall.widget.cehuashanchu.DragItemHelper;

import java.util.List;

/**
 * 侧滑页面对应的adapter
 */
public class CehuaAdapter extends BaseRecyclerAdapter<String> {

    public CehuaAdapter(List<String> stringList, Context context) {
        super(stringList, context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_cehua_adapter_layout;
    }

    @Override
    protected void bindView(BaseVH holder, int position) {
        holder.getTextView(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DragItemHelper.getInstance().closeItemDragLayout();
            }
        });

    }
}
