import agilor.distributed.storage.connection.Connection;
import agilor.distributed.storage.msg.LoginMsg;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/8/11.
 */
public class ConnTest {

    private List<Connection> list = new ArrayList<>();

    @Test
    public void test01() throws InterruptedException, IOException {
//        for(int i=0;i<500;i++) {
//            Connection conn = new Connection();
//            list.add(conn);
//            //System.out.println();
//        }
//
//
//        while (true)
//        {
//            Thread.sleep(3000);
//        }

//        Socket socket = new Socket(InetAddress.getLocalHost(),9982);
//        InputStream inStream = socket.getInputStream();
//        OutputStream outStream = socket.getOutputStream();
//
//        ObjectMapper mapper  = new ObjectMapper();
//        outStream.write(mapper.writeValueAsBytes(new LoginMsg()));
//
//        outStream.flush();
//
//        DataInputStream dis = new DataInputStream(inStream);
//        String st = dis.readUTF();
//
//        socket.close();
//        System.out.println(st);

        Connection connection = new Connection();
        //connection.open();
        //connection.getChannel().writeAndFlush(888);
        while (true)
        {
            Thread.sleep(2000);
        }




    }


}
