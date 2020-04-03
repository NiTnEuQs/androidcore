package fr.dtrx.androidcore.utils;

import android.app.Activity;
import android.content.Context;

public class ContextUtils {

    public static boolean isContextValid(final Context context) {
        if (context == null) {
            return false;
        }

        if (context instanceof Activity) {
            final Activity activity = (Activity) context;

            return !activity.isDestroyed() && !activity.isFinishing();
        }

        return true;
    }

}
