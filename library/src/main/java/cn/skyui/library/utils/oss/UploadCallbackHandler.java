package cn.skyui.library.utils.oss;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.sdk.android.oss.ServiceException;
import com.orhanobut.logger.Logger;

public class UploadCallbackHandler {

    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int PROGRESS_MESSAGE = 1;
    protected static final int FAILURE_MESSAGE = 2;

    private Handler handler;

    public UploadCallbackHandler() {
        if (Looper.myLooper() != null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    UploadCallbackHandler.this.handleMessage(msg);
                }
            };
        }
    }

    public void onSuccess(String content) {

    }
    public void onProgress(String objectKey, long byteCount, long totalSize) {

    }

    public void onFailure(String objectKey, ServiceException e) {
        if(e != null) {
            e.printStackTrace();
        }
    }

    protected void sendSuccessMessage(String objectKey) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{objectKey}));
    }

    protected void sendProgressMessage(String objectKey, int byteCount, int totalSize) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]{objectKey, new Integer(byteCount), new Integer(totalSize)}));
    }

    protected void sendFailureMessage(String objectKey, ServiceException ossException) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{objectKey, ossException}));
    }

    protected void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                handleSuccessMessage((String) response[0]);
                break;
            case PROGRESS_MESSAGE:
                response = (Object[]) msg.obj;
                handleProgressMessage(((String) response[0]), (int) response[1], (int) response[2]);
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                handleFailureMessage((String) response[0], (ServiceException) response[1]);
                break;
        }
    }

    protected void handleSuccessMessage(String responseBody) {
        onSuccess(responseBody);
    }

    protected void handleProgressMessage(String objectKey, long byteCount, long totalSize) {
        onProgress(objectKey, byteCount, totalSize);
    }

    protected void handleFailureMessage(String objectKey, ServiceException ossException) {
        if(ossException != null) {
            Logger.i(ossException.getMessage());
        }
        onFailure(objectKey, ossException);
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (handler != null) {
            msg = this.handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }
}
