package cn.skyui.library;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import cn.skyui.library.data.model.User;
import cn.skyui.library.utils.AppUtils;
import cn.skyui.library.utils.Utils;

/**
 * @author tianshaojie
 */
public class LibraryInitProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        init(getContext());
        return true;
    }

    public static void init(Context context) {
        initUtils(context);
        initLogger();
        initMMKV();
        initUser();
        initRouter();
        initStetho();
        Logger.e("isAppDebug = " + AppUtils.isAppDebug());
//        initCrashReporter(context);
//        initLeakCanary(context);
    }

    private static void initUtils(Context context) {
        Utils.init(context);
    }

    private static void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private static void initMMKV() {
        MMKV.initialize(Utils.getApp());
    }

    private static void initUser() {
        User.getInstance().init();
    }

    private static void initRouter() {
        if (AppUtils.isAppDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(Utils.getApp());
    }

    /**
     * Facebook调试工具，Chrome输入chrome://inspect
     */
    private static void initStetho() {
        Stetho.initializeWithDefaults(Utils.getApp());
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

//    private static void initLeakCanary(Context context) {
//        LeakCanary.install(context);
//    }
//
//    /**
//     * 初始化Bugly & 应用升级
//     * @param context
//     */
//    private static void initCrashReporter(Context context) {
//        /***** Beta高级设置 *****/
//        /**
//         * true表示app启动自动初始化升级模块; false不会自动初始化;
//         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
//         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
//         */
//        Beta.autoInit = false;
//
//        /**
//         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
//         */
//        Beta.autoCheckUpgrade = false;
//
//
//
//        /***** Bugly高级设置 *****/
////        BuglyStrategy strategy = new BuglyStrategy();
//        /**
//         * 设置app渠道号
//         */
////        strategy.setAppChannel(APP_CHANNEL);
//
//        /***** 统一初始化Bugly产品，包含Beta *****/
//        Bugly.init(context, "c57feab2c2", AppUtils.isDebug());
//    }
//
//
//    public static void initUpgrade(Context context, Class<? extends Activity> clazz) {
//        /**
//         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
//         */
//        Beta.upgradeCheckPeriod = 0;
//        /**
//         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
//         */
//        Beta.initDelay = 1 * 1000;
//        /**
//         * 设置通知栏大图标，largeIconId为项目中的图片资源;
//         */
//        Beta.largeIconId = R.mipmap.ic_launcher;
//        /**
//         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
//         */
//        Beta.smallIconId = R.mipmap.ic_launcher;
//        /**
//         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
//         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
//         */
//        Beta.defaultBannerId = R.mipmap.ic_launcher;
//        /**
//         * 设置sd卡的Download为更新资源保存目录;
//         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
//         */
//        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//
//        // 设置是否显示消息通知
//        Beta.enableNotification = true;
//        /**
//         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
//         */
//        Beta.showInterruptedStrategy = true;
//        /**
//         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
//         */
//        Beta.canShowUpgradeActs.add(clazz);
//
//        // 完成初始化
//        Beta.init(context,true);
//
//        // 主动检查一次
//        Beta.checkUpgrade(false, false);
//    }

}
