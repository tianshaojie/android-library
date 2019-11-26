package cn.skyui.moudle.market.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VerticalScrollView extends ScrollView {

    private onScrollChangedListener scrollChangedListener;
    private onScrollCompleteListener scrollCompleteListener;

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // Auto-generated method stub
        if (null != scrollChangedListener) {
            scrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setScrollChangedListener(
            onScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    public interface onScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isUp = ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL;
        boolean ret = super.onTouchEvent(ev);

        if(isUp){
            if(null != scrollCompleteListener){
                scrollCompleteListener.scrollComplete();
            }
        }
        return ret;
    }

    public interface onScrollCompleteListener{
        void scrollComplete();
    }

    public void setScrollCompleteListener(onScrollCompleteListener listener){
        this.scrollCompleteListener = listener;
    }
}