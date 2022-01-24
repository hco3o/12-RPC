package xyz.hco3o.rpc.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

// 表示一个具体服务
@Data
@AllArgsConstructor
public class ServiceInstance {
    // 哪个对象提供服务
    private Object target;
    // 对象的哪个方法暴露成为一个服务
    private Method method;
}
