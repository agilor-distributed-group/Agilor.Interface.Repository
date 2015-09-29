package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.DEVICE;
import agilor.distributed.storage.inter.thrift.HandleErrorException;
import agilor.distributed.storage.inter.thrift.TAGNODE;
import agilor.distributed.storage.inter.thrift.TAGVAL;
import org.apache.thrift.TException;

/**
 * Created by LQ on 2015/8/12.
 * 点集合迭代器
 */
public class TargetIterator extends AWidget implements Iterator<Target>  {
    private int hwnd=-1;

    IClient client = null;

    private Target _next=null;
    private boolean isEnd=false;
    private Device device=null;


    public TargetIterator(Device device) throws TException, NoSuchFieldException, IllegalAccessException {


        device.getAgilor().attach(this);
        this.client=agilor.getClient();
        this.device = device;
        hwnd = client.QueryTagsbyDevice(device.getName());
        if(hwnd<0)isEnd=true;
    }


    @Override
    public boolean hasNext() throws TException {

        if (_next != null) return true;
        if (isEnd) return false;

        try {
            TAGNODE node = client.EnumNextTag(hwnd);
            this._next = AParse.parse(node);
        }catch (HandleErrorException e)
        {
            isEnd=true;

        }
        finally {
            return _next!=null;
        }

    }

    @Override
    public void close() {

    }

    @Override
    public Target next() throws TException, NoSuchFieldException, IllegalAccessException {
        if(isEnd) return null;
        if(_next!=null) {
            Target t = _next;
            t.setDeviceName(device.getName());
            this.getAgilor().attach(t);

            _next = null;
            return t;
        }

        TAGNODE node = client.EnumNextTag(hwnd);
        if (node.id < 0) {
            isEnd = true;
            return null;
        } else return AParse.parse(node);
    }
}
