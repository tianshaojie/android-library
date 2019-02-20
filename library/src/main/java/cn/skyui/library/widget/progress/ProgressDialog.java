package cn.skyui.library.widget.progress;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import cn.skyui.library.R;

/**
 * Created by tiansj on 2018/2/21.
 */

public class ProgressDialog extends android.support.v7.app.AlertDialog {

    private String loadingMessage;
    private TextView textView;

    public ProgressDialog(Context context) {
        super(context, R.style.TransparentDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_progress_dialog);
        textView = findViewById(R.id.textView);
        if(getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(params);
        }
    }

    public ProgressDialog setLoadingMessage(String msg) {
        this.loadingMessage = msg;
        return this;
    }

    @Override
    public void show() {
        super.show();
        if(loadingMessage != null && loadingMessage.length() > 0) {
            textView.setText(loadingMessage);
        }
    }
}