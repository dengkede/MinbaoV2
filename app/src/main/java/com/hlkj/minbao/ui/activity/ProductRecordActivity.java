package com.hlkj.minbao.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.ProductRecordPresenter;
import com.hlkj.minbao.ui.fragment.RecordReceiveFragment;
import com.hlkj.minbao.ui.fragment.RecordRefundFragment;
import com.hlkj.minbao.ui.fragment.RecordUseFragment;
import com.hlkj.minbao.util.AppUtil;
import com.hlkj.minbao.view.IProductRecordView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class ProductRecordActivity extends BaseActivity<ProductRecordPresenter> implements IProductRecordView {
    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.pager_content)
    ViewPager pagerContent;

    private String[] mProductRecordTabTitles;
    private List<Fragment> vpContentListFragmentList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_record;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.productRecord_title);
    }

    @Override
    public ProductRecordPresenter initPresenter() {
        return new ProductRecordPresenter(this, mActivityName, this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mProductRecordTabTitles = getResources().getStringArray(R.array.list_title_productRecord);
        if (mProductRecordTabTitles != null && mProductRecordTabTitles.length > 0) {
            for (int i = 0; i < mProductRecordTabTitles.length; i++) {
                layoutTab.addTab(layoutTab.newTab().setCustomView(
                        AppUtil.getTabView(this, mProductRecordTabTitles[i])));
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
        vpContentListFragmentList.add(RecordReceiveFragment.newInstance());
        vpContentListFragmentList.add(RecordUseFragment.newInstance());
        vpContentListFragmentList.add(RecordRefundFragment.newInstance());

        pagerContent.setAdapter(new ProductRecordActivity.VpContentAdapter(getSupportFragmentManager()));
        pagerContent.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(layoutTab));
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
