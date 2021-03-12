package com.example.hanall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LooperViewPagerAdapter extends PagerAdapter {

    private List<Integer> imageViews = new ArrayList<>();
    private Context mContext;

    public LooperViewPagerAdapter(Context mContext, List<Integer> imageViews) {
        this.imageViews = imageViews;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (imageViews != null && imageViews.size() > 0) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    //是否复用当前view
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    //初始化每个条目要显示的内容
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % imageViews.size();

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(imageViews.get(realPosition));
        if (imageView.getParent() instanceof ViewGroup) {
                ((ViewGroup) imageView.getParent()).removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //获取到条目要显示的内容的imageView
        container.removeView((View) object);
    }
}
