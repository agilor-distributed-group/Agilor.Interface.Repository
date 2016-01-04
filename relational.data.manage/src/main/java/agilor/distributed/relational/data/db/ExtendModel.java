package agilor.distributed.relational.data.db;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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


    protected static String getCacheName(Object obj)
    {
        Cache annotation = obj.getClass().getAnnotation(Cache.class);
        return annotation!=null?annotation.value():obj.getClass().getCanonicalName();
    }


    private static String buildCacheKey(Object ... values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++)
            builder.append(values[i].toString()).append('-');

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }



    public <T> T build(Class cls) throws IllegalAccessException, InstantiationException {
        T _instance = (T) cls.newInstance();


        Map<String, Object> attrs = getAttrs();


        for (Map.Entry<String, Object> it : attrs.entrySet()) {
            Object val = it.getValue();
            if(val==null) continue;

            try {

                Field field = cls.getDeclaredField(it.getKey());

                Method method = getMethod(cls, buildMethodName("set", it.getKey()), field.getType());

                if (method != null) {

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





    @Override
    public M findById(Object idValue) {



        M model = CacheKit.get(getCacheName(this), idValue);

        if(model==null) {
            model = super.findById(idValue);
            if (model != null)
                CacheKit.put(getCacheName(this), idValue, model);
        }
        return model;
    }


    @Override
    public M findById(Object... idValues) {

        M model = CacheKit.get(getCacheName(this),buildCacheKey(idValues));
        if(model==null) {
            model = super.findById(idValues);
            if (model != null)
                CacheKit.put(getCacheName(this), buildCacheKey(idValues), model);
        }
        return model;
    }

    @Override
    public boolean save() {


        boolean result = super.save();

        if (result)
            CacheKit.put(getCacheName(this), getInt("id"), this);
        return result;

    }


    @Override
    public boolean update() {
        boolean result = super.update();
        if(result)
            CacheKit.put(getCacheName(this),getInt("id"),this);
        return result;
    }


    @Override
    public boolean delete() {
        boolean result = super.delete();
        if (result)
            CacheKit.remove(getCacheName(this), getInt("id"));
        return result;
    }


    @Override
    public boolean deleteById(Object idValue) {


        boolean result = super.deleteById(idValue);

        if (result)
            CacheKit.remove(getCacheName(this), idValue);
        return result;
    }


    @Override
    public boolean deleteById(Object... idValues) {

        boolean result =  super.deleteById(idValues);
        if(result)
            CacheKit.remove(getCacheName(this),buildCacheKey(idValues));
        return result;
    }


}
