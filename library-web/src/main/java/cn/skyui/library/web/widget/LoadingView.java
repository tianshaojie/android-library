package cn.skyui.library.web.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.skyui.library.web.R;

public class LoadingView extends FrameLayout {
    private View mView;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_loading_view,this);
    }

    public void setErrorBackgroundColor(int reId){
        mView.setBackgroundColor(reId);
    }
}