package com.wxh.common4mvp.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.wxh.common4mvp.R;

public class AlertUtils {

    public static void showMsgAlert(Context context, String title) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle(title);
            dialogBuilder.setNegativeButton(
                    R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            dialog.dismiss();

                        }
                    });
            dialogBuilder.show();
        }
    }

    public static void showMsgAlert(Context context, String title, String message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle(title);
            dialogBuilder.setMessage(message);
            dialogBuilder.setNegativeButton(
                    R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            dialog.dismiss();

                        }
                    });
            dialogBuilder.show();
        }
    }

    public static AlertDialog.Builder showMsgAlert(Context context, String title, String message,
                                                   DialogInterface.OnClickListener negativeListener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle(title);
            dialogBuilder.setMessage(message);
            dialogBuilder.setNegativeButton(R.string.ok, negativeListener);
            dialogBuilder.show();

            return dialogBuilder;
        } else {
            return null;
        }
    }

    public static void showMsgAlert(Context context, int title, int message, int btnTitle) {
        if (!((Activity) context).isFinishing()) {
            new AlertDialog.Builder(context).setTitle(title)
                    .setMessage(message).setPositiveButton(btnTitle, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            }).show();
        }
    }

    public static void showMsgAlert(Context context, int title, int message) {
        if (!((Activity) context).isFinishing()) {
            new AlertDialog.Builder(context).setTitle(title)
                    .setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            }).show();
        }
    }

    public static void showMsgAlert(Context context, int iconId, int title, int message, int btnTitle) {
        if (!((Activity) context).isFinishing()) {
            new AlertDialog.Builder(context).setIcon(iconId).setTitle(title)
                    .setMessage(message).setPositiveButton(btnTitle, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            }).show();
        }
    }

    public static void showDialog(Context context, String error) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(error)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    public static void showDialog(Context context, String error, int title) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(error)
                    .setNeutralButton(title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }


    public static void showDialog(Context context, int messageId,
                                  DialogInterface.OnClickListener positiveListener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(messageId)
                    .setPositiveButton(R.string.ok, positiveListener)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    public static void showAlert(Context context, int messageRes, final DialogInterface.OnClickListener listener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(messageRes)
                    .setNeutralButton(R.string.ok, listener);
            builder.create().show();
        }
    }

    public static void showAlert(Context context, int messageRes) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(messageRes)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    public static void showAlert(Context context, String message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(message)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    public static void showDialog(Context context, int messageId,
                                  DialogInterface.OnClickListener negativeListener,
                                  DialogInterface.OnClickListener positiveListener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(messageId)
                    .setPositiveButton(R.string.ok, positiveListener)
                    .setNegativeButton(R.string.cancel, negativeListener);
            builder.create().show();
        }
    }

    public static void showDialog(Context context, String message,
                                  DialogInterface.OnClickListener negativeListener,
                                  DialogInterface.OnClickListener positiveListener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok, positiveListener)
                    .setNegativeButton(R.string.cancel, negativeListener);
            builder.create().show();
        }
    }
}
