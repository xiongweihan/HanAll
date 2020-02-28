package com.example.hanall.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hanall.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

public abstract class TitleActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.head_layout)
    RelativeLayout headLayout;//title头布局
    @BindView(R.id.iv_back)
    ImageView mIvBack;  //返回按钮
    @BindView(R.id.tv_back)
    TextView mTvBack;    //左侧返回文字
    @BindView(R.id.tv_title)
    TextView mTvTitle;   //标题
    @BindView(R.id.iv_right)
    ImageView mIvTitleRight;   //右侧小图标
    @BindView(R.id.tv_right)
    TextView mTvTitleRight;   //右侧文字

//    @BindView(R.id.ll_title_layout)
//    LinearLayout mllTitlelayout;//中间布局

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;//刷新控件
    @BindView(R.id.fl_refresh_content)
    FrameLayout mflContent;     //布局控件


    @Override
    protected int getLayoutId() {
        return R.layout.activity_title_layout;
    }

    @Override
    protected void initView() {
        setRefreshType(RefreshType.NONE);
        LayoutInflater.from(this).inflate(getLayoutId2(), mflContent);
    }

    //需要加载的布局
    protected abstract int getLayoutId2();

    /**
     * 设置title
     *
     * @param title
     */
    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
    }

    protected void setRightTitle(String title){
        if(TextUtils.isEmpty(title)){
            mTvTitleRight.setText(title);
        }
    }
    protected void setBackTitle(String title){
        if(TextUtils.isEmpty(title)){
            mTvBack.setText(title);
        }
    }



    @Override
    protected void initListener() {
        super.initListener();
        //返回按钮
        mIvBack.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        //刷新/加载更多
        mRefreshLayout.setOnRefreshListener(this);//刷新
        mRefreshLayout.setOnLoadMoreListener(this);//加载更多
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回按钮
            case R.id.tv_back:
                finish();
                dismissProgressDialog();
                onFinishClick();
                break;
        }
    }
    /**
     * 点击返回键的操作
     */
    private void onFinishClick() {

    }

    /**
     * 刷新操作
     * @param refreshLayout
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        // TODO: 2020/2/25  这个做测试，一会删除
        mRefreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
    }

    /**
     * 加载更多
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        // TODO: 2020/2/25  这个做测试，一会删除
        mRefreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败


    }

    public void setRefreshType(RefreshType refreshType) {
        switch (refreshType) {
            case ALL:
                mRefreshLayout.setEnableRefresh(true);
                mRefreshLayout.setEnableLoadMore(true);
                break;
            case ONLY_REFRESH:
                mRefreshLayout.setEnableRefresh(true);
                mRefreshLayout.setEnableLoadMore(false);
                break;
            case ONLY_LOAD_MORE:
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableLoadMore(true);
                break;
            case NONE:
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableLoadMore(false);
                break;
        }
    }

    public enum RefreshType {

        // 下拉刷新和上拉加载
        ALL,
        // 只有下拉刷新，没有上拉加载
        ONLY_REFRESH,
        // 没有下拉刷新，只有上拉加载
        ONLY_LOAD_MORE,
        // 无下拉刷新和上拉加载 ---默认
        NONE,

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshLayout != null) {
            mRefreshLayout = null;
        }
    }
}
