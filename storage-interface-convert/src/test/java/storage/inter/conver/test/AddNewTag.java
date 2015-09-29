package storage.inter.conver.test;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.TAGNODE;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LQ on 2015/7/31.
 */
public class AddNewTag {
    IClient client = null;

    @Before
    public void before() throws TTransportException {
        //client = IClient.alloc();
        client.open();
    }

    @Test
    public void test01() throws TException {

        TAGNODE node= new TAGNODE();
        node.setName("addNewTagxx");
        node.setDeviceName("hahah");
        node.setGroupName("hahah");
        client.send_AddNewTag(node,true);



    }

    @After
     public void after() throws Exception {
        client.close();
    }



}
