package agilor.distributed.storage.inter.client;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LQ on 2015/8/31.
 */
public class ThriftPoolObjectFactory implements KeyedPooledObjectFactory<TAddress,TSocket> {
    private Logger logger = LoggerFactory.getLogger(ThriftPoolObjectFactory.class);
    public ThriftPoolObjectFactory()
    {

    }

    @Override
    public PooledObject<TSocket> makeObject(TAddress tAddress) throws Exception {
        TSocket socket = new TSocket(tAddress.getAddress(), tAddress.getPort(), tAddress.getTimeOut());
        logger.info("trigger makeObject");

        return new DefaultPooledObject<>(socket);
    }

    @Override
    public void destroyObject(TAddress tAddress, PooledObject<TSocket> pooledObject) throws Exception {
        logger.info("trigger destroyObject");
        TTransport transport = pooledObject.getObject();

        if(transport.isOpen()) transport.close();
    }

    @Override
    public boolean validateObject(TAddress tAddress, PooledObject<TSocket> pooledObject) {
        logger.info("trigger makeObject");
        TTransport transport = pooledObject.getObject();
        return transport.isOpen();

    }

    @Override
    public void activateObject(TAddress tAddress, PooledObject<TSocket> pooledObject) throws Exception {
        logger.info("trigger activateObject");
        TSocket socket = pooledObject.getObject();
        socket.setTimeout(tAddress.getTimeOut());
    }

    @Override
    public void passivateObject(TAddress tAddress, PooledObject<TSocket> pooledObject) throws Exception {
        logger.info("trigger passivateObject");

    }
}
