package com.hlkj.minbao.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.GoodsReceivePresenter;
import com.hlkj.minbao.ui.fragment.GoodsLeiGuanFragment;
import com.hlkj.minbao.ui.fragment.GoodsSuoLeiFragment;
import com.hlkj.minbao.ui.fragment.GoodsZhaYaoFragment;
import com.hlkj.minbao.util.AppUtil;
import com.hlkj.minbao.view.IGoodsReceiveView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class GoodsReceiveActivity extends BaseActivity<GoodsReceivePresenter> implements IGoodsReceiveView {

    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.pager_content)
    ViewPager pagerContent;
    @BindView(R.id.btn_review)
    Button btnReview;
    @BindView(R.id.fab_upload_video)
    FloatingActionButton fabUploadVideo;
    @BindView(R.id.fab_upload_form)
    FloatingActionButton fabUploadForm;

    private String[] mGoodsReceiveTabTitles;
    private List<Fragment> vpContentListFragmentList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_receive;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.goods_receive_title);
    }

    @Override
    public GoodsReceivePresenter initPresenter() {
        return new GoodsReceivePresenter(this, mActivityName, this);
    }

    @Override
    protected void onToolBarRightTextClick() {
        super.onToolBarRightTextClick();

    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mGoodsReceiveTabTitles = getResources().getStringArray(R.array.list_title_receiveRegister);
        if (mGoodsReceiveTabTitles != null && mGoodsReceiveTabTitles.length > 0) {
            for (int i = 0; i < mGoodsReceiveTabTitles.length; i++) {
                layoutTab.addTab(layoutTab.newTab().setCustomView(
                        AppUtil.getTabView(this, mGoodsReceiveTabTitles[i])));
            }
        }

        AppUtil.updateTabTextView(layoutTab.getTabAt(layoutTab.getSelectedTabPosition()), true);
        layoutTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                AppUtil.updateTabTextView(tab, true);
                pagerContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                AppUtil.updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpContentListFragmentList = new LinkedList<>();
        vpContentListFragmentList.add(GoodsZhaYaoFragment.newInstance());
        vpContentListFragmentList.add(GoodsLeiGuanFragment.newInstance());
        vpContentListFragmentList.add(GoodsSuoLeiFragment.newInstance());

        pagerContent.setAdapter(new VpContentAdapter(getSupportFragmentManager()));
        pagerContent.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(layoutTab));

        btnReview.setOnClickListener(this);
        fabUploadVideo.setOnClickListener(this);
        fabUploadForm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab_upload_video:
                break;
            case R.id.fab_upload_form:
                break;
            case R.id.btn_review:
                break;
            default:
                break;
        }
    }

    class VpContentAdapter extends FragmentPagerAdapter {

        public VpContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return vpContentListFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return vpContentListFragmentList.size();
        }
    }
}
