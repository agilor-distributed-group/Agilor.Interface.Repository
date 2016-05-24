package agilor.distributed.communication.result;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.utils.ConvertUtils;

/**
 * Created by xinlongli on 16/5/13.
 */
public class TagInfoRes implements Comparable<TagInfoRes>{
    public String tagName;

    public Value.Types type;
    public int len;
    public static final int TYPE_LEN=1+5;//type 1 char;string prefix 1+4 char;
    public TagInfoRes(byte[] buf,int st){
        tagName= ConvertUtils.toString(buf, st);
        len=tagName.length()+TYPE_LEN;
        type=Value.Types.value(buf[st+len-1]);
    }

    public TagInfoRes(String tagName){
        this.tagName= tagName;
    }

    @Override
    public int compareTo(TagInfoRes o) {
        return tagName.compareTo(o.tagName);
    }
}
