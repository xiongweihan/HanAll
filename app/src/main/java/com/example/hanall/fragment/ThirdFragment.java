package com.example.hanall.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.hanall.R;
import com.example.hanall.adapter.LooperViewPagerAdapter;
import com.example.hanall.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;


public class ThirdFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    TextView tvMan;
    TextView tvWoman;

    //无限循环的viewpager 轮播图
    private ViewPager vpLooper;
    private LooperViewPagerAdapter looperViewPagerAdapter;
    private List<Integer> imageList = new ArrayList<>();
    private int currentPosition = 0;//当前viewPager滚动的下标
    private LinearLayout llDotsLayout;
    private int[] imageUrls = new int[]{R.drawable.bg_slide_srceen_one, R.drawable.bg_slide_srceen_two/*, R.drawable.bg_slide_srceen_three*/};
    private ImageView[] dotImgs;//底部小圆点图片

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_third;
    }

    @Override
    protected void initView(View view) {
        tvMan = view.findViewById(R.id.tv_sex_man);
        tvWoman = view.findViewById(R.id.tv_sex_woman);
        tvMan.setSelected(true);//设置默认选择
        tvMan.setOnClickListener(this);
        tvWoman.setOnClickListener(this);

        //---------viewpager--start---
        vpLooper = view.findViewById(R.id.vp_looper);
        llDotsLayout = view.findViewById(R.id.ll_dots_layout);
        initViewPager();
        initDots();
        looperViewPagerAdapter = new LooperViewPagerAdapter(mContext, imageList);
        vpLooper.setAdapter(looperViewPagerAdapter);
        //设置到中间位置
        vpLooper.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
        vpLooper.addOnPageChangeListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sex_man:
                setSelectStatus(true, false);
                break;
            case R.id.tv_sex_woman:
                setSelectStatus(false, true);
                break;
        }

    }

    private void setSelectStatus(boolean b, boolean b1) {
        tvMan.setSelected(b);
        tvWoman.setSelected(b1);

    }

    /**
     * 添加数据
     */
    private void initViewPager() {
        imageList.clear();
        ImageView im;
        for (int imageUrl : imageUrls) {
            imageList.add(imageUrl);
        }
    }

    private void initDots() {
        llDotsLayout.removeAllViews();
        dotImgs = new ImageView[imageUrls.length];
        //设置小圆点宽和高
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(10, 0, 10, 0);
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.dot_drawable);
            if (i == 0) {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotImgs[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            llDotsLayout.addView(imageView);//添加到布局里面显示
        }

    }

    private void setDotImages() {
        for (int i = 0; i < dotImgs.length; i++) {
            Log.e("current", "--->" + vpLooper.getCurrentItem() % 2);
            if (i == vpLooper.getCurrentItem() % 2) {
                dotImgs[i].setSelected(true);
            } else {
                dotImgs[i].setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        Log.e("position","onPageSelected--:" + position);
        if (position == 0) {
            vpLooper.setCurrentItem(imageList.size() - 2, false);
        }
        if (position == imageList.size() - 1) {
            vpLooper.setCurrentItem(1, false);
        }
        setDotImages();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
