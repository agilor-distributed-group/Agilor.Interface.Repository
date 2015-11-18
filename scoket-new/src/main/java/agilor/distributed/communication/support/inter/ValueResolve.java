package agilor.distributed.communication.support.inter;

import agilor.distributed.communication.client.Value;

/**
 * Created by LQ on 2015/11/12.
 */
public interface ValueResolve {
    byte[] toBytes(Value value) throws Exception;

}
