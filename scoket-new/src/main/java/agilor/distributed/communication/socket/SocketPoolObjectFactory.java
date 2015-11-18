package agilor.distributed.communication.socket;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.Socket;

/**
 * Created by LQ on 2015/10/19.
 */
public class SocketPoolObjectFactory implements KeyedPooledObjectFactory<String,Socket> {
    @Override
    public PooledObject<Socket> makeObject(String key) throws Exception {

         String[] address = key.split(":");

        return new DefaultPooledObject<>(new Socket(address[0],Integer.parseInt(address[1])));
    }

    @Override
    public void destroyObject(String key, PooledObject<Socket> p) throws Exception {
        Socket socket = p.getObject();
        if(!socket.isClosed()) {
            socket.close();
        }
    }

    @Override
    public boolean validateObject(String key, PooledObject<Socket> p) {

        if(p.getObject().isConnected())
            return true;
        if(p.getObject().isClosed())
            return false;

        return true;
    }

    @Override
    public void activateObject(String key, PooledObject<Socket> p) throws Exception {


    }

    @Override
    public void passivateObject(String key, PooledObject<Socket> p) throws Exception {

    }
}
