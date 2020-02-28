package com.example.hanall.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseVH extends RecyclerView.ViewHolder {

    private View rootView;

    public BaseVH(@NonNull View itemView) {
        super(itemView);
        this.rootView = itemView;
    }

    /**
     * @return 返回viewHolder的根布局
     */
    public View getRootView() {
        return rootView;
    }

    /**
     *置TextView的文字内容
     * @param resId
     * @param value
     */
    public void setTextview(int resId,String value){
        if(rootView != null){
            TextView textView = rootView.findViewById(resId);
            if(textView != null){
                textView.setText(value);
            }
        }
    }

public ImageView getImageView(int imageId){
        if(rootView != null){
            ImageView imageView = rootView.findViewById(imageId);
            if(imageView != null){
                return imageView;
            } else {
                return null;
            }
        }
        return null;
}

    /**
     *获取TextView对象
     * @param resId
     * @return
     */
    public TextView getTextView(int resId){
        if(rootView != null){
            TextView textView = rootView.findViewById(resId);
            if(textView != null){
                return textView;
            }
        }
        return null;
}

    /**
     * 返回指定类型的view
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int resId){
        if(rootView != null){
            T view = (T)rootView.findViewById(resId);
            if(view != null){
                 return view;
            }
        }
        return null;
}

    /**
     * 返获取指定类型的ViewGrounp
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends ViewGroup>T getViewGroup(int resId){
        if(rootView != null){
            T viewGroup = (T)rootView.findViewById(resId);
            if(viewGroup != null){
                return viewGroup;
            }
        }

        return null;
}


}
