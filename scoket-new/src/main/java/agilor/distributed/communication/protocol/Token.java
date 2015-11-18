package agilor.distributed.communication.protocol;

import java.util.List;

/**
 * Created by LQ on 2015/11/10.
 */
public interface Token {
    ProtocolDataTypes getType();

    int toInt() throws Exception;
    byte toByte();
    float toFloat() throws Exception;
    double toDouble();
    char toChar();
    long toLong();
    boolean toBoolean() throws Exception;
    String toStd() throws Exception;
    <T> T toClass(Class cls) throws Exception;
    <T> T[] toArray(Class cls);
    <T> List<T> toList(Class cls) throws Exception;

}
