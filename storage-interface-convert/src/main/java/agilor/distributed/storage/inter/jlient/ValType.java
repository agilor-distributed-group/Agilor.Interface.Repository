package agilor.distributed.storage.inter.jlient;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.Serializable;

/**
 * Created by LQ on 2015/8/12.
 * 点值类型
 */
public enum  ValType implements Serializable {
    STRING((byte)'S'),
    FLOAT((byte)'R'),
    LONG((byte)'L'),
    BOOLEAN((byte)'B');

    private byte v;

    ValType(byte c)
    {
        this.v =c;
    }


    public int toInt() {
        return (int) v;
    }

    public byte value()
    {
        return v;
    }

    public static ValType value(byte c)
    {
        if(c=='R') return FLOAT;
        if(c=='S') return STRING;
        if(c=='B') return BOOLEAN;
        if(c=='L') return LONG;
        return null;
    }


}
