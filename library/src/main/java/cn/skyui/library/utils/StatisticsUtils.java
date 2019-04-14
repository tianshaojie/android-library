package cn.skyui.library.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * umeng 统计
 * 自定义事件的代码需要写在onResume–onPause之间
 */
public class StatisticsUtils {

    public static final String event_login = "login";
    public static final String event_recharge = "recharge";
    public static final String event_video_chat = "chat";
    public static final String event_video = "video";
    public static final String event_push = "push";

    public static void addEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void addEvent(Context context, String eventId, Map<String, String> map) {
        MobclickAgent.onEvent(context, eventId, map);
    }
}
