package agilor.distributed.communication.protocol;

import java.util.List;

/**
 * Created by LQ on 2015/10/20.
 */
public class ProtocolBase implements Protocol {
    @Override
    public byte[] resolve(int data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(boolean data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(float data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(double data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(long data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(char data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(byte data) {
        return new byte[0];
    }

    @Override
    public byte[] resolve(String data) throws Exception {
        return new byte[0];
    }

    @Override
    public <T> byte[] resolve(T data) throws Exception {
        if (data instanceof ProtocolObject)
            return ((ProtocolObject) data).toBytes();
        return new byte[0];
    }

    @Override
    public <T> byte[] resolve(T[] data) throws Exception {
        return new byte[0];
    }

    @Override
    public <T> byte[] resolve(List<T> data) throws Exception {
        return new byte[0];
    }

    @Override
    public Assemble assemble(byte[] data,int pos,int len) throws Exception {
        return null;
    }
}
