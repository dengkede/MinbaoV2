package com.hlkj.minbao.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.wxh.common4mvp.customView.CommonRecycleAdapter;
import com.wxh.common4mvp.util.StringUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GeoFenceRecordListAdapter extends CommonRecycleAdapter<JSONObject> {

    public GeoFenceRecordListAdapter(Activity mContext, Handler mHandler) {
        super(mContext, mHandler);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEWTYPE_FOOT) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_base_foot, parent, false);
            return new BaseFootViewHolder(itemView);
        }
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_geofence_record_list, parent, false);
        return new GeoFenceRecordListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemViewType(position) == VIEWTYPE_NORMAL) {
            try {
                JSONObject mData = mDataSetList.get(position);
                GeoFenceRecordListHolder mHolder = (GeoFenceRecordListHolder) holder;

                long time = mData.getLong("time");
                String content = mData.getString("content");

                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd，HH:mm");
                String timeStr = format.format(new Date(time));

                mHolder.tvTime.setText(StringUtils.isEmpty(timeStr) ? "" : timeStr);
                mHolder.tvContent.setText(StringUtils.isEmpty(content) ? "" : content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GeoFenceRecordListHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;
        private TextView tvContent;

        public GeoFenceRecordListHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
