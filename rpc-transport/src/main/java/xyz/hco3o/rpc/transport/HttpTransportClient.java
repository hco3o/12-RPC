package xyz.hco3o.rpc.transport;

import org.apache.commons.io.IOUtils;
import xyz.hco3o.rpc.Peer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTransportClient implements TransportClient {
    private String url;

    @Override
    public void connect(Peer peer) {
        this.url = "http://" + peer.getHost() + ":" + peer.getPort();
    }

    @Override
    public InputStream write(InputStream data) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
            // 这个连接需要读写数据
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            // 其它的初始化
            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod("POST");
            // 开启连接
            httpConnection.connect();

            // 把data数据发送给server
            IOUtils.copy(data, httpConnection.getOutputStream());

            int resultCode = httpConnection.getResponseCode();
            if (resultCode == HttpURLConnection.HTTP_OK) {
                return httpConnection.getInputStream();
            } else {
                return httpConnection.getErrorStream();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {

    }
}
