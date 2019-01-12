package cn.skyui.app.library.widget.recyclerview;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * RecyclerView 间距适配：LinearLayoutManager
 * <p>
 * GridSpacingItemDecoration
 * <p>
 * int spanCount = 3; // 3 columns
 * int spacing = 50; // 50px
 * boolean includeEdge = false;
 * recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
 *
 * @author tianshaojie
 * @date 2018/2/8
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private Paint dividerPaint;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        dividerPaint = new Paint();
        dividerPaint.setColor(Color.parseColor("#000000"));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(layoutParams instanceof GridLayoutManager.LayoutParams){
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) layoutParams;
            int position = parent.getChildAdapterPosition(view); // item position
            int column = params.getSpanIndex(); // item column
//            Logger.i("column="+column);

            int type = parent.getAdapter().getItemViewType(position);
            if (type == BaseQuickAdapter.HEADER_VIEW) {
                outRect.set(0, 0, 0, 0);
                return;
            }
            if(column == 0) {
                outRect.left = 0;
                outRect.right =  spacing / spanCount;
            } else if(column == spanCount - 1) {
                outRect.left = spacing / spanCount;
                outRect.right =  0;
            } else {
                outRect.left = spacing / spanCount;
                outRect.right =  spacing / spanCount;
            }

//            outRect.left = column * spacing / spanCount;
//            outRect.right = (column + 1) * spacing / spanCount;;
            if (position >= 0) {
                outRect.top = spacing; // item top
            }
        }
    }

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        int childCount = parent.getChildCount();
//        Logger.i("childCount="+childCount);
//
////        int left = parent.getPaddingLeft();
////        int right = parent.getWidth() - parent.getPaddingRight();
//
//        for (int i = 0; i < childCount - 1; i++) {
//            View view = parent.getChildAt(i);
//            float left = view.getLeft();
//            Logger.i("left="+left + "-paddingLeft" + view.getPaddingLeft());
//            float right = view.getRight();
//            float top = view.getTop();
//            float bottom = view.getBottom() + spacing;
//            c.drawRect(left, top, right, bottom, dividerPaint);
//        }
//    }
}
