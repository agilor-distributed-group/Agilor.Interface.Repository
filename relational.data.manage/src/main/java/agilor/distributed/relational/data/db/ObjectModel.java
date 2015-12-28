package agilor.distributed.relational.data.db;

import com.jfinal.plugin.activerecord.Model;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by LQ on 2015/12/23.
 */
public class ObjectModel<M extends Model> extends Model<M> {

    private String buildMethodName(String prefix,String name) {
        byte[] items = name.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(prefix + items);
    }

    public <T>  T build(Class cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        T _instance = (T) cls.newInstance();



        Map<String,Object> attrs = getAttrs();

        for(Map.Entry<String,Object> it:attrs.entrySet()) {
            Method method = cls.getMethod(buildMethodName("set", it.getKey()));
            if (method != null)
                method.invoke(_instance, it.getValue());
        }

        return _instance;
    }


}
