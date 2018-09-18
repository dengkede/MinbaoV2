package com.hlkj.minbao.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.wxh.common4mvp.customView.CommonRecycleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class TransportListAdapter extends CommonRecycleAdapter<JSONObject> {

    public static final int TYPE_LIST_STORAGE = 0;
    public static final int TYPE_LIST_OUTGOING = 1;

    private int mType;

    public TransportListAdapter(Activity mContext, Handler mHandler, int type) {
        super(mContext, mHandler);
        this.mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEWTYPE_FOOT) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_base_foot, parent, false);
            return new BaseFootViewHolder(itemView);
        }

        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_transport_list, parent, false);
        return new TransportListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemViewType(position) == VIEWTYPE_NORMAL) {
            try {
                final JSONObject mData = mDataSetList.get(position);
                TransportListHolder mHolder = (TransportListHolder) holder;

                String type = mData.getString("type");
                String license = mData.getString("license");
                int count = mData.getInt("count");

                String title = "";
                if (mType == TYPE_LIST_STORAGE) {
                    title = String.format(mContext.getResources().getString(R.string.goods_storage_list_item_title), license, count);
                    mHolder.tvEmpty.setText(R.string.goods_storage_list_item_empty);
                } else if (mType == TYPE_LIST_OUTGOING) {
                    title = String.format(mContext.getResources().getString(R.string.goods_outgoing_list_item_title), license, count);
                    mHolder.tvEmpty.setText(R.string.goods_outgoing_list_item_empty);
                }
                mHolder.tvTitle.setText(title);

                if (type.equals("1")) {
                    mHolder.layoutContent.setVisibility(View.VISIBLE);
                    mHolder.layoutEmpty.setVisibility(View.GONE);
                } else if (type.equals("0")) {
                    mHolder.layoutContent.setVisibility(View.GONE);
                    mHolder.layoutEmpty.setVisibility(View.VISIBLE);
                }

                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNormalViewClick(v, position, mData);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class TransportListHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvEmpty;
        private LinearLayout layoutZhayao;
        private LinearLayout layoutLeiguan;
        private LinearLayout layoutSuolei;
        private LinearLayout layoutPerson;
        private LinearLayout layoutTime;
        private TextView tvContentZhayao;
        private TextView tvContentLeiguan;
        private TextView tvContentSuolei;
        private TextView tvContentPerson;
        private TextView tvContentTime;

        private RelativeLayout layoutEmpty;
        private LinearLayout layoutContent;

        public TransportListHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvEmpty = itemView.findViewById(R.id.tv_empty);
            tvContentZhayao = itemView.findViewById(R.id.tv_content_zhayao);
            tvContentLeiguan = itemView.findViewById(R.id.tv_content_leiguan);
            tvContentSuolei = itemView.findViewById(R.id.tv_content_suolei);
            tvContentPerson = itemView.findViewById(R.id.tv_content_person);
            tvContentTime = itemView.findViewById(R.id.tv_content_time);
            layoutZhayao = itemView.findViewById(R.id.layout_zhayao);
            layoutLeiguan = itemView.findViewById(R.id.layout_leiguan);
            layoutSuolei = itemView.findViewById(R.id.layout_suolei);
            layoutPerson = itemView.findViewById(R.id.layout_person);
            layoutTime = itemView.findViewById(R.id.layout_time);

            layoutEmpty = itemView.findViewById(R.id.layout_empty);
            layoutContent = itemView.findViewById(R.id.layout_content);
        }
    }
}
