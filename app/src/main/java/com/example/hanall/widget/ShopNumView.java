package com.example.hanall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.hanall.R;


/**
 * 自定义购物车数量加减控件
 */
public class ShopNumView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private static final int MIN_VALUE = 0;  //最小数量
    private static final int MAX_VALUE = 99; //最大数量

    private int maxValue;//最大数量
    private int countValue = MIN_VALUE;
    private Context mContext;
    private ImageView ivDelete, ivAdd;
    private EditText etBuyCount;
    private boolean canInputType = false; //是否可输入

    public ShopNumView(Context context) {
        this(context, null);
    }

    public ShopNumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShopNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        View parent = LayoutInflater.from(mContext).inflate(R.layout.item_shop_num_view_layout, this);
        ivAdd = parent.findViewById(R.id.iv_buy_add);
        ivDelete = parent.findViewById(R.id.iv_buy_delete);
        etBuyCount = parent.findViewById(R.id.tv_buy_count);
        ivAdd.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        etBuyCount.addTextChangedListener(this);


        @SuppressLint({"Recycle", "CustomViewStyleable"}) TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.shopNumberView);
        int maxValue = array.getInt(R.styleable.shopNumberView_maxValue, MAX_VALUE);
        setMaxValue(maxValue);
        boolean b = array.getBoolean(R.styleable.shopNumberView_canInput, false);
        setCanInput(b);

        array.recycle();
    }

    /**
     * 设置最大数量
     *
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;

    }

    /**
     * edittext 是否可编辑
     *
     * @param inputType
     */
    public void setCanInput(boolean inputType) {
        etBuyCount.setFocusable(inputType);
        etBuyCount.setFocusableInTouchMode(inputType);
        etBuyCount.setCursorVisible(inputType);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_buy_add:
                countValue++;
                btnChangeWord();
                break;
            case R.id.iv_buy_delete:
                countValue--;
                btnChangeWord();
                break;
        }

    }

    private void btnChangeWord() {
        ivDelete.setEnabled(countValue > MIN_VALUE);
        ivAdd.setEnabled(countValue < maxValue);
        etBuyCount.setText(String.valueOf(countValue));
        etBuyCount.setSelection(etBuyCount.getText().toString().trim().length());
        if (callBack != null) {
            callBack.changeClick(countValue);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean needupdata = false;
        if (!TextUtils.isEmpty(s.toString())) {
            countValue = Integer.parseInt(s.toString());
            if (countValue <= MIN_VALUE) {
                countValue = MIN_VALUE;
                ivDelete.setEnabled(false);
                ivAdd.setEnabled(true);
                needupdata = true;
            } else if (countValue >= maxValue) {
                countValue = maxValue;
                ivDelete.setEnabled(true);
                ivAdd.setEnabled(false);
                needupdata = true;
            } else {
                ivAdd.setEnabled(true);
                ivDelete.setEnabled(true);
            }
        } else {
            //如果输入框被清空了，直接填写1
            countValue = 1;
            ivAdd.setEnabled(true);
            ivDelete.setEnabled(false);
            needupdata = true;
        }
        changeWord(needupdata);

    }

    /**
     * @param needupdata --是否需要重新赋值
     */
    private void changeWord(boolean needupdata) {
        if (needupdata) {
            etBuyCount.removeTextChangedListener(this);
            if (!TextUtils.isEmpty(etBuyCount.getText().toString().trim())) {
                etBuyCount.setText(String.valueOf(countValue));
            }
            etBuyCount.addTextChangedListener(this);
        }
        etBuyCount.setSelection(etBuyCount.getText().toString().trim().length());
        if (callBack != null) {
            callBack.changeClick(countValue);
        }
    }


    public interface QuantityChangeCallBack {
        void changeClick(int value);
    }

    public QuantityChangeCallBack callBack;

    public void setOnQuantityChangeCallBack(QuantityChangeCallBack callBack) {
        this.callBack = callBack;
    }
}
