package agilor.distributed.communication;

import agilor.distributed.communication.client.Client;
import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.SimpleProtocol;
import agilor.distributed.communication.socket.Connection;
import agilor.distributed.communication.result.ResultFuture;
import agilor.distributed.communication.utils.DQBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by xinlongli on 16/5/6.
 */
public class TestTest {
    Connection connection;
    int addValueTotal;
    final int LoopCount=10;

    interface TestCallback{
        void callback(Client c,int i,int j,DQBuffer q) throws Exception;

    }
    interface ComfirmCallback{
        void callback(int i,ResultFuture tmp)throws Exception;
    }
    @Before
    public void init(){
        addValueTotal=0;
        connection = new Connection("11.0.0.16",10001, SimpleProtocol.getInstance());
    }
    @After
    public void tearDown(){
        //System.out.println(addValueTotal);
        System.out.println(addValueTotal);
        assertFalse(addValueTotal!=LoopCount);
    }
    private void testFrameWork(int tagMax,int loopMax,TestCallback test,ComfirmCallback confirm){
        final DQBuffer queue1=new DQBuffer(1024,100);
        Thread parseThread=new Thread(()->{
            int i=0;
            while(true){
                try{
                    ResultFuture tmp=queue1.getData(-1);
                    confirm.callback(i,tmp);
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

            for(int j=0;j<loopMax;j++){
                for(int i=1;i<=tagMax;i++){
//                    ResultFuture res=client.addTarget("Simu12_" + i, "Simu12", val, false);
//                    while(!queue1.putData(res))
//                        Thread.sleep(1);
                    test.callback(client,i,j,queue1);
                }
            }
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
//    @Test
//    public void testAddTag(){
//        testFrameWork(1000,1,(Client client,int i,DQBuffer queue1)->{
//            Value val = new Value(Value.Types.FLOAT);
//            val.setFvalue((float)10.123+i);
//            ResultFuture res=client.addTarget("Simu11_" + i, "Simu11", val, false);
//            while(!queue1.putData(res))
//                Thread.sleep(1);
//        },(int i,ResultFuture tmp)->{
//            int res=((Byte)(tmp.get(-1,null))).intValue();
//            assertFalse(res!=0);
//
//        });
//    }

    @Test
    public void testAddValue(){
        testFrameWork(1000,LoopCount,(Client client,int i,int j,DQBuffer queue1)->{
            Value val = new Value(Value.Types.FLOAT);
            val.setFvalue((float)10.123+j);
            ResultFuture res=client.addValue("Simu11_" + i, "Simu11", val, false);
            while(!queue1.putData(res))
                Thread.sleep(1);
        },(int i,ResultFuture tmp)->{
            int res=((Byte)(tmp.get(-1,null))).intValue();
            if(res!=0){
                if(i%1000==404){
                    System.out.println(i);
                    addValueTotal++;
                }else{
                    assertFalse(true);
                }
            }
        });
    }
}
