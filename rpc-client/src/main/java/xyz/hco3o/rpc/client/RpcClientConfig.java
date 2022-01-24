package xyz.hco3o.rpc.client;

import lombok.Data;
import xyz.hco3o.rpc.Peer;
import xyz.hco3o.rpc.codec.Decoder;
import xyz.hco3o.rpc.codec.Encoder;
import xyz.hco3o.rpc.codec.JSONDecoder;
import xyz.hco3o.rpc.codec.JSONEncoder;
import xyz.hco3o.rpc.transport.HttpTransportClient;
import xyz.hco3o.rpc.transport.TransportClient;

import java.util.Arrays;
import java.util.List;

@Data
public class RpcClientConfig {
    // 需要的client的实现类型
    private Class<? extends TransportClient> transportClass = HttpTransportClient.class;
    // 序列化和反序列化的配置信息
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    // 路由选择策略信息（默认随即策略）
    private Class<? extends TransportSelector> selectorClass = RandomTransportSelector.class;
    // 每一个server的peer需要建立多少连接
    private int connectCount = 1;
    // 可以连哪些网络端点
    private List<Peer> servers = Arrays.asList(new Peer("127.0.0.1", 3000));
}
