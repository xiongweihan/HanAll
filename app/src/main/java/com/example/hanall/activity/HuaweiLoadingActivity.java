package com.example.hanall.activity;


import android.content.Context;
import android.content.Intent;

import com.example.hanall.R;

public class HuaweiLoadingActivity extends TitleActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HuaweiLoadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId2() {
        return R.layout.activity_huawei_loading;
    }
}