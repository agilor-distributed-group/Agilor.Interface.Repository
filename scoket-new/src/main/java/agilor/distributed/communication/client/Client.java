package agilor.distributed.communication.client;

import agilor.distributed.communication.client.exception.WriteValueException;
import agilor.distributed.communication.protocol.Assemble;
import agilor.distributed.communication.protocol.Protocol;
import agilor.distributed.communication.protocol.Token;
import agilor.distributed.communication.socket.Connection;
import agilor.distributed.communication.socket.ResultFuture;
import agilor.distributed.communication.utils.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by LQ on 2015/10/19.
 */
public class Client {




    private Connection connection = null;
    public Client(Connection connection)
    {
        this.connection = connection;
    }

    public void open() throws Exception {

        connection.open();

    }
    public void close() throws Exception {
        connection.close();
    }
    public boolean  isOpen(){
        return connection.isOpen();
    }


    //具体函数

//    public void ping(){
//        byte[] data = new byte[1];
//        data[0]='A';
//        connection.write(data);
//    }

    public ResultFuture addTarget(String tagName,String tageName,Value value,boolean flush) throws Exception {
        try{
            if(connection!=null)
                return connection.write2(Constant.COMM_ADD_TAG,flush, tagName, tageName, value);
        }catch(Exception e){
            connection.destory();
        }
        return null;
    }

//    public void addValue(String tagName,String deviceName,Value value) throws Exception {
//        byte[] result = connection.write2(new byte[]{Constant.COMM_ADD_VAL},false, tagName, deviceName, value);
//
//        if (!(result != null && result[0] == Constant.RES_OK))
//            throw new WriteValueException(deviceName, tagName);
//    }
//
//
//    public ValueCollection getValue(Calendar startTime,Calendar endTime,String tagName) throws Exception {
//
//
//
//        int start = (int) (startTime.getTimeInMillis() / 1000);
//        int end = (int) (endTime.getTimeInMillis() / 1000);
//
//        byte[] data = connection.write(new byte[]{Constant.COMM_GET_VALUE}, start, end, tagName);
//
//        if (data != null && data[0]==Constant.RES_OK) {
//            Protocol protocol = this.connection.getProtocol();
//
//            Assemble assemble = protocol.assemble(data, 1, data.length - 1);
//            Token token = assemble.next();
//            if (token != null) {
//                return token.toClass(ValueCollection.class);
//            }
//        }
//
//        return null;
//    }
//
//    public TargetCollection getAllTag() throws Exception {
//        byte[] data = connection.write(new byte[]{Constant.COMM_GET_ALL_TAG},null);
//        if(data!=null&&data[0]==Constant.RES_OK)
//        {
//            Assemble assemble = this.connection.getProtocol().assemble(data,1,data.length-1);
//            Token token = assemble.next();
//            if(token!=null)
//                return token.toClass(TargetCollection.class);
//        }
//        return null;
//    }




}
