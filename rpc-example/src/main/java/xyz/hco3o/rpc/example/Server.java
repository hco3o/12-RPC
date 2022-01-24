package xyz.hco3o.rpc.example;

import xyz.hco3o.rpc.server.RpcServer;

public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        server.register(CalculatorService.class, new CalculatorServiceImpl());
        server.start();
    }
}
