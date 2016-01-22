package agilor.distributed.web.inter.server.controller;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.services.DeviceService;
import agilor.distributed.relational.data.services.DeviceTypeService;
import agilor.distributed.web.inter.server.interceptor.CreatorInterceptor;
import agilor.distributed.web.inter.server.interceptor.DebugInterceptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfinal.aop.Before;
import com.jfinal.core.DistrController;
import agilor.distributed.web.inter.server.interceptor.LoginInterceptor;
import agilor.distributed.web.inter.server.result.Action;

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

    public void insert() {

        /**
         * 是否根据type生成device
         */

        boolean t = getParaToBoolean("t", false);


        Device device = null;
        String n = getPara("n");
        if (t) {
            DeviceTypeService ts = new DeviceTypeService();
            int id = getParaToInt("ti");
            try {
                DeviceType type = ts.getById(id);
                if (type == null)
                    renderResult(Action.notFound());
                else if (type.getCreatorId() != userId() && type.getScope() != DeviceType.ScopeTypes.PUBLIC)
                    renderResult(Action.error());
                else {
                    device = type.build();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        } else {

            try {
                device = new Device();
                device.addSensors(toList(getPara("s"), Sensor.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (device != null) {

            device.setName(n);
            device.setCreatorId(userId());
            for (Sensor sensor : device.getSensors())
                sensor.setCreatorId(userId());

            try {
                service.insert(device);
                renderResult(Action.success());
            } catch (NullParameterException e) {
                renderResult(Action.validate(e.getMessage()));
            } catch (SqlHandlerException e) {
                renderResult(Action.failed(null, e.getMessage()));
            }
        } else
            renderResult(Action.error());
    }


    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = Device.class)
    public void delete()
    {

        Device device = getData();
        if(service.delete(device.getId()))
            renderResult(Action.success());
        else
           renderResult(Action.failed());
    }


    @Before({LoginInterceptor.class,CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = Device.class)
    public void update() {
        String name = getPara("n");
        Device device = getData();
        service.update(device.getId(), name);
        renderResult(Action.success());
    }



    @Before({LoginInterceptor.class, DebugInterceptor.class})
    public void last() throws InstantiationException, IllegalAccessException, JsonProcessingException {
        DB.Device device = DB.Device.instance().findFirst("SELECT * FROM DEVICES ORDER BY ID DESC");

        if (device != null)
            renderResult(Action.success(toJson(device.build(Device.class))));
        else
            renderResult(Action.failed());
    }
}
