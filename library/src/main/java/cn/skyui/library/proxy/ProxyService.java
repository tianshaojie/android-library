package cn.skyui.library.proxy;

import java.util.HashMap;

/**
 * @author tianshaojie
 *
 * 代理服务，用于组件间解耦
 *
 * 1. 提供方组件
 *      1）在cn.skyui.library.proxy.iface下添加接口类IModuleNameProxy.class
 *      2）自己组件内对接口类进行实现
 *      3）在组件自己初始化时，调用ProxyService.addService()添加实例
 * 2. 使用方
 *      1）通过 ProxyService.getService(IModuleNameProxy.class)调用暴露的组件接口
 *
 */
public class ProxyService {

    private static HashMap<String, Object> services = new HashMap<>();

    public static void addService(String iServiceName, Object serverImpl) {
        services.put(iServiceName, serverImpl);
    }

    public static <T> T getService(Class<T> clazz) {
        return (T) services.get(clazz.getSimpleName());
    }

}
