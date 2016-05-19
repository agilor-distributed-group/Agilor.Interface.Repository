package agilor.distributed.communication;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.SimpleProtocol;
import agilor.distributed.communication.result.ValueRes;
import agilor.distributed.communication.socket.Connection;
import agilor.distributed.communication.result.ResultFuture;
import agilor.distributed.communication.utils.DQBuffer;

import java.util.Calendar;
import java.util.List;

/**
 * Created by xinlongli on 16/4/6.
 */
public class TestClient {

    public static DQBuffer queue1;
    public static void main(String[] args){
        Connection connection = new Connection("11.0.0.16",10001, SimpleProtocol.getInstance());

        queue1=new DQBuffer(1024,100);
        Thread parseThread=new Thread(()->{
            int i=0;
            int resFailedcount=0;
            while(true){
                try{
                    ResultFuture tmp=queue1.getData(100);
                    while(tmp==null){
                        queue1.flush();
                        tmp=queue1.getData(100);
                    }
                    int res=((Byte)(tmp.get(-1,null))).intValue();
                    if(res!=0){
                        System.out.println(i+" res："+res);
                        if(i%1000!=404){
                            System.err.println("error! : "+i);
                        }else{
                            resFailedcount++;
                            System.out.println(resFailedcount);
                        }
                    }
                    i++;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        try{
            client.open();
//            parseThread.start();
//            for(int j=0;j<100;j++){
//                for(int i=1;i<=1000;i++){
//                    Value val = new Value(Value.Types.FLOAT);
//                    val.setFvalue((float)10.123+j);
//                    ResultFuture res=client.addValue("Simu11_" + i, "Simu11", val, false);
//                    while(!queue1.putData(res))
//                        Thread.sleep(1);
//                    //Thread.sleep(10);
//                }
//            }
//            ResultFuture res=client.getAllTag();
//            List<TagInfoRes> valRes=((List<TagInfoRes>)res.get(-1, null));
//            System.out.println("tag 0 , tagName: "+valRes.get(0).tagName+" type: "+(byte)valRes.get(0).type+" total len :"+valRes.size());

            Calendar st=Calendar.getInstance();
            st.set(2016,4,18,18,48,40);
            Calendar ed= Calendar.getInstance();
            ed.set(2016,4,18,18,50,0);
            ResultFuture res=client.getValue(st, ed, "Simu11_1");
            List<ValueRes> valRes=((List<ValueRes>)res.get(-1, null));
            System.out.println("tag 0 , time: "+valRes.get(0).timeStamp+" value: "+(Float)valRes.get(0).value+" total len :"+valRes.size());
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}
