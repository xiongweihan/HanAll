package com.example.hanall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanall.utils.NotchScreenTool;

import butterknife.ButterKnife;

/**
 * activity 父类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);//第三方注解绑定activity
        initView();
        initData();
        initListener();
    }

    protected void initData() {

    }

    protected void initListener() {

    }

    /**
     * 初始化窗口，在setContentView之前执行
     */
    protected void beforSetContentView() {
    }

    ;

    protected abstract void initView();

    protected abstract int getLayoutId();

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击后退键时是否取消Dialog
     *
     * @param whenBack true取消，false不取消
     */
    public void showProgressDialog(boolean whenBack) {
        if (isFinishing()) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(whenBack);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public void showProgressDialog() {
        showProgressDialog(true);
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 是否需要适配刘海屏的方法，哪里需要哪里调
     */
    public void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        } else {
            //判断手机厂商：华为、小米、oppo、vivo
            String brand = android.os.Build.BRAND.toLowerCase();
            if ("huawei".equals(brand)) {
                if (NotchScreenTool.hasNotchAtHuawei(this)) {
                    NotchScreenTool.setFullScreenAtHuawei(getWindow());
                }
            } else if ("xiaomi".equals(brand)) {
                if (NotchScreenTool.hasNotchAtXiaomi(this)) {
                    NotchScreenTool.setFullScreenAtXiaomi(getWindow());
                }
            } else if ("vivo".equals(brand)) {
                if (NotchScreenTool.hasNotchAtVivo(this)) {
                    //无需做处理
                }
            } else if ("oppo".equals(brand)) {
                if (NotchScreenTool.hasNotchAtOppo(this)) {
                    //无需做处理
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
