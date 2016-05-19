package agilor.distributed.communication.result;

import agilor.distributed.communication.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xinlongli on 16/5/12.
 */
public class GetValueResultFuture extends ResultFuture<List<ValueRes>>{


    public int errorCode=0;
    public String tagName;
    public GetValueResultFuture(String tagname){

        tagName=tagname;
    }

    @Override
    public boolean myParseData(byte[] in, int st, int len) {
        if(in[0]==0){
            int valListLen= ConvertUtils.toInt(in, 2);
            System.out.println(valListLen);
            result=new ArrayList<>();
            byte type=in[6];
            int iPos=7;
            for(int i=0;i<valListLen;){
                ValueRes tmpRes=new ValueRes(in,i+iPos,type);
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
