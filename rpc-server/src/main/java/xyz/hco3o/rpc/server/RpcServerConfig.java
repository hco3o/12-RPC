package xyz.hco3o.rpc.server;

import lombok.Data;
import xyz.hco3o.rpc.codec.Decoder;
import xyz.hco3o.rpc.codec.Encoder;
import xyz.hco3o.rpc.codec.JSONDecoder;
import xyz.hco3o.rpc.codec.JSONEncoder;
import xyz.hco3o.rpc.transport.HttpTransportServer;
import xyz.hco3o.rpc.transport.TransportServer;

// server配置
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HttpTransportServer.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 3000;
}
