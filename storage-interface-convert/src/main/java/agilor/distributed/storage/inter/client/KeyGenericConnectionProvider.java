package agilor.distributed.storage.inter.client;


import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LQ on 2015/8/31.
 */
public class KeyGenericConnectionProvider implements ConnectionProvider {
    Logger logger = LoggerFactory.getLogger(KeyGenericConnectionProvider.class);

    private KeyedObjectPool<TAddress,TSocket> pool = null;

    public KeyGenericConnectionProvider()
    {

        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotal(GenericKeyedObjectPoolConfig.DEFAULT_MAX_TOTAL);

        pool = new GenericKeyedObjectPool<TAddress,TSocket>(new ThriftPoolObjectFactory());




    }



    @Override
    public TSocket getConnection(TAddress address) throws Exception {

        try {

            return pool.borrowObject(address);
        }
        finally {
            logger.info("active count:{}", pool.getNumActive());
            logger.info("idle count:{}", pool.getNumIdle());
        }
    }

    @Override
    public void returnConnection(TAddress address, TSocket socket) throws Exception {
        try {
            pool.returnObject(address, socket);
        }finally {
            logger.info("active count:{}", pool.getNumActive());
            logger.info("idle count:{}", pool.getNumIdle());
        }
    }
}
