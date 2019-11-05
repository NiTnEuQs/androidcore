package fr.dtrx.androidcore.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

@SuppressWarnings("unused")
public class KeyboardUtils {

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                imm.showSoftInput(activity.getCurrentFocus(), 0);
            }
        }
    }

}
