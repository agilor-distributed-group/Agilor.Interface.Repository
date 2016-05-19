package agilor.distributed.communication.support.inter;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.AssembleContext;

/**
 * Created by LQ on 2015/11/12.
 */
public interface ValueAssemble {
    AssembleContext<Value> assemble(byte[] data,int pos) throws Exception;
}
