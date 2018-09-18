package com.hlkj.minbao.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.wxh.common4mvp.customView.CommonRecycleAdapter;
import com.wxh.common4mvp.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecordUseListAdapter extends CommonRecycleAdapter<JSONObject> {

    public RecordUseListAdapter(Activity mContext, Handler mHandler) {
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
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_use_register_list, parent, false);
        return new RecordUseListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemViewType(position) == VIEWTYPE_NORMAL) {
            try {
                final JSONObject mData = mDataSetList.get(position);
                RecordUseListHolder mHolder = (RecordUseListHolder) holder;

                String title = mData.getString("title");
                String geoFence = mData.getString("geoFence");
                JSONArray jaZhayao = mData.getJSONArray("zhayao");
                JSONArray jaLeiguan = mData.getJSONArray("leiguan");
                JSONArray jaDaobaowu = mData.getJSONArray("daobaowu");

                mHolder.tvTitle.setText(StringUtils.isEmpty(title) ? "" : title);
                mHolder.layoutGeofence.setVisibility(View.VISIBLE);
                mHolder.tvContentGeofence.setText(StringUtils.isEmpty(geoFence) ? "" : geoFence);

                if (jaZhayao != null && jaZhayao.length() > 0) {
                    mHolder.layoutZhayao.setVisibility(View.VISIBLE);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < jaZhayao.length(); i++) {
                        JSONObject jo = (JSONObject) jaZhayao.get(i);
                        sb.append(jo.getString("content"));
                        if (i < jaZhayao.length() - 1) {
                            sb.append("\n");
                        }
                    }
                    mHolder.tvContentZhayao.setText(StringUtils.isEmpty(sb.toString()) ? "" : sb.toString());
                } else
                    mHolder.layoutZhayao.setVisibility(View.GONE);

                if (jaLeiguan != null && jaLeiguan.length() > 0) {
                    mHolder.layoutLeiguan.setVisibility(View.VISIBLE);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < jaLeiguan.length(); i++) {
                        JSONObject jo = (JSONObject) jaLeiguan.get(i);
                        sb.append(jo.getString("content"));
                        if (i < jaLeiguan.length() - 1) {
                            sb.append("\n");
                        }
                    }
                    mHolder.tvContentLeiguan.setText(StringUtils.isEmpty(sb.toString()) ? "" : sb.toString());
                } else {
                    mHolder.layoutLeiguan.setVisibility(View.GONE);
                }

                if (jaDaobaowu != null && jaDaobaowu.length() > 0) {
                    mHolder.layoutSuolei.setVisibility(View.VISIBLE);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < jaDaobaowu.length(); i++) {
                        JSONObject jo = (JSONObject) jaDaobaowu.get(i);
                        sb.append(jo.getString("content"));
                        if (i < jaDaobaowu.length() - 1) {
                            sb.append("\n");
                        }
                    }
                    mHolder.tvContentSuolei.setText(StringUtils.isEmpty(sb.toString()) ? "" : sb.toString());
                } else
                    mHolder.layoutSuolei.setVisibility(View.GONE);

                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNormalViewClick(v, position, mData);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RecordUseListHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvContentZhayao;
        private LinearLayout layoutZhayao;
        private LinearLayout layoutLeiguan;
        private LinearLayout layoutSuolei;
        private LinearLayout layoutGeofence;
        private TextView tvContentLeiguan;
        private TextView tvContentSuolei;
        private TextView tvContentGeofence;

        public RecordUseListHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContentZhayao = itemView.findViewById(R.id.tv_content_zhayao);
            tvContentLeiguan = itemView.findViewById(R.id.tv_content_leiguan);
            tvContentSuolei = itemView.findViewById(R.id.tv_content_suolei);
            tvContentGeofence = itemView.findViewById(R.id.tv_content_geofence);
            layoutZhayao = itemView.findViewById(R.id.layout_zhayao);
            layoutLeiguan = itemView.findViewById(R.id.layout_leiguan);
            layoutSuolei = itemView.findViewById(R.id.layout_suolei);
            layoutGeofence = itemView.findViewById(R.id.layout_geofence);
        }
    }
}
