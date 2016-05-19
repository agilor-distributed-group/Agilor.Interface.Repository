package agilor.distributed.communication.client;

import agilor.distributed.communication.protocol.*;
import agilor.distributed.communication.support.factory.ValueAssembleFactoryOfCollection;
import agilor.distributed.communication.support.factory.ValueResolveFactoryOfCollection;
import agilor.distributed.communication.support.inter.ValueAssemble;
import agilor.distributed.communication.support.inter.ValueResolve;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/11/11.
 */
public class ValueCollection  extends ArrayList<Value> implements ProtocolObject  {



    @Override
    public byte[] toBytes() throws Exception {
        byte[] result = null;

         ProtocolDataTypes type = this.get(0).getValueType().convert();
        if(type==null)
            throw new Exception("no define protocol type");



        ValueResolve resolve = ValueResolveFactoryOfCollection.factory(type);
        if(type!=ProtocolDataTypes.STRING) {


            byte[] first_bytes = resolve.toBytes(this.get(0));

            result = new byte[1 + first_bytes.length * size()];
            result[0] = type.value();
            System.arraycopy(first_bytes, 0, result, 1, first_bytes.length);

            int pos = 1+first_bytes.length;
            for (int i = 1; i < size(); i++) {
                System.arraycopy(resolve.toBytes(get(i)), 0, result, pos, first_bytes.length);
                pos += first_bytes.length;
            }
        }
        else {
            List<byte[]> bytes_list = new ArrayList<>(size());
            int result_len = 1;
            for(Value i:this) {
                byte[] val_bytes = resolve.toBytes(i);
                result_len += val_bytes.length;
            }
            result = new byte[result_len];

            int pos =1;
            for(byte[] byte_it:bytes_list) {
                System.arraycopy(byte_it, 0, result, pos, byte_it.length);
                pos += byte_it.length;
            }
        }

        return result;
    }

    @Override
    public void fromBytes(byte[] data, int pos, int len) throws Exception {

        ValueAssemble assemble = ValueAssembleFactoryOfCollection.factory(ProtocolDataTypes.type(data[pos]));

        int end = pos + len;
        for (int i = pos+ 1; i < end; ) {
            AssembleContext<Value> context = assemble.assemble(data, i);
            i += context.getLen();
            add(context.getValue());
        }
    }

    @Override
    public boolean isAutoPrefix() {
        return true;
    }
}
