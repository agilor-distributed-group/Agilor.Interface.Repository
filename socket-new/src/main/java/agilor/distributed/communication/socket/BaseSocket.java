package agilor.distributed.communication.socket;

import agilor.distributed.communication.result.ResultFuture;
import agilor.distributed.communication.utils.Constant;
import agilor.distributed.communication.utils.ConvertUtils;
import agilor.distributed.communication.utils.DQBuffer;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by xinlongli on 16/4/6.
 */
public class BaseSocket {
    final int RECV_QUEUE_WAIT_TIMEOUT=10;
    Socket socket=null;
    Thread recvThread=null;
    DQBuffer queue=null;
    private Boolean recvIsRunning;
    protected DataInputStream  in = null;
    protected DataOutputStream os = null;
    public BaseSocket(String ip,int port) throws IOException{
        socket=new Socket(ip,port);
        socket.setReceiveBufferSize(Constant.RECV_BUFFER_SIZE);
        socket.setSendBufferSize(Constant.SEND_BUFFER_SIZE);
        socket.setSoTimeout(1000);
        in=new DataInputStream(socket.getInputStream());
        os=new DataOutputStream(socket.getOutputStream());
        recvIsRunning=false;
        queue=new DQBuffer(Constant.DQBUFFER_ELEMENT_LEN,Constant.DQBUFFER_QUEUE_LEN);
        recvThread=new Thread(()->{
            int iPos=0;
            byte[] buffer=new byte[Constant.RECV_BUFFER_LEN];
            while(recvIsRunning){
                try{
                    int praseLen=in.read(buffer,iPos,Constant.RECV_BUFFER_LEN-iPos);
                    if(praseLen>0){
                        praseLen+=iPos;
                        iPos=0;
                        while(praseLen-iPos>Constant.COMM_HEAD_LEN){
                            ResultFuture tmp=queue.getData(RECV_QUEUE_WAIT_TIMEOUT);
                            while(tmp==null){
                                queue.flush();
                                tmp=queue.getData(RECV_QUEUE_WAIT_TIMEOUT);
                            }
                            switch (buffer[iPos]){
                                case Constant.COMM_ADD_VAL:
                                case Constant.COMM_ADD_TAG:
//                                {
//                                    if(ConvertUtils.toInt(buffer, iPos + 1)<=praseLen-iPos-Constant.COMM_HEAD_LEN){
//                                        //AddValueResultFuture tmpRes=(AddValueResultFuture)tmp;
//                                        if((tmp).getPackageSize()>0){
//                                            (tmp).parseData((buffer),
//                                                    iPos + Constant.COMM_HEAD_LEN, tmp.getPackageSize());
//                                            iPos+=(tmp.getPackageSize()+Constant.COMM_HEAD_LEN);
//                                        }
//                                    }
//                                    break;
//                                }
                                case Constant.COMM_GET_ALL_TAG:
                                case Constant.COMM_GET_VALUE:{
                                    int totalSize=ConvertUtils.toInt(buffer, iPos + 1);
                                    byte[] resBuffer=new byte[totalSize];
                                    iPos+=Constant.COMM_HEAD_LEN;
                                    int resPos=0;
                                    while(totalSize-resPos>praseLen-iPos){
                                        System.arraycopy(buffer,iPos,resBuffer,resPos,praseLen-iPos);
                                        resPos+=praseLen-iPos;
                                        iPos=0;
                                        praseLen=in.read(buffer,iPos,Constant.RECV_BUFFER_LEN-iPos);
                                    }
                                    System.arraycopy(buffer,iPos,resBuffer,resPos,totalSize-resPos);
                                    iPos+=totalSize-resPos;
                                    //System.out.println("packageSize : "+totalSize);
                                    tmp.parseData((resBuffer),
                                            0, totalSize);
                                    break;
                                }
                            }

                        }
                        System.arraycopy(buffer,iPos,buffer,0,praseLen-iPos);
                        iPos=praseLen-iPos;
                    }else{
                        System.out.println("receive error"+praseLen);
                    }

                }catch(SocketTimeoutException e){
                //e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                    try{
                        close(-1);
                    }catch (Exception ce){
                        recvIsRunning=false;
                    }
                }catch (InterruptedException e1){
                    recvIsRunning=false;
                }
            }
            synchronized (recvIsRunning){
                recvIsRunning.notifyAll();
            }
        });
    }

    public boolean isClosed(){
        return socket.isClosed();
    }

    public void startRecv(){
        synchronized (recvIsRunning){
            recvIsRunning=true;
        }
        recvThread.start();
    }

    public void close(int timeOut) throws IOException,InterruptedException{
        socket.close();
        synchronized (recvIsRunning){
            recvIsRunning=false;
            if(timeOut<=0){
                recvIsRunning.wait();
            }else{
                recvIsRunning.wait(timeOut);
            }

        }
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    public String getHostAddress(){
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort(){
        return socket.getPort();
    }


    public void putData(ResultFuture futures,byte[] data,boolean flush)throws InterruptedException,IOException{
        synchronized (this){
            while(!queue.putData(futures))
                Thread.sleep(1);
            os.write(data);
            if(flush){
                os.flush();
                queue.flush();
            }
        }
    }

}
