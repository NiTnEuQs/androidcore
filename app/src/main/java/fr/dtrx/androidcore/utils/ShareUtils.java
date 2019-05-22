package fr.dtrx.androidcore.utils;

import android.content.Context;
import android.content.Intent;

@SuppressWarnings("unused")
public class ShareUtils {

    public static void share(Context context, String url) {
        if (context != null && url != null) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Partage");
            i.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(i, "Partage"));
        }
    }

}
