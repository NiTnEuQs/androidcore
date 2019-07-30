package fr.dtrx.androidcore.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

@SuppressWarnings("unused")
public class DialogUtils {

    private final static String TEXT_LOADING = "Chargement";

    private static DialogUtils instance = null;

    private Dialog dialog;
    private boolean isShowing;

    public static DialogUtils getInstance() {
        if (instance == null) {
            instance = new DialogUtils();
        }
        return instance;
    }

    public void showProgressDialog(Context context) {
        showProgressDialog(context, TEXT_LOADING);
    }

    public void showProgressDialog(Context context, String text) {
        showProgressDialog(context, text, false);
    }

    public void showProgressDialog(Context context, String text, boolean cancelable) {
        if (!isShowing) {
            dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(cancelable);
            ((ProgressDialog) dialog).setMessage(text);
            dialog.show();
            isShowing = true;
        }
    }

    public void dismiss() {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isShowing = false;
            dialog = null;
        }
    }

    public static void choice(Context context, String message, String positiveButtonText, String negativeButtonText, final OnChoiceDialogListener callback) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(positiveButtonText, (dialogInterface, i) -> callback.onPositiveResponse());
        alertBuilder.setNegativeButton(negativeButtonText, (dialogInterface, i) -> callback.onNegativeResponse());

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void information(Context context, String message, String buttonText, final OnInformationDialogListener callback) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(buttonText, (dialogInterface, i) -> callback.onResponse());

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public interface OnChoiceDialogListener {
        void onPositiveResponse();

        void onNegativeResponse();
    }

    public interface OnInformationDialogListener {
        void onResponse();
    }

}
