package agilor.distributed.web.inter.server.controller;

import agilor.distributed.relational.data.entities.Creator;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.DeviceTypeService;
import agilor.distributed.web.inter.server.interceptor.CreatorInterceptor;
import agilor.distributed.web.inter.server.interceptor.DebugInterceptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfinal.aop.Before;
import com.jfinal.core.DistrController;
import agilor.distributed.web.inter.server.interceptor.LoginInterceptor;
import agilor.distributed.web.inter.server.result.Action;

import java.io.IOException;
import java.util.List;

/**
 * Created by LQ on 2015/12/28.
 */
public class DeviceTypeController extends DistrController {

    DeviceTypeService service = null;


    public DeviceTypeController() throws Exception {
        service = new DeviceTypeService();
    }


    @Before(LoginInterceptor.class)
    public void insert() {
        DeviceType data = new DeviceType();
        data.setName(getPara("n"));

        try {

            List<SensorOfType> sensors = toList(getPara("s"),SensorOfType.class);



            data.addSensor(sensors);
        } catch (IOException e) {
            e.printStackTrace();
        }


        data.setCreatorId(userId());


        data.setScope(DeviceType.ScopeTypes.value(getParaToInt("sc", 0).byteValue()));

        try {
            service.insert(data);
            renderResult(Action.success());
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (SqlHandlerException e) {
            renderResult(Action.failed(null,e.getMessage()));
        }

    }


    @Before({LoginInterceptor.class, CreatorInterceptor.class, DebugInterceptor.class})
    @CreatorInterceptor.Param(model = DeviceType.class)
    public void single() throws IllegalAccessException, InstantiationException, JsonProcessingException {



        DeviceType data = getData();

        if (data != null && data.getCreatorId() == userId())
            renderResult(Action.success(toJson(data)));
        else
            renderResult(Action.failed());
    }



    @Before(LoginInterceptor.class)
    public void all() throws JsonProcessingException {

        List<DeviceType> list = service.all(userId());
        renderResult(Action.success(toJson(list)));
    }

    @Before({LoginInterceptor.class,CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = DeviceType.class)
    public void update() {
        try {

            DeviceType data = getData();
            data.setName(getPara("n"));
            data.setScope(DeviceType.ScopeTypes.value(getParaToInt("sc", data.getScope().value()).byteValue()));
            service.update(data);
            renderResult(Action.success());

        } catch (ValidateParameterException e) {
            renderResult(Action.validate(e.getError().getDescription()));
        }
    }

    @Before({LoginInterceptor.class,CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = DeviceType.class)
    public void delete() {


        DeviceType data = getData();
        if (service.delete(data.getId()))
            renderResult(Action.success());
        else
            renderResult(Action.failed());
    }





}
