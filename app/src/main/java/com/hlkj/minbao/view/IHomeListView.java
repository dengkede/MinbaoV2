package com.hlkj.minbao.view;

import com.wxh.common4mvp.base.baseInterface.IBaseView;

public interface IHomeListView extends IBaseView {

    /**
     * 更新分页列表状态
     *
     * @param success
     * @param isLoadMore
     */
    void updateListStatus(boolean success, boolean isLoadMore);
}
