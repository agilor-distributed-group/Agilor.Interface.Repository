package agilor.distributed.relational.data.context;


/**
 * Created by LQ on 2015/12/24.
 */
public interface Context {
    Object getSession(String key) throws Exception;
    void setSession(String key,Object value) throws Exception;
}
