package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.thrift.*;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by LQ on 2015/8/10.
 * 设备类
 */
public class Device extends AWidget {
    private String name;




    //private final static Device DEFAULT = new Device();


    @Deprecated
    public Device() {
        //client.QueryTagsbyDevice()
    }

    public Device(String name)
    {
        this.name=name;
    }




    /**
     * 点数量
     * @return 点数量
     */
    public long targetCount() throws TException {


        long count = agilor.getClient().TagCountByDevice(this.name);
        return count;
    }


    /**
     * 获取target集合指针
     * @return 点集合初始迭代器
     * @throws TException
     */
    public TargetIterator target() throws TException, NoSuchFieldException, IllegalAccessException {
        return new TargetIterator(this);

        //client.QuerySnapshots()
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取target集合
     * @return 点集合列表
     * @throws TException
     */

    public List<Target> targets() throws TException, NoSuchFieldException, IllegalAccessException {
        List<Target> list = new ArrayList<>();
        Iterator<Target> it = target();

        try {
            while (it.hasNext()) {
                list.add(it.next());
            }
            return list;
        } finally {
            it.close();
        }
    }


    /**
     * 获取符合条件的点集合
     * @param p 指定条件
     * @return 点集合列表
     * @throws TException
     */
    public List<Target> targets(Predicate<Target> p) throws TException, NoSuchFieldException, IllegalAccessException {
        List<Target> list = new ArrayList<>();
        Iterator<Target> it = target();

        while (it.hasNext()) {
            Target t = it.next();
            if (p.test(t))
                list.add(t);
        }
        return list;
    }

    /**
     * 获取符合的第一个点
     * @param p 指定条件
     * @return 点
     * @throws TException
     */
    public Target fisrt(Predicate<Target> p) throws TException, NoSuchFieldException, IllegalAccessException {
        Iterator<Target> it = target();
        try {
            while (it.hasNext()) {
                Target t = it.next();
                if (p.test(t)) return t;
            }
            return null;

        } finally {
            it.close();
        }
    }

    /**
     * 删除点
     * @param target 指定要删除的点
     * @return 是否删除成功
     * @throws TException
     */
    public void remove(Target target) throws TException {

        agilor.getClient().RemoveTag(target.getId());


    }


    /**
     * 增加点
     * @param target 要增加的点
     * @return 是否增加成功
     * @throws TException
     */
    public void insert(Target target) throws TException, NoSuchFieldException, IllegalAccessException {

        target.setDeviceName(this.name);
        TAGNODE node = AParse.parse(target);


        agilor.getClient().AddNewTag(node, false);

        agilor.attach(target);

    }



    public void  attached()
    {

    }



}