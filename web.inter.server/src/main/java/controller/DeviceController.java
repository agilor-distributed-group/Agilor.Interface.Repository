package controller;

import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.services.DeviceService;
import agilor.distributed.relational.data.services.DeviceTypeService;
import com.jfinal.aop.Before;
import interceptor.LoginInterceptor;
import result.Action;

import java.io.IOException;

/**
 * Created by LQ on 2015/12/28.
 */
public class DeviceController extends DistrController {

    private DeviceService service = null;


    public DeviceController() throws Exception {
        service = new DeviceService();
    }


    @Before(LoginInterceptor.class)
    public void insert()  {

        /**
         * 是否根据type生成device
         */
        boolean t = getParaToBoolean("t",false);
        String n = getPara("n");
        if(t) {
            DeviceTypeService ts = new DeviceTypeService();
            int id = getParaToInt("ti");
            try {
                DeviceType type =  ts.getById(id);
                if(type==null)
                    renderResult(Action.notFound());
                else if(type.getCreatorId()!=userId()&& type.getScope()!= DeviceType.ScopeTypes.PUBLIC)
                    renderResult(Action.error());
                else {
                    Device device = type.build();
                    device.setName(n);
                    device.setCreatorId(userId());
                    service.insert(device);
                    renderResult(Action.success());
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullParameterException e) {
                e.printStackTrace();
                renderResult(Action.validate(e.getMessage()));
            }


        }
        else {

            Device device = new Device();
            device.setName(n);
            try {
                device.addSensors(toList(getPara("s"), Sensor.class));
                device.setCreatorId(userId());
                service.insert(device);

                renderResult(Action.success());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullParameterException e) {
                renderResult(Action.validate(e.getMessage()));
            }
        }
    }


    @Before(LoginInterceptor.class)
    public void delete()
    {
        int id = getParaToInt("i",0);


        try {
            Device device = service.getById(id);
            if(device==null)
                renderResult(Action.notFound());
            else if(device.getCreatorId()!=userId())
                renderResult(Action.error());
            else {
                if (service.delete(id))
                    renderResult(Action.success());
                else renderResult(Action.failed());
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Before(LoginInterceptor.class)
    public void update() {
        String name = getPara("n");
        int id = getParaToInt("i", 0);


        try {
            Device device = service.getById(id);
            if (device == null)
                renderResult(Action.notFound());
            else if (device.getCreatorId() != userId())
                renderResult(Action.error());

            service.update(device.getId(), name);

            renderResult(Action.success());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
