package com.fanrong.frwallet.tools;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class recyclerViewItemVisibleUtils {

    public static Integer getVisibleViewsCount(RecyclerView reView) {
        if (reView == null || reView.getVisibility() != View.VISIBLE ||
                !reView.isShown() || !reView.getGlobalVisibleRect(new Rect())) {
            return 0;
        }
        //保险起见，为了不让统计影响正常业务，这里做下try-catch
        try {
            int[] range = new int[2];
            RecyclerView.LayoutManager manager = reView.getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                range = findRangeLinear((LinearLayoutManager) manager);
            } else if (manager instanceof GridLayoutManager) {
                range = findRangeGrid((GridLayoutManager) manager);
            } else if (manager instanceof StaggeredGridLayoutManager) {
                range = findRangeStaggeredGrid((StaggeredGridLayoutManager) manager);
            }
            if (range == null || range.length < 2) {
                return 0;
            }
            return range[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int[] findRangeLinear(LinearLayoutManager manager) {
        int[] range = new int[2];
        range[0] = manager.findFirstVisibleItemPosition();
        range[1] = manager.findLastVisibleItemPosition();
        return range;
    }

    private static int[] findRangeGrid(GridLayoutManager manager) {
        int[] range = new int[2];
        range[0] = manager.findFirstVisibleItemPosition();
        range[1] = manager.findLastVisibleItemPosition();
        return range;

    }

    private static int[] findRangeStaggeredGrid(StaggeredGridLayoutManager manager) {
        int[] startPos = new int[manager.getSpanCount()];
        int[] endPos = new int[manager.getSpanCount()];
        manager.findFirstVisibleItemPositions(startPos);
        manager.findLastVisibleItemPositions(endPos);
        int[] range = findRange(startPos, endPos);
        return range;
    }
    private static int[] findRange(int[] startPos, int[] endPos) {
        int start = startPos[0];
        int end = endPos[0];
        for (int i = 1; i < startPos.length; i++) {
            if (start > startPos[i]) {
                start = startPos[i];
            }
        }
        for (int i = 1; i < endPos.length; i++) {
            if (end < endPos[i]) {
                end = endPos[i];
            }
        }
        int[] res = new int[]{start, end};
        return res;
    }


}
