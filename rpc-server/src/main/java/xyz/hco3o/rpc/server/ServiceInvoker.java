package xyz.hco3o.rpc.server;

import xyz.hco3o.rpc.Request;
import xyz.hco3o.rpc.common.utils.ReflectionUtils;

// 调用具体服务
public class ServiceInvoker {
    public Object invoke(ServiceInstance serviceInstance, Request request) {
        // request中存放参数
        return ReflectionUtils.invoke(serviceInstance.getTarget(), serviceInstance.getMethod(), request.getParameters());
    }
}
