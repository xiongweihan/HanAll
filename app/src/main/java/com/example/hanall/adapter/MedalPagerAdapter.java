package com.example.hanall.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 勋章的adapter
 */
public class MedalPagerAdapter extends PagerAdapter {
    private List<View> imags = new ArrayList<>();

    public MedalPagerAdapter(List<View> imags){
        this.imags = imags;
    }

    @Override
    public int getCount() {
        if(imags != null){
            return imags.size();
        }
        return 0;
    }

    //判断是否用对象生成界面
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    //从ViewGroup中移除当前对象（图片）
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(imags.get(position));
    }

    //当前要显示的对象（图片）
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(imags.get(position));
        return imags.get(position);
    }
}
