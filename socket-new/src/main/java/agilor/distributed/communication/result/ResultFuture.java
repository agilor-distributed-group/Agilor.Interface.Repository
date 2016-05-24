package agilor.distributed.communication.result;

import agilor.distributed.communication.utils.ConvertUtils;
import agilor.distributed.communication.utils.LinkedBytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by xinlongli on 16/3/30.
 */
public class ResultFuture<T> implements Future<T> {
//    boolean parseData(byte[] in,int st,int len) throws IOException;
//    int getPackageSize();


    protected boolean isDone=false;
    public T result=null;
    public int errorCode=0;

    protected  boolean  myParseData(byte[] in,int st,int len) {
        return false;
    }

    public boolean parseData(byte[] in,int st,int len) {
        synchronized(this){
            myParseData(in, st, len);
            isDone=true;
            this.notifyAll();
            return true;
        }
    }

    public int getPackageSize(){
        return -1;
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
    public boolean isDone() {
        return isDone;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if(errorCode!=0){
            return null;
        }

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
