package com.example.hanall.adapter;

import android.content.Context;

import com.example.hanall.R;
import com.example.hanall.model.CityBean;

import java.util.List;

/**
 * 通讯录adapter
 */
public class AddressBookAdapter extends BaseRecyclerAdapter<CityBean>{
    public AddressBookAdapter(List<CityBean> addressBookList, Context context) {
        super(addressBookList,context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_address_book_adapter_layout;
    }

    @Override
    protected void bindView(BaseVH holder, int position) {
        holder.setTextview(R.id.tv_name,mList.get(position).getCity());
    }
}
