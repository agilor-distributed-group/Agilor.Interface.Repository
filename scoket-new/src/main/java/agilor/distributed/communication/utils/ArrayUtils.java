package agilor.distributed.communication.utils;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * Created by LQ on 2015/11/9.
 */
public class ArrayUtils {

//    public static<T> T[] split(T[] data,int pos,int len) {
//        T[] result = (T[]) Array.newInstance(data.getClass().getComponentType(), len);
//
//        System.arraycopy(data, pos, result, 0, len);
//        return result;
//    }

    public static Object split(Object data,int pos,int len) {
        Object result = Array.newInstance(data.getClass(), len);
        System.arraycopy(data, pos, result, 0, len);
        return result;
    }



}
