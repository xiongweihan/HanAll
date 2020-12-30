package com.example.hanall.activity;

import android.graphics.Color;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;
import com.example.hanall.adapter.BasicRecyclerAdapter;
import com.example.hanall.utils.ToastUtil;
import com.example.hanall.utils.recycleview.IconItemDecoration;
import com.example.hanall.utils.recycleview.LinearItemDecoration;
import com.example.hanall.utils.recycleview.ProgressItemDecoration;
import com.example.hanall.utils.recycleview.RecycleUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MaterialDesignActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_head_layout)
    LinearLayout llHeadLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_material_design;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void beforSetContentView() {
        super.beforSetContentView();
        //允许使用transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        super.initData();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//这是是否显示actionBar 自带的 返回箭头按钮。不设置默认没有，可以在toolbar里自己写ImageView
            //如果不喜欢自带的箭头，可以将箭头设置成我们的图片
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_logo);
        }
//        //这里是对返回键点击事件的监听
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showToast("返回");
//                finish();
//            }
//        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -llHeadLayout.getHeight() / 2) {
                    //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
                    collapsingToolbarLayout.setTitle("韩雄伟");
                    //通过CollapsingToolbarLayout修改字体颜色
                    collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.main_bottom_tab_glod));//设置收缩后Toolbar上字体的颜色

                } else {
                    collapsingToolbarLayout.setTitle("");
                }

            }
        });

        ///recyclerview 设置
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("Basic");
        }
        BasicRecyclerAdapter adapter = new BasicRecyclerAdapter(list, MaterialDesignActivity.this);

        ProgressItemDecoration itemDecoration = new ProgressItemDecoration(this);
        RecycleUtil.getInstance(MaterialDesignActivity.this, recyclerView)
                .setVertical()
                .setDivder(itemDecoration)
                .setAdapter(adapter);
        itemDecoration.setDoingPosition(recyclerView,4);

    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home://toolbar的返回按钮监听
//                finish();
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    //在ActionBar设置条目
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.titlebar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        switch (item.getItemId()) {
            case android.R.id.home://toolbar的返回按钮监听
                msg += "返回";
                finish();
                break;
            case R.id.webview:
                msg += "博客跳转";
                break;
            case R.id.weibo:
                msg += "微博跳转";
                break;
            case R.id.action_settings:
                msg += "设置";
                break;
        }
        if (!msg.equals("")) {
            Toast.makeText(MaterialDesignActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
