package com.newsmvpdemo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.newsmvpdemo.danmaku.DanmakuInfoTypeAdapter;
import com.newsmvpdemo.local.table.DanmakuInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ictcxq on 2018/3/4.
 * Gson数据转化处理
 * https://www.cnblogs.com/jianyungsun/p/6647203.html
 */

public final class GsonHelper {

    private static Gson mGson;
    private static JsonParser mJsonParser = new JsonParser();

    static {
        mGson = new GsonBuilder()
                .registerTypeAdapter(DanmakuInfo.class,new DanmakuInfoTypeAdapter())
                .create();
    }

    private GsonHelper() {}

    /**
     * 将json数据转化为实体数据
     * @param jsonData json字符串
     * @param entityClass 类型
     * @return 实体
     */
    public static <T> T convertEntity(String jsonData, Class<T> entityClass) {
        T entity = null;
        try {
            entity = mGson.fromJson(jsonData.toString(), entityClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 将json数组转化为实体列表数据
     * @param jsonData json字符串
     * @param entityClass 类型
     * @return 实体列表集合
     */
    public static <T> List<T> convertEntities(String jsonData, Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        try {
            //JsonParser是Gson解析类，它可以把JSON数据分别通过getAsJsonObject和getAsJsonArray解析成JsonObject和JsonArray
            JsonArray jsonArray = mJsonParser.parse(jsonData).getAsJsonArray();
            //JsonElement它是一个抽象类，代表JSON串中的某一个元素，可以是JsonObject/JsonArray...中的任何一种元素
            for (JsonElement element : jsonArray) {
                entities.add(mGson.fromJson(element, entityClass));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entities;
    }

    /**
     * 将 Object 对象转为 String
     * @param jsonObject json对象
     * @return json字符串
     */
    public static String object2JsonStr(Object jsonObject) {
        return mGson.toJson(jsonObject);
    }
}
