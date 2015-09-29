package storage.inter.conver.test;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.DEVICE;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LQ on 2015/7/31.
 */
public class QueryDeviceInfo {
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
        long r = client.QueryDeviceInfo();
        Assert.assertNotEquals(r, -1);
    }

    @Test
    public void test02() throws TException {
        long r = client.QueryDeviceInfo();
        //DEVICE device = client.EnumDeviceInfo(r);

        //client.EnumDeviceInfo(r);
        //System.out.println(device.getId());
        //System.out.println(device.getName());

        //client.Q

    }

}
