package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.client.IClient;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by LQ on 2015/8/10.
 * Agilor主类，用于操作设备，以及部分基础类（未完成）
 * 点订阅，及操作传感器功能暂未添加
 * 异常类型暂未添加
 */
public class Agilor {

    private String address;
    private int port;
    private long timeOut;

    private IClient client = null;



    public Agilor(String address, int port, int timeOut) throws Exception {
        this.address = address;
        this.port = port;
        this.timeOut = timeOut;

        client = IClient.alloc(address, port, timeOut);

    }


    public void open() throws TTransportException {
        if (client != null) client.open();
    }

    public void close() throws Exception {
        if (client != null && client.isOpen()) client.close();
    }

    public void ping() throws TException {
        client.ping();
    }


    /**
     * 插入设备
     *
     * @param device 设备
     * @return
     * @throws TException
     */
    public void insert(Device device) throws TException {
        System.out.println("trigger insert " + device.getName());

        client.AddDevice(AParse.parse(device));


    }

    /**
     * 删除一个设备
     *
     * @param deviceName
     * @return
     * @throws Exception
     */
    public void delete(String deviceName) throws Exception {

        client.DeleteDevice(deviceName);
    }

    /**
     * 获取设备集合的初始指针
     *
     * @return
     * @throws TException
     */
    public DeviceIterator device() throws TException {
        return new DeviceIterator(this);
    }


    /**
     * 获取设备集合
     *
     * @return
     * @throws TException
     */
    public List<Device> devices() throws TException, NoSuchFieldException, IllegalAccessException {
        Iterator<Device> it = device();

        try {
            List<Device> result = new ArrayList<>();

            while (it.hasNext()) {
                result.add(it.next());
            }

            return result;
        } finally {
            it.close();
        }
    }

    /**
     * 获取符合条件的第一个设备
     *
     * @param p 条件
     * @return
     * @throws TException
     */
    public Device first(Predicate<Device> p) throws TException, NoSuchFieldException, IllegalAccessException {
        Iterator<Device> it = device();

        try {

            while (it.hasNext()) {
                Device d = it.next();
                if (p.test(d)) return d;
            }

            return null;
        } finally {
            it.close();
        }
    }

    public Device first() throws TException, NoSuchFieldException, IllegalAccessException {
        Iterator<Device> it = device();
        try {
            if (it.hasNext()) return it.next();
            else return null;
        } finally {
            it.close();
        }
    }

    /**
     * 获取符合条件的所有设备
     *
     * @param p
     * @return
     * @throws TException
     */
    public List<Device> devices(Predicate<Device> p) throws TException, NoSuchFieldException, IllegalAccessException {
        Iterator<Device> it = device();

        try {
            List<Device> result = new ArrayList<>();

            while (it.hasNext()) {
                Device device = it.next();
                if (p.test(device)) {
                    result.add(device);
                }
            }
            return result;
        } finally {
            it.close();
        }
    }


    public IClient getClient() {
        return client;
    }


    public <T extends AWidgetImpl> void attach(T t) throws IllegalAccessException, NoSuchFieldException {


        for (Class<?> c = t.getClass(); c != Object.class; c = c.getSuperclass()) {
            if (c == AWidget.class) {
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType().isAssignableFrom(Agilor.class)) {
                        field.set(t, this);
                    }
                }
            }
        }
    }

}