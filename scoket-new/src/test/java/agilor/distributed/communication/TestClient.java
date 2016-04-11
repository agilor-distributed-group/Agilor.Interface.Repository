package agilor.distributed.communication;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.SimpleProtocol;
import agilor.distributed.communication.socket.Connection;
import agilor.distributed.communication.socket.ResultFuture;
import agilor.distributed.communication.utils.DQBuffer;

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
            while(true){
                try{
                    ResultFuture tmp=queue1.getData(-1);
                    int res=((Byte)(tmp.get(-1,null))).intValue();
                    if(res!=0){
                        System.out.println(i+"res："+res);
                    }
                    i++;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        parseThread.start();

        agilor.distributed.communication.client.Client client = new agilor.distributed.communication.client.Client(connection);
        try{
            client.open();
            Value val = new Value(Value.Types.FLOAT);
            val.setFvalue(10);
            for(int j=0;j<5000;j++){
                for(int i=1;i<=1000;i++){
                    ResultFuture res=client.addTarget("Simu11_" + i, "Simu11", val, false);
                    while(!queue1.putData(res))
                        Thread.sleep(1);
                }
            }
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}
