package agilor.distributed.communication.client;

import agilor.distributed.communication.client.exception.WriteValueException;
import agilor.distributed.communication.protocol.Assemble;
import agilor.distributed.communication.protocol.Protocol;
import agilor.distributed.communication.protocol.SimpleProtocol;
import agilor.distributed.communication.protocol.Token;
import agilor.distributed.communication.socket.Connection;
import agilor.distributed.communication.utils.ConvertUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by LQ on 2015/10/19.
 */
public class Client {
    public static final byte COMM_ADD_TAG=1;
    public static final byte COMM_ADD_VAL=2;
    public static final byte COMM_GET_VALUE =3;


    public static final byte RES_OK=0;
    public static final byte RES_FAIL_COMMON=1;
    public static final byte RES_FAIL_NO_TAG=2;
    public static final byte RES_FAIL_RETURN_EMPTY=3;



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

    public void ping(){
        byte[] data = new byte[1];
        data[0]='A';
        connection.write(data);
    }

    public void addTarget(String tagName,String tageName,Value value) throws Exception {
        byte[] result = connection.write(new byte[]{COMM_ADD_TAG}, tagName, tageName, value);

        if (result != null && result[0] == RES_OK)
            System.out.println("ok");
        else
            System.out.println("fail");
    }

    public void addValue(String tagName,String deviceName,Value value) throws Exception {
        byte[] result = connection.write(new byte[]{COMM_ADD_VAL}, tagName, deviceName, value);

        if (!(result != null && result[0] == RES_OK))
            throw new WriteValueException(deviceName, tagName);
    }


    public ValueCollection getValue(Calendar startTime,Calendar endTime,String tagName) throws Exception {

//        SimpleDateFormat df = new SimpleDateFormat("YYYYMMDDhhmmss");
//        String start = df.format(startTime.getTime());
//        String end = df.format(endTime.getTime());

        int start = (int) (startTime.getTimeInMillis() / 1000);
        int end = (int) (endTime.getTimeInMillis() / 1000);

        byte[] data = connection.write(new byte[]{COMM_GET_VALUE}, start, end, tagName);

        if (data != null && data[0]==RES_OK) {
            Protocol protocol = this.connection.getProtocol();

            Assemble assemble = protocol.assemble(data, 1, data.length - 1);
            Token token = assemble.next();
            if (token != null) {
                return token.toClass(ValueCollection.class);
            }
        }
        else
        System.out.println(data[0]);
        return null;
    }




}
