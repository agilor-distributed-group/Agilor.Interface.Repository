package agilor.distributed.relational.data.context;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * Created by LQ on 2016/1/14.
 */
public class ZkUtils {

    static void delete(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        if (zk.exists(path, false) != null) {
            List<String> child = zk.getChildren(path, false);
            if (child != null) {
                for (String it : child) {
                    //System.out.println(path+"/"+it);
                    delete(zk, path+"/"+it);
                }
            }
            zk.delete(path, -1);
        }
    }
}
