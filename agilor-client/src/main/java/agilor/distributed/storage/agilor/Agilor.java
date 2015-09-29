package agilor.distributed.storage.agilor;

import agilor.distributed.storage.connection.Connection;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by LQ on 2015/7/30.
 */
public class Agilor {
    private Connection connection = null;


    /**
     * 建立连接,不需地址 和端口，自动在master中获取
     */
    public Agilor() throws InterruptedException {
        connection = new Connection();
        System.out.println("connection...");
    }


    /**
     * 流式获取device
     * @return 返回Device类型，无设备则返回Null
     */
    public Device next()
    {
        System.out.println("next...");
        return  new Device();
        //return null;
    }


    /**
     * 获取设备集合
     * @return 设备集合
     */
    public DeviceCollection getDevices() {
        DeviceCollection collection = new DeviceCollection();
        System.out.println("getDevices...");


        if(true) return collection;
        Device device = null;
        while ((device = next()) != null) {
            collection.add(device);
        }
        return collection;
    }

    /**
     * 根据指定条件的设备集合
     * @param p 指定的条件
     * @return 设备集合
     */

    public DeviceCollection getDevices(Predicate<Device> p) {
        DeviceCollection collection = new DeviceCollection();

        System.out.println("getDevices by p");
        if(true) return collection;

        Device device = null;
        while ((device = next()) != null) {
            if (p.test(device))
                collection.add(device);
        }

        //DeviceCollection collection = getDevices().stream().filter(p).collect(DeviceCollection::new, DeviceCollection::add, DeviceCollection::addAll);
        return collection;

    }

    /**
     * 增加设备
     * @param device
     */
    public void addDevice(Device device){
        System.out.println("addDevice...");
    }

    /**
     * 更新设备
     * @param device
     */
    public void updateDevice(Device device){
        System.out.println("updateDevice...");
    }


    /**
     * 删除设备
     * @param device 设备名称
     */
    public void deleteDevice(String device){
        System.out.println("deleteDevice...");
    }


    /**
     * 订阅点
     * @param device 订阅的点所属设备成
     * @param target 订阅的点名称
     * @param watcher 点处理函数
     */
    public static void watch(String device, String target,Watcher watcher) {
        System.out.println("watch...");
    }

    /**
     * 订阅点
     * @param target 点
     * @param watcher 处理函数
     */
    public static void watch(Target target,Watcher watcher)
    {
        System.out.println("watch...");
    }

    /**
     * 取消订阅
     * @param device 取消订阅的点的设备
     * @param target 取消鼎业的点的名称
     */
    public static void unWatch(String device,String target)
    {
        System.out.println("unwatch...");

    }
    public static void unWatch(Target target){
        System.out.println("unwatch...");
    }


}
