package agilor.distributed.communication.socket;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.net.Socket;

/**
 * Created by LQ on 2015/10/19.
 */
public class KeyGenericSocketProvider implements SocketProvider {

    private KeyedObjectPool<String,Socket> pool = null;


    public KeyGenericSocketProvider()
    {

        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotal(GenericKeyedObjectPoolConfig.DEFAULT_MAX_TOTAL);
        config.setTestOnBorrow(true);

        pool = new GenericKeyedObjectPool<String,Socket>(new SocketPoolObjectFactory(),config);

    }



    @Override
    public Socket getSocket(String address, int port) throws Exception {

        String key =address+":"+port;

        try {
            return pool.borrowObject(key);
        }finally {
            System.out.println("busy:"+pool.getNumActive(key)+" idle:"+pool.getNumIdle(key));
        }
    }

    @Override
    public void returnSocket(Socket socket) throws Exception {
        String address = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();

        String key = address+":"+port;

        pool.returnObject(key,socket);

        System.out.println("busy:" + pool.getNumActive(key) + " idle:" + pool.getNumIdle(key));


    }
}
