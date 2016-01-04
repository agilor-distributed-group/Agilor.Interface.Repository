package agilor.distributed.relational.data.db;

import agilor.distributed.relational.data.entities.DeviceType;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.spec.ECField;
import java.util.Map;

/**
 * Created by LQ on 2015/12/23.
 */
public class ExtendModel<M extends Model> extends Model<M> {

    private static String buildMethodName(String prefix, String name) {
        byte[] items = name.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return prefix + (new String(items));
    }

    private static Method getMethod(Class cls, String name, Class... parameterTypes) {
        try {
            return cls.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public <T> T build(Class cls) throws IllegalAccessException, InstantiationException {
        T _instance = (T) cls.newInstance();


        Map<String, Object> attrs = getAttrs();


        for (Map.Entry<String, Object> it : attrs.entrySet()) {

            try {

                Field field = cls.getDeclaredField(it.getKey());

                Method method = getMethod(cls, buildMethodName("set", it.getKey()), field.getType());

                if (method != null) {
                    Object val = it.getValue();
                    if (field.getType().isEnum()) {
                        Method static_method = getMethod(field.getType(), "value", int.class);
                        if (static_method != null) {
                            val = static_method.invoke(null, Integer.parseInt(it.getValue().toString()));
                        } else {
                            static_method = getMethod(field.getType(), "value", byte.class);
                            if (static_method != null) {
                                val = static_method.invoke(null, Byte.parseByte(it.getValue().toString()));
                            }
                        }
                    }
                    method.invoke(_instance, val);
                }
            } catch (NoSuchFieldException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _instance;
    }

//
//    @Override
//    public M findById(Object idValue) {
//
//        M model = CacheKit.get(this.getClass().getName(), idValue);
//        return model!=null?model:super.findById(idValue);
//    }
}
