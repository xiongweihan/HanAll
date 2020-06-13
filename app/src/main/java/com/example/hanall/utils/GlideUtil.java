package com.example.hanall.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanall.R;

public class GlideUtil {

    private static GlideUtil glideUtil;
    private static Activity mContext;

    public static GlideUtil getInstance(Activity activity) {
        mContext = activity;
        if (glideUtil == null) {
            glideUtil = new GlideUtil();
        }
        return glideUtil;
    }

    private GlideUtil() {

    }

    public static void load(Context mContext, String url, ImageView imageView) {
        if ((mContext == null)) {
            imageView.setImageResource(R.mipmap.icon_loading);
            return;
        }
        if (mContext instanceof Activity) {
            Activity mActivity = (Activity) mContext;
            if (mActivity.isFinishing()) {
                imageView.setImageResource(R.mipmap.icon_loading);
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_loading)
                .error(R.mipmap.icon_image_load_error)
                .centerCrop()           //等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//只缓存最终的图片
                .dontAnimate()
                ;
        /**
         *  RequestOptions options = new RequestOptions()
         *                 .placeholder(R.mipmap.ic_launcher)                //加载成功之前占位图
         *                 .error(R.mipmap.ic_launcher)                    //加载错误之后的错误图
         *                 .override(400,400)                                //指定图片的尺寸
         *                 //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
         *                 .fitCenter()
         *                 //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
         *                 .centerCrop()
         *                 .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
         *                 .skipMemoryCache(true)                            //跳过内存缓存
         *                 .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
         *                 .diskCacheStrategy(DiskCacheStrategy.NONE)        //跳过磁盘缓存
         *                 .diskCacheStrategy(DiskCacheStrategy.DATA)        //只缓存原来分辨率的图片
         *                 .diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
         *                 ;
         */

        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void load(Context mContext, int resource, ImageView imageView) {
        if ((mContext == null)) {
            imageView.setImageResource(R.mipmap.icon_loading);
            return;
        }
        if (mContext instanceof Activity) {
            Activity mActivity = (Activity) mContext;
            if (mActivity.isFinishing()) {
                imageView.setImageResource(R.mipmap.icon_loading);
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_loading)
                .error(R.mipmap.icon_image_load_error)
                .centerCrop()           //等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//只缓存最终的图片
                .dontAnimate()
                ;
        /**
         *  RequestOptions options = new RequestOptions()
         *                 .placeholder(R.mipmap.ic_launcher)                //加载成功之前占位图
         *                 .error(R.mipmap.ic_launcher)                    //加载错误之后的错误图
         *                 .override(400,400)                                //指定图片的尺寸
         *                 //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
         *                 .fitCenter()
         *                 //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
         *                 .centerCrop()
         *                 .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
         *                 .skipMemoryCache(true)                            //跳过内存缓存
         *                 .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
         *                 .diskCacheStrategy(DiskCacheStrategy.NONE)        //跳过磁盘缓存
         *                 .diskCacheStrategy(DiskCacheStrategy.DATA)        //只缓存原来分辨率的图片
         *                 .diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
         *                 ;
         */

        Glide.with(mContext)
                .load(resource)
                .apply(options)
                .into(imageView);
    }
}
