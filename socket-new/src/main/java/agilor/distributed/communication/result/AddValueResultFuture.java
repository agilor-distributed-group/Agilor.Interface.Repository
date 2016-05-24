package agilor.distributed.communication.result;

import agilor.distributed.communication.client.Value;

/**
 * Created by xinlongli on 16/3/31.
 */
public class AddValueResultFuture extends ResultFuture<Byte>{
    //    private static int packageSize=6;
//    public int tmpid;
    public String tagName;
    public String deviceName;
    public Value val;
    public AddValueResultFuture(String tagName, String deviceName,Value val){
        this.tagName=tagName;
        this.deviceName=deviceName;
        this.val=val;
    }
    public AddValueResultFuture(int error){
        this.errorCode=error;
    }

    @Override
    public int getPackageSize(){
        return 1;
    }

    @Override
    public boolean myParseData(byte[] in,int st,int len) {

        if(in==null||st+len>in.length){
            return false;
        }else{
            result=in[st];
        }
        return true;
    }
}