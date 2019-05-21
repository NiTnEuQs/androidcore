package fr.dtrx.androidcore.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;

public class FileUtils {

    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    public static File copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }

        return dest;
    }

    public static void deleteFile(final Context context, final File file) {
        if (file != null && file.exists()) {
            file.delete();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(file);
//                scanIntent.setData(contentUri);
//                context.sendBroadcast(scanIntent);
//            }
//            else {
//                Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
//                context.sendBroadcast(intent);
//            }
//
//            MediaScannerConnection.scanFile(
//                    context,
//                    new String[]{
//                            file.getAbsolutePath()
//                    },
//                    null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                        }
//                    });

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                MediaScannerConnection.scanFile(context,
//                        new String[]{Environment.getExternalStorageDirectory().toString()},
//                        null,
//                        new MediaScannerConnection.OnScanCompletedListener() {
//                            public void onScanCompleted(String path, Uri uri) {
//                                Log.e("ExternalStorage", "Scanned " + path + ":");
//                                Log.e("ExternalStorage", "-> uri=" + uri);
//                            }
//                        }
//                );
//            }
//            else {
//                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//            }
        }
    }

    public static Uri fileToUri(Context context, File sourceFile) {
        return FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                sourceFile
        );
    }

}
