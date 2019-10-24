package cn.skyui.library.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.orhanobut.logger.Logger;

public final class LocationUtils {

    private volatile static LocationUtils mLocationUtils = null;
    private AMapLocationClientOption mLocationOption;

    private LocationUtils() {
        initOption();
    }

    public static LocationUtils getInstance() {
        if (mLocationUtils == null) {
            synchronized (LocationUtils.class) {
                if (mLocationUtils == null) {
                    mLocationUtils = new LocationUtils();
                }
            }
        }
        return mLocationUtils;
    }


    private void initOption(){
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption()
                .setNeedAddress(true)// 设置是否返回地址信息（默认返回地址信息）
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)// 设置定位模式为高精度模式
                .setInterval(0)//设置定位间隔,单位毫秒,默认为2000ms
                .setOnceLocation(true); // 设置为单次定位
        mLocationOption.setOnceLocationLatest(true);//获取最近3s内精度最高的一次定位结果
    }

    public void startLocation(final OnLocationChangedListener listener) {
        AMapLocationClient locationClient = new AMapLocationClient(Utils.getApp());
        locationClient.setLocationListener(amapLocation -> {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    if (listener != null) {
                        listener.onSuccess(amapLocation);
                        Logger.i(amapLocation.toStr());
                    }

                } else {
                    if (listener != null) {
                        listener.onFail(amapLocation.getErrorCode(), amapLocation.getErrorInfo());
                    }
                }
            }
        });

        //设置定位参数
        locationClient.setLocationOption(mLocationOption);
        locationClient.startLocation();
    }


    public interface OnLocationChangedListener {
        /**
         * 成功
         */
        void onSuccess(AMapLocation mapLocation);

        /**
         * 失败
         *
         * @param errCode 错误码
         * @param errInfo 错误信息
         */
        void onFail(int errCode, String errInfo);
    }
}
