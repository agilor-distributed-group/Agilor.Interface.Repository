package agilor.distributed.communication.support.factory;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.ProtocolDataTypes;
import agilor.distributed.communication.support.impl.*;
import agilor.distributed.communication.support.inter.ValueResolve;

/**
 * Created by LQ on 2015/11/12.
 */
public class ValueResolveFactoryOfCollection {

    public static ValueResolve factory(ProtocolDataTypes type  ) {
        switch (type) {
            case INT:
                return new IntValueResolveOfCollection();
            case FLOAT:
                return new FloatValueResolveOfCollection();
            case BOOL:
                return new BoolValueResolveOfCollection();
            case STRING:
                return new StringValueResolveOfCollection();
            default:
                return null;
        }
    }
}
