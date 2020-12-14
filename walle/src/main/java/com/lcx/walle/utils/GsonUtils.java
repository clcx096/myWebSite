package com.lcx.walle.utils;


import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GSON工具类
 */
public class GsonUtils {

    private static final Gson gsonBuilder = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    private static final Gson GSON = new Gson();

    /**
     * 【GSON】String转json对象
     */
    public static JsonObject toGsonObject(String str){
        return gsonBuilder.fromJson(str, JsonObject.class);
    }

    /**
     * 【GSON】String转json数组
     */
    public static JsonArray toGsonArray(String str){
        return gsonBuilder.fromJson(str, JsonArray.class);
    }

    /**
     * 【GSON】String转gson对象，包含对象和数组
     */
    public static JsonElement toGsonElement(String str){
        return gsonBuilder.fromJson(str, JsonElement.class);
    }

    /**
     * 使用GSON将javaObject转换成字符串，保留null值
     */
    public static String toJsonString(Object obj) {
        return gsonBuilder.toJson(obj);
    }

    /**
     * 将java对象转换成JsonObject对象
     */
    public static JsonObject parseJavaObject(Object obj) {
        return  gsonBuilder.fromJson(gsonBuilder.toJson(obj), JsonObject.class);
    }


    /**
     * 使用GSON将javaObject转换成字符串
     * @param obj 字符串
     * @param abandonNull 是否丢弃NULL值
     */
    public static String toJsonString(Object obj, Boolean abandonNull) {
        return abandonNull ? GSON.toJson(obj) : toJsonString(obj);
    }

    /**
     * 丢弃json对象中的为null的key
     */
    public static JsonObject abandonNull(JsonObject json){
        return toGsonObject(toJsonString(json, true));
    }

    public static JsonObject toGsonObject(Object o, boolean abandonNull){
        return toGsonObject(toJsonString(o, abandonNull));
    }

    public static JsonObject toGsonObject(Map map){
        return toGsonObject(toJsonString(map));
    }

    public static JsonArray toGsonArray(List list){
        return toGsonArray(toJsonString(list));
    }

    public static JsonArray toGsonArray(List list, Boolean abandonNull){
        return toGsonArray(toJsonString(list, abandonNull));
    }

    public static <T> T toJavaObject(JsonObject obj, Class<T> t){
        return toJavaObject(toJsonString(obj), t);
    }

    public static <T> T toJavaObject(String str, Class<T> t){
        return gsonBuilder.fromJson(str, t);
    }

    public static <T> List<T> toJavaList(String str, Class<T> t){
        JsonArray elements = toGsonArray(str);
        return toJavaList(elements, t);
    }

    public static <T> List<T> toJavaList(JsonArray arr, Class<T> t){
        List<T> list = new ArrayList<T>();
        for(int i=0; i<arr.size(); i++){
            T javaObj = toJavaObject(toJsonString(arr.get(i).getAsJsonObject()), t);
            list.add(javaObj);
        }
        return list;
    }

    /**
     * 获取JsonObject的属性
     * @param json json对象
     * @param key key值
     * @return value，不存在该属性时返回空字符串
     */
    public static String getStringValue(JsonObject json, String key){
        Object value = json.get(key);
        if (value==null || value instanceof JsonNull){
            return "";
        }
        return json.get(key).getAsString();
    }

}

