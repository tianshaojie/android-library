package cn.skyui.library.web.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.skyui.library.utils.NetworkUtils;
import cn.skyui.library.web.R;

public class LoadErrorView extends FrameLayout {
    private ReloadListener mReloadListener;
    private View mView;

    public LoadErrorView(Context context) {
        this(context,null);
    }

    public LoadErrorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_load_error,null);
        addView(mView);
        mView.setOnClickListener(view -> {
            if(!NetworkUtils.isConnected()) {
                Toast.makeText(getContext(), "当前网络不可用，请检查您的网络", Toast.LENGTH_SHORT).show();
            }
            if(mReloadListener!=null){
                mReloadListener.reload();
            }
        });
    }

    public void setReloadListener(ReloadListener listener){
        this.mReloadListener=listener;
    }

    public void setErrorBackgroundColor(int reId){
        mView.setBackgroundColor(reId);
    }

    public interface ReloadListener{
        void reload();
    }
}