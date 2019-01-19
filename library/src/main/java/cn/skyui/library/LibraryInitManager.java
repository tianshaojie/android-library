package cn.skyui.library;

import android.app.Application;

import com.chenenyu.router.Configuration;
import com.chenenyu.router.Router;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import cn.skyui.library.data.model.User;
import cn.skyui.library.utils.Utils;

/**
 * Created by tiansj on 2018/4/4.
 */

public class LibraryInitManager {

    public static void init(Application application, boolean isDebug, String... modules) {
        initUtils(application, isDebug);
        initLogger();
        initMMKV(application);
        initUser();
        initRouter(isDebug, modules);
//        initCrashReporter(application);
//        initLeakCanary(application);
        initStetho(application);
    }

    private static void initUtils(Application application, boolean isDebug) {
        Utils.init(application, isDebug);
    }

    private static void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private static void initUser() {
        User.getInstance().init();
    }

    private static void initRouter(boolean debuggable, String... modules) {
        Router.initialize(new Configuration.Builder()
                // 调试模式，开启后会打印log
                .setDebuggable(debuggable)
                // 模块名，每个使用Router的module都要在这里注册，getResources().getStringArray(R.array.modules)
                .registerModules(modules)
                .build());
    }

//    private static void initLeakCanary(Application application) {
//        LeakCanary.install(application);
//    }
//
//    /**
//     * 初始化Bugly & 应用升级
//     * @param application
//     */
//    private static void initCrashReporter(Application application) {
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
//        Bugly.init(application, "c57feab2c2", AppUtils.isDebug());
//    }
//
    /**
     * Facebook调试工具，Chrome输入chrome://inspect
     */
    private static void initStetho(Application application) {
        Stetho.initializeWithDefaults(application);
    }
//
//    public static void initUpgrade(Application application, Class<? extends Activity> clazz) {
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
//        Beta.init(application,true);
//
//        // 主动检查一次
//        Beta.checkUpgrade(false, false);
//    }

    private static void initMMKV(Application application) {
        MMKV.initialize(application);
    }
}
