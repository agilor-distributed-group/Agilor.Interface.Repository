package agilor.distributed.communication.protocol;

import java.util.List;

/**
 * Created by LQ on 2015/10/20.
 */
public interface Protocol {
    byte[] resolve(int data);

    byte[] resolve(boolean data);

    byte[] resolve(float data);

    byte[] resolve(double data);

    byte[] resolve(long data);

    byte[] resolve(char data);

    byte[] resolve(byte data);

    byte[] resolve(String data) throws Exception;

    <T> byte[] resolve(T data) throws Exception;

    <T> byte[] resolve(T[] data) throws Exception;

    <T> byte[] resolve(List<T> data) throws Exception;

    Assemble assemble(byte[] data,int pos,int len) throws Exception;
}
