package agilor.distributed.relational.data.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LQ on 2015/12/24.
 */
public class RequestContext implements Context {

    private static Logger logger = LoggerFactory.getLogger(RequestContext.class);

    private IConnection connection;

    private SessionCnxn ssCnxn = null;








    public RequestContext(IConnection conn) throws Exception {

        this.connection = conn;
        ssCnxn = new ZkSessionCnxn(conn);


        //第一次请求 添加key
        if (!ssCnxn.isValid()) {
            ssCnxn.create();;
        }

        else {
            ssCnxn.update();
        }
    }

//    public User user()
//    {
//        return user();
//    }


    public IConnection getConnection(){
        return this.connection;
    }


    @Override
    public Object getSession(String key) throws Exception {
        return ssCnxn.getSession(key);
    }

    @Override
    public void setSession(String key, Object value) throws Exception {
        ssCnxn.setSession(key,value);
    }


    public void removeSession(String key) throws Exception {
        ssCnxn.removeSession(key);
    }









}
