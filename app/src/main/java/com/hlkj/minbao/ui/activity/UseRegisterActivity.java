package com.hlkj.minbao.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.UseRegisterPresenter;
import com.hlkj.minbao.ui.fragment.CheckAfterFragment;
import com.hlkj.minbao.ui.fragment.CheckBeforeFragment;
import com.hlkj.minbao.ui.fragment.GeoFenceFragment;
import com.hlkj.minbao.ui.fragment.GoodsInfoFragment;
import com.hlkj.minbao.util.AppUtil;
import com.hlkj.minbao.view.IUseRegisterView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class UseRegisterActivity extends BaseActivity<UseRegisterPresenter> implements IUseRegisterView {

    @BindView(R.id.layout_tab)
    TabLayout layoutTab;
    @BindView(R.id.pager_content)
    ViewPager pagerContent;

    private String[] mUseRegisterTabTitles;
    private List<Fragment> vpContentListFragmentList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_use_register;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.useRegister_title);
    }

    @Override
    public UseRegisterPresenter initPresenter() {
        return new UseRegisterPresenter(this, mActivityName, this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mUseRegisterTabTitles = getResources().getStringArray(R.array.list_title_useRegister);
        if (mUseRegisterTabTitles != null && mUseRegisterTabTitles.length > 0) {
            for (int i = 0; i < mUseRegisterTabTitles.length; i++) {
                layoutTab.addTab(layoutTab.newTab().setCustomView(
                        AppUtil.getTabView(this, mUseRegisterTabTitles[i])));
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
        vpContentListFragmentList.add(GoodsInfoFragment.newInstance());
        vpContentListFragmentList.add(CheckBeforeFragment.newInstance());
        vpContentListFragmentList.add(GeoFenceFragment.newInstance());
        vpContentListFragmentList.add(CheckAfterFragment.newInstance());

        pagerContent.setAdapter(new UseRegisterActivity.VpContentAdapter(getSupportFragmentManager()));
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
