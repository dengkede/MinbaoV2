package com.hlkj.minbao.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hlkj.minbao.R;

/**
 * app内公共方法统一管理
 */
public class AppUtil {

    //-----app相关tab标签统一处理-----
    public static View getTabView(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_home, null);
        TextView textView = view.findViewById(R.id.tv_item_tab);
        textView.setText(title);
        return view;
    }

    public static void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (isSelect) {
            //选中字体加大加粗
            TextView tabSelect = tab.getCustomView().findViewById(R.id.tv_item_tab);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextSize(16);
        } else {
            TextView tabUnSelect = tab.getCustomView().findViewById(R.id.tv_item_tab);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextSize(14);
        }
    }
    //-----app相关tab标签统一处理-----
}
