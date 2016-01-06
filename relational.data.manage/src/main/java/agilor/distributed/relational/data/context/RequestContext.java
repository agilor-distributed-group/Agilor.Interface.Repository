package agilor.distributed.relational.data.context;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by LQ on 2015/12/24.
 */
public class RequestContext implements Context {


    private IConnection connection;

    private SessionCnxn ssCnxn = null;








    public RequestContext(IConnection conn) throws Exception {

        this.connection = conn;

        ssCnxn = new ZkSessionCnxn(conn);

        //第一次请求 添加key
        if (!ssCnxn.isValid()) {
            ssCnxn.create();
        }
    }

    public User user()
    {
        return user();
    }

    @Override
    public Object getSession(String key) throws Exception {
        return ssCnxn.getSession(key);
    }

    @Override
    public void setSession(String key, Object value) throws Exception {
        ssCnxn.setSession(key,value);
    }









}
