package xyz.hco3o.rpc.client;

import xyz.hco3o.rpc.Peer;
import xyz.hco3o.rpc.transport.TransportClient;

import java.util.List;

// 表示选择哪个server去连接
public interface TransportSelector {
    /**
     * 初始化selector
     * @param peers 可以连接的server端点列表
     * @param count client与server建立多少个连接
     * @param clazz client的实现class
     */
    void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz);
    // 选一个transport和server做交互，返回一个网络client
    TransportClient select();
    // 释放用完的client
    void release(TransportClient client);

    // 关闭selector
    void close();
}
