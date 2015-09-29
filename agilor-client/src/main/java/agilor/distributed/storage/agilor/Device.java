package agilor.distributed.storage.agilor;

import java.util.function.Predicate;

/**
 * Created by LQ on 2015/7/30.
 * 设备类
 */
public class Device {
    private String name;


    public Device() {
    }

    /**
     * 获取点集合
     * @return
     */
    public TargetCollection getTargets()
    {
        System.out.println("getTargets...");
        return new TargetCollection();
    }

    /**
     * 获取满足指定条件的点
     * @param p
     * @return
     */
    public TargetCollection getTargets(Predicate<Target> p) {
        System.out.println("getTargets by p");
        if(true) return new TargetCollection();
        TargetCollection collection = getTargets().stream().filter(p).collect(TargetCollection::new, TargetCollection::add, TargetCollection::addAll);
        return collection;
    }


    /**
     * 获取满足条件的第一个点
     * @param p
     * @return
     */
    public Target getTarget(Predicate<Target> p)
    {
        System.out.println("getTarget...");
        return new Target();
    }


    /**
     * 增加点
     * @param target
     */
    public void addTarget(Target target) {
        System.out.println("addTarget...");
    }

    /**
     * 修改此设备,(如设备不存在于数据库，会抛出异常，异常暂未添加)

     */
    public void update() {
        System.out.println("update...");
    }

    /**
     * 删除点
     * @param target
     */
    public void deleteTarget(Target target) {
        System.out.println("deleteTarget...");
    }

    /**
     * 获取名称（其他属性暂未添加,同name）
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称(其他属性暂未添加,同name)
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
