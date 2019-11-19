package fr.dtrx.androidcore.utils;

@SuppressWarnings("unused")
public class Log {

    private final static String PARENT_TAG = "App";

    // INFO

    public static void i(String tag, String message) {
        android.util.Log.i(PARENT_TAG + "." + tag, message);
    }

    public static void i(Object obj, String message) {
        Log.i(obj.getClass().getSimpleName(), message);
    }

    // DEBUG

    public static void d(Object obj, String message) {
        Log.d(obj.getClass().getSimpleName(), message);
    }

    public static void d(String tag, String message) {
        android.util.Log.d(PARENT_TAG + "." + tag, message);
    }

    // ERROR

    public static void e(Object obj, String message) {
        Log.e(obj.getClass().getSimpleName(), message);
    }

    public static void e(String tag, String message) {
        android.util.Log.e(PARENT_TAG + "." + tag, message);
    }

    // WARNING

    public static void w(Object obj, String message) {
        Log.w(obj.getClass().getSimpleName(), message);
    }

    public static void w(String tag, String message) {
        android.util.Log.w(PARENT_TAG + "." + tag, message);
    }

    // ASSERT

    public static void wtf(Object obj, String message) {
        Log.wtf(obj.getClass().getSimpleName(), message);
    }

    public static void wtf(String tag, String message) {
        android.util.Log.wtf(PARENT_TAG + "." + tag, message);
    }

}
