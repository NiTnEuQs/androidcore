package fr.dtrx.androidcore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

@SuppressWarnings("unused")
public class CacheHandler {

    private final static String SHARED_PREFERENCES_ID = "shared_preferences_data";

    private static CacheHandler mInstance;

    public static CacheHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CacheHandler(context);
        }
        return mInstance;
    }

    private Gson mGson;
    private SharedPreferences mSharedPreferences;

    private CacheHandler(Context context) {
        mGson = new Gson();
        mSharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_ID,
                Context.MODE_PRIVATE
        );
    }

    public <T> void SaveDataToCache(String key, T object) {
        String value = mGson.toJson(object);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public <T> T ReadDataFromCache(String key, Type t) {
        String objectAsString = mSharedPreferences.getString(key, "");
        if (!TextUtils.isEmpty(objectAsString)) {
            return mGson.fromJson(objectAsString, t);
        }
        return null;
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

}

