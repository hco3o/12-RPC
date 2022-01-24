package xyz.hco3o.rpc.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import xyz.hco3o.rpc.Request;
import xyz.hco3o.rpc.Response;
import xyz.hco3o.rpc.ServiceDescriptor;
import xyz.hco3o.rpc.codec.Decoder;
import xyz.hco3o.rpc.codec.Encoder;
import xyz.hco3o.rpc.transport.TransportClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 调用远程服务的代理类
@Slf4j
public class RemoteInvoker implements InvocationHandler {

    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz, Encoder encoder, Decoder decoder, TransportSelector selector) {
        this.clazz = clazz;
        this.encoder = encoder;
        this.decoder = decoder;
        this.selector = selector;
    }

    /*
        如果要调远程服务
            1、先要构造一个请求
            2、通过网络把请求发送给server
            3、发送完后等待server的响应
            4、从响应里拿到返回的数据
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);
        // 通过网络传输调用远程服务
        // 把request传过去，server响应一个response
        Response response = invokeRemote(request);
        if (response == null || response.getCode() != 0) {
            throw new IllegalStateException("fail to invoke remote: " + response);
        }
        return response.getData();
    }

    // 实现网络传输
    private Response invokeRemote(Request request) {
        Response response = null;
        TransportClient client = null;
        try {
            // 选一个client
            client = selector.select();
            // 序列化request
            byte[] outBytes = encoder.encode(request);
            // 获取到的数据
            InputStream receive = client.write(new ByteArrayInputStream(outBytes));
            // 读出接收到的数据
            byte[] inBytes = IOUtils.readFully(receive, receive.available());
            // 反序列化
            response = decoder.decode(inBytes, Response.class);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            response = new Response();
            response.setCode(1);
            response.setMessage("RpcClient got error: " + e.getClass() + ": " + e.getMessage());
        } finally {
            if (client != null) {
                selector.release(client);
            }
        }
        return response;
    }
}
