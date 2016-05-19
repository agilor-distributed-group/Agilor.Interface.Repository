package agilor.distributed.communication.client;

import agilor.distributed.communication.protocol.*;

import java.util.ArrayList;

/**
 * Created by LQ on 2015/12/15.
 */
public class TargetCollection extends ArrayList<Target> implements ProtocolObject {
    @Override
    public byte[] toBytes() throws Exception {
        return new byte[0];
    }

    @Override
    public void fromBytes(byte[] data, int pos, int len) throws Exception {
        Assemble assemble = new SimpleAssemble(data,pos,len);
        Token token = null;
        while ((token=assemble.next())!=null) {
            this.add(new Target(token.toStd(), Value.Types.value(data[assemble.pos()])));
            try {
                assemble.forward(1);
            }catch (RuntimeException e)
            {
                break;
            }


        }
    }

    @Override
    public boolean isAutoPrefix() {
        return false;
    }
}
