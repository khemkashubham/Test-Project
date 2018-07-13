package com.testapp.myapplication.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public VerticalSpaceDecoration(int spacing) {
        this.spacing = spacing;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = spacing;

    }
}
