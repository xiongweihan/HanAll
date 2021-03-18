package com.example.hanall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.hanall.R;

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
                Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
