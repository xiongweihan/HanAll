package com.example.hanall.utils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DoubleClickUtils implements View.OnTouchListener {

    private static final int Time = 1000;//防止双击的间隔时间
    private static long lastClickTime = 0;

    /**
     * 是否做了双击
     *
     * @return true-- 是  ；false -- 否
     */
    public static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        long timeInterval = currentTime - lastClickTime;

        if (timeInterval > 0 && timeInterval < Time) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

//---------------------------------------------------------------------

    private final int intervalTime = 350; //双击时间间隔
    private int count = 0;  //点击次数
    private long firClick = 0;  //第一次点击时间
    private long secClick = 0;  //第二次点击时间

    public DoubleClickUtils(OnDoubleClickListener doubleClickListener) {
        super();
        this.doubleClickListener = doubleClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                count++;
                if (1 == count) {
                    firClick = System.currentTimeMillis();
                } else if (2 == count) {
                    secClick = System.currentTimeMillis();
                    if (secClick - firClick < intervalTime) { //如果两次点击间隔小与
                        if (doubleClickListener != null) {
                            doubleClickListener.onDoubleClick(v);
                        } else {
                            Log.e("错误提示：","请在构造方法中传入一个双击回调");
                        }
                        count = 0;
                        firClick = 0;
                    } else {
                        firClick = secClick;
                        count = 1;
                    }
                    secClick = 0;
                }
                break;
        }
        return true;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }

    public OnDoubleClickListener doubleClickListener;

    public void setOnDoubleClickListener(OnDoubleClickListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }

    /**
     *          使用
     *         mTvReceive.setOnTouchListener(new DoubleClickUtils(new DoubleClickUtils.OnDoubleClickListener() {
     *             @Override
     *             public void onDoubleClick(View view) {
     *
     *             }
     *         }));
     */


}
