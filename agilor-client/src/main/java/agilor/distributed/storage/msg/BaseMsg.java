package agilor.distributed.storage.msg;

import java.io.Serializable;

/**
 * Created by LQ on 2015/7/31.
 */
public class BaseMsg implements Msg,Serializable {
    protected MsgType type;
    protected String clientId;


    @Override
    public MsgType getType() {
        return null;
    }

    @Override
    public String getClientId() {
        return null;
    }

    public BaseMsg()
    {
        this.type= MsgType.NONE;
    }
}
