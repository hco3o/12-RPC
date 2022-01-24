package xyz.hco3o.rpc.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

// 反射工具类
public class ReflectionUtils {
    /**
     * 根据class创建对象
     *
     * @param clazz 待创建对象的类
     * @param <T>   对象类型
     * @return 创建好的对象
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 获取某个class的共有方法
     *
     * @param clazz 当前class
     * @return 当前类生命的共有方法
     */
    public static Method[] getPublicMethods(Class clazz) {
        // 当前类所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> publicMethods = new ArrayList<>();
        // 只需要public修饰的方法
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                publicMethods.add(method);
            }
        }
        return publicMethods.toArray(new Method[0]);
    }

    /**
     * 调用指定对象的指定方法
     * @param obj       被调用方法的对象
     * @param method    被调用的方法
     * @param args      方法的参数
     * @return          返回结果
     */
    public static Object invoke(Object obj, Method method, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
