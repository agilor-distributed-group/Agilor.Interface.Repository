package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.thrift.TAGVAL;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

import static org.junit.Assert.*;

/**
 * Created by LQ on 2015/8/27.
 */
public class TargetTest {
    public Agilor agilor=null;
    public Target target =null;

    @Before
    public void setUp() throws Exception {
        agilor = new Agilor("127.0.0.1",9090,2000);
        agilor.open();
        //Device device = agilor.first();


       // target = device.fisrt(x->true);

    }

    @Test
    public void testHistory() throws Exception {
        //Val  target.history(Calendar.getInstance());




        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 7, 30, 16, 21, 16);

        Val val = target.history(calendar);
        System.out.println(val.toString());

    }

    @Test
    public void testHistories() throws Exception {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.set(2015,7,30,16,21,20);
        start.set(2015, 7, 30, 16, 21, 15);
        List<Val> list = target.histories(start, end);


        for(Val v:list)
        {
            System.out.println(v.toString());
        }

        //target.histories()

    }

    @Test
    public void testHistories1() throws Exception {

    }

    //此方法未实现
    @Test
    public void testSave() throws Exception {


        TTransport transport = new TSocket("127.0.0.1",9090);
        TProtocol protocol = new TBinaryProtocol(transport);
        agilor.distributed.storage.inter.thrift.Agilor.Client client = new agilor.distributed.storage.inter.thrift.Agilor.Client(protocol);
        transport.open();

        long start = Calendar.getInstance().getTimeInMillis();
        for(int i=0;i<10000;i++) {
            client.ping();
            //agilor.getClient().ping();
        }


        long end = Calendar.getInstance().getTimeInMillis();

        System.out.println(end-start);

        transport.close();
    }

    @Test
    public void testWrite() throws Exception {

        target.write(new Val(145f));

    }


    @Test
    public void write_lots_value() throws TException, InterruptedException {


        Random random = new Random();

        int begin = (int)(Calendar.getInstance().getTimeInMillis()/1000);

        for(int i=0;i<100000;i++)
        {
            //Float v = random.nextFloat()*1000;

            TAGVAL VAL = new TAGVAL();
            VAL.name=target.getName();
            VAL.setTimestamp(begin+i);
            VAL.device = target.getDeviceName();
            VAL.value = new Integer(i).toString();
            VAL.type='R';
            //System.out.println(v);
            agilor.getClient().SetValue(VAL);

        }
    }
}