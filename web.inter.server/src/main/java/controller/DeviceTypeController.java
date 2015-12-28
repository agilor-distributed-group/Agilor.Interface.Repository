package controller;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.services.DeviceTypeService;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

import javax.print.DocFlavor;

/**
 * Created by LQ on 2015/12/28.
 */
public class DeviceTypeController extends DistrController {

    DeviceTypeService service = null;


    public DeviceTypeController() throws Exception {
        service = new DeviceTypeService(getContext());
    }


    public void insert()
    {
        DeviceType data = new DeviceType();
        data.setName(getPara("n"));
        data.setSensor(getParaToInt("s", 1));
        data.setScope(DeviceType.ScopeTypes.value(getParaToInt("sc",0).byteValue()));
    }


    public void update()
    {
        DeviceType data = new DeviceType();
        data.setId(getParaToInt("i",0));
        data.setName(getPara("n"));
        data.setSensor(getParaToInt("s", 1));
        data.setScope(DeviceType.ScopeTypes.value(getParaToInt("sc", 0).byteValue()));
        service.update(data);
    }

    public void delete() {
        boolean ok = getParaToBoolean("ok", false);
        int id = getParaToInt("i", 0);
        if (!ok) {
            long c = service.getDeviceCount(id);
            if (c > 0) {
                //返回
            }
        }

        service.delete(id);

    }





}
