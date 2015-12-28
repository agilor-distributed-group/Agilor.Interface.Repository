package agilor.distributed.relational.data.context;


import agilor.distributed.relational.data.entities.User;

/**
 * Created by LQ on 2015/12/24.
 */
public interface Context {
    Object getSession(String key) throws Exception;
    void setSession(String key,Object value) throws Exception;
}
