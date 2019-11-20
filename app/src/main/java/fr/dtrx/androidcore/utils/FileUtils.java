package fr.dtrx.androidcore.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.content.FileProvider;

@SuppressWarnings({"unused", "WeakerAccess"})
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

    /**
     * Copy a file
     *
     * @param source Source file
     * @param dest   Destination file
     * @return The destination file, null else
     */
    public static File copyFile(File source, File dest) {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return dest;
    }

    /**
     * Delete a file
     *
     * @param context Context
     * @param file    File to remove
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    /**
     * Get URI from file
     *
     * @param context    Context
     * @param sourceFile The file to read
     * @return An URI from the file, null else
     */
    public static Uri uriFromFile(Context context, File sourceFile) {
        return FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                sourceFile
        );
    }

    /**
     * Fonction pour récupérer du JSON à partir d'une URL
     *
     * @param url URL renvoyant du JSON
     * @return Un JSONObject représentant les données récupérées
     */
    public static JSONObject readJsonFromUrl(String url) {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Get object from a raw json file
     *
     * @param context       Context
     * @param rawResourceId Resource id of the raw file
     * @return An object that represents the Json file content
     */
    public static <T> T jsonFromRaw(Context context, int rawResourceId, Class<T> clazz) {
        return new Gson().fromJson(stringFromInputStream(context.getResources().openRawResource(rawResourceId)), clazz);
    }

    /**
     * Read text from input stream
     *
     * @param inputStream The input stream to read
     * @return A string with the input stream content, null else
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String stringFromInputStream(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);

            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Read text from file
     *
     * @param file The file to read
     * @return A string with the file content, null else
     */
    public static String stringFromFile(File file) {
        try {
            StringBuilder text = new StringBuilder();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();

            return text.toString();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Save the image to an external directory
     *
     * @param context App context
     * @param image   Bitmap image
     * @return The path of the image
     */
    public static String saveImage(Context context, Bitmap image) {
        String appName = context.getPackageName();
        String filename = "IMG_" + new Date().getTime() + ".png";

        String savedImagePath = null;

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + appName);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, filename);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            addImageToGallery(context, savedImagePath);
            Toast.makeText(context, "Image ajouté à la galerie", Toast.LENGTH_SHORT).show();
        }
        return savedImagePath;
    }

    /**
     * Add the image to the gallery
     *
     * @param context   App context
     * @param imagePath Image path
     */
    public static void addImageToGallery(Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

}
