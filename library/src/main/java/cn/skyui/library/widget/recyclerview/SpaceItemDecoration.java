package cn.skyui.library.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private static final int DEFAULT_COLUMN = Integer.MAX_VALUE;
    private int space;
    private int column;

    public SpaceItemDecoration(int space) {
        this(space, DEFAULT_COLUMN);
    }

    public SpaceItemDecoration(int column, int space) {
        this.space = space;
        this.column = column;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
        int pos = parent.getChildLayoutPosition(view);
//        if (isFirstRow(pos)) {
//            outRect.top = 0;
//        }
        if (column != DEFAULT_COLUMN) {
            float avg = (column - 1) * space * 1.0f / column;
            outRect.left = (int) (pos % column * (space - avg));
            outRect.right = (int) (avg - (pos % column * (space - avg)));
        }
    }

    private boolean isFirstRow(int pos) {
        return pos < column;
    }

    private boolean isLastRow(int pos, int total) {
        return total - pos <= column;
    }

    private boolean isFirstColumn(int pos) {
        return pos % column == 0;
    }

    private boolean isSecondColumn(int pos) {
        return isFirstColumn(pos - 1);
    }

    private boolean isEndColumn(int pos) {
        return isFirstColumn(pos + 1);
    }

    private boolean isNearEndColumn(int pos) {
        return isEndColumn(pos + 1);
    }
}
