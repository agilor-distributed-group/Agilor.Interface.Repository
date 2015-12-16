package agilor.distributed.communication.protocol;

import agilor.distributed.communication.utils.ConvertUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/11/10.
 */
public class SimpleToken implements Token {
    private byte[] _data = null;

    private int _pos = 0;
    private int _len = 0;
    private ProtocolDataTypes _type=ProtocolDataTypes.NULL;


    public SimpleToken(byte[] data, int pos, int len) {
        this._data = data;
        this._pos = pos;
        this._len = len;

        _type =ProtocolDataTypes.type(data[_pos]);
    }


    @Override
    public ProtocolDataTypes getType() {
        return _type;
    }

    @Override
    public int toInt() throws Exception {
        if (_type != ProtocolDataTypes.INT)
            throw new Exception("not a int data");

        int result = 0;
        result = (_data[_pos + 1]);
        result += ((_data[_pos + 2] & 0xFF) << 8);
        result += ((_data[_pos + 3] & 0xFF) << 16);
        result += ((_data[_pos + 4] & 0xFF) << 24);
        return result;
    }

    @Override
    public byte toByte() {
        return 0;
    }

    @Override
    public float toFloat() throws Exception {
        if (_type != ProtocolDataTypes.FLOAT)
            throw new Exception("not float data");
        byte[] int_bytes = new byte[4];
        System.arraycopy(_data, _pos + 1, int_bytes, 0, 4);

        float resut = Float.intBitsToFloat(ConvertUtils.toInt(int_bytes));
        return resut;
    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public char toChar() {
        return 0;
    }

    @Override
    public long toLong() {
        return 0;
    }

    @Override
    public boolean toBoolean() throws Exception {
        if (_type != ProtocolDataTypes.BOOL)
            throw new Exception("not boolean data");
        if (_data[_pos + 1] == 1)
            return true;
        else return false;
    }

    @Override
    public String toStd() throws Exception {
        if (_type != ProtocolDataTypes.STRING)
            throw new Exception("not string data");

        String result = new String(_data, _pos + 5, _len - 5);
        return result;
    }

    @Override
    public <T> T toClass(Class cls) throws Exception {
        T result = (T) cls.newInstance();
        if(ProtocolObject.class.isAssignableFrom(cls)) {
            ProtocolObject obj = (ProtocolObject) result;
            obj.fromBytes(_data, _pos+5, _len-5);
            return result;
        }
        //else 反射生成对象

        return null;
    }

    @Override
    public <T> T[] toArray(Class cls) {

        return null;
    }

    @Override
    public <T> List<T> toList(Class cls) throws Exception {

        if (_type != ProtocolDataTypes.ARRAY)
            throw new Exception("not  array data");

        List<T> result = new ArrayList<>();


        int base_pos = _pos+5;
        int base_len = _len;
        int base_end = _pos+_len;
        int size =0;
        ProtocolDataTypes type = ProtocolDataTypes.type(_data[base_pos]);
        for (; base_pos < base_end; base_pos+=size) {
            switch (type) {
                case BOOL:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Boolean(new SimpleToken(_data, base_pos, size).toBoolean()));
                    break;
                case INT:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Integer(new SimpleToken(_data, base_pos, size).toInt()));
                    break;
                case FLOAT:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Float(new SimpleToken(_data, base_pos, size).toFloat()));
                    break;
                case CHAR:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Character(new SimpleToken(_data, base_pos, size).toChar()));
                    break;
                case BYTE:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Byte(new SimpleToken(_data, base_pos, size).toByte()));
                    break;
                case DOUBLE:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Double(new SimpleToken(_data, base_pos, size).toDouble()));
                    break;
                case LONG:
                    size = ProtocolDataTypes.sizeof(type) + 1;
                    result.add((T) new Long(new SimpleToken(_data, base_pos, size).toLong()));
                    break;
                case CLASS:
                    size = ConvertUtils.toInt(_data[base_pos + 1], _data[base_pos + 2], _data[base_pos + 3], _data[base_pos + 4]) + 5;
                    result.add(new SimpleToken(_data, base_pos, size).toClass(cls));
                    break;

                case STRING:
                    size = ConvertUtils.toInt(_data[base_pos + 1], _data[base_pos + 2], _data[base_pos + 3], _data[base_pos + 4]) + 5;
                    result.add((T) new SimpleToken(_data, base_pos, size).toString());
                    break;

                case ARRAY:
                    size = ConvertUtils.toInt(_data[base_pos + 1], _data[base_pos + 2], _data[base_pos + 3], _data[base_pos + 4]) + 5;
                    result.add((T) new SimpleToken(_data, base_pos, size).toArray(cls));
                    break;
            }

        }




        return result;
    }

}
