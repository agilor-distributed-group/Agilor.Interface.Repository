package agilor.distributed.communication.client;

import agilor.distributed.communication.protocol.*;

import java.util.Calendar;
import java.util.LinkedHashMap;

/**
 * Created by LQ on 2015/10/21.
 */
public class Value implements ProtocolObject {
    @Override
    public byte[] toBytes() throws Exception {
        Protocol protocol = SimpleProtocol.getInstance();
        //byte[] time_bytes = protocol.resolve((int) (time.getTimeInMillis() / 1000));

        byte[] val = null;
        switch (valueType) {
            case BOOL:
                val = protocol.resolve(Bvalue);
                break;
            case FLOAT:
                val = protocol.resolve(Fvalue);
                break;
            case INT:
                val = protocol.resolve(Lvalue);
                break;
            case STRING:
                val = protocol.resolve(Svalue);
                break;
        }
        return val;

//        int size = time_bytes.length + val.length - 1;
//        byte[] result = new byte[size];
//        System.arraycopy(time_bytes, 0, result, 0, time_bytes.length);
//        System.arraycopy(val, 1, result, 5, val.length);
//        return result;
    }

    @Override
    public void fromBytes(byte[] data,int pos,int len) throws Exception {
        Protocol protocol = SimpleProtocol.getInstance();

        Assemble assemble = protocol.assemble(data, pos, len);

        long timestap = assemble.next().toInt();
        time.setTimeInMillis(timestap * 1000);

        Token token = assemble.next();

        if (token.getType() == ProtocolDataTypes.BOOL) {
            Bvalue = token.toBoolean();
        } else if (token.getType() == ProtocolDataTypes.FLOAT) {
            Fvalue = token.toFloat();
        } else if (token.getType() == ProtocolDataTypes.LONG) {
            Lvalue = token.toInt();
        } else if (token.getType() == ProtocolDataTypes.STRING) {
            Svalue = token.toStd();
        }
    }

    @Override
    public boolean isAutoPrefix() {
        return false;
    }

    public static enum Types
    {
        BOOL((byte)'B'),
        FLOAT((byte)'F'),
        INT((byte)32),
        STRING((byte)'S');

        private byte flag;
        Types(byte flag)
        {
            this.flag=flag;
        }

        public byte value(){ return flag;}
        public static Types value(byte b)
        {
            switch (b) {
                case 'B':
                    return BOOL;
                case 'F':
                    return FLOAT;
                case 32:
                    return INT;
                case 'S':
                    return STRING;
                default:
                    throw new RuntimeException("error type data");
            }
        }


        ProtocolDataTypes convert() {
            switch (this) {
                case BOOL:
                    return ProtocolDataTypes.BOOL;
                case FLOAT:
                    return ProtocolDataTypes.FLOAT;
                case INT:
                    return ProtocolDataTypes.INT;
                case STRING:
                    return ProtocolDataTypes.STRING;
                default:
                    return ProtocolDataTypes.NULL;
            }
        }
    }


    private Types valueType;
    private int Lvalue;
    private boolean Bvalue;
    private float Fvalue;
    private String Svalue;
    private Calendar time;




    public Types getValueType() {
        return valueType;
    }

    public void setValueType(Types valueType) {
        this.valueType = valueType;
    }

    public int getLvalue() {
        return Lvalue;
    }

    public void setLvalue(int lvalue) {
        Lvalue = lvalue;
    }

    public boolean isBvalue() {
        return Bvalue;
    }

    public void setBvalue(boolean bvalue) {
        Bvalue = bvalue;
    }

    public float getFvalue() {
        return Fvalue;
    }

    public void setFvalue(float fvalue) {
        Fvalue = fvalue;
    }

    public String getSvalue() {
        return Svalue;
    }

    public void setSvalue(String svalue) {
        Svalue = svalue;
    }


    public Value(Types type) {
        this.valueType = type;
        time = Calendar.getInstance();
    }


    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}
