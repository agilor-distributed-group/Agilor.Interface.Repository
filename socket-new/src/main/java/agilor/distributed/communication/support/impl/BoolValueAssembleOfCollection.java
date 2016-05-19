package agilor.distributed.communication.support.impl;

import agilor.distributed.communication.client.Value;

import agilor.distributed.communication.protocol.AssembleContext;
import agilor.distributed.communication.support.inter.ValueAssemble;

/**
 * Created by LQ on 2015/11/12.
 */
public class BoolValueAssembleOfCollection implements ValueAssemble {
    @Override
    public AssembleContext<Value> assemble(byte[] data, int pos) {
        Value value = new Value(Value.Types.BOOL);
        value.setBvalue(data[pos] == 1);
        return new AssembleContext<>(value, 1);
    }
}
