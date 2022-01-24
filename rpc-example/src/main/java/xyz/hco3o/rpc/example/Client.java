package xyz.hco3o.rpc.example;

import xyz.hco3o.rpc.client.RpcClient;

public class Client {
    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        // 拿到远程代理对象
        CalculatorService service = client.getProxy(CalculatorService.class);
        int r1 = service.add(1, 2);
        int r2 = service.minus(10, 8);
        System.out.println(r1);
        System.out.println(r2);
    }
}
