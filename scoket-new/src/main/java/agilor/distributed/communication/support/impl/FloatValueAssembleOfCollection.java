package agilor.distributed.communication.support.impl;

import agilor.distributed.communication.client.Value;

import agilor.distributed.communication.protocol.AssembleContext;
import agilor.distributed.communication.protocol.ProtocolDataTypes;
import agilor.distributed.communication.support.inter.ValueAssemble;
import agilor.distributed.communication.utils.ConvertUtils;

import java.util.Calendar;

/**
 * Created by LQ on 2015/11/12.
 */
public class FloatValueAssembleOfCollection implements ValueAssemble {

    @Override
    public AssembleContext<Value> assemble(byte[] data, int pos) throws Exception {
        Value value = new Value(Value.Types.FLOAT);
        long timestap = ConvertUtils.toInt(data[pos],data[pos+1],data[pos+2],data[pos+3]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestap *1000);
        value.setTime(calendar);


        float val = ConvertUtils.toFloat(data[pos+4],data[pos+5],data[pos+6],data[pos+7]);
        value.setFvalue(val);





//
//        int len = ConvertUtils.toInt(data[pos + 1], data[pos + 2], data[pos + 3], data[pos + 4]);
//        byte[] value_bytes = new byte[len + 1];
//
//
//        int interval = len  - 4;
//
//        System.arraycopy(data, pos + 5, value_bytes, 0, interval);
//        value_bytes[interval] = ProtocolDataTypes.FLOAT.value();
//        System.arraycopy(data, pos + interval + 5, value_bytes, interval + 1, 4);
//
//        value.fromBytes(value_bytes, 0, value_bytes.length);

        return new AssembleContext<>(value,8);
    }
}
