package agilor.distributed.communication.protocol;

/**
 * Created by LQ on 2015/11/12.
 */
public class AssembleContext<T> {
    private int len;
    private T value;


    public AssembleContext(T value, int len) {
        this.value = value;
        this.len = len;
    }


    public int getLen() {
        return len;
    }


    public T getValue() {
        return value;
    }
}
