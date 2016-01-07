package controller;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.ExceptionTypes;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.DeviceTypeService;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import interceptor.LoginInterceptor;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import result.ActionResult;
import result.Data;

import javax.print.DocFlavor;
import java.io.IOException;

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
            data.addSensor(toList(getPara("s"), SensorOfType.class));
        } catch (IOException e) {
            e.printStackTrace();
        }


        data.setScope(DeviceType.ScopeTypes.value(getParaToInt("sc", 0).byteValue()));

        try {
            service.insert(data);
            renderResult(Data.success());
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (SqlHandlerException e) {
            e.printStackTrace();
        }
    }


    @Before(LoginInterceptor.class)
    public void update()
    {
        try {
            DeviceType data = service.getById(getParaToInt("i", 0));

            if(data==null||data.getCreatorId()!=userId()) {
                renderResult(Data.notFound());
            }

            data.setName(getPara("n"));
            data.setScope(DeviceType.ScopeTypes.value(getParaToInt("sc", 0).byteValue()));
            service.update(data);
            renderResult(Data.success());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ValidateParameterException e) {
            renderResult(Data.validate(e.getError().getDescription()));
        }
    }

    @Before(LoginInterceptor.class)
    public void delete() {

        try {
            DeviceType data = service.getById(getParaToInt("i", 0));
            if (data == null || data.getCreatorId() != userId()) {
                renderResult(Data.notFound());
            }

            if (service.delete(data.getId()))
                renderResult(Data.success());


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }





}
