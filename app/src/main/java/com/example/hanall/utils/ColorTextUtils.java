package com.example.hanall.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class ColorTextUtils {

    /**
     *  将String 文字，指定部分设为不同颜色
     *
     * @param text  文字
     * @param color  指定的颜色
     * @param startIndex  开始下标
     * @param endIndex    结束下标
     * @return
     */
    public static SpannableString getColorText(String text, int color, int startIndex, int endIndex) {

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), startIndex, endIndex > 0 ? endIndex : text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        //其中，str 为你要改变的文本。
        // setSpan方法有四个参数，ForegroundColorSpan是为文本设置前景色，也就是文字颜色。如果要为文字添加背景颜色，可替换为BackgroundColorSpan。
        // 参数2：为文本颜色改变的起始位置，
        // 参数3：为文本颜色改变的结束位置。
        // 最后一个参数为布尔型，可以传入以下四种。
        //Spannable.SPAN_EXCLUSIVE_EXCLUSIVE：前后都不包括，即在指定范围的前面和后面插入新字符都不会应用新样式
        //Spannable.SPAN_EXCLUSIVE_INCLUSIVE：前面不包括，后面包括。即仅在范围字符的后面插入新字符时会应用新样式
        //Spannable.SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括。
        //Spannable.SPAN_INCLUSIVE_INCLUSIVE：前后都包括。 

        return spannableString;
    }
}
