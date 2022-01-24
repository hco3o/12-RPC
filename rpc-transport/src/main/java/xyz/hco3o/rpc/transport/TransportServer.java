package xyz.hco3o.rpc.transport;

/**
 * 1、启动，监听端口
 * 2、等待网络客户端连接，接收请求（接收到byte数据流，用RequestHandler进行转换）
 * 3、关闭监听
 */
public interface TransportServer {
    void init(int port, RequestHandler handler);
    void start();
    void stop();
}
