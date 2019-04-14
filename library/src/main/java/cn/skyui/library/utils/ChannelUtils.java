package cn.skyui.library.utils;

import android.content.Context;

import com.meituan.android.walle.WalleChannelReader;

public class ChannelUtils {

    private static final String DEFAULT_CHANNEL = "guanwang";

    public static String getChannel(Context context) {
        String channel = WalleChannelReader.getChannel(context);
        return (channel != null && channel.length() > 0) ? channel : DEFAULT_CHANNEL;
    }

}
