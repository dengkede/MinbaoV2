package com.hlkj.minbao.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.wxh.common4mvp.customView.CommonRecycleAdapter;
import com.wxh.common4mvp.util.StringUtils;

import org.json.JSONObject;

public class ReviewLeiGuanListAdapter extends CommonRecycleAdapter<JSONObject> {

    public ReviewLeiGuanListAdapter(Activity mContext, Handler mHandler) {
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
        itemView = LayoutInflater.from(mContext).inflate(R.layout.subitem_goodsinfo_leiguan, parent, false);
        return new ReviewLeiGuanListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemViewType(position) == VIEWTYPE_NORMAL) {
            try {
                JSONObject mData = mDataSetList.get(position);
                ReviewLeiGuanListHolder mHolder = (ReviewLeiGuanListHolder) holder;

                String name = mData.getString("name");
                String type = mData.getString("type");
                int count = mData.getInt("count");

                mHolder.tvName.setText(StringUtils.isEmpty(name) ? "" : name);
                mHolder.tvName.setCompoundDrawables(null, null, null, null);
                mHolder.tvType.setText(StringUtils.isEmpty(type) ? "" : type);
                mHolder.tvType.setCompoundDrawables(null, null, null, null);
                mHolder.editCount.setEnabled(false);
                mHolder.editCount.setText("");
                mHolder.editCount.setHint(String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ReviewLeiGuanListHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvType;
        private EditText editCount;

        public ReviewLeiGuanListHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            editCount = itemView.findViewById(R.id.edit_count);
        }
    }
}
