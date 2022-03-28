package com.fanrong.otherlib.smartrefresh

import com.scwang.smartrefresh.layout.SmartRefreshLayout

fun SmartRefreshLayout.extSetLoadMoreEnable(pageNo: Int, pageSize: Int, total: Int) {
    var hasMore = (pageNo * pageSize) < total
    setEnableLoadMore(hasMore)

}