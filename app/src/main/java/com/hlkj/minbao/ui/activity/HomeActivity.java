package com.hlkj.minbao.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.hedan.textdrawablelibrary.TextViewDrawable;
import com.hlkj.minbao.R;
import com.hlkj.minbao.entity.LoginUserInfo;
import com.hlkj.minbao.presenter.HomePresenter;
import com.hlkj.minbao.ui.fragment.HomeListFragment;
import com.hlkj.minbao.util.AppConfig;
import com.hlkj.minbao.util.AppUtil;
import com.hlkj.minbao.view.IHomeView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;
import com.wxh.common4mvp.util.SPUtils;
import com.wxh.common4mvp.util.SystemUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity<HomePresenter> implements IHomeView {

    @BindView(R.id.iv_head)
    RoundedImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_duty)
    TextView tvDuty;
    @BindView(R.id.tv_company)
    TextViewDrawable tvCompany;
    @BindView(R.id.tab_home_header)
    TabLayout tabHomeHeader;
    @BindView(R.id.vp_home_content)
    ViewPager vpHomeContent;

    private String[] mLoginUserTypes;
    private String[] mLoginUserCompanyTypes;
    private String[] mHomeTabTitles;
    private List<HomeListFragment> vpHomeContentFragmentList;

    private LoginUserInfo mUserInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter(this, mActivityName, this);
    }

    @Override
    public void onBackPressed() {
        mPresenter.onHomeBackPressed(this);
    }

    @Override
    public void onViewInit() {
        super.onViewInit();
        mLoginUserTypes = getResources().getStringArray(R.array.list_loginUser_type);
        mLoginUserCompanyTypes = getResources().getStringArray(R.array.list_company_type);
        mUserInfo = (LoginUserInfo) SPUtils.getInstance().readObject(AppConfig.CACHE_USERINFO);
            if (mUserInfo != null) {
            String userId = mUserInfo.getUserId();
            int loginUserType = mUserInfo.getUserType();
            int loginUserCompanyType = mUserInfo.getCompanyType();
            String name = mUserInfo.getUserName();
            String company = mUserInfo.getDeptName();
            String duty = mLoginUserTypes[loginUserType];
            String companyTypeStr = mLoginUserCompanyTypes[loginUserCompanyType];

            tvName.setText(name);
            tvDuty.setText(duty);
            tvCompany.setText(company);

            String filePath = SystemUtils.getAppFilePath(this, AppConfig.FOLDER_USERPHOTO);
            String fileName = "user_" + userId + ".jpg";
            File fileHead = new File(filePath + File.separator + fileName);
            if (fileHead.exists() && fileHead.isFile()) {
                Picasso.with(getApplicationContext())
                        .load(fileHead)
                        .tag(mActivityName)
                        .into(ivHead);
            }
        }

        mHomeTabTitles = getResources().getStringArray(R.array.list_home_title_normal);
        if (mHomeTabTitles.length > 0) {
            for (int i = 0; i < mHomeTabTitles.length; i++) {
                tabHomeHeader.addTab(tabHomeHeader.newTab().setCustomView(
                        AppUtil.getTabView(this, mHomeTabTitles[i])));
            }
        }

        AppUtil.updateTabTextView(tabHomeHeader.getTabAt(tabHomeHeader.getSelectedTabPosition()), true);
        tabHomeHeader.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                AppUtil.updateTabTextView(tab, true);
                vpHomeContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                AppUtil.updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpHomeContentFragmentList = new LinkedList<>();
        vpHomeContentFragmentList.add(HomeListFragment.newInstance(1));
        vpHomeContentFragmentList.add(HomeListFragment.newInstance(2));
        vpHomeContentFragmentList.add(HomeListFragment.newInstance(3));

        vpHomeContent.setAdapter(new VpContentAdapter(getSupportFragmentManager()));
        vpHomeContent.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHomeHeader));
        vpHomeContent.setOffscreenPageLimit(2);
    }

    class VpContentAdapter extends FragmentPagerAdapter {

        public VpContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return vpHomeContentFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return vpHomeContentFragmentList.size();
        }
    }
}
