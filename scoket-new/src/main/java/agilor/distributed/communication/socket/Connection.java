package agilor.distributed.communication.socket;

import agilor.distributed.communication.protocol.Protocol;
import agilor.distributed.communication.utils.ConvertUtils;

import javax.xml.soap.SOAPConnection;
import java.io.*;
import java.net.Socket;


/**
 * Created by LQ on 2015/10/19.
 */
public class Connection {





    private final static SocketProvider provider = new KeyGenericSocketProvider();


    protected Socket socket = null;




    protected DataInputStream  in = null;
    protected DataOutputStream os = null;





    private String ip;
    private int port;
    private int timeout;


    private Protocol protocol=null;

    private Object lock= new Object();

    public static long timm_all =0;






    public Connection(String ip,int port,int timeout,Protocol protocol)
    {
        this.ip=ip;
        this.port = port;
        this.timeout=timeout;

        this.protocol=protocol;
    }




    public   byte[] write(byte[] data)
    {

        synchronized(lock) {
            try {

                os.write(data);
                os.flush();

                in.readByte();



                int length = ConvertUtils.toInt(in.readByte(),in.readByte(),in.readByte(),in.readByte());

                byte[] result = new byte[length];

                int position = 0;
                while (position < length) {
                    byte[] tmp = new byte[length - position];
                    int len = in.read(tmp);
                    System.arraycopy(tmp, 0, result, position, len);
                    position += len;
                }


                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    /***
     * 新的wirte函数，不定长参数，效率比旧的慢(300-)ms/10000000
     * 如果非性能瓶颈，建议使用这个
     * @param head
     * @param objects
     * @return
     * @throws Exception
     */
    public  byte[] write2(byte[] head,Object... objects) throws Exception {
         byte[][] d0 = new byte[objects.length][];


        int len = 0;

        for(int i=0;i<objects.length;i++) {
            d0[i] = protocol.resolve(objects[i]);
            len += d0[i].length;
        }
        byte[] data = new byte[len+(head==null?0:head.length)+4];

        int position=0;

        if(head!=null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }


        data[position++] = (byte)(len&0xff);
        data[position++] = (byte)((len>>8)&0xff);
        data[position++] = (byte)((len>>16)&0xff);
        data[position++] = (byte)((len>>24)&0xff);


        for(int i=0;i<d0.length;i++) {
            System.arraycopy(d0[i], 0, data, position, d0[i].length);
            position += d0[i].length;
        }


        return write(data);
    }







    @Deprecated
    public <TRESULT,T0> byte[] write(byte[] head, T0 t0) throws Exception {

        byte[] d0 = protocol.resolve(t0);


        int len = d0==null?0:d0.length;

        byte[] data = new byte[len+(head==null?0:head.length)+4];

        int position=0;
        if(head!=null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }

        data[position++] = (byte)(len&0xff);
        data[position++] = (byte)((len>>8)&0xff);
        data[position++] = (byte)((len>>16)&0xff);
        data[position++] = (byte)((len>>24)&0xff);


        if(d0!=null) System.arraycopy(d0,0,data,position,d0.length);


        return write(data);
    }
    @Deprecated
    public <TRESULT,T0,T1> byte[] write(byte[] head,T0 t0,T1 t1) throws Exception {
        byte[] d0 = protocol.resolve(t0);
        byte[] d1 = protocol.resolve(t1);



        int len = d0.length+d1.length;

        byte[] data = new byte[len+(head==null?0:head.length)+4];

        int position=0;
        if(head!=null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }

        data[position++] = (byte)(len&0xff);
        data[position++] = (byte)((len>>8)&0xff);
        data[position++] = (byte)((len>>16)&0xff);
        data[position++] = (byte)((len>>24)&0xff);


        System.arraycopy(d0,0,data,position,d0.length);
        position+=d0.length;
        System.arraycopy(d1,0,data,position,d1.length);



        return write(data);
    }

    @Deprecated
    public <TRESULT,T0,T1,T2> byte[] write(byte[] head,T0 t0,T1 t1,T2 t2) throws Exception {

        byte[] d0 = protocol.resolve(t0);
        byte[] d1 = protocol.resolve(t1);
        byte[] d2 = protocol.resolve(t2);


        int len = d0.length + d1.length + d2.length;

        byte[] data = new byte[len + (head == null ? 0 : head.length) + 4];

        int position = 0;
        if (head != null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }

        data[position++] = (byte) (len & 0xff);
        data[position++] = (byte) ((len >> 8) & 0xff);
        data[position++] = (byte) ((len >> 16) & 0xff);
        data[position++] = (byte) ((len >> 24) & 0xff);


        System.arraycopy(d0, 0, data, position, d0.length);
        position += d0.length;
        System.arraycopy(d1, 0, data, position, d1.length);
        position += d1.length;
        System.arraycopy(d2, 0, data, position, d2.length);



        return write(data);


    }

    @Deprecated
    public <TRESULT,T0,T1,T2,T3> byte[] write(byte[] head,T0 t0,T1 t1,T2 t2,T3 t3) throws Exception {
        byte[] d0 = protocol.resolve(t0);
        byte[] d1 = protocol.resolve(t1);
        byte[] d2 = protocol.resolve(t2);
        byte[] d3 = protocol.resolve(t3);


        int len = d0.length+d1.length+d2.length+d3.length;

        byte[] data = new byte[len+(head==null?0:head.length)+4];

        int position=0;
        if(head!=null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }

        data[position++] = (byte)(len&0xff);
        data[position++] = (byte)((len>>8)&0xff);
        data[position++] = (byte)((len>>16)&0xff);
        data[position++] = (byte)((len>>24)&0xff);


        System.arraycopy(d0,0,data,position,d0.length);
        position+=d0.length;
        System.arraycopy(d1,0,data,position,d1.length);
        position+=d1.length;
        System.arraycopy(d2,0,data,position,d2.length);
        position+=d2.length;
        System.arraycopy(d3,0,data,position,d3.length);




        return write(data);
    }



    @Deprecated
    public <TRESULT,T0,T1,T2,T3,T4> byte[] write(byte[] head,T0 t0,T1 t1,T2 t2,T3 t3,T4 t4) throws Exception {
        byte[] d0 = protocol.resolve(t0);
        byte[] d1 = protocol.resolve(t1);
        byte[] d2 = protocol.resolve(t2);
        byte[] d3 = protocol.resolve(t3);
        byte[] d4 = protocol.resolve(t4);


        int len = d0.length+d1.length+d2.length+d3.length+d4.length;

        byte[] data = new byte[len+(head==null?0:head.length)+4];

        int position=0;
        if(head!=null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }

        data[position++] = (byte)(len&0xff);
        data[position++] = (byte)((len>>8)&0xff);
        data[position++] = (byte)((len>>16)&0xff);
        data[position++] = (byte)((len>>24)&0xff);


        System.arraycopy(d0,0,data,position,d0.length);
        position+=d0.length;
        System.arraycopy(d1,0,data,position,d1.length);
        position+=d1.length;
        System.arraycopy(d2,0,data,position,d2.length);
        position+=d2.length;
        System.arraycopy(d3,0,data,position,d3.length);
        position+=d3.length;
        System.arraycopy(d4,0,data,position,d4.length);



        return write(data);
    }

    @Deprecated
    public <TRESULT,T0,T1,T2,T3,T4,T5> byte[] write(byte[] head,T0 t0,T1 t1,T2 t2,T3 t3,T4 t4,T5 t5) throws Exception {
        byte[] d0 = protocol.resolve(t0);
        byte[] d1 = protocol.resolve(t1);
        byte[] d2 = protocol.resolve(t2);
        byte[] d3 = protocol.resolve(t3);
        byte[] d4 = protocol.resolve(t4);
        byte[] d5 = protocol.resolve(t5);

        int len = d0.length+d1.length+d2.length+d3.length+d4.length+d5.length;

        byte[] data = new byte[len+(head==null?0:head.length)+4];

        int position=0;
        if(head!=null) {
            System.arraycopy(head, 0, data, position, head.length);
            position = head.length;
        }

        data[position++] = (byte)(len&0xff);
        data[position++] = (byte)((len>>8)&0xff);
        data[position++] = (byte)((len>>16)&0xff);
        data[position++] = (byte)((len>>24)&0xff);


        System.arraycopy(d0,0,data,position,d0.length);
        position+=d0.length;
        System.arraycopy(d1,0,data,position,d1.length);
        position+=d1.length;
        System.arraycopy(d2,0,data,position,d2.length);
        position+=d2.length;
        System.arraycopy(d3,0,data,position,d3.length);
        position+=d3.length;
        System.arraycopy(d4,0,data,position,d4.length);
        position+=d4.length;
        System.arraycopy(d5,0,data,position,d5.length);




        return write(data);
    }


    public Protocol getProtocol() {
        return this.protocol;
    }




    public void open() throws Exception {
        if(!isOpen()) {
            socket = provider.getSocket(ip,port);
            socket.setSoTimeout(timeout);
            socket.setSendBufferSize(2048);
            socket.setReceiveBufferSize(2048);

            in = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());

        }

    }
    public void close() throws Exception {
        synchronized (lock) {
            if (isOpen()) {
                os.flush();
                provider.returnSocket(socket);
                socket = null;
            }
        }
    }
    public boolean isOpen(){
        return (socket!=null&&!socket.isClosed());
    }

}
