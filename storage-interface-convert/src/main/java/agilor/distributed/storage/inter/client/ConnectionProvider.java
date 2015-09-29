package agilor.distributed.storage.inter.client;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by LQ on 2015/8/31.
 */
public interface ConnectionProvider {
    TSocket getConnection(TAddress address) throws Exception;
    void  returnConnection(TAddress address,TSocket socket) throws Exception;
}
