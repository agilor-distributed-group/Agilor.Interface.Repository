package agilor.distributed.communication.protocol;

import agilor.distributed.communication.utils.ConvertUtils;
import io.netty.handler.ssl.ApplicationProtocolConfig;

/**
 * Created by LQ on 2015/11/10.
 */
public class SimpleAssemble implements Assemble {

    private byte[] _data = null;
    private int _pos =0;
    private int _len=0;
    private int _end =0;



    public SimpleAssemble(byte[] data, int pos, int len) {
        this._data = data;
        this._pos = pos;
        this._len = len;
        this._end = pos+len;
    }


    private int next0() {
        ProtocolDataTypes type = ProtocolDataTypes.type(_data[_pos]);
        int len = 0;
        switch (type) {
            case BOOL:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG: {
                len = ProtocolDataTypes.sizeof(type)+1;


            }
            break;

            case STRING:
            case ARRAY:
            case CLASS: {
                len = ConvertUtils.toInt(_data[_pos + 1], _data[_pos + 2], _data[_pos + 3], _data[_pos + 4]) + 5;

            }
            break;

//            case ARRAY: {
//                int array_len = get_len(_data, ++_pos, 4);
//                _pos += 4;
//                while (array_len-- > 0) {
//                    len += next0();
//                }
//            }
        }
        _pos+=len;
        return len;
    }



    @Override
    public Token next() {

        if (_pos >= _end)
            return null;

        Token token = null;
        int pos = _pos;
        int len = next0();

        token = new SimpleToken(_data, pos, len);
        return token;
    }
}
