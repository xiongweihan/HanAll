package com.example.hanall.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hanall.R;
import com.example.hanall.widget.leaf.AnimationUtils;
import com.example.hanall.widget.leaf.LeafLoadingView;

import java.util.Random;

public class LeafLoadingActivity extends TitleActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int REFRESH_PROGRESS = 0X10;
    private ImageView mFanView;
    private Button mClearButton;
    private LeafLoadingView mLeafLoadingView;
    private TextView mMplitudeText;

    private SeekBar mRotateTimeSeekBar,mAmpireSeekBar, mDistanceSeekBar, mFloatTimeSeekBar;
    private TextView mDisparityText, mProgressText, mFloatTimeText, mRotateTimeText;
    private Button mAddProgress;
    private int mProgress = 0;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_PROGRESS:
                    if(mProgress < 40){
                        mProgress += 1;
                        //随机800ms内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(800));
                        mLeafLoadingView.setmProgress(mProgress);
                    } else {
                        //随机1200ms内刷新一次
                        mProgress += 1;
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(1200));
                        mLeafLoadingView.setmProgress(mProgress);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LeafLoadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId2() {
        return R.layout.activity_leaf_loading;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void initView() {
        super.initView();
        mFanView = findViewById(R.id.fan_pic);
        RotateAnimation rotateAnimation = AnimationUtils.initRotateAnimation(
                false, 1500, true, Animation.INFINITE);
        mFanView.setAnimation(rotateAnimation);

        mClearButton = findViewById(R.id.clear_progress);
        mClearButton.setOnClickListener(this);

        mLeafLoadingView = findViewById(R.id.leaf_loading);
        mMplitudeText = findViewById(R.id.tv_ampair);
        mMplitudeText.setText(String.format(getResources().getString(R.string.current_mplitude),
                mLeafLoadingView.getMiddleAmplitude()));

        mAmpireSeekBar = findViewById(R.id.seekbar_ampair);
        mAmpireSeekBar.setOnSeekBarChangeListener(this);
        mAmpireSeekBar.setProgress(mLeafLoadingView.getMiddleAmplitude());
        mAmpireSeekBar.setMax(50);

        mDisparityText = findViewById(R.id.tv_disparity);
        mDisparityText.setText(getString(R.string.current_Disparity,
                mLeafLoadingView.getAmplitudeDisparity()));

        mDistanceSeekBar = findViewById(R.id.seekbar_disparity);
        mDistanceSeekBar.setOnSeekBarChangeListener(this);
        mDistanceSeekBar.setProgress(mLeafLoadingView.getAmplitudeDisparity());
        mDistanceSeekBar.setMax(20);

        mAddProgress = findViewById(R.id.add_progress);
        mAddProgress.setOnClickListener(this);
        mProgressText = findViewById(R.id.tv_progress);

        mFloatTimeText = findViewById(R.id.tv_flaot_time);
        mFloatTimeSeekBar = findViewById(R.id.seekbar_float_time);
        mFloatTimeSeekBar.setOnSeekBarChangeListener(this);
        mFloatTimeSeekBar.setMax(5000);
        mFloatTimeSeekBar.setProgress((int) mLeafLoadingView.getLeafFloatTime());
        mFloatTimeText.setText(getResources().getString(R.string.current_float_time,
                mLeafLoadingView.getLeafFloatTime()));

        mRotateTimeText = findViewById(R.id.tv_rotate_time);
        mRotateTimeSeekBar = findViewById(R.id.seekbar_rotate_time);
        mRotateTimeSeekBar.setOnSeekBarChangeListener(this);
        mRotateTimeSeekBar.setMax(5000);
        mRotateTimeSeekBar.setProgress((int) mLeafLoadingView.getLeafRotateTime());
        mRotateTimeText.setText(getResources().getString(R.string.current_rotate_time,
                mLeafLoadingView.getLeafRotateTime()));

        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,3000);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.clear_progress:
                mLeafLoadingView.setmProgress(0);
                mHandler.removeCallbacksAndMessages(null);
                mProgress = 0;
                break;
            case R.id.add_progress:
                mProgress ++;
                mLeafLoadingView.setmProgress(mProgress);
                mProgressText.setText(String.valueOf(mProgress));
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar == mAmpireSeekBar){
            mLeafLoadingView.setMiddleAmplitude(progress);
            mMplitudeText.setText(getString(R.string.current_mplitude,
                    progress));
        } else if(seekBar == mDistanceSeekBar){
            mLeafLoadingView.setAmplitudeDisparity(progress);
            mDisparityText.setText(getString(R.string.current_Disparity,
                    progress));
        } else if(seekBar == mFloatTimeSeekBar){
            mLeafLoadingView.setLeafFloatTime(progress);
            mFloatTimeText.setText(getResources().getString(R.string.current_float_time,
                    progress));
        } else if(seekBar == mRotateTimeSeekBar) {
            mLeafLoadingView.setLeafRotateTime(progress);
            mRotateTimeText.setText(getResources().getString(R.string.current_rotate_time,
                    progress));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}