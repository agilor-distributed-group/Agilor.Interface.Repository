package agilor.distributed.relational.data.context;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by LQ on 2015/12/25.
 */
public class ZkSessionCnxn implements SessionCnxn {


    private   final static String SESSION_ID="DISTRIBUTED_SESSION_FINAL";

    private Map<String,String> base = new ConcurrentHashMap<>();

    private static ZooKeeper zk = null;

    private static LinkedBlockingQueue<ZkExcData> zkExcQueue = new LinkedBlockingQueue<>();

    private static Thread zkExcThread = null;



    private final static byte UPDATE_SESSION=0;
    private final static byte DELETE_SESSION=1;


    //zk异步执行队列






    static {
        Config c = new Config();
        try {

            zk = new ZooKeeper(Config.zookeeper.getAddress(), Config.zookeeper.getTimeout(), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });

            //创建session保存节点
            String path = Config.zookeeper.getDataPath();


            String tmp="";

            for(String i:path.split("/")) {
                tmp += "/" + i;
                if (zk.exists(tmp, false) == null)
                    zk.create(tmp, null, null, CreateMode.PERSISTENT);
            }


            //创建用户session节点
            if(zk.exists(path+"/session",false)==null)
                zk.create(path+"/session",null,null,CreateMode.PERSISTENT);

            //启动一个session 操作线程

            zkExcThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (Config.isRuning())
                    {
                        try {
                            ZkExcData data = zkExcQueue.poll(3000, TimeUnit.MILLISECONDS);
                            if (data == null)
                                continue;

                            if (data.getAction() == ZkExcData.UPDATE)
                                zk.setData(data.getPath(), data.getData().serializable(), 0);
                            else if (data.getAction() == ZkExcData.DELETE)
                                zk.delete(data.getPath(), 0);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    private IConnection connection = null;
    private String base_path = null;

    public ZkSessionCnxn(IConnection connection) {
        this.connection = connection;
        base_path = Config.zookeeper.getDataPath() + "/" + get_address_str(connection);
    }


    @Override
    public String create() throws KeeperException, InterruptedException {

        SessionMetaData s = new SessionMetaData();

        String path = zk.create( base_path , s.serializable(), null, CreateMode.EPHEMERAL);

        if (!StringUtils.isEmpty(path)) {
            base.put(s.getKey(), get_address_str(connection));

        }

        return null;
    }

    @Override
    public boolean isValid() throws KeeperException, InterruptedException {
        String key = connection.attr(SESSION_ID);

        if(!(StringUtils.isEmpty(key)||base.get(key)!=get_address_str(connection))) {

            byte[] data = zk.getData(base_path, false, null);
            if (data != null) {
                SessionMetaData s = new SessionMetaData(data);
                if (s.isValid()) {
                    s.setLastAccessTime(System.currentTimeMillis());
                    zkExcQueue.put(new ZkExcData(ZkExcData.UPDATE, base_path, s));
                    return true;
                } else
                    zkExcQueue.put(new ZkExcData(ZkExcData.DELETE, base_path, null));
            }
        }
        return false;

    }

    @Override
    public void setSession(String key, Object value) throws KeeperException, InterruptedException {
        String path = base_path + "/" + key;

        if (zk.exists(path, false) == null)
            zk.create(path, new SessionMetaData(value).serializable(), null, CreateMode.EPHEMERAL);
        else
            zk.setData(path, new SessionMetaData(value).serializable(), 0);
    }

    @Override
    public byte[] getSession(String key) throws KeeperException, InterruptedException {
        String path = base_path + "/" + key;
        return zk.getData(path, false, null);
    }


    private static String get_address_str(IConnection conn)
    {
        return conn.getHost()+":"+conn.getPort();
    }












}
