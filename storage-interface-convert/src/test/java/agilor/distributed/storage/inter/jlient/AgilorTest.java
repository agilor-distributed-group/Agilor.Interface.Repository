package agilor.distributed.storage.inter.jlient;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

/**
 * Created by LQ on 2015/8/16.
 */
public class AgilorTest {

    public Agilor agilor = null;


    @Before
    public void before() throws Exception {
        agilor = new Agilor("127.0.0.1",9090,2000);
        agilor.open();
    }


    //通过
    @Test
    public void testInsert() throws Exception {


        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        agilor.open();

        for(int i =0;i<30;i++)
        {
            Device device = new Device("d");
            device.setName("DEVICE_"+i);
            agilor.insert(device);
        }
        agilor.close();

    }

    //通过
    @Test
    public void testDelete() throws Exception {
        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        agilor.open();
        for(int i=0;i<50;i++)
        {
            agilor.delete("DEVICE_"+i);
        }

        agilor.close();



        agilor.delete("DEVICETEST");
    }

    @Test
    public void testDevice() throws Exception {
        DeviceIterator it = agilor.device();
        while (it.hasNext())
        {
            Device d = it.next();
            System.out.println(d.getName());
        }
        it.close();


    }


    @Test
    public void testDevices() throws Exception {


        List<Device> list = agilor.devices();

        System.out.println(list.size());

    }

    @Test
    public void testFirst() throws Exception {
        Device device1 = agilor.first(x -> x.getName().contains("123"));
        org.junit.Assert.assertEquals(device1, null);

        Device device2 = agilor.first(x -> x.getName().contains("DEVICE"));

        org.junit.Assert.assertNotEquals(device2, null);
    }


    @Test
    public void  testConnectionPoolBase() throws Exception {
        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        agilor.open();
        agilor.getClient().ping();
        agilor.close();
    }

    @Test
    public void testConnectionPool_notClose() throws Exception {
        for(int i=0;i<10;i++)
        {
            System.out.println("the test:"+i);
            Agilor agilor = new Agilor("127.0.0.1",9090,2000);
            agilor.open();
        }
    }

    @Test
    public void testConnectionPool_close() throws Exception {
        for(int i=0;i<10;i++)
        {
            System.out.println("the test:"+i);
            Agilor agilor = new Agilor("127.0.0.1",9090,2000);
            agilor.open();
            agilor.close();
        }
    }

    @Test
    public void testConnectionPool_someClose() throws Exception {
        for(int i=0;i<10;i++)
        {
            System.out.println("the test:" + i);
            Agilor agilor = new Agilor("127.0.0.1",9090,2000);
            agilor.open();
            if(i%2==0)
                agilor.close();
        }
    }


    @Test
    public void testConnectionPool_thread() throws Exception {
        Random random = new Random();

        for(int i=0;i<10;i++)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Random random = new Random();
                        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
                        System.out.println("thread");
                        agilor.open();
                        //Thread.sleep(new Random().n);
                        Thread.sleep(5000);
                        agilor.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        Thread.sleep(60000);
    }






}