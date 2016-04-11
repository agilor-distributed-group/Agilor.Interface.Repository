package agilor.distributed.communication.utils;

import agilor.distributed.communication.socket.ResultFuture;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by xinlongli on 16/4/1.
 * put can only be consumed in one thread,
 * get can only be produced in the other thread
 */
public class DQBuffer {
    final private int elementLen;
    final private int bufferMaxLen;
    Object swapLock;
    Queue<ResultFuture> consumerQue;
    Queue<ResultFuture> producerQue;
    Queue<Queue<ResultFuture>> bufferQueue;
    public DQBuffer(int eleLen, int maxLen){
        elementLen =eleLen;
        bufferMaxLen =maxLen;
        consumerQue =new ArrayDeque<>();
        producerQue=new ArrayDeque<>();
        bufferQueue=new ArrayDeque<Queue<ResultFuture>>();
        swapLock=new Object();
    }

    public boolean putData(ResultFuture element){
        synchronized (producerQue){
            //buffer full
            if(bufferQueue.size()> bufferMaxLen){
                return false;
            }
            if(producerQue.size()> elementLen){
                synchronized (bufferQueue){
                    bufferQueue.add(producerQue);
                    bufferQueue.notifyAll();
                }
                producerQue=new ArrayDeque<>();
            }
            producerQue.add(element);
            return true;
        }
    }

    public boolean flush(){
        synchronized (producerQue){
            synchronized (bufferQueue){
                bufferQueue.add(producerQue);
            }
            producerQue=new ArrayDeque<>();
        }
        return true;
    }

    public ResultFuture getData(int timeout) throws InterruptedException{
        if(consumerQue.size()==0){
            if(bufferQueue.size()==0){
                synchronized (bufferQueue){
                    if(bufferQueue.size()==0) {
                        if (timeout > 0) {
                            bufferQueue.wait(timeout);
                        } else {
                            bufferQueue.wait();
                        }
                    }
                    if(bufferQueue.size()==0){
                        return null;
                    }
                }
            }
            synchronized (bufferQueue){
                consumerQue=bufferQueue.poll();
            }
        }
        return consumerQue.poll();
    }

}
