package com.hlkj.minbao.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.HomeListInfo;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.util.AppConfig;
import com.wxh.common4mvp.customView.CommonRecycleAdapter;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeListAdapter extends CommonRecycleAdapter<HomeListInfo> {

    private LoginUserInfo mLoginUserInfo;
    private int mListStatus;

    public HomeListAdapter(Activity mContext, Handler mHandler, int listStatus) {
        super(mContext, mHandler);
        this.mListStatus = listStatus;
        mLoginUserInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEWTYPE_FOOT) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_base_foot, parent, false);
            return new BaseFootViewHolder(itemView);
        }
        itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_list, parent, false);
        return new HomeListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemViewType(position) == VIEWTYPE_NORMAL) {
                try {
                final HomeListInfo mData = mDataSetList.get(position);
                HomeListHolder mHolder = (HomeListHolder) holder;

                int order = mData.getOrder();
                //爆破公司: 未知
                //配送公司: 0.出库配送 1.退库配送 2.销售配送 3.变更配送
                //仓储公司: 0.销售入库 1.当班出库 2.退库入库
                int type = mData.getType();
                //爆破公司: 1.作业中 2.退库中
                int status = mData.getStatus();
                String name = mData.getName();
                String address = mData.getAddress();
                long time = mData.getTime();

                String titleStr = String.format(mContext.getResources().getString(R.string.home_list_item_title), order);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                String timeStr = format.format(new Date(time));

                mHolder.tvTitle.setText(StringUtils.isEmpty(titleStr) ? "" : titleStr);
                mHolder.tvContent.setText(StringUtils.isEmpty(name) ? "" : name);
                mHolder.tvLocation.setText(StringUtils.isEmpty(address) ? "" : address);
                mHolder.tvTime.setText(StringUtils.isEmpty(timeStr) ? "" : timeStr);

                if (mLoginUserInfo != null) {
                    int companyType = mLoginUserInfo.getCompanyType();
                    if (companyType == 0 || companyType == 1) {//爆破公司/监理公司
                        mHolder.tvType.setVisibility(View.GONE);
                        mHolder.ivState.setVisibility(View.VISIBLE);
                        GradientDrawable drawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.home_list_point);
                        switch (mListStatus) {
                            case 1:
                                if (status == 1) {//作业中
                                    drawable.setColor(mContext.getResources().getColor(R.color.color_home_list_dot_process2));
                                    mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                    mHolder.ivState.setImageResource(R.mipmap.img_state_process_5);
                                } else if (status == 2) {//退库中
                                    drawable.setColor(mContext.getResources().getColor(R.color.color_home_list_dot_process2));
                                    mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                    mHolder.ivState.setImageResource(R.mipmap.img_state_process_6);
                                }
                                break;
                            case 2:
                                drawable.setColor(mContext.getResources().getColor(R.color.color_home_list_dot_complete));
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                mHolder.ivState.setVisibility(View.GONE);
                                break;
                            case 3:
                                drawable.setColor(mContext.getResources().getColor(R.color.color_home_list_dot_unStart));
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                mHolder.ivState.setVisibility(View.GONE);
                                break;
                            default:
                                drawable.setColor(Color.parseColor("#00000000"));
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                                mHolder.ivState.setVisibility(View.GONE);
                                break;
                        }
                    } else if (companyType == 2) {//配送公司
                        mHolder.tvType.setVisibility(View.VISIBLE);
                        mHolder.ivState.setVisibility(View.GONE);
                        switch (type) {
                            case 0:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.img_type_2, 0, 0, 0);
                                mHolder.tvType.setText(mContext.getText(R.string.home_list_item_type_outgoing));
                                break;
                            case 1:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.img_type_3, 0, 0, 0);
                                mHolder.tvType.setText(mContext.getText(R.string.home_list_item_type_refund));
                                break;
                            case 2:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.img_type_1, 0, 0, 0);
                                mHolder.tvType.setText(mContext.getText(R.string.home_list_item_type_storage));
                                break;
                            case 3:
                                break;
                            default:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                mHolder.tvType.setText("");
                                break;
                        }
                    } else if (companyType == 3) {//仓储公司
                        mHolder.tvType.setVisibility(View.VISIBLE);
                        mHolder.ivState.setVisibility(View.GONE);
                        switch (type) {
                            case 0:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.img_type_1, 0, 0, 0);
                                mHolder.tvType.setText(mContext.getText(R.string.home_list_item_type_storage));
                                break;
                            case 1:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.img_type_2, 0, 0, 0);
                                mHolder.tvType.setText(mContext.getText(R.string.home_list_item_type_outgoing));
                                break;
                            case 2:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.img_type_3, 0, 0, 0);
                                mHolder.tvType.setText(mContext.getText(R.string.home_list_item_type_refund));
                                break;
                            default:
                                mHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                mHolder.tvType.setText("");
                                break;
                        }
                    }
                }

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

    class HomeListHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvType;
        private TextView tvContent;
        private TextView tvLocation;
        private TextView tvTime;
        private ImageView ivState;

        public HomeListHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvType = itemView.findViewById(R.id.tv_type);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivState = itemView.findViewById(R.id.iv_state);
        }
    }
}
