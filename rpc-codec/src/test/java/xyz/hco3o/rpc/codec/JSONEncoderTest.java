package xyz.hco3o.rpc.codec;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONEncoderTest {

    @Test
    public void encode() {
        TestBean bean = new TestBean();
        bean.setName("haicheng");
        bean.setAge(18);

        Encoder encoder = new JSONEncoder();
        byte[] bytes = encoder.encode(bean);

        assertNotNull(bytes);
    }
}