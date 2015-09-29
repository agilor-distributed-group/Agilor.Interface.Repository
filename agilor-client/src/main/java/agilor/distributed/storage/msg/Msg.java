package agilor.distributed.storage.msg;

import java.io.Serializable;

/**
 * Created by LQ on 2015/7/31.
 */
public interface Msg extends Serializable {
    MsgType getType();
    String getClientId();

}
