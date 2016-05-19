package agilor.distributed.communication.result;

import agilor.distributed.communication.utils.ConvertUtils;

/**
 * Created by xinlongli on 16/5/12.
 */
public class ValueRes {
    public int timeStamp;
    public Object value;
    public int len;
    public static final int HEAD_LEN=4;
    public ValueRes(byte[] buf,int st,int type){
        timeStamp=ConvertUtils.toInt(buf,st);
        switch(type){
            case 'L':
                value=ConvertUtils.toInt(buf,st+HEAD_LEN);
                len=HEAD_LEN+4;
                break;
            case 'S':
                value=ConvertUtils.toInt(buf,st+HEAD_LEN);
                len=HEAD_LEN+4;
                break;
            case 'B':
                value=ConvertUtils.toInt(buf,st+HEAD_LEN);
                len=HEAD_LEN+4;
                break;
            case 'F':
                value=ConvertUtils.toFloat(buf,st+HEAD_LEN);
                len=HEAD_LEN+4;
                break;
        }
    }
}
