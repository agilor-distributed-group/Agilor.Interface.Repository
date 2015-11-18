package agilor.distributed.communication.support.impl;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.Protocol;
import agilor.distributed.communication.protocol.SimpleProtocol;
import agilor.distributed.communication.support.inter.ValueResolve;

/**
 * Created by LQ on 2015/11/12.
 */
public class IntValueResolveOfCollection implements ValueResolve {

    private static Protocol protocol = SimpleProtocol.getInstance();



    @Override
    public byte[] toBytes(Value value) throws Exception {
        byte[] data = protocol.resolve(value);
        byte[] result = new byte[data.length - 1];
        System.arraycopy(data, 0, result, 0, data.length - 5);
        System.arraycopy(data, data.length-4, result, data.length - 5, 4);
        return result;
    }
}
