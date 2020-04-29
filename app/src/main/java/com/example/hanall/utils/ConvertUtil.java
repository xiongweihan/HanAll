package com.example.hanall.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanall.base.App;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 转换工具类
 */
public class ConvertUtil {

    /**
     * @param context  上下文
     * @param dipValue dp或者dip的值
     * @return 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dipToPx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dipToPx(float dipValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 加密手机号码,把手机号码的第三位到第七位隐藏
     *
     * @param view TextView的对象
     */
    public static void setPhoneNumber(TextView view) {
        String str = view.getText().toString();
        StringBuffer buffer = new StringBuffer(str);
        StringBuffer replace = buffer.replace(3, 7, "****");
        view.setText(replace);
    }

    /**
     * @param times   传入的毫秒值
     * @param pattern 转换的标准，例如：hh:mm:ss
     * @return
     */
    public static String ParsingTime(long times, String pattern) {
        String results = "";

        Date dates = new Date(times);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String ss = sdf.format(dates);

        results = ss;
        return results;
    }

    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(d);

        return format;
    }

    /**
     * @param value
     * @return 把dauble类型的数据进行转换，有小数就转换成小数，没有小数就直接转换成整数
     */
    public static String ConverNumber(double value) {
        String results = "";

        if (value > 0) {
            String valueOf = String.valueOf(value);
            String[] s1 = valueOf.split("\\.");

            String s2 = s1[0];
            String s3 = s1[1];

            double aDouble = Double.valueOf("0." + s3);
            if (aDouble > 0) {

                results = value + "";
            } else {
                results = s2;
            }
        }
        return results;
    }

    /**
     * @param list
     * @return 数组转换集合
     */
    public static String[] ListToArray(List<String> list) {

        String[] arr = new String[list.size()];

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                arr[i] = s;
            }
        }
        return arr;
    }

    /**
     * @param array
     * @param <T>   数组的泛型
     * @return 把一个数组转换成集合
     */
    public static <T> List<T> ArrayToList(T[] array) {
        List<T> list = new ArrayList<>();

        if (array != null && array.length > 0) {
            list = Arrays.asList(array);
        }
        return list;
    }

    /**
     * 格式化一个long类型的毫秒值，如果有小时，则格式化为：01:30:59，否则格式化为：30:59
     *
     * @param duration
     * @return
     */
    public static CharSequence formatMillis(long duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.add(Calendar.MILLISECOND, (int) duration);
        boolean hasHour = duration / (60 * 60 * 1000) > 0;
        CharSequence pattern = hasHour ? "kk:mm:ss" : "mm:ss";
        return DateFormat.format(pattern, calendar);
    }

    /**
     * 动态设置shape的颜色
     *
     * @param imageView
     * @param color
     */
    public static void setShapeColor(ImageView imageView, String color) {

        Drawable background = imageView.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(Color.parseColor(color));
        }
    }

    public static void setShapeColor(TextView imageView, int color) {

        Drawable background = imageView.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            if (gradientDrawable != null) {
                gradientDrawable.setColor(App.getInstance().getResources().getColor(color));
            }
        }
    }

    /**
     * @param digit 保留小数的位数
     * @param value 传入的数字类型的字符串，必须是整数
     * @return 根据传入指定类型的字符串，解析成带单位的数据
     */
    public static String converNumberUnit(int digit, String value) {
        // 返回值
        String endValue = "";
        String mUnit = "";

        //+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            Log.e("XJX", "不是一个数字类型的字符串！");
            return "";
        } else {
            boolean contains = value.contains(".");
            if (contains) {
                Log.e("XJX", "不能传入一个小数！");
                return "";
            } else {

                if (TextUtils.isEmpty(value)) {
                    Log.e("XJX", "这是一个空数据！");
                    return "";
                } else {
                    try {
                        Double aDouble = Double.parseDouble(value);
                        int length = value.length();

                        if (length < 4) {
                            // 0 - 999之间
                            endValue = value;
                        } else if (length == 4) {
                            // 1000 - 9999之间
                            double d = aDouble / 1000;
                            String end = String.valueOf(d);

                            mUnit = "k";
                            endValue = end;

                        } else if (length > 4 && length <= 8) {
                            // 10000 - 99999999
                            double d = aDouble / 10000;

                            String end = String.valueOf(d);
                            mUnit = "w";
                            endValue = end;
                        } else if (length > 8) {
                            // 10000000 --- 99999999

                            double d = aDouble / 100000000;
                            String end = String.valueOf(d);
                            mUnit = "e";
                            endValue = end;
                        }
                    } catch (NumberFormatException e) {
                        Log.e("XJX", "数据转换异常，这个数据不是一个number类型的数据！");
                        return "";
                    }

                    String saveDigit = saveDigit(1, endValue);

                    return saveDigit + mUnit;
                }
            }
        }
    }

    /**
     * @param digit 指定小数的位数
     * @param value 需要转换的字符串
     * @return 格式化字符串，如果是：3.0，则为3
     */
    public static String saveDigit(int digit, String value) {
        // 小数部分r
        String substring = "";
        // 最终数据
        String maxValue = "";

        if (TextUtils.isEmpty(value)) {
            return "";
        } else {
            if (value.contains(".")) {
                // 分割字符串
                String[] split = value.split("\\.");
                // 获取整数部分
                String max = split[0];
                // 获取小数部分
                String min = split[1];

                // 转换小数部分为String
                String toString = min.toString();
                int length = toString.length();

                if (length > digit) {
                    // 获取转换过的小数
                    substring = toString.substring(0, digit);
                } else {
                    substring = toString;
                }
                maxValue = max + "." + substring;
            } else {
                maxValue = value;
            }

            BigDecimal bigDecimal = new BigDecimal(maxValue);

            String toPlainString = bigDecimal.stripTrailingZeros().toPlainString();

            return toPlainString;
        }
    }

    // 将bitmap转为byte格式的数组
    public static byte[] bmpToByteArray(final Bitmap bitmap, final boolean needRecycle) {
        //创建字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Bitmap.compress()方法的参数format可设置JPEG或PNG格式；quality可选择压缩质量；fOut是输出流（OutputStream）
        boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        Log.e("压缩：" , compress+"");
        if (needRecycle) {
            bitmap.recycle();
        }
        //将字节数组输出流转为byte数组
        byte[] result = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param pattern 指定的匹配符
     * @param value   需要转换的字符串
     * @return 把指定的字符串通过固定格式转换成long类型的数据
     */
    public static long SimpleDateStringToLong(String pattern, String value) {

        // 1:把日期转换成毫秒，和当前的时间做对比
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//24小时制
        long time = 0;
        try {
            time = simpleDateFormat.parse(value).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param pattern
     * @return 通过固定的格式，获取当前的日期
     */
    public static String SimpleDateToString(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            return "";
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String format1 = format.format(date);
        return format1;
    }

    public static String MapToJson(Map<String, Object> map) {
        String json = "";
        if (map != null || map.size() > 0) {
//            String userId = SPUtils.getInstance().getString(Constants.USER_NAME);
//            map.put("mUserId", userId);
            //Map 转成  JSONObject 字符串
            JSONObject jsonObj = new JSONObject(map);
            if (jsonObj != null) {
                json = jsonObj.toString();
            }
        }
        return json;
    }

    /**
     * @param content
     * @return 改变文字颜色的字符串，使用的时候需要TextView去重新设定
     */
    public static SpannableString ConverTextColor(String content, String[] stringArray, int[] colorArray) {
        int length = 0;
        SpannableString spanString;
        ForegroundColorSpan span;

        if ((stringArray == null) || (stringArray.length <= 0) || (colorArray == null) || (colorArray.length <= 0) || (TextUtils.isEmpty(content)) || (stringArray.length != colorArray.length)) {
            return null;
        } else {

            // 文字的内容
            spanString = new SpannableString(content);

            for (int i = 0; i < stringArray.length; i++) {
                String contentValue = stringArray[i];
                int colorValue = colorArray[i];

                span = new ForegroundColorSpan(App.getInstance().getResources().getColor(colorValue));
//                LogUtil.e("start:" + length + "   end:" + (length + contentValue.length()));
                spanString.setSpan(span, length, (length + contentValue.length()), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                length += contentValue.length();
            }
        }
        return spanString;
    }

    /**
     * @param map
     * @return 把map的value 转换为 list集合
     */
    public static <T> ArrayList<T> MapToList(Map<String, T> map) {
        ArrayList<T> list = new ArrayList<T>();
        if (map != null && map.size() > 0) {
            Set<Map.Entry<String, T>> entries = map.entrySet();
            for (Map.Entry<String, T> bean : entries) {
                T value = bean.getValue();
                list.add(value);
            }
        }
        return list;
    }

//    public static <T> String ArrayListToJson(ArrayList<T> list) {
//        String json = "";
//
//        Gson gson = new Gson();
//        json = gson.toJson(list);
//
//        return json;
//    }

    /**
     * @param value      需要的文本内容
     * @param oldReplace 老的内容
     * @param newReplace 替换新的内容
     * @return 把一个字符串从老的内容替换成新的内容
     */
    public static String replaceText(String value, String oldReplace, String newReplace) {
        String result = "";
        if ((!TextUtils.isEmpty(value)) && (!TextUtils.isEmpty(oldReplace)) && (!TextUtils.isEmpty(newReplace))) {
            result = value.replace(oldReplace, newReplace);
        }
        return result;
    }

    /**
     * @param value
     * @param replace
     * @return 把一个字符串按照指定的规则分割成一个字符数组
     */
    public static String[] splitText(String value, String replace) {
        String[] arr = null;
        if ((!TextUtils.isEmpty(value)) && (!TextUtils.isEmpty(replace))) {
            arr = value.split(replace);
        }
        return arr;
    }

    /**
     * @param value
     * @return 把 double 数据类型的数据进行格式化，默认保留两位小数，例如：3.0 --->3, 3.120 ---> 3.12
     */
    public static String FormatDouble(double value) {
        String result = "";

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        result = nf.format(value);

        return result;
    }

    /**
     * @param value
     * @return 把 float 数据类型的数据进行格式化，默认保留两位小数，例如：3.0 --->3, 3.120 ---> 3.12
     */
    public static String FormatFloat(float value) {
        String result = "";

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        result = nf.format(value);

        return result;
    }

    /**
     * @param set set 集合
     * @return 把一个set集合转换为list集合
     */
    public static <T> ArrayList<T> SetToList(HashSet<T> set) {
        if (set == null) {
            return null;
        }

        ArrayList<T> result = new ArrayList<T>(set);

        return result;
    }

    /**
     * @param dp 具体的dp值
     * @return 使用标准的dp值
     */
    public static float toDp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * @param px px的值
     * @return 返回一个标准的px的值
     */
    public static float toPx(float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().getDisplayMetrics());
    }
}
