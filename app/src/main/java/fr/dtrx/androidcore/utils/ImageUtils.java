package fr.dtrx.androidcore.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import fr.dtrx.androidcore.R;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ImageUtils {

    private static int[] lastWidthHeight = new int[2];

    public static void takeScreenshot(Activity activity) {
        savePicture(activity, getScreenshot(activity));
    }

    @SuppressWarnings("UnusedReturnValue")
    public static File savePicture(Context context, Bitmap bitmap) {
        String imageName = "IMG_" + DateUtils.formatDate(new Date(), "yyyyMMdd_HHmmss");
        String imageParent = Environment.getExternalStoragePublicDirectory("Pictures") + "/" + context.getString(R.string.app_name);
        String filePath = imageParent + "/" + imageName + ".png";
        File file = new File(filePath);

        //noinspection ResultOfMethodCallIgnored
        new File(imageParent).mkdirs();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            addImageToGallery(context, filePath);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fileToBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);

            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getScreenshot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

//        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
//        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static void addImageToGallery(final Context context, final String filePath) {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static Bitmap getBitmapFromView(Context context, View view) {
        if (view == null) {
            return null;
        }

        int width = view.getWidth();
        int height;

        if (view instanceof ListView) {
            height = ViewUtils.getTotalHeightofListView((ListView) view);
        }
        else {
            if (view instanceof ViewPager) {
                ViewPager viewPager = (ViewPager) view;
                view = viewPager.getChildAt(viewPager.getCurrentItem());
            }

            height = view.getHeight();
        }

        Bitmap bitmap;

        if (width > 0 && height > 0) {
            lastWidthHeight[0] = width;
            lastWidthHeight[1] = height;
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        else {
            bitmap = getScreenViewBitmap(view, lastWidthHeight[0], lastWidthHeight[1]);
        }

        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }
        else {
            canvas.drawColor(ContextCompat.getColor(context, R.color.colorBackground));
        }

        view.draw(canvas);

        return bitmap;
    }

    public static Bitmap getBitmapFromViews(Context context, View... views) {
        List<Bitmap> bmps = new ArrayList<>();

        int offset = 0;
        for (int i = 0; i < views.length; i++) {
            View view = views[i + offset];
            if (view instanceof ListView) {
                ListView listView = (ListView) view;
                for (int j = 0; j < listView.getAdapter().getCount(); j++) {
                    View childView = listView.getChildAt(j);

                    if (childView == null) {
                        childView = listView.getAdapter().getView(j, null, listView);
                    }

                    bmps.add(ImageUtils.getBitmapFromView(context, childView));
                    offset++;
                }
            }
            else {
                bmps.add(ImageUtils.getBitmapFromView(context, view));
            }
        }

        return mergeBitmapsVertically(bmps.toArray(new Bitmap[0]));
    }

    public static Bitmap getScreenViewBitmap(final View v) {
        v.setDrawingCacheEnabled(true);

        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache

        return b;
    }

    public static Bitmap getScreenViewBitmap(final View layout, int width, int height) {
        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);

        layout.setDrawingCacheEnabled(true);

        layout.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY));

        layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());
        canvas.drawBitmap(layout.getDrawingCache(), 0, 0, new Paint());
        return bmp;
    }

    public static Bitmap mergeBitmapsVertically(Bitmap... bmps) {
        if (bmps.length < 2) return null;

        int sumHeight = 0;
        int maxWidth = 0;

        for (Bitmap bmp : bmps) {
            sumHeight += bmp.getHeight();
            maxWidth = Math.max(bmp.getWidth(), maxWidth);
        }

        Bitmap bmMerged = Bitmap.createBitmap(maxWidth, sumHeight, bmps[0].getConfig());
        Canvas canvas = new Canvas(bmMerged);

        for (int i = 0, currentHeight = 0; i < bmps.length; i++) {
            canvas.drawBitmap(bmps[i], 0, currentHeight, null);
            currentHeight += bmps[i].getHeight();
        }

        return bmMerged;
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getExternalFilesDir(null) + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            byte[] bytes = new byte[inputStream.read()];
            IOUtils.copyStream(inputStream, outputStream);
//            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long copyStream(InputStream var0, OutputStream var1) throws IOException {
        int var3 = 1024;
        byte[] var4 = new byte[var3];
        long var5 = 0L;

        int var7;
        try {
            while ((var7 = var0.read(var4, 0, var3)) != -1) {
                var5 += (long) var7;
                var1.write(var4, 0, var7);
            }
        } finally {
        }

        return var5;
    }

}
