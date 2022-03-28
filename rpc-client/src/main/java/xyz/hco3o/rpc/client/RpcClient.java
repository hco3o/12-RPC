package xyz.hco3o.rpc.client;

import xyz.hco3o.rpc.codec.Decoder;
import xyz.hco3o.rpc.codec.Encoder;
import xyz.hco3o.rpc.common.utils.ReflectionUtils;

import java.lang.reflect.Proxy;

public class RpcClient {
    private final Encoder encoder;
    private final Decoder decoder;
    private final TransportSelector selector;

    // 无参构造使用默认配置
    public RpcClient() {
        this(new RpcClientConfig());
    }

    public RpcClient(RpcClientConfig config) {
        // 把组件初始化
        this.encoder = ReflectionUtils.newInstance(config.getEncoderClass());
        this.decoder = ReflectionUtils.newInstance(config.getDecoderClass());
        this.selector = ReflectionUtils.newInstance(config.getSelectorClass());
        // selector需要初始化
        this.selector.init(config.getServers(), config.getConnectCount(), config.getTransportClass());
    }

    // 获取接口的代理对象
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{clazz},
                new RemoteInvoker(clazz, encoder, decoder, selector));
    }
}
