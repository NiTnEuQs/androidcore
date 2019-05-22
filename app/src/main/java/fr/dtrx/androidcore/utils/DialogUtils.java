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

    public static void onRequestFailed(Context context, String message, String positiveButtonText, String negativeButtonText, final OnDialogListener callback) {
        AlertDialog.Builder isConnectedAlertBuilder = new AlertDialog.Builder(context);
        isConnectedAlertBuilder.setMessage(message);
        isConnectedAlertBuilder.setPositiveButton(positiveButtonText, (dialogInterface, i) -> callback.onPositiveResponse());
        isConnectedAlertBuilder.setNegativeButton(negativeButtonText, (dialogInterface, i) -> callback.onNegativeResponse());

        AlertDialog isConnectedAlertDialog = isConnectedAlertBuilder.create();
        isConnectedAlertDialog.setCanceledOnTouchOutside(false);
        isConnectedAlertDialog.show();
    }

    public interface OnDialogListener {
        void onPositiveResponse();

        void onNegativeResponse();
    }

}
