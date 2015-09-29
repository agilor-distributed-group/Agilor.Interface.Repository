package agilor.distributed.storage.inter.client;

import agilor.distributed.storage.inter.thrift.Agilor;
import agilor.distributed.storage.inter.thrift.TAGNODE;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LQ on 2015/7/27.
 * thrift客户端，用于接口语言转换C-->Java
 * 包含所有ACI接口
 * 无法自动new创建，需调用alloc分配
 */
public class IClient extends Agilor.Client  {
    private TTransport transport=null;
    private TProtocol protocol=null;

    private boolean isOpen=false;

    private TAddress key;
    private TSocket value;


    private final static ConnectionProvider provider = new KeyGenericConnectionProvider();
    private final static Map<String,TAddress> addressMap = new ConcurrentHashMap<>();

    /**
     * 分配一个与服务器的连接
     * @return
     * @throws TTransportException
     */
    public  static IClient alloc(String address,int port,int timeOut) throws Exception {


        String key = address+":"+port;
        TAddress addr = addressMap.get(key);
        if(addr!=null) {   addr.setTimeOut(timeOut);      }
        else {

            addr = new TAddress();
            addr.setAddress(address);
            addr.setPort(port);
            addr.setTimeOut(timeOut);
            addressMap.put(key, addr);
        }

        TSocket socket = provider.getConnection(addr);


        IClient client = new IClient(socket);
        client.key = addr;
        client.value=socket;
        return client;
    }

    public static IClient alloc(String address,int port) throws Exception {
        return alloc(address, port, 2000);
    }


    private IClient(TSocket socket) {
        super(new TBinaryProtocol(socket));

        transport = this.getInputProtocol().getTransport();
        protocol = this.getInputProtocol();
    }






    private IClient(String address, int port, int timeOut) {
        super(new TBinaryProtocol(
                new TSocket(address, port, timeOut)
        ));


        transport = this.getInputProtocol().getTransport();
        protocol = this.getInputProtocol();

    }


    private static StringBuffer parseTags(String[] tagNames)
    {
        StringBuffer buffer = new StringBuffer();
        for (String tagName : tagNames) {
            byte[] src = tagName.getBytes();
            byte[] dest = new byte[64];
            System.arraycopy(src, 0, dest, 0, Math.min(src.length, dest.length - 1));
            dest[dest.length - 1] = 0;
            buffer.append(new String(dest));
        }
        return buffer;
    }


    private void open0() throws TTransportException {
        if (!value.isOpen())
            value.open();
    }


    private void close0()
    {
        if(transport.isOpen()) transport.close();
    }

    /**
     * 打开连接,必须主动关闭
     * @throws TTransportException
     */
    public void open() throws TTransportException {
        open0();
        this.isOpen=true;

    }

    /**
     * 关闭连接，打开后必须主动关闭
     */
    public void close() throws Exception {

        provider.returnConnection(key,value);
        this.isOpen=false;

    }

    public boolean isOpen()
    {
        return this.isOpen;
    }



    public long QuerySnapshots(String[] tagNames) throws TException {

        StringBuffer buffer = parseTags(tagNames);

        return super.QuerySnapshots(buffer.toString(), tagNames.length);

    }


    public int QueryTagHistory(String tagName,int start,int end,int step) throws TException {
        return super.QueryTagHistory(tagName, start, end, step);
    }












}
