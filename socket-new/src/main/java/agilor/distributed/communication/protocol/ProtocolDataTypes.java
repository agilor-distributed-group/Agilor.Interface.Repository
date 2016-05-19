package agilor.distributed.communication.protocol;

/**
 * Created by LQ on 2015/10/22.
 */
public enum ProtocolDataTypes {
    NULL((byte)0),
    INT((byte)32),
    CHAR((byte)16),
    BOOL((byte)'B'),
    FLOAT((byte)'F'),
    DOUBLE((byte)'D'),
    BYTE((byte)8),
    LONG((byte)64),
    CLASS((byte)'C'),
    ARRAY((byte)'A'),
    STRING((byte)'S');


    private byte flag;
    ProtocolDataTypes(byte flag) {
        this.flag = flag;
    }

    public byte value()
    {
        return flag;
    }

    public static ProtocolDataTypes type(byte val) {
        switch (val) {
            case 32:
                return INT;
            case 16:
                return CHAR;
            case 'B':
                return BOOL;
            case 'F':
                return FLOAT;
            case 'D':
                return DOUBLE;
            case 8:
                return BYTE;
            case 64:
                return LONG;
            case 'C':
                return CLASS;
            case 'A':
                return ARRAY;
            case 'S':
                return STRING;
            default:
                return NULL;
        }
    }

    public static int sizeof(ProtocolDataTypes type) {
        switch (type) {
            case BOOL:
                return 1;
            case BYTE:
                return 1;
            case CHAR:
                return 2;
            case DOUBLE:
                return 8;
            case FLOAT:
                return 4;
            case INT:
                return 4;
            case LONG:
                return 8;
            default:
                return 0;
        }
    }

}
