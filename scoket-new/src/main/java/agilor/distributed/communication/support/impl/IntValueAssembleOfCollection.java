package agilor.distributed.communication.support.impl;

import agilor.distributed.communication.client.Value;

import agilor.distributed.communication.protocol.AssembleContext;
import agilor.distributed.communication.support.inter.ValueAssemble;
import agilor.distributed.communication.utils.ConvertUtils;

/**
 * Created by LQ on 2015/11/12.
 */
public class IntValueAssembleOfCollection implements ValueAssemble {
    @Override
    public AssembleContext<Value> assemble(byte[] data, int pos) {
        Value value = new Value(Value.Types.INT);
        value.setLvalue(ConvertUtils.toInt(data[0], data[1], data[2], data[3]));
        return new AssembleContext<>(value, 4);
    }
}
