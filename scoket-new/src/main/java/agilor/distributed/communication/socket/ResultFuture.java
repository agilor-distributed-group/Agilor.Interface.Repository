package agilor.distributed.communication.socket;

import agilor.distributed.communication.utils.LinkedBytes;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by xinlongli on 16/3/30.
 */
public interface ResultFuture<T> extends Future<T> {
    boolean parseData(LinkedBytes in,int st,int len) throws IOException;
    int getPackageSize();
}
