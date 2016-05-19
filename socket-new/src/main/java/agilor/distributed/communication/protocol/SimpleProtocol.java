package agilor.distributed.communication.protocol;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by LQ on 2015/10/20.
 */
public final class SimpleProtocol extends ProtocolBase {
    private static SimpleProtocol _instance = null;

    private final static int MAX_LEN = (Integer.MAX_VALUE - 5);

    private static Object lock=new Object();
    public static SimpleProtocol getInstance() {
        if (_instance == null)
            synchronized (lock){
                if(_instance ==null ){
                    _instance=new SimpleProtocol();
                }
            }
        return _instance;
    }


    @Override
    public byte[] resolve(int data) {
        byte[] result = new byte[5];
        result[0] = ProtocolDataTypes.INT.value();
        result[1] = (byte) (data & 0xff);
        result[2] = (byte) ((data >> 8) & 0xff);
        result[3] = (byte) ((data >> 16) & 0xff);
        result[4] = (byte) ((data >> 24) & 0xff);


        return result;
    }

    @Override
    public byte[] resolve(boolean data) {

        byte[] result = new byte[2];
        result[0] = ProtocolDataTypes.BOOL.value();
        result[1] = (byte) (data ? 1 : 0);
        return result;
    }

    @Override
    public byte[] resolve(float data) {
        byte[] result = resolve(Float.floatToIntBits(data));
        result[0] = ProtocolDataTypes.FLOAT.value();
        return result;
    }

    @Override
    public byte[] resolve(double data) {
        byte[] result = resolve(Double.doubleToLongBits(data));
        result[0] = ProtocolDataTypes.DOUBLE.value();
        return result;
    }

    @Override
    public byte[] resolve(long data) {
        byte[] result = new byte[9];
        result[0] = ProtocolDataTypes.LONG.value();

        for (int i = 0; i < 8; i++)
            result[i + 1] = (byte) ((data >> (i * 8)) & 0xff);

        return result;
    }

    @Override
    public byte[] resolve(char data) {
        byte[] result = new byte[3];
        result[0] = ProtocolDataTypes.CHAR.value();
        result[1] = (byte) (data & 0xff);
        result[2] = (byte) ((data >> 8) & 0xff);
        return result;

    }

    @Override
    public byte[] resolve(byte data) {
        byte[] result = new byte[2];
        result[0] = ProtocolDataTypes.BYTE.value();
        result[1] = data;
        return result;
    }

    //数据最大长度为 65531
    @Override
    public byte[] resolve(String data) throws Exception {
        if (data!=null&& data.length() > MAX_LEN)
            throw new Exception("the string length must between 0 and " + MAX_LEN);


        if(data==null)
            return new byte[]{ProtocolDataTypes.STRING.value(),1,0,0,0,0};





        byte[] result = new byte[data.length() + 6];

        byte[] bytes = data.getBytes();


        result[0] = ProtocolDataTypes.STRING.value();
        int len = data.length()+1;
        result[1] = (byte) (len & 0xff);
        result[2] = (byte) ((len >> 8) & 0xff);
        result[3] = (byte) ((len >> 16) & 0xff);
        result[4] = (byte) ((len >> 24) & 0xff);
        System.arraycopy(bytes, 0, result, 5, data.length());
        result[result.length-1]=0;
        return result;
    }

    /**
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> byte[] resolve(T data) throws Exception {

        if(data==null) return null;

        byte[] result = null;
        if (data instanceof ProtocolObject) {
            ProtocolObject obj = (ProtocolObject) data;
            byte[] obj_data = ((ProtocolObject) data).toBytes();
            if(obj.isAutoPrefix()) {

                result = new byte[obj_data.length + 5];
                result[0] = ProtocolDataTypes.CLASS.value();
                result[1] = (byte) (obj_data.length & 0xff);
                result[2] = (byte) ((obj_data.length >> 8) & 0xff);
                result[3] = (byte) ((obj_data.length >> 16) & 0xff);
                result[4] = (byte) ((obj_data.length >> 24) & 0xff);
                System.arraycopy(obj_data, 0, result, 5, obj_data.length);
            }else
                return obj_data;
        } else {
            if (data instanceof String) //String
                result = resolve((String) data);
            else if (data instanceof Integer) //Integer
                result = resolve(((Integer) data).intValue());
            else if (data instanceof Boolean) //Boolean
                result = resolve(((Boolean) data).booleanValue());
            else if (data instanceof Long) //Long
                result = resolve(((Long) data).longValue());
            else if (data instanceof Float) //Float
                result = resolve(((Float) data).floatValue());
            else if (data instanceof Double) //Double
                result = resolve(((Double) data).doubleValue());
            else if (data instanceof Byte) //Byte
                result = resolve(((Byte) data).byteValue());
            else if (data instanceof Character)//Char
                result = resolve(((Character) data).charValue());
            else {
                Field[] fields = data.getClass().getDeclaredFields();
                int len = 0, _data_len = 0;
                List<byte[]> list = new ArrayList<>();
                for (Field field : fields) {
                    Method m = data.getClass().getMethod("get" + getMethodName(field.getName()));
                    if (m == null) continue;
                    len++;
                    byte[] _data_tmp = resolve(m.invoke(data));
                    _data_len += _data_tmp.length;
                    list.add(_data_tmp);
                }

                if (_data_len > MAX_LEN)
                    throw new Exception("结构体总大小不能超过 " + MAX_LEN + "byte");

                int positon = 5;
                result = new byte[_data_len + 5];
                for (byte[] it : list) {
                    System.arraycopy(it, 0, result, positon, it.length);
                    positon += it.length;
                }
                result[0] = ProtocolDataTypes.CLASS.value();
                result[1] = (byte) (len & 0xff);
                result[2] = (byte) ((len >> 8) & 0xff);
                result[3] = (byte) ((len >> 16) & 0xff);
                result[4] = (byte) ((len >> 24) & 0xff);
            }
        }
        return result;
    }

    /**
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> byte[] resolve(T[] data) throws Exception {

        List<byte[]> list = new ArrayList<>();
        int len = 0;
        for (T i : data) {
            byte[] t = resolve(i);
            len += t.length;
            list.add(t);
        }
        if (len > MAX_LEN)
            throw new Exception("数组长度不能超过" + (MAX_LEN));


        byte[] result = new byte[len + 5];

        int postion = 5;

        for (byte[] i : list) {
            System.arraycopy(i, 0, result, postion, i.length);
            postion += i.length;
        }
        result[0] = ProtocolDataTypes.ARRAY.value();
        result[1] = (byte) (len & 0xff);
        result[2] = (byte) ((len >> 8) & 0xff);
        result[3] = (byte) ((len >> 16) & 0xff);
        result[4] = (byte) ((len >> 24) & 0xff);
        return result;
    }


    /**
     * @param data
     * @param <T>
     * @return
     * @throws Exception
     */

    @Override
    public <T> byte[] resolve(List<T> data) throws Exception {

        if(data instanceof ProtocolObject) {
            ProtocolObject obj = (ProtocolObject) data;
            byte[] obj_data = obj.toBytes();
            if (!obj.isAutoPrefix())
                return obj_data;
            else {
                byte[] result = new byte[5 + obj_data.length];
                result[0] = ProtocolDataTypes.CLASS.value();
                result[1] = (byte) (obj_data.length & 0xff);
                result[2] = (byte) ((obj_data.length >> 8) & 0xff);
                result[3] = (byte) ((obj_data.length >> 16) & 0xff);
                result[4] = (byte) ((obj_data.length >> 24) & 0xff);
                System.arraycopy(obj_data, 0, result, 5, obj_data.length);
                return result;
            }
        }


        List<byte[]> list = new ArrayList<>();
        int len = 0;
        for (T i : data) {
            byte[] t = resolve(i);
            len += t.length;
            list.add(t);
        }
        if (len > MAX_LEN)
            throw new Exception("数组长度不能超过" + (MAX_LEN));


        byte[] result = new byte[len + 5];

        int postion = 5;

        for (byte[] i : list) {
            System.arraycopy(i, 0, result, postion, i.length);
            postion += i.length;
        }
        result[0] = ProtocolDataTypes.ARRAY.value();
        result[1] = (byte) (len & 0xff);
        result[2] = (byte) ((len >> 8) & 0xff);
        result[3] = (byte) ((len >> 16) & 0xff);
        result[4] = (byte) ((len >> 24) & 0xff);
        return result;
    }

    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }


    @Override
    public Assemble assemble(byte[] data,int pos,int len) throws Exception {
        return new SimpleAssemble(data, pos, len);
    }
}
