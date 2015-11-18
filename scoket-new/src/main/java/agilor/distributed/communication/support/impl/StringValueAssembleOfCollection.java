package agilor.distributed.communication.support.impl;

import agilor.distributed.communication.client.Value;

import agilor.distributed.communication.protocol.AssembleContext;
import agilor.distributed.communication.support.inter.ValueAssemble;
import agilor.distributed.communication.utils.ConvertUtils;

/**
 * Created by LQ on 2015/11/12.
 */
public class StringValueAssembleOfCollection implements ValueAssemble {
    @Override
    public AssembleContext<Value> assemble(byte[] data, int pos) {

        int str_len = ConvertUtils.toInt(data[pos + 0], data[pos + 1], data[pos + 2], data[pos + 3]);

        Value value = new Value(Value.Types.STRING);

        value.setSvalue(new String(data, pos + 4, str_len));

        return new AssembleContext<>(value, 4 + str_len);

    }
}
