package com.example.hanall.activity;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.hanall.R;
import com.example.hanall.adapter.FragmentViewPagerAdapter;
import com.example.hanall.fragment.FirstFragment;
import com.example.hanall.fragment.FourthFragment;
import com.example.hanall.fragment.SecondFragment;
import com.example.hanall.fragment.ThirdFragment;
import com.example.hanall.utils.DoubleClickUtils;
import com.example.hanall.utils.ScreenInfoUtils;
import com.example.hanall.utils.ToastUtil;
import com.example.hanall.widget.HeadView;
import com.example.hanall.widget.PopDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.iv_main_head)
    HeadView ivMainHead;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    private HeadView ivDrawerHead;
    private TextView tvDrawerName;
    @BindView(R.id.footer_item_setting)
    Button btnSetting;
    @BindView(R.id.footer_item_out)
    Button btnout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationTabLayout;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforSetContentView() {
        super.beforSetContentView();
        ScreenInfoUtils.fullScreen(this);//设置全屏，设置状态栏透明
    }

    @Override
    protected void initView() {
        // (被遮挡部分的)阴影部分的颜色
        drawer.setScrimColor(Color.parseColor("#66666666"));

        /*------------添加头布局--------------------*/
        View headView = navigationView.getHeaderView(0);//获取头部布局
        //添加头布局的另外一种方式
        //View headview=navigationview.inflateHeaderView(R.layout.navigationview_header);
        ivDrawerHead = headView.findViewById(R.id.iv_head);
        tvDrawerName = headView.findViewById(R.id.tv_name);

        //设置item的条目颜色
        // 有-为未选中的颜色, 没有-为选中的颜色
        int[][] states = new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}};
        int[] colors = new int[]{getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorAccent)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        navigationView.setItemIconTintList(colorStateList);//设置图标颜色
        navigationView.setItemTextColor(colorStateList);//设置文字颜色.设置为null显示本来图片的颜色

        //设置消息数量
//        LinearLayout llAndroid = (LinearLayout) navigationView.getMenu().findItem(R.id.single_1).getActionView();
//        llAndroid.setBackgroundColor(Color.RED);
////        TextView msg = llAndroid.findViewById(R.id.tv_msg_bg);
////        msg.setText("99+");

        //设置默认第一个选中
        navigationView.setCheckedItem(R.id.single_1);
        //设置条目点击监听
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        ivMainHead.setImageResource(R.mipmap.ic_logo);//首页头像
        tvMainTitle.setText("首页标题~");
        ivDrawerHead.setImageResource(R.mipmap.ic_logo);//侧滑头像
        tvDrawerName.setText("韩雄伟");
        //设置底部按钮与页面fragment联动

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());
        fragments.add(new FourthFragment());
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
        super.initListener();
        ivMainHead.setOnClickListener(this);
        ivDrawerHead.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
//        btnout.setOnClickListener(this);
        //双击
        btnout.setOnTouchListener(new DoubleClickUtils(view -> {
            ToastUtil.showToast("双击操作");
        }));

        mViewPager.addOnPageChangeListener( new PageChangeListener());
        bottomNavigationTabLayout.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId){
                case R.id.tab_1:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.tab_2:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.tab_3:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.tab_4:
                    mViewPager.setCurrentItem(3);
                    break;
            }
            return false;
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer == null) return;
        // 返回键: 侧滑开着就将其关闭, 关着则退出应用
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //侧滑抽屉menu点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //点击哪个按钮
        ToastUtil.showToast("" + item.getTitle());
        switch (item.getItemId()) {
            case R.id.single_1:
                break;
            case R.id.single_2:
                break;
            case R.id.single_3:
                break;
            case R.id.single_4:
                break;
            case R.id.item_1:
                break;
            case R.id.item_2:
                break;
            case R.id.item_3:
                break;
        }
        //关闭侧边栏
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_head://首页title上的头像
                drawer.openDrawer(Gravity.LEFT, true);//打开侧滑抽屉
                break;
            case R.id.iv_head://抽屉里的头像
                ToastUtil.showToast("侧滑头像");
                //需要些选择图像替换
                break;
            case R.id.footer_item_setting:
                if(DoubleClickUtils.isDoubleClick()){
                    ToastUtil.showToast("请勿重复点击");
                    return;
                }
                ToastUtil.showToast("设置");
                break;
//            case R.id.footer_item_out:
//                ToastUtil.showToast("退出");
//                //有时间写登录页面
//                showDialog();
//                break;
        }
    }

    private void showDialog() {
        PopDialog popDialog = new PopDialog(this);
        popDialog.setTitle("测试title")
                .setMessage("测试message")
                .setNegtive("取消")
                .setSingle(true)
                .setOnClickBottomListener(new PopDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        ToastUtil.showToast("onPositiveClick");
                        popDialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        ToastUtil.showToast("onNegtiveClick");
                        popDialog.dismiss();
                    }
                }).show();
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // 将当前的页面对应的底部标签设为选中状态
            bottomNavigationTabLayout.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
