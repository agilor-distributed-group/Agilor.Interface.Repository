package agilor.distributed.storage.inter.jlient;

import agilor.distributed.storage.inter.client.IClient;
import agilor.distributed.storage.inter.thrift.DEVICE;
import agilor.distributed.storage.inter.thrift.HandleErrorException;
import org.apache.thrift.TException;

/**
 * Created by LQ on 2015/8/12.
 * 设备集合迭代器
 */
public class DeviceIterator extends AWidget implements Iterator<Device> {
    private int hwnd=-1;

    IClient client = null;

    private Device _next=null;
    private boolean isEnd=false;



    public DeviceIterator(Agilor agilor) throws TException {
        this.agilor = agilor;
        this.client = agilor.getClient();
        hwnd = client.QueryDeviceInfo();
    }

    @Override
    public boolean hasNext() throws TException {
        if(hwnd<0) return false;
        if (_next != null) return true;
        if (isEnd) return false;

        try {
            DEVICE device = client.EnumDeviceInfo(hwnd);
            this._next = AParse.parse(device);

        }
        catch (HandleErrorException e)
        {
            isEnd=true;
        }
        finally {
            return _next != null;
        }

    }

    @Override
    public void close() { }

    @Override
    public Device next() throws TException, NoSuchFieldException, IllegalAccessException {
        if(hwnd<0) return null;
        if(isEnd) return null;
        if(_next!=null) {
            Device t = _next;
            _next = null;
            agilor.attach(t);
            return t;
        }

        DEVICE device = client.EnumDeviceInfo(hwnd);
        if (device.id < 0) {
            isEnd = true;
            return null;
        } else return AParse.parse(device);


    }

}
