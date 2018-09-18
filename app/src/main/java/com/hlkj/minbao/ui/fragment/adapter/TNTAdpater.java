package com.hlkj.minbao.ui.fragment.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.hlkj.minbao.R;

import java.util.List;

/**
 * Created by leiel on 2018/7/20 0020.
 */

public class TNTAdpater extends BaseQuickAdapter<String, BaseViewHolder> {

    public TNTAdpater(@Nullable List<String> data) {
        super(R.layout.item_tnt_video, data);
        setMultiTypeDelegate(new MultiTypeDelegate<String>() {
            @Override
            protected int getItemType(String s) {
                return s.equals("1") ? 1 : 2;
            }
        });


        getMultiTypeDelegate().registerItemType(1, R.layout.item_tnt_video);
        getMultiTypeDelegate().registerItemType(2, R.layout.item_tnt_video_add);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        switch (helper.getItemViewType()) {
            case 1:
                // do something
                break;
            case 2:
                // do something
                break;
        }
    }

}
