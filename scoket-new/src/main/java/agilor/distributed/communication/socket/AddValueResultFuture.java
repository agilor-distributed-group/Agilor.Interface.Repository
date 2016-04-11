package agilor.distributed.communication.socket;

import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.utils.LinkedBytes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by xinlongli on 16/3/31.
 */
public class AddValueResultFuture implements ResultFuture<Byte>{
    private boolean isDone=false;
    private Byte result=null;
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
    @Override
    public int getPackageSize(){
        return 1;
    }

    @Override
    public boolean parseData(LinkedBytes in,int st,int len)throws IOException {
        synchronized(this){
            if(in==null||st+len>in.content.length){
                return false;
            }else{
                result=in.content[st];
            }
            isDone=true;
            this.notifyAll();
            return true;
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public Byte get() throws InterruptedException, ExecutionException {
        return result;
    }

    @Override
    public Byte get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if(result!=null){
            return result;
        }
        synchronized (this){
            if(result==null){
                if(timeout<0){
                    this.wait();
                }else{
                    this.wait(unit.toMillis(timeout));
                }
            }
        }
        return result;
    }
}