package agilor.distributed.relational.data.context;

import org.apache.zookeeper.KeeperException;

/**
 * Created by LQ on 2015/12/25.
 */
public interface SessionCnxn {
    String create() throws Exception;

    boolean isValid() throws Exception;


    void setSession(String key,Object value) throws Exception;
    Object getSession(String key) throws Exception;
    void removeSession(String key) throws Exception;


    void update() throws Exception;

}
