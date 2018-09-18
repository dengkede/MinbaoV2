package com.hlkj.minbao.ui.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlkj.minbao.R;
import com.hlkj.minbao.presenter.SuperviseRecordPresenter;
import com.hlkj.minbao.view.ISuperviseRecordView;
import com.wxh.common4mvp.base.baseImpl.BaseActivity;

import butterknife.BindView;

public class SuperviseRecordActivity extends BaseActivity<SuperviseRecordPresenter> implements ISuperviseRecordView {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_lab_count)
    TextView tvLabCount;
    @BindView(R.id.tv_content_count)
    TextView tvContentCount;
    @BindView(R.id.tv_content_person)
    TextView tvContentPerson;
    @BindView(R.id.tv_subcontent_1)
    TextView tvSubcontent1;
    @BindView(R.id.layout_lab3_sub_1)
    RelativeLayout layoutLab3Sub1;
    @BindView(R.id.tv_subcontent_2)
    TextView tvSubcontent2;
    @BindView(R.id.layout_lab3_sub_2)
    RelativeLayout layoutLab3Sub2;
    @BindView(R.id.tv_subcontent_3)
    TextView tvSubcontent3;
    @BindView(R.id.layout_lab3_sub_3)
    RelativeLayout layoutLab3Sub3;
    @BindView(R.id.tv_subcontent_4)
    TextView tvSubcontent4;
    @BindView(R.id.layout_lab3_sub_4)
    RelativeLayout layoutLab3Sub4;
    @BindView(R.id.tv_subcontent_5)
    TextView tvSubcontent5;
    @BindView(R.id.layout_lab3_sub_5)
    RelativeLayout layoutLab3Sub5;
    @BindView(R.id.edit_hint_1)
    EditText editHint1;
    @BindView(R.id.edit_hint_2)
    EditText editHint2;
    @BindView(R.id.btn_signature)
    Button btnSignature;
    @BindView(R.id.tv_construction)
    TextView tvConstruction;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_supervise_record;
    }

    @Override
    protected void initToolBar() {
        setToolbarBtnLeft(R.mipmap.btn_common_back);
        setToolbarCenter(R.string.supervise_record_title);
    }

    @Override
    public SuperviseRecordPresenter initPresenter() {
        return new SuperviseRecordPresenter(this, mActivityName, this);
    }
}
