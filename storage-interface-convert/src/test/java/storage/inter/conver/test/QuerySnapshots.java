package storage.inter.conver.test;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.TAGVAL;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LQ on 2015/7/31.
 */
public class QuerySnapshots {

    IClient client = null;
    @Before
    public void before() throws TTransportException {
        //client = IClient.alloc();
        client.open();
    }
    @After
    public void after() throws Exception {
        client.close();

    }

    @Test
    public void test01() throws TException {
        //client.GetNextTagValue()

        long r = client.QuerySnapshots(new String[]{"test"});
        System.out.println(r);
    }


    @Test
    public void test02() throws TException {
        long r = client.QuerySnapshots( new String[]{"test"});
        System.out.println(r);

        //TAGVAL val = client.GetNextTagValue(r, false);
        //System.out.println(val.getName());
    }
}
