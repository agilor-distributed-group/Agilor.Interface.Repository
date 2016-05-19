package agilor.distributed.communication.utils;

/**
 * Created by xinlongli on 16/4/5.
 */
public class LinkedBytes {
    public byte[] content;
    public LinkedBytes next=null;
    public LinkedBytes(byte[] init){
        content=init;
        next=null;
    }
}
