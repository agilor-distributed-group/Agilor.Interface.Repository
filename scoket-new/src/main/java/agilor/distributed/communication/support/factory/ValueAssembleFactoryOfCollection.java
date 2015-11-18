package agilor.distributed.communication.support.factory;


import agilor.distributed.communication.protocol.ProtocolDataTypes;
import agilor.distributed.communication.support.impl.BoolValueAssembleOfCollection;
import agilor.distributed.communication.support.impl.FloatValueAssembleOfCollection;
import agilor.distributed.communication.support.impl.IntValueAssembleOfCollection;
import agilor.distributed.communication.support.impl.StringValueAssembleOfCollection;
import agilor.distributed.communication.support.inter.ValueAssemble;

/**
 * Created by LQ on 2015/11/12.
 */
public class ValueAssembleFactoryOfCollection {
    public static ValueAssemble factory(ProtocolDataTypes type)
    {
        switch (type) {
            case INT:
                return new IntValueAssembleOfCollection();
            case FLOAT:
                return new FloatValueAssembleOfCollection();
            case BOOL:
                return new BoolValueAssembleOfCollection();
            case STRING:
                return new StringValueAssembleOfCollection();
        }
        return null;
    }
}
