package xyz.hco3o.rpc.transport;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class HttpTransportServer implements TransportServer {
    private RequestHandler handler;
    private Server server;

    @Override
    public void init(int port, RequestHandler handler) {
        this.handler = handler;
        this.server = new Server(port);

        // servlet接收请求
        ServletContextHandler ctx = new ServletContextHandler();
        server.setHandler(ctx);

        // holder: jetty处理网络请求的一个抽象
        ServletHolder holder = new ServletHolder(new RequestServlet());
        // /*: 处理所有的路径
        ctx.addServlet(holder, "/*");
    }

    @Override
    public void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    class RequestServlet extends HttpServlet {
        // 需要处理post请求，重写一下
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            log.info("client connected!");
            ServletInputStream in = req.getInputStream();
            OutputStream out = resp.getOutputStream();
            if (handler != null) {
                handler.onRequest(in, out);
            }
            out.flush();
        }
    }
}
