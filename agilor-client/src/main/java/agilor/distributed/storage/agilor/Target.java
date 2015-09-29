package agilor.distributed.storage.agilor;

/**
 * Created by LQ on 2015/7/30.
 */
public class Target {
    /**
     * 订阅点
     * @param watcher 处理函数
     */
    public void watch(Watcher watcher){}


    private int id;
    private Val value;
    private String name;



    public Val getValue() {
        return value;
    }

    public void setValue(Val value) {
        this.value = value;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
