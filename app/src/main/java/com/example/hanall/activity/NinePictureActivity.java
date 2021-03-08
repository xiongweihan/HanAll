package com.example.hanall.activity;


import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hanall.R;
import com.example.hanall.utils.ToastUtil;
import com.example.hanall.widget.NineImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 仿微信 九宫格 图片展示
 */
public class NinePictureActivity extends TitleActivity {

    NineImageView nineImageView;

    private final List<String> mDatas = new ArrayList<>();
    private static final String imageUrl = "https://kr.shanghai-jiuxin.com/file/2020/1205/small07a4130b980565e055cfe53f8e552527.jpg";


    @Override
    protected void initView() {
        super.initView();
        nineImageView = findViewById(R.id.nine_iamge);
        mDatas.add(imageUrl);
    }

    @Override
    protected int getLayoutId2() {
        return R.layout.activity_nine_picture;
    }

    @Override
    protected void initData() {
        super.initData();
        nineImageView.setAdapter(new NineImageView.NineImageAdapter() {
                                     @Override
                                     public int getItemCount() {
                                         Log.e("---->", mDatas.size() + "--mDatas.size");
                                         return mDatas.size();
                                     }

                                     @Override
                                     public View createView(LayoutInflater inflater, ViewGroup parent, int position) {
                                         return inflater.inflate(R.layout.item_nine_imageview, parent, false);
                                     }

                                     @Override
                                     public void bindView(View view, int position) {
                                         final ImageView imageView = view.findViewById(R.id.image_view);
                                         if (mDatas.size() == 1) {
                                             Glide.with(NinePictureActivity.this)
                                                     .asBitmap()
                                                     .load(mDatas.get(0))
                                                     .into(new SimpleTarget<Bitmap>() {
                                                         @Override
                                                         public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                                             final int width = bitmap.getWidth();
                                                             final int height = bitmap.getHeight();
                                                             nineImageView.setSingleImage(width, height, imageView);
                                                         }
                                                     });
                                             Glide.with(NinePictureActivity.this).load(mDatas.get(0)).into(imageView);

                                         } else {
                                             Glide.with(NinePictureActivity.this).load(mDatas.get(position))
                                                     .into(imageView);
                                         }

                                     }

                                     @Override
                                     public void OnItemClick(View view, int position) {
                                         super.OnItemClick(view, position);
                                         ToastUtil.showToast("---->>" + position);
                                     }
                                 }
        );
    }

    /**
     * 加号点击事件
     */
    public void addClick(View view) {
        if (mDatas.size() < 9) {
//        mDatas.add("https://c-ssl.duitang.com/uploads/item/202006/16/20200616115751_lcicn.thumb.1000_0.jpg");
            mDatas.add(imageUrl);
        }
        initData();

    }

    /**
     * 减号点击事件
     */
    public void reduceClick(View view) {
        if (mDatas.size() > 1) {
            mDatas.remove(mDatas.size() - 1);
        }
        initData();
    }
}
