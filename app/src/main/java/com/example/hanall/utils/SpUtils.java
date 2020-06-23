package com.example.hanall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 使用 在application 初始化   SpUtils.init(getContext());
 * 在其他地方直接调用 get/set 方法即可
 * 如下： 存： SpUtils.putData("han","5555");
 *        取：String han = (String) SpUtils.getData("han", "333");
 */
public class SpUtils {
    private static final String file_name = "cache_sp";
    private static SharedPreferences sp;
    private static SpUtils spUtils;

    private static Context mContext;


    private SpUtils(Context mContext) {
        this.mContext = mContext;
        sp = mContext.getSharedPreferences(file_name, Context.MODE_PRIVATE);
    }

    /**
     * 初始化单例
     *
     * @param mContext
     */
    public static synchronized void init(Context mContext) {
        if (spUtils == null) {
            spUtils = new SpUtils(mContext);
        }
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static SpUtils getInstance() {
        if (spUtils == null) {
            throw new RuntimeException("class should init!");
        }
        return spUtils;
    }

    /**
     * 存值
     *
     * @param key
     * @param value
     * @param <E>
     */
    public static <E> void putData(String key, E value) {
        SharedPreferences.Editor editor = sp.edit();
        String type = value.getClass().getSimpleName();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) value);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) value);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) value);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) value);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, new Gson().toJson(value));
        }
        SPCompat.apply(editor);
    }

    /**
     * 取值
     *
     * @param key
     * @param defaultValue
     * @param <E>
     * @return
     */
    public static <E> Object getData(String key, E defaultValue) {

        String type = defaultValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if ("String".equals(type)) {
            return sp.getString(key, (String) defaultValue);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultValue);
        } else {
            return sp.getString(key, (String) defaultValue);
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SPCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SPCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        return sp.contains(key);
    }

    public static boolean contains(String key) {
        return contains(mContext, key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        return sp.getAll();
    }

    /**
     * 保存对象到sp文件中 被保存的对象须要实现 Serializable 接口
     *
     * @param key
     * @param value
     */
    public static void saveObject(String key, Object value) {
        putData(key, value);
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param key
     * @return modified:
     */
    public static <T> T readObject(String key, Class<T> clazz) {
        try {
            return (T) getData(key, clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SPCompat {
        private static final Method S_APPLY_METHOD = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class<SharedPreferences.Editor> clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        static void apply(SharedPreferences.Editor editor) {
            try {
                if (S_APPLY_METHOD != null) {
                    S_APPLY_METHOD.invoke(editor);
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }


}
