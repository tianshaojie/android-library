package cn.skyui.library.utils;

import com.googlecode.openbeans.BeanInfo;
import com.googlecode.openbeans.IntrospectionException;
import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    public static final Object toBean(Class<?> type, Map<String, ? extends Object> map){
        Object obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            obj = type.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();    // failed to call setters
        } catch (IntrospectionException e) {
            e.printStackTrace();    // failed to get class fields
        } catch (InstantiationException e) {
            e.printStackTrace();    // failed to instant JavaBean
        } catch (IllegalAccessException e) {
            e.printStackTrace();    // failed to instant JavaBean
        }
        return obj;
    }

    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     */
    public static final Map<String, Object> toMap(Object bean) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if(result != null) {
                        returnMap.put(propertyName, result);
                    }
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();    // failed to call setters
        } catch (IntrospectionException e) {
            e.printStackTrace();    // failed to get class fields
        } catch (IllegalAccessException e) {
            e.printStackTrace();    // failed to instant JavaBean
        }

        return returnMap;
    }

}
