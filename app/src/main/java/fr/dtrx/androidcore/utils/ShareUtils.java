package fr.dtrx.androidcore.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import java.io.File;

import androidx.core.content.FileProvider;

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

    public static void shareFile(Context context, File file) {
        if (file.exists()) {
            Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".utils.GenericFileProvider", file);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.setType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
            context.startActivity(Intent.createChooser(shareIntent, "Partager un fichier"));
        }
    }

    public static void shareViews(Context context, View... views) {
        Bitmap b = ImageUtils.getBitmapFromViews(context, views);

        if (b != null) {
            File file = ImageUtils.savePicture(context, b);
            if (file != null) {
                DialogUtils.getInstance().dismiss();
                shareFile(context, file);
            }
        }
    }

}
