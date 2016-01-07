package agilor.distributed.communication.socket;

import agilor.distributed.communication.client.Client;
import agilor.distributed.communication.client.Value;
import agilor.distributed.communication.protocol.SimpleProtocol;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by LQ on 2016/1/7.
 */
public class ConnectionTest {


    @Test
    public void writeTest() throws Exception {
        Connection connection = new Connection("", 0, 3000, SimpleProtocol.getInstance());
        byte[] data1 = connection.write2(new byte[]{Client.COMM_ADD_TAG}, "afdb", "dgas");

        byte[] data2 = connection.write(new byte[]{Client.COMM_ADD_TAG}, "afdb", "dgas");

        Assert.assertArrayEquals(data1,data2);
    }


    @Test
    public void writeTest_2() throws Exception {
        Connection connection = new Connection("", 0, 3000, SimpleProtocol.getInstance());

        Value value = new Value(Value.Types.FLOAT);
        value.setFvalue(100.333f);


        int count =10000000;




        long start = System.currentTimeMillis();
        for(int i=0;i<count;i++) {

            connection.write2(new byte[]{Client.COMM_ADD_TAG}, "afdb", value, value, value);

        }

        System.out.println(System.currentTimeMillis() - start);


        start = System.currentTimeMillis();
        for(int i=0;i<count;i++) {
            connection.write2(new byte[]{Client.COMM_ADD_TAG}, "afdb", value, value, value);
        }

        System.out.println(System.currentTimeMillis() - start);


        start = System.currentTimeMillis();
        for(int i=0;i<count;i++) {
            connection.write(new byte[]{Client.COMM_ADD_TAG}, "afdb", value, value, value);

        }
        System.out.println(System.currentTimeMillis() - start);

        //Assert.assertArrayEquals(data1, data2);


    }
}
