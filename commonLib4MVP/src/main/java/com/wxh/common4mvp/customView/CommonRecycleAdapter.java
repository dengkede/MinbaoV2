package com.wxh.common4mvp.customView;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wxh.common4mvp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxh on 2017/4/18.
 */

public abstract class CommonRecycleAdapter<DATA> extends RecyclerView.Adapter {

    protected final int VIEWTYPE_NORMAL = 0x01;
    protected final int VIEWTYPE_FOOT = 0x10;

    public static final int MSG_LISTITEM_FOOTVIEW_CLICK = 0x00;
    public static final int MSG_LISTITEM_ITEM_CLICK = 0x01;
    public static final int MSG_LOGINDIALOG_CANCEL = 0xff;

    public final String FOOTVIEW_TYPE_LOADING = "footView_type_loading";
    public final String FOOTVIEW_TYPE_ERROR = "footView_type_error";

    protected List<DATA> mDataSetList = new ArrayList<>();
    protected Activity mContext;
    protected Handler mHandler;
    protected boolean hasMore = false;
    protected String footViewType = FOOTVIEW_TYPE_LOADING;


    public CommonRecycleAdapter(Activity mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    private void onFootViewClick(View view, int position) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_LISTITEM_FOOTVIEW_CLICK;
        msg.arg1 = position;
        msg.sendToTarget();
    }

    public void onNormalViewClick(View view, int position, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_LISTITEM_ITEM_CLICK;
        msg.arg1 = position;
        msg.obj = obj;
        msg.sendToTarget();
    }

    public void appendData(List<DATA> dataSet) {
        if (dataSet != null && !dataSet.isEmpty()) {
            int itemCount = dataSet.size();
            this.mDataSetList.addAll(dataSet);
            notifyItemRangeChanged(getItemCount() - 1, itemCount);
        }
    }

    public void updateData(List<DATA> dataSet) {
//        if (dataSet != null && !dataSet.isEmpty()) {
//            int itemCount = dataSet.size();
//            this.mDataSetList.clear();
//            this.mDataSetList.addAll(dataSet);
//            notifyItemRangeChanged(0, getItemCount());
//        }
        if (dataSet != null && !dataSet.isEmpty()) {
            if (getItemCount() > 0) {
                notifyItemRangeRemoved(0, getItemCount());
            }
            this.mDataSetList.clear();
            this.mDataSetList.addAll(dataSet);
            notifyItemRangeInserted(0, getItemCount());
        }
    }

    public void updateData(List<DATA> dataSet, boolean isLoadMore, boolean hasMore) {
        int itemCountOld = getItemCount();
        if (dataSet != null && !dataSet.isEmpty()) {
            if (isLoadMore) {
                setHasMore(hasMore);
                this.mDataSetList.addAll(dataSet);
                notifyItemRangeChanged(itemCountOld - 1, getItemCount() - itemCountOld + 1);
            } else {
                if (itemCountOld > 0) {
                    notifyItemRangeRemoved(0, itemCountOld);
                }
                setHasMore(hasMore);
                this.mDataSetList.clear();
                this.mDataSetList.addAll(dataSet);
                notifyItemRangeInserted(0, getItemCount());
            }
        }
    }

    public void updateFootView(String footViewType) {
        this.footViewType = footViewType;
        notifyItemChanged(getItemCount() - 1);
    }

    public void removeFootView() {
        if (hasMore && mDataSetList.size() > 0) {
            hasMore = false;
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    public List<DATA> getDataSetList() {
        return mDataSetList;
    }

    public void setDataSetList(List<DATA> list) {
        this.mDataSetList = list;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEWTYPE_FOOT) {
            final BaseFootViewHolder mHolder = (BaseFootViewHolder) holder;
            if (footViewType.equals(FOOTVIEW_TYPE_LOADING)) {
                mHolder.layoutLoading.setVisibility(View.VISIBLE);
                mHolder.layoutError.setVisibility(View.GONE);
                mHolder.itemView.setClickable(false);
            }
            if (footViewType.equals(FOOTVIEW_TYPE_ERROR)) {
                mHolder.layoutLoading.setVisibility(View.GONE);
                mHolder.layoutError.setVisibility(View.VISIBLE);
                mHolder.itemView.setClickable(true);
                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onFootViewClick(v, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (hasMore && mDataSetList.size() > 0)
            return mDataSetList.size() + 1;
        else
            return mDataSetList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasMore && mDataSetList.size() > 0) {
            if (position == getItemCount() - 1)
                return VIEWTYPE_FOOT;
        }

        return VIEWTYPE_NORMAL;
    }

    protected class BaseFootViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutLoading;
        LinearLayout layoutError;

        public BaseFootViewHolder(View itemView) {
            super(itemView);
            layoutLoading = (LinearLayout) itemView.findViewById(R.id.layout_foot_loading);
            layoutError = (LinearLayout) itemView.findViewById(R.id.layout_foot_error);
        }
    }
}
