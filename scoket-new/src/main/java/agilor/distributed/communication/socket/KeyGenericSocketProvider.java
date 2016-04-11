package agilor.distributed.communication.socket;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.net.Socket;

/**
 * Created by LQ on 2015/10/19.
 */
public class KeyGenericSocketProvider implements SocketProvider {

    private KeyedObjectPool<String,BaseSocket> pool = null;


    public KeyGenericSocketProvider()
    {

        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotal(GenericKeyedObjectPoolConfig.DEFAULT_MAX_TOTAL);
        config.setTestOnBorrow(true);

        pool = new GenericKeyedObjectPool<String,BaseSocket>(new SocketPoolObjectFactory(),config);

    }



    @Override
    public BaseSocket getSocket(String address, int port) throws Exception {

        String key = address + ":" + port;


        return pool.borrowObject(key);

    }

    @Override
    public void returnSocket(BaseSocket socket) throws Exception {
        String address = socket.getHostAddress();
        int port = socket.getPort();
        String key = address+":"+port;
        pool.returnObject(key,socket);

    }

    @Override
    public void destorySocket(BaseSocket socket)throws Exception{
        String address = socket.getHostAddress();
        int port = socket.getPort();
        String key = address+":"+port;
        pool.invalidateObject(key,socket);
    }
}
