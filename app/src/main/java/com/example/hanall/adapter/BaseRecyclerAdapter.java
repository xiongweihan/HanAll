package com.example.hanall.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;

import java.util.List;



/**
 * 封装要求：
 * <p>
 * 布局和数据由外部（构造函数）传入；
 * Adapter的数据类型使用泛型；
 * 绑定数据时，无需通过position获取，直接返回泛型对象；
 * 需类型安全；
 * 封装点击事件，处理多重点击；
 * 点击事件中返回泛型，即无需通过position到集合中拿数据，直接返回泛型对象。
 *
 * @param <T>
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseVH> implements View.OnClickListener, View.OnLongClickListener {

    protected List<T> mList;
    protected Context mContext;

    public BaseRecyclerAdapter() {

    }

    public BaseRecyclerAdapter(List<T> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        BaseVH baseVH = new BaseVH(viewRoot);
        // View和Holder进行双向绑定
        viewRoot.setTag(R.id.tag_recycler_holder, baseVH);
        viewRoot.setOnClickListener(this);
        viewRoot.setOnLongClickListener(this);
        return baseVH;
    }

    protected abstract int getLayoutId();

    @Override
    public void onBindViewHolder(@NonNull BaseVH holder, int position) {
        bindView(holder, position);
    }

    protected abstract void bindView(BaseVH holder, int position);

    @Override
    public int getItemCount() {
        if (mList != null && mList.size() >= 0) {
            return mList.size();
        }
        return 0;
    }

    public List<T> getmList() {
        return mList;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
        if (mList != null) {
            Log.e("---size:=== ", mList.size() + " ----------------------------------");
        }
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据并通知插入
     **/
    public void add(T data) {
        mList.add(data);
        notifyItemInserted(mList.size() - 1);
    }

    /**
     * 删除操作
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (data != null) {
            int index = mList.indexOf(data);
            if (index != -1) {
                mList.remove(data);
                notifyItemRemoved(index);
            }
        }
    }

    @Override
    public void onClick(View v) {
        BaseVH holder = (BaseVH) v.getTag(R.id.tag_recycler_holder);
        if(onItemClickListener != null){
            int position = holder.getAdapterPosition();
            if(position >= 0 && position < mList.size()){
                onItemClickListener.onItemClick(position, mList.get(position), v);
            }

        }
    }

    @Override
    public boolean onLongClick(View v) {
        BaseVH holder = (BaseVH) v.getTag(R.id.tag_recycler_holder);
        if(onLongItemClickListener != null){
            int position = holder.getAdapterPosition();
            if(position >=0 && position < mList.size()){
                onItemClickListener.onItemClick(position, mList.get(position), v);
            }
        }

        return false;
    }


    private onItemClickListener<T> onItemClickListener;
    private onLongItemClickListener<T> onLongItemClickListener;

    public void setOnItemClickListener(onItemClickListener<T> onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongItemClickListener(onLongItemClickListener<T> onLongItemClickListener){
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public interface onItemClickListener<T>{
        void onItemClick(int position, T data, View view);
    }

    public interface onLongItemClickListener<T>{
        void onLongItemClick(int position, T data, View view);
    }

}


