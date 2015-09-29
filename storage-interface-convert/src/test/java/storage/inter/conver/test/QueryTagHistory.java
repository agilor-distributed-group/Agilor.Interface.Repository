package storage.inter.conver.test;

import agilor.distributed.storage.inter.client.IClient;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LQ on 2015/7/31.
 */
public class QueryTagHistory {
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
    public void test01()
    {
        //不用测试，肯定能用
        //client.QueryTagHistory("AgilorTest.test",)

    }

}
