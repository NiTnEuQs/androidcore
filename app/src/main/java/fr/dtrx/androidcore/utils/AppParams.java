package fr.dtrx.androidcore.utils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AppParams {

    public static Map<String, Object> params = new HashMap<>();

    public static void put(String key, Object value) {
        params.put(key, value);
    }

    public static Object get(String key) {
        return get(key, null);
    }

    public static Object get(String key, Object defaultValue) {
        Object object = params.get(key);

        return object == null ? defaultValue : object;
    }

}
