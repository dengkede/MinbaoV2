package com.hlkj.minbao.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hlkj.minbao.R;
import com.hlkj.minbao.ui.fragment.adapter.TNTAdpater;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpVideoFragment extends Fragment {


    @BindView(R.id.list)
    RecyclerView mList;
    Unbinder unbinder;

    public UpVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        TNTAdpater tntAdpater = new TNTAdpater(list);

        mList.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        mList.setAdapter(tntAdpater);
        tntAdpater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List data = adapter.getData();
                String o = (String) data.get(position);
                if (o.equals("2")) {
                    list.remove("2");
                    list.add("1");
                    list.add("2");
                    adapter.setNewData(list);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
