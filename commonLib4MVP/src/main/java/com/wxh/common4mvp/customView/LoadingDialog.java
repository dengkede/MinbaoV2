package com.wxh.common4mvp.customView;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.wxh.common4mvp.R;


public class LoadingDialog extends AppCompatDialog {

    private static LoadingDialog customProgressDialog = null;
    private Context context;
    private TextView tvMsg;
    private AVLoadingIndicatorView avLoadingView;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LoadingDialog createDialog(Context context) {
        synchronized (LoadingDialog.class) {
            customProgressDialog = new LoadingDialog(context,
                    R.style.CustomLoadingDialog);
            customProgressDialog
                    .setContentView(R.layout.layout_loadingdialog);
            customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        }
        return customProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    class Task extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dismiss();

        }


    }

    public LoadingDialog setMessage(String strMessage) {
        tvMsg = (TextView) customProgressDialog
                .findViewById(R.id.tv_content);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }

    public LoadingDialog startAnim() {
        avLoadingView = (AVLoadingIndicatorView) customProgressDialog.findViewById(R.id.avLoading);
        if (avLoadingView != null) {
            avLoadingView.show();
        }

        return customProgressDialog;
    }

    public LoadingDialog stopAnim() {
        avLoadingView = (AVLoadingIndicatorView) customProgressDialog.findViewById(R.id.avLoading);
        if (avLoadingView != null) {
            avLoadingView.hide();
        }

        return customProgressDialog;
    }

}
