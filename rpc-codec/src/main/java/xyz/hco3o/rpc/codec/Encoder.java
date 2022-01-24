package xyz.hco3o.rpc.codec;

/**
 * 序列化
 */
public interface Encoder {
    byte[] encode(Object obj);
}
