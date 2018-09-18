package com.wxh.common4mvp.customView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义RecycleView布局管理器
 * 1.设置recycleView是否可以滑动
 * 2.设置recycleView高度最大显示item数量
 */
public class RecycleLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;
    private int maxItemCount = -1;

    public RecycleLinearLayoutManager(Context context) {
        super(context);
    }

    public RecycleLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RecycleLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int currItemCount = state.getItemCount();
        if (currItemCount > 0 && maxItemCount > 0) {
            if (currItemCount > maxItemCount)
                currItemCount = maxItemCount;

            int realHeight = 0;
            int realWidth = 0;
            for (int i = 0; i < currItemCount; i++) {
                View view = recycler.getViewForPosition(0);
                if (view != null) {
                    measureChild(view, widthSpec, heightSpec);
                    int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                    int measuredHeight = view.getMeasuredHeight();
                    realWidth = realWidth > measuredWidth ? realWidth : measuredWidth;
                    realHeight += measuredHeight;
                }
            }
            setMeasuredDimension(realWidth, realHeight);
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }

    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    public int getMaxItemCount() {
        return maxItemCount;
    }

    public void setMaxItemCount(int maxItemCount) {
        this.maxItemCount = maxItemCount;
    }
}
