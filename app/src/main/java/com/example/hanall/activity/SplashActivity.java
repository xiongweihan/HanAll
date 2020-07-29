package com.example.hanall.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.hanall.R;
import com.example.hanall.adapter.MedalPagerAdapter;
import com.example.hanall.widget.PageTransformer.SplashTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ll_dots_layout)
    LinearLayout llDotsLayout;

    private List<View> imags = new ArrayList<>();//用于包含引导页要显示的图片
    private ImageView[] dotImgs;//底部小圆点图片
    private MedalPagerAdapter viewPagerAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        initNavigationImages();
        initDots();
        viewPagerAdapter = new MedalPagerAdapter(imags);
        mViewPager.setPageTransformer(true,new SplashTransformer());
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

    }

    @Override
    protected void initListener() {
        super.initListener();
        tvNext.setOnClickListener(this);
    }

    /**
     * 初始化滑屏图片
     */
    private void initNavigationImages() {
        imags.clear();
        View view1 = LayoutInflater.from(this).inflate(R.layout.item_splash_first_view, null);
        imags.add(view1);
        View view2 = LayoutInflater.from(this).inflate(R.layout.item_splash_second_view, null);
        imags.add(view2);
        View view3 = LayoutInflater.from(this).inflate(R.layout.item_splash_third_view, null);
        imags.add(view3);
        View view4 = LayoutInflater.from(this).inflate(R.layout.item_splash_fourth_view, null);
        imags.add(view4);
        TextView tvSkipEnd = view4.findViewById(R.id.tv_experience);
        tvSkipEnd.setOnClickListener(v -> {
            startMainActivity();
        });


    }

    /**
     * 初始化小圆点
     */
    private void initDots() {
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.width = 10;
        mParams.height = 10;
        mParams.setMargins(10,0,10,0);
        mParams.gravity = Gravity.CENTER;
        dotImgs = new ImageView[imags.size()];
        for (int i = 0; i < imags.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.dot_drawable);
            imageView.setLayoutParams(mParams);
            if (i == 0) {
                imageView.setSelected(true);
            } else {
                imageView.setSelected(false);
            }
            dotImgs[i] = imageView;
            llDotsLayout.addView(imageView);
        }
    }


    @Override
    public void onClick(View v) {
        if(R.id.tv_next == v.getId()){
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < imags.size(); i++) {
            if(position == i){
                dotImgs[i].setSelected(true);
            } else {
                dotImgs[i].setSelected(false);
            }
        }
        if(position == imags.size() - 1){
            tvNext.setVisibility(View.GONE);
        } else {
            tvNext.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
