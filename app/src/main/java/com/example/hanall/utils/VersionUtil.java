package com.example.hanall.utils;

/**
 * 比较版本号
 */
public class VersionUtil {
    /**
     *
     * @param version1  服务端版本号
     * @param version2  本地版本号
     * @return  true--需要更新，false --不需要更新
     *
     */
    public static boolean compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return false;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return true;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return false;
                }
            }
            return false;
        } else {
            return diff > 0 ? true : false;
        }
    }
}
