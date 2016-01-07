package agilor.distributed.relational.data.context;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.omg.CORBA.DATA_CONVERSION;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by LQ on 2015/12/25.
 */
public class ZkSessionCnxn implements SessionCnxn {


    private final static String SESSION_ID="DISTRIBUTED_SESSION_FINAL";

    private static Map<String,SessionMetaData> base = new ConcurrentHashMap<>();

    private static LinkedBlockingQueue<ZkExcData> zkExcQueue = new LinkedBlockingQueue<>();

    private static ZooKeeper zk = null;
    private static Thread zkExcThread = null;

    private static String BASE_PATH = null;


    private String SESSION_KEY = null;





    static {
        try {

            zk = new ZooKeeper(Config.zookeeper.getAddress(), Config.zookeeper.getTimeout(), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });

            //创建session保存节点
            String path = Config.zookeeper.getDataPath();

            String tmp = "";

            for (String i : path.split("/")) {
                if (StringUtils.isEmpty(i))
                    continue;
                tmp += "/" + i;
                if (zk.exists(tmp, false) == null)
                    zk.create(tmp, tmp.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            BASE_PATH=path+"/session";

            //创建用户session节点
            if (zk.exists(BASE_PATH, false) == null)
                zk.create(BASE_PATH, BASE_PATH.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


            //启动一个session 操作线程
            zkExcThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (Config.isRuning()) {
                        try {
                            ZkExcData data = zkExcQueue.poll(3000, TimeUnit.MILLISECONDS);
                            if (data == null)
                                continue;

                            if (data.getAction() == ZkExcData.UPDATE)
                                zk.setData(data.getPath(), data.getData().serialize(), 0);
                            else if (data.getAction() == ZkExcData.DELETE)
                                zk.delete(data.getPath(), 0);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            zkExcThread.start();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    private IConnection connection = null;


    public ZkSessionCnxn(IConnection connection) {
        this.connection = connection;

        SESSION_KEY = connection.attr(SESSION_ID);

    }


    @Override
    public String create() throws KeeperException, InterruptedException, IOException {

        SessionMetaData root = new SessionMetaData(get_address_str(connection).getBytes());

        SESSION_KEY = root.getKey();
        String path = zk.create(getSessionRoot(), root.serialize(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


        if (!StringUtils.isEmpty(path)) {
            base.put(root.getKey(), root);
        }

        return SESSION_KEY;
    }

    @Override
    public boolean isValid() throws KeeperException, InterruptedException, IOException, ClassNotFoundException {


        if (StringUtils.isEmpty(SESSION_KEY)) return false;

        SessionMetaData root = null;


        if (!base.containsKey(SESSION_KEY) || !(root = base.get(SESSION_KEY)).isValid()) {
            byte[] data = zk.getData(getSessionRoot(), false, null);
            if (data != null) {
                root = SessionMetaData.deserialize(data);
                if (root.isValid()) {
                    update0(root);
                    base.put(root.getKey(), root);
                    return true;
                } else {
                    delete0(root.getKey());
                    base.remove(root.getKey());
                    SESSION_KEY = null;
                    return false;
                }
            }
        }


        Host host = new Host(new String((byte[]) root.value));


        return (host.getAddress() == connection.getHost() && host.getPort() == connection.getPort());
    }

    @Override
    public void setSession(String key, Object value) throws KeeperException, InterruptedException, IOException, ClassNotFoundException {

        if (!isValid())
            create();


        String path = getSessionRoot()+"/"+key;

        if (zk.exists(path, false) == null)
            zk.create(path, new SessionMetaData(key,value).serialize(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        else
            zk.setData(path, new SessionMetaData(key,value).serialize(), 0);
    }

    @Override
    public Object getSession(String key) throws KeeperException, InterruptedException, IOException, ClassNotFoundException {
        String path = getSessionRoot() + "/" + key;
        byte[] data = zk.getData(path, false, null);
        if(data!=null)
            return SessionMetaData.deserialize(data).value;

        return null;
    }

    @Override
    public void removeSession(String key) throws Exception {
        delete0(key);
    }


    private static String get_address_str(IConnection conn)
    {
        return conn.getHost()+":"+conn.getPort();
    }




    private void update0(SessionMetaData meta) throws InterruptedException {
        meta.setLastAccessTime(System.currentTimeMillis());

        if (StringUtils.equals(SESSION_KEY, meta.getKey()))
            zkExcQueue.put(new ZkExcData(ZkExcData.UPDATE, getSessionRoot(), meta));
        else
            zkExcQueue.put(new ZkExcData(ZkExcData.UPDATE, getSessionRoot() + "/" + meta.getKey(), meta));
    }


    private void delete0(String name) throws InterruptedException {

        if (StringUtils.equals(name,SESSION_KEY))
            zkExcQueue.put(new ZkExcData(ZkExcData.DELETE, getSessionRoot(), null));
        else
            zkExcQueue.put(new ZkExcData(ZkExcData.DELETE, getSessionRoot() + "/" + name, null));
    }


    private String getSessionRoot()
    {
        return BASE_PATH+"/"+SESSION_KEY;
    }


    public void update() throws InterruptedException, ClassNotFoundException, KeeperException, IOException {
        if(isValid()) {
            SessionMetaData data = base.get(SESSION_KEY);
            update0(data);
        }
    }


















}
