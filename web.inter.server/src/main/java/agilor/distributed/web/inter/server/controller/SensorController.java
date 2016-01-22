package agilor.distributed.web.inter.server.controller;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.DeviceService;
import agilor.distributed.relational.data.services.DeviceTypeService;
import agilor.distributed.relational.data.services.SensorOfTypeService;
import agilor.distributed.relational.data.services.SensorService;
import agilor.distributed.web.inter.server.interceptor.DebugInterceptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfinal.aop.Before;
import com.jfinal.core.DistrController;
import agilor.distributed.web.inter.server.interceptor.LoginInterceptor;
import agilor.distributed.web.inter.server.result.Action;

import java.util.Date;

/**
 * Created by LQ on 2015/12/30.
 */
public class SensorController extends DistrController {

    private SensorService service = null;
    private SensorOfTypeService type_service = null;


    public SensorController()  {

        service = new SensorService();
        type_service= new SensorOfTypeService();
    }


    @Before(LoginInterceptor.class)
    public void insert() {

        int uid = userId();

        int deviceId = getParaToInt("di");
        DeviceService ds = new DeviceService();
        try {
            Device device = ds.getById(deviceId);
            if(device==null)
                renderResult(Action.notFound());
            else if(device.getCreatorId()!=userId())
                renderResult(Action.error());
            else {
                Sensor sensor = new Sensor();
                sensor.setName(getPara("n"));
                sensor.setDeviceId(device.getId());
                sensor.setType(Value.Types.valueOf(getPara("t")));
                sensor.setCreatorId(device.getCreatorId());
                sensor.setDateCreated(new Date());

                service.insert(sensor);

                renderResult(Action.success());
            }
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Before(LoginInterceptor.class)
    public void insertOfType()
    {
        int typeId = getParaToInt("i", 0);

        DeviceTypeService dts = new DeviceTypeService();
        try {
            DeviceType dt = dts.getById(typeId);
            if(dt==null)
                renderResult(Action.notFound());
            else if(dt.getCreatorId()!=userId())
                renderResult(Action.error());
            else {
                SensorOfTypeService sts = new SensorOfTypeService();
                SensorOfType data = new SensorOfType();
                data.setType(Value.Types.valueOf(getPara("t")));
                data.setTypeId(typeId);
                sts.insert(data);
                renderResult(Action.success());
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullParameterException e) {
            renderResult(Action.validate(e.getMessage()));
        }
//        catch (SqlHandlerException e) {
//            e.printStackTrace();
//        }
    }


    @Before(LoginInterceptor.class)
    public void deleteOfType() {
        int id = getParaToInt("i", 0);
        try {
            SensorOfType s = type_service.findById(id);
            if(s==null)
                renderResult(Action.notFound());
            else {
                DeviceTypeService dt_service = new DeviceTypeService();
                DeviceType dt = dt_service.getById(s.getTypeId());
                if(dt==null|| dt.getCreatorId()!=userId())
                    renderResult(Action.error());
                else
                {
                    type_service.delete(s.getId());
                    renderResult(Action.success());
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Before(LoginInterceptor.class)
    public void delete() {
        int id = getParaToInt("i", 0);
        try {
            Sensor sensor = service.findById(id);
            if (sensor == null)
                renderResult(Action.notFound());
            else if (sensor.getCreatorId() != userId())
                renderResult(Action.error());
            else {
                service.delete(sensor.getId());
                renderResult(Action.success());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Before(LoginInterceptor.class)
    public void update() throws IllegalAccessException, InstantiationException {
        int id = getParaToInt("i");
        Sensor sensor = service.findById(id);
        if(sensor==null)
            renderResult(Action.notFound());
        else if(sensor.getCreatorId()!=userId())
            renderResult(Action.error());
        else {
            String n = getPara("n");
            sensor.setName(n);
            if (service.update(sensor))
                renderResult(Action.success());
            else
                renderResult(Action.failed());
        }
    }

    @Before(LoginInterceptor.class)
    public void updateOfType()
    {
        int id = getParaToInt("i",0);

        try {
            SensorOfType s = type_service.findById(id);
            if(s==null)
                renderResult(Action.notFound());
            else {
                DeviceTypeService dt_service = new DeviceTypeService();
                DeviceType dt = dt_service.getById(s.getTypeId());
                if(dt==null||dt.getCreatorId()!=userId())
                    renderResult(Action.error());
                else
                {
                    s.setType(Value.Types.valueOf(getPara("t",s.getType().toString())));
                    type_service.update(s);
                    renderResult(Action.success());
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Before({LoginInterceptor.class, DebugInterceptor.class})
    public void single() throws IllegalAccessException, InstantiationException, JsonProcessingException {
        Sensor sensor = service.findById(getParaToInt("i", 0));

        renderText(toJson(sensor));
    }



    @Before({LoginInterceptor.class,DebugInterceptor.class})
    public void singleOfType() throws IllegalAccessException, InstantiationException, JsonProcessingException {
        SensorOfType sensor = type_service.findById(getParaToInt("i", 0));

        if(sensor!=null)
            renderResult(Action.success(toJson(sensor)));
        else
            renderResult(Action.notFound());
    }




    @Before(LoginInterceptor.class)
    public void write()  {
        int id = getParaToInt("i", 0);
        String v = getPara("v");

        try {
            Sensor sensor = service.findById(id);
            if(sensor==null)
                renderResult(Action.notFound());
            else if(sensor.getCreatorId()!=userId())
                renderResult(Action.error());
            else {

                Value val = new Value(sensor.getType());

                switch (val.getValueType()) {
                    case BOOL:
                        val.setBvalue(Boolean.valueOf(v));
                        break;
                    case FLOAT:
                        val.setFvalue(Float.valueOf(v));
                        break;
                    case INT:
                        val.setLvalue(Integer.valueOf(v));
                        break;
                    case STRING:
                        val.setSvalue(v);
                        break;
                    default:
                }

                sensor.write(val);

            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
