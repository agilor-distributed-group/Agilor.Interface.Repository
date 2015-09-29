package storage.inter.conver.test;

import agilor.distributed.storage.inter.client.IClient;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LQ on 2015/7/30.
 */
public class Ping {
    IClient client = null;

    @Before
    public void before() throws TTransportException {
        //client = IClient.alloc();
        client.open();
    }

    @Test
    public void test01() throws TException {
        client.ping();


    }

    @After
    public void after() throws Exception {
        client.close();
    }

}
