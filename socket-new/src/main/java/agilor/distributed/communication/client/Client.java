package agilor.distributed.communication.client;

import agilor.distributed.communication.socket.Connection;
import agilor.distributed.communication.result.ResultFuture;
import agilor.distributed.communication.utils.Constant;

import java.util.Calendar;

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

    public ResultFuture addValue(String tagName,String deviceName,Value value,boolean flush) throws Exception {
        try{
            if(connection!=null)
                return connection.write2(Constant.COMM_ADD_VAL,flush, tagName, deviceName, value);
        }catch(Exception e){
            connection.destory();
        }
        return null;
    }

    public ResultFuture addTarget(String tagName,String deviceName,Value value,boolean flush) throws Exception {
        try{
            if(connection!=null)
                return  connection.write2(Constant.COMM_ADD_TAG,flush, tagName, deviceName, value);
        }catch(Exception e){
            connection.destory();
        }
        return null;
    }

    public ResultFuture getValue(Calendar startTime,Calendar endTime,String tagName) throws Exception {
        int start = (int) (startTime.getTimeInMillis() / 1000);
        int end = (int) (endTime.getTimeInMillis() / 1000);
        try {
            if (connection != null)
                return connection.write2(Constant.COMM_GET_VALUE, true, tagName,start, end );
        }catch(Exception e){
            connection.destory();
        }
        return null;
    }
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
    public ResultFuture getAllTag() throws Exception {
        try {
            if (connection != null)
                return connection.write2(Constant.COMM_GET_ALL_TAG, true);
        }catch(Exception e){
            connection.destory();
        }
        return null;
    }




}
