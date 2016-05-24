package agilor.distributed.communication.result;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.utils.ConvertUtils;

/**
 *
 * Created by xinlongli on 16/5/12.
 */
public class ValueRes {
    public int timeStamp;
    public Value value=null;
    public int len;
    public static final int TIMESTAMP_LEN =4;
    public ValueRes(byte[] buf,int st,int type){
        timeStamp=ConvertUtils.toInt(buf,st);

        switch(type){
            case 'L':
                value=new Value(Value.Types.INT);
                value.setLvalue(ConvertUtils.toInt(buf,st+ TIMESTAMP_LEN));
                len= TIMESTAMP_LEN +4;
                break;
            case 'S':
                value=new Value(Value.Types.STRING);
                int strlen=ConvertUtils.toInt(buf,st+ TIMESTAMP_LEN);
                value.setSvalue(new String(buf,st+ TIMESTAMP_LEN+4,strlen));
                len= TIMESTAMP_LEN +4+strlen;
                break;
            case 'B':
                value=new Value(Value.Types.BOOL);
                value.setBvalue(buf[st+ TIMESTAMP_LEN] == 1);
                len= TIMESTAMP_LEN +1;
                break;
            case 'F':
                value=new Value(Value.Types.FLOAT);
                value.setFvalue(ConvertUtils.toFloat(buf, st + TIMESTAMP_LEN));
                len= TIMESTAMP_LEN +4;
                break;
        }
    }
}
