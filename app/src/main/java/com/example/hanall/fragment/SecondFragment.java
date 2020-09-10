package com.example.hanall.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.hanall.activity.MaterialDesignActivity;
import com.example.hanall.activity.NinePictureActivity;
import com.example.hanall.adapter.MedalPagerAdapter;
import com.example.hanall.utils.GlideUtil;
import com.example.hanall.utils.ScreenUtil;
import com.example.hanall.R;
import com.example.hanall.widget.PageTransformer.HorizontalStackTransformerWithRotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager mViewPager;
    private LinearLayout mViewPagerContainer;
    private LinearLayout llDotLayout;
    //需要显示的勋章图片
    private int[] mPics = new int[]{R.mipmap.ic_gallery, R.mipmap.ic_gallery, R.mipmap.ic_gallery, R.mipmap.ic_gallery,
            R.mipmap.ic_gallery, R.mipmap.ic_gallery, R.mipmap.ic_gallery, R.mipmap.ic_gallery, R.mipmap.ic_gallery, R.mipmap.ic_gallery};
    private ImageView[] dotImgs;//底部小圆点图片
    private List<View> imagViews;//用于包含勋章要显示的图片
    private int currentPosition;//当前选中的下标

    @BindView(R.id.iv_scenery)
    ImageView iv_scenery;


    @Override
    protected void initView(View view) {
        mViewPager = view.findViewById(R.id.viewpager);
        llDotLayout = view.findViewById(R.id.dot_layout);
        mViewPagerContainer = view.findViewById(R.id.viewPagerContainer);

        view.findViewById(R.id.btn_toNineImageActivity).setOnClickListener(this);
    }


    //模仿请求数据
    private void getDataDetail() {

        initNavigationImages();
        initDots();
        initViewPager();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second;
    }

    @Override
    protected void initData() {
        super.initData();
        getDataDetail();
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_scenery.setOnClickListener(this);
    }

    /**
     * 设置小圆点
     */
    private void initDots() {
        llDotLayout.removeAllViews();
        dotImgs = new ImageView[imagViews.size()];
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(10, 0, 10, 0);//设置小圆点左右之间的间隔
        for (int i = 0; i < imagViews.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.dot_drawable);
            if (i == 0) {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotImgs[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            llDotLayout.addView(imageView);//添加到布局里面显示
        }
    }

    private void initNavigationImages() {

        imagViews = new ArrayList<>();
        imagViews.clear();
        for (int mPic : mPics) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_gallery_layout, null);
            ImageView imageView = view.findViewById(R.id.iv_gallery);
            ViewGroup.LayoutParams imageLp = imageView.getLayoutParams();
            imageLp.width = (int) (ScreenUtil.getScreenWidth(Objects.requireNonNull(getContext())) * 0.36);
            imageLp.height = (int) (ScreenUtil.getScreenWidth(getContext()) * 0.36);
            imageView.setLayoutParams(imageLp);

            GlideUtil.load(getContext(), mPic, imageView);

            imagViews.add(view);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViewPager() {
        //设置ViewPager的布局
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ScreenUtil.getScreenWidth(Objects.requireNonNull(getContext())) * 6 / 10,
                ScreenUtil.getScreenWidth(getContext()) * 6 / 10);

        params.topMargin = ScreenUtil.getScreenHeight(mContext) / 15;

        /* *  重要部分 **/
        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
        mViewPager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        mViewPagerContainer.setClipChildren(false);

        mViewPager.setLayoutParams(params);

        mViewPager.addOnPageChangeListener(new OnPageChangeListener());

//        viewpager设置setPageTransformer（）方法一定要在setAdapter（）方法之前

        //设置ViewPager切换效果，即实现画廊效果
        mViewPager.setPageTransformer(true, new HorizontalStackTransformerWithRotation(mContext, mViewPager));
        //为ViewPager设置PagerAdapter
        mViewPager.setAdapter(new MedalPagerAdapter(imagViews));
        //设置预加载数量
        mViewPager.setOffscreenPageLimit(2);
        //设置每页之间的左右间隔
        mViewPager.setPageMargin(40);

        //将容器的触摸事件反馈给ViewPager
        mViewPagerContainer.setOnTouchListener((v, event) -> {
            // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
            return mViewPager.dispatchTouchEvent(event);
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_toNineImageActivity:
                intent = new Intent(mContext, NinePictureActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_scenery:
//                intent = new Intent(mContext, MaterialDesignActivity.class);
//                startActivity(intent);
                //动画
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(iv_scenery, iv_scenery.getWidth() / 2, iv_scenery.getHeight() / 2, 0, 0);
                ActivityCompat.startActivity(mContext, new Intent(mContext, MaterialDesignActivity.class), compat.toBundle());

//                //共享元素动画
//                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        (Activity) mContext,
//                        iv_scenery,
//                        "basic"
//                );
//                intent = new Intent(mContext, MaterialDesignActivity.class);
//                startActivity(intent, optionsCompat.toBundle());
                break;
        }
    }

    class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mViewPagerContainer != null) {
                mViewPagerContainer.invalidate();
            }
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            for (int i = 0; i < dotImgs.length; i++) {
                if (position == i) {
                    dotImgs[i].setSelected(true);
                } else {
                    dotImgs[i].setSelected(false);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
