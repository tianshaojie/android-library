package cn.skyui.module.main.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 首页接收的外界参数封装装
 */
public class MainIntentProtocol implements Parcelable {

    public static final String MAIN_PROTOCOL = "MainIntentProtocol";
    public static final String MAIN_PROTOCOL_BUNDLE = "MainProtocolBundle";
    public static final int DEFAULT_SELECTED_INDEX = 0;

    public static MainIntentProtocol DEFAULT = new MainIntentProtocol();

    /**
     * 主Tab选中项（主界面底部Tab）
     */
    public int primaryTabIndex = DEFAULT_SELECTED_INDEX;

    /**
     * 打开二级页面的Router
     */
    public String openPageRouter;

    /**
     * 附加参数
     */
    public Bundle bundle;

    public MainIntentProtocol() {}

    public MainIntentProtocol(int primaryTabIndex) {
        this.primaryTabIndex = primaryTabIndex;
    }

    public MainIntentProtocol(int primaryTabIndex, String openPageRouter) {
        this.primaryTabIndex = primaryTabIndex;
        this.openPageRouter = openPageRouter;
    }

    public MainIntentProtocol(int primaryTabIndex, String openPageRouter, Bundle bundle) {
        this.primaryTabIndex = primaryTabIndex;
        this.openPageRouter = openPageRouter;
        this.bundle = bundle;
    }

    public MainIntentProtocol(Parcel in) {
        primaryTabIndex = in.readInt();
        openPageRouter = in.readString();
        bundle = in.readBundle();
    }

    public static final Creator<MainIntentProtocol> CREATOR = new Creator<MainIntentProtocol>() {
        @Override
        public MainIntentProtocol createFromParcel(Parcel in) {
            return new MainIntentProtocol(in);
        }

        @Override
        public MainIntentProtocol[] newArray(int size) {
            return new MainIntentProtocol[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryTabIndex);
        dest.writeString(openPageRouter);
        dest.writeBundle(bundle);
    }
}
