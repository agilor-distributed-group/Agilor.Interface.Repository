package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.TAGVAL;
import org.apache.thrift.TException;

/**
 * Created by LQ on 2015/8/13.
 * 历史点集合迭代器
 */
public class HistoryIterator extends AWidget implements Iterator<Val> {


    private int hwnd=-1;


    private Val _next=null;
    private boolean isEnd=false;

    public HistoryIterator(Target target,long start,long end,long step) throws TException, NoSuchFieldException, IllegalAccessException {

        target.getAgilor().attach(this);


        agilor.getClient().open();

        hwnd = agilor.getClient().QueryTagHistory(target.getName(), (int)(start / 1000), (int)(end / 1000), (int)step);
        if(hwnd<0) isEnd=true;
    }

    public HistoryIterator(Target target) throws TException, NoSuchFieldException, IllegalAccessException {
        this(target, System.currentTimeMillis(), target.getDateCreated().getTimeInMillis(), 0);
    }


    @Override
    public Val next() throws TException {

        if (isEnd) return null;
        if (_next != null) {
            Val t = _next;
            _next = null;
            return t;
        }

        TAGVAL val = agilor.getClient().GetNextTagValue(hwnd, true);
        if (val.id < 0) {
            isEnd = true;
            return null;
        } else return AParse.parse(val);
    }

    @Override
    public boolean hasNext() throws TException {

        if (_next != null) return true;
        if (isEnd) return false;

        TAGVAL val = agilor.getClient().GetNextTagValue(hwnd, true);
        if (val.id > 0) this._next = AParse.parse(val);
        else isEnd = true;
        return _next != null;
    }

    @Override
    public void close() {

    }
}
