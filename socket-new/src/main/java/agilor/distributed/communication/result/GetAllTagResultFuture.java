package agilor.distributed.communication.result;

import agilor.distributed.communication.utils.ConvertUtils;

import java.util.ArrayList;

/**
 * Created by xinlongli on 16/5/13.
 */
public class GetAllTagResultFuture extends ResultFuture<java.util.List<TagInfoRes>> {
    public int errorCode=0;
    /***
     * @param in necessary, data content res(1byte)+'A'(1byte)+len(4byte)+T-N_pair
     * @param st optional, first index in data
     * @param len optional, length of data
    * */
    @Override
    public boolean myParseData(byte[] in, int st, int len) {
        if(in[0]==0){
            int valListLen= ConvertUtils.toInt(in, 2);
            System.out.println(valListLen);
            result=new ArrayList<>();
            int iPos=6;
            for(int i=0;i<valListLen;){
                TagInfoRes tmpRes=new TagInfoRes(in,i+iPos);
                result.add(tmpRes);
                i+=tmpRes.len;
            }
        }else{
            errorCode=in[0];
        }
        return true;
    }

    @Override
    public int getPackageSize() {
        return result.size();
    }
}
