package cn.skyui.library.utils;

import com.meituan.android.walle.WalleChannelReader;

public class ChannelUtils {

    private static final String DEFAULT_CHANNEL = "guanwang";

    public static String getChannel() {
        String channel = WalleChannelReader.getChannel(Utils.getApp());
        return (channel != null && channel.length() > 0) ? channel : DEFAULT_CHANNEL;
    }

}
