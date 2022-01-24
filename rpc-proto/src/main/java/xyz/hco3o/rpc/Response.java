package xyz.hco3o.rpc;

import lombok.Data;

/**
 * 表示RPC的返回
 */
@Data
public class Response {
    // 调用远程服务的时候返回code = 0表示成功，非零表示失败
    private int code = 0;
    // 具体失败的原因（错误信息）
    private String message = "OK";
    // 返回的数据
    private Object data;
}
