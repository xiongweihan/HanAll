package com.example.hanall.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;
import com.example.hanall.adapter.CehuaAdapter;
import com.example.hanall.utils.recycleview.RecycleUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧滑activity
 */
public class CehuaActivity extends TitleActivity {
    private List<String> stringList = new ArrayList<>();


    private RecyclerView ceHuaRv;

    public static void StartActivity(Context activity){
        Intent intent = new Intent(activity,CehuaActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId2() {
        return R.layout.activity_cehua;
    }

    @Override
    protected void initView() {
        super.initView();
        ceHuaRv = findViewById(R.id.rv_ch);
        for (int i = 0; i < 20; i++) {
            stringList.add("h撒旦发射点");
        }
        CehuaAdapter adapter = new CehuaAdapter(stringList,CehuaActivity.this);
        RecycleUtil.getInstance(CehuaActivity.this,ceHuaRv)
                .setVertical()
                .setAdapter(adapter);



    }
}