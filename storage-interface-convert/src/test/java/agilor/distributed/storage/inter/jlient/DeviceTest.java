package agilor.distributed.storage.inter.jlient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LQ on 2015/8/26.
 */
public class DeviceTest {

    @Test
    public void testTargetCount() throws Exception {

        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        Device device = agilor.first(x->x.getName().contains("abc"));

        System.out.println(device.targetCount());


    }

    @Test
    public void testTarget() throws Exception {
        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        Device device = agilor.first();
        Iterator<Target> it = device.target();
        if(it.hasNext()) {
            Target target = it.next();
            System.out.println(target.getName());
        }
        it.close();
    }

    @Test
    public void testTargets() throws Exception {
        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        Device device = agilor.first();
        Iterator<Target> it = device.target();

        while (it.hasNext())
        {
            Target target = it.next();
            System.out.println(target.getName());
        }
        it.close();

    }

    @Test
    public void testTargets1() throws Exception {

    }

    @Test
    public void testFisrt() throws Exception {

        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        Device device = agilor.first();
        Target target = device.fisrt(x->true);
        System.out.println(target.getName());
    }

    @Test
    public void testRemove() throws Exception {
        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        Device device = agilor.first();
        Target target = device.fisrt(x->x.getName().contains("CSD_001"));
        device.remove(target);
    }

    @Test
    public void testInsert() throws Exception {


        Agilor agilor = new Agilor("127.0.0.1",9090,2000);
        agilor.open();

        for(int i=0;i<3;i++)
        {
            Device device = new Device("DEVICE_"+i);
            agilor.attach(device);

            for(int j=0;j<400;j++) {
                Target target = new Target("device_" + i + "target_" + j, ValType.FLOAT);
                device.insert(target);
            }
        }

        agilor.ping();
        agilor.close();

    }
}