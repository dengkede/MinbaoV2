package com.wxh.common4mvp.customView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.wxh.common4mvp.R;

/**
 * Created by wxh on 2017/4/6.
 */

public class CustomPopWindow extends PopupWindow {

    private Activity mContext;
    private View mPopView;

    public CustomPopWindow(Activity context, int mPopViewResId) {
        super(context);
        this.mContext = context;
        this.mPopView = LayoutInflater.from(mContext).inflate(mPopViewResId, null);
        setContentView(mPopView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.CustomPopWindowAnim);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        setBackgroundDrawable(dw);
        setOnDismissListener(dismissListener);
        update();
    }

    OnDismissListener dismissListener = new OnDismissListener() {
        @Override
        public void onDismiss() {
            darkenBackground(1f);
        }
    };

    public View getmPopView() {
        return this.mPopView;
    }

    public void showPopWindow(View parent) {
        if (!this.isShowing()) {
            darkenBackground(0.6f);
            this.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            darkenBackground(1f);
            this.dismiss();
        }
    }

    /**
     * 改变背景透明度
     *
     * @param bgcolor
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgcolor;

        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mContext.getWindow().setAttributes(lp);
    }
}
