package agilor.distributed.storage.msg;

/**
 * Created by LQ on 2015/7/31.
 */
public class PingMsg extends BaseMsg {
    private boolean isKeep = false;


    public PingMsg() {
        this(false);
    }

    public PingMsg(boolean isKeep)
    {
        this.isKeep=isKeep;
        this.type=MsgType.PING;
    }

}
