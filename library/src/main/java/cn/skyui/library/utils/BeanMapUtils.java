package cn.skyui.library.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tiansj on 2018/4/7.
 */

public class BeanMapUtils {

    /**
     * Converts a map to a JavaBean.
     *
     * @param type type to convert
     * @param map map to convert
     * @return JavaBean converted
     */
//    public static final Object toBean(Class<?> type, Map<String, ? extends Object> map){
//        Object obj = null;
//        try {
//            BeanInfo beanInfo = Introspector.getBeanInfo(type);
//            obj = type.newInstance();
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//            for (int i = 0; i< propertyDescriptors.length; i++) {
//                PropertyDescriptor descriptor = propertyDescriptors[i];
//                String propertyName = descriptor.getName();
//                if (map.containsKey(propertyName)) {
//                    Object value = map.get(propertyName);
//                    Object[] args = new Object[1];
//                    args[0] = value;
//                    descriptor.getWriteMethod().invoke(obj, args);
//                }
//            }
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();    // failed to call setters
//        } catch (IntrospectionException e) {
//            e.printStackTrace();    // failed to get class fields
//        } catch (InstantiationException e) {
//            e.printStackTrace();    // failed to instant JavaBean
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();    // failed to instant JavaBean
//        }
//        return obj;
//    }

    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     */
    public static Map<String, Object> toMap(Object bean) {
        try {
            return (Map) JSON.parse(JSON.toJSONString(bean));
        } catch (Exception e) {
            e.printStackTrace();    // failed to call setters
        }
        return new HashMap<String, Object>();
    }

}
