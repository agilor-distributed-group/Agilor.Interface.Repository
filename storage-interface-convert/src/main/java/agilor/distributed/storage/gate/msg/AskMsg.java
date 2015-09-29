package agilor.distributed.storage.gate.msg;

/**
 * Created by LQ on 2015/8/10.
 */
public class AskMsg extends BaseMsg {

    protected byte[] data;

    public AskMsg()
    {
        this.type=MsgType.ASK;
    }

    public byte[] getData() {
        return data;
    }
}
