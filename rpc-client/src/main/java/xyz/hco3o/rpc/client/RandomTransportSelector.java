package xyz.hco3o.rpc.client;

import lombok.extern.slf4j.Slf4j;
import xyz.hco3o.rpc.Peer;
import xyz.hco3o.rpc.common.utils.ReflectionUtils;
import xyz.hco3o.rpc.transport.TransportClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 随机选择一个网络端点
// 这些方法有可能并发执行，所以每一个方法加上synchronized
@Slf4j
public class RandomTransportSelector implements TransportSelector {


    // 已经连接好的clients
    private final List<TransportClient> clients;

    public RandomTransportSelector() {
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void init(List<Peer> peers, int count, Class<? extends TransportClient> clazz) {
        // count必须大于等于1
        count = Math.max(count, 1);
        // 循环建立连接
        for (Peer peer : peers) {
            for (int i = 0; i < count; i++) {
                TransportClient client = ReflectionUtils.newInstance(clazz);
                client.connect(peer);
                clients.add(client);
            }
            log.info("connect server: {}", peer);
        }
    }

    @Override
    public synchronized TransportClient select() {
        // 随机选择一个client列表中的client
        int i = new Random().nextInt(clients.size());
        return clients.remove(i);
    }

    @Override
    public synchronized void release(TransportClient client) {
        clients.add(client);
    }

    @Override
    public synchronized void close() {
        // 所有client关闭
        for (TransportClient client : clients) {
            client.close();
        }
        clients.clear();
    }
}
