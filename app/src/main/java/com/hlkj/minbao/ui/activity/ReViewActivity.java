package com.hlkj.minbao.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hlkj.minbao.R;
import com.hlkj.minbao.ui.activity.adapter.BaseAdapter;
import com.wxh.common4mvp.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReViewActivity extends AppCompatActivity {

    @BindView(R.id.list1)
    RecyclerView mList1;
    @BindView(R.id.list2)
    RecyclerView mList2;
    @BindView(R.id.list3)
    RecyclerView mList3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_view);
        ButterKnife.bind(this);

        mList1.setLayoutManager(new LinearLayoutManager(this));
        mList2.setLayoutManager(new LinearLayoutManager(this));
        mList3.setLayoutManager(new LinearLayoutManager(this));
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("2");
        }
        BaseAdapter baseAdapter1 = new BaseAdapter(R.layout.item_goodsinfo_zhayao, list);
        BaseAdapter baseAdapter2 = new BaseAdapter(R.layout.item_goodsinfo_leiguan, list);
        BaseAdapter baseAdapter3 = new BaseAdapter(R.layout.item_goodsinfo_suolei, list);

        mList1.addItemDecoration(new DividerItemDecoration());
        mList2.addItemDecoration(new DividerItemDecoration());
        mList3.addItemDecoration(new DividerItemDecoration());
        mList1.setAdapter(baseAdapter1);
        mList2.setAdapter(baseAdapter2);
        mList3.setAdapter(baseAdapter3);
    }

    class DividerItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) != 0){
                outRect.top =  SystemUtils.dp2px(parent.getContext(),1);
            }
        }
    }

}
