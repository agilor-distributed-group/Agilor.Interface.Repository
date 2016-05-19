package agilor.distributed.communication.socket;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.Socket;

/**
 * Created by LQ on 2015/10/19.
 */
public class SocketPoolObjectFactory implements KeyedPooledObjectFactory<String,BaseSocket> {
    @Override
    public PooledObject<BaseSocket> makeObject(String key) throws Exception {

         String[] address = key.split(":");
        BaseSocket socket=new BaseSocket(address[0],Integer.parseInt(address[1]));
        socket.startRecv();
        return new DefaultPooledObject<>(socket);
    }

    @Override
    public void destroyObject(String key, PooledObject<BaseSocket> p) throws Exception {
        BaseSocket socket = p.getObject();
        if(!socket.isClosed()) {
            socket.close(-1);
        }
    }

    @Override
    public boolean validateObject(String key, PooledObject<BaseSocket> p) {

        if(p.getObject().isConnected())
            return true;
        if(p.getObject().isClosed())
            return false;

        return true;
    }

    @Override
    public void activateObject(String key, PooledObject<BaseSocket> p) throws Exception {


    }

    @Override
    public void passivateObject(String key, PooledObject<BaseSocket> p) throws Exception {

    }
}
