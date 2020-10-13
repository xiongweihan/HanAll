package com.example.hanall.fragment;

import android.view.View;
import android.widget.TextView;

import com.example.hanall.R;


public class ThirdFragment extends BaseFragment implements View.OnClickListener {

    TextView tvMan;
    TextView tvWoman;

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
}
