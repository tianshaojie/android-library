package cn.skyui.library.widgets.progress;

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
        TextView textView = findViewById(R.id.textView);
        if(loadingMessage != null && loadingMessage.length() > 0) {
            textView.setText(loadingMessage);
        }
        if(getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(params);
        }
    }

    public void setLoadingMessage(String msg) {
        this.loadingMessage = msg;
    }
}