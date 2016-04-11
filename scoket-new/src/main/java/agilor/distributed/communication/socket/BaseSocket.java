package agilor.distributed.communication.socket;

import agilor.distributed.communication.utils.Constant;
import agilor.distributed.communication.utils.ConvertUtils;
import agilor.distributed.communication.utils.DQBuffer;
import agilor.distributed.communication.utils.LinkedBytes;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by xinlongli on 16/4/6.
 */
public class BaseSocket {
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
                            if(ConvertUtils.toInt(buffer, iPos + 1)<=praseLen-iPos-Constant.COMM_HEAD_LEN){
                                ResultFuture tmp=queue.getData(-1);
                                if(tmp.getPackageSize()>0){
                                    tmp.parseData(new LinkedBytes(buffer),
                                            iPos+Constant.COMM_HEAD_LEN,tmp.getPackageSize());
                                    iPos+=(tmp.getPackageSize()+Constant.COMM_HEAD_LEN);
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
