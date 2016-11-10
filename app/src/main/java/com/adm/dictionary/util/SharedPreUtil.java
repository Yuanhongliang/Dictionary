package com.adm.dictionary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SharedPreUtil {

    //存储的sharedpreferences文件名
    private static final String FILE_NAME = "history";
    private static final String FILE_NAME_GROUP = "group";
    private static Gson gson = new Gson();

    /**
     * 保存数据到文件
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveHistory(Context context, String key, Object data) {

        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        } else {
            String object = gson.toJson(data);
            editor.putString(key, object);
        }
        editor.commit();
    }

    /**
     * 从文件中读取历史集合
     *
     * @param context
     * @param key
     * @return
     */
    public static List<String> getList(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);
        String listStr = sharedPreferences.getString(key, null);
        List<String> list = gson.fromJson(listStr, List.class);
        return list;
    }

    /**
     * 清空搜索历史
     *
     * @param context
     */
    public static void clearHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        Toast.makeText(context, "清除成功", Toast.LENGTH_SHORT).show();

    }

    /**
     * 保存分组信息
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveGroup(Context context, String key, List<String> data) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME_GROUP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String group = gson.toJson(data);
        editor.putString(key, group);
        editor.commit();
    }

    /**
     * 获取分组信息
     *
     * @param context
     * @param key
     * @return
     */
    public static List<String> getGroup(Context context, String key) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME_GROUP, Context.MODE_PRIVATE);
        String group = sharedPreferences.getString(key, "");
        List<String> data = gson.fromJson(group, List.class);
        return data;
    }
}

