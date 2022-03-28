package xyz.hco3o.rpc.server;

import lombok.extern.slf4j.Slf4j;
import xyz.hco3o.rpc.Request;
import xyz.hco3o.rpc.ServiceDescriptor;
import xyz.hco3o.rpc.common.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 管理RPC暴露的服务（两个方法：注册服务和查找服务）
@Slf4j
public class ServiceManager {

    // 注册的服务放到map里
    // k：服务的描述；v：服务的具体实现）
    private final Map<ServiceDescriptor, ServiceInstance> services;

    public ServiceManager() {
        this.services = new ConcurrentHashMap<>();
    }

    /**
     * 注册服务
     *
     * @param interfaceClass 注册的时候指定的接口（也是一个Class）
     * @param bean           服务的具体提供者（因为是单例，要作为参数传进来）
     * bean是接口实现的子类的一个对象，可以把两者用泛型关联起来
     */
    public <T> void register(Class<T> interfaceClass, T bean) {
        // 扫描接口里所有的方法，和bean绑定成为一个ServiceInstance，放入map
        // 把interface中所有方法方法都当成服务注册到ServiceManager
        Method[] methods = ReflectionUtils.getPublicMethods(interfaceClass);
        for (Method method : methods) {
            ServiceInstance serviceInstance = new ServiceInstance(bean, method);
            ServiceDescriptor serviceDescriptor = ServiceDescriptor.from(interfaceClass, method);
            services.put(serviceDescriptor, serviceInstance);
            log.info("register service: {} {}", serviceDescriptor.getClazz(), serviceDescriptor.getMethod());
        }
    }

    /**
     * 查找服务
     * @param request   RPC的request
     * @return          返回服务
     */
    public ServiceInstance lookup(Request request) {
        ServiceDescriptor serviceDescriptor = request.getService();
        // get的时候根据ServiceDescriptor的equals方法来判断，需重写一个equals和hashcode方法
        return services.get(serviceDescriptor);
    }
}
