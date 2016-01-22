package agilor.distributed.web.inter.server.controller;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.SensorOfTypeService;
import agilor.distributed.relational.data.services.SensorService;
import agilor.distributed.web.inter.server.interceptor.CreatorInterceptor;
import agilor.distributed.web.inter.server.interceptor.DebugInterceptor;
import agilor.distributed.web.inter.server.interceptor.LoginInterceptor;
import agilor.distributed.web.inter.server.result.Action;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfinal.aop.Before;
import com.jfinal.core.DistrController;

import java.util.Date;

/**
 * Created by LQ on 2015/12/30.
 */
public class SensorController extends DistrController {

    private SensorService service = null;
    private SensorOfTypeService type_service = null;


    public SensorController() {

        service = new SensorService();
        type_service = new SensorOfTypeService();
    }


    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = Device.class, id = "di", context = "device")
    public void insert() {

        try {
            Device device = getData("device");

            Sensor sensor = new Sensor();
            sensor.setName(getPara("n"));
            sensor.setDeviceId(device.getId());
            sensor.setType(Value.Types.valueOf(getPara("t")));
            sensor.setCreatorId(device.getCreatorId());
            sensor.setDateCreated(new Date());

            service.insert(sensor);

            renderResult(Action.success());

        } catch (ValidateParameterException e) {
            renderResult(Action.validate(e.getMessage()));
        } catch (NullParameterException e) {
            renderResult(Action.validate(e.getMessage()));
        }
    }


    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = DeviceType.class)
    public void insertOfType() {

        DeviceType dts = getData();
        SensorOfTypeService sts = new SensorOfTypeService();
        SensorOfType data = new SensorOfType();

        try {
            data.setType(Value.Types.valueOf(getPara("t")));
            data.setTypeId(dts.getId());
            sts.insert(data);
            renderResult(Action.success());
        } catch (NullParameterException e) {
            renderResult(Action.validate(e.getMessage()));
        } catch (IllegalArgumentException e) {
            renderResult(Action.failed());
        }


    }


    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = SensorOfType.class)
    public void deleteOfType() {

        SensorOfType sensor = getData();

        if (type_service.delete(sensor.getId()))
            renderResult(Action.success());
        else
            renderResult(Action.failed());


    }

    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = Sensor.class)
    public void delete() {

        Sensor sensor = getData();
        if (service.delete(sensor.getId()))
            renderResult(Action.success());
        else
            renderResult(Action.failed());
    }

    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = Sensor.class)
    public void update() {

        Sensor sensor = getData();
        String n = getPara("n");
        sensor.setName(n);
        if (service.update(sensor))
            renderResult(Action.success());
        else
            renderResult(Action.failed());
    }

    @Before({LoginInterceptor.class, CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = SensorOfType.class)
    public void updateOfType() {
        int id = getParaToInt("i", 0);


        SensorOfType sensor = getData();
        sensor.setType(Value.Types.valueOf(getPara("t", sensor.getType().toString())));
        type_service.update(sensor);
        renderResult(Action.success());
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




    @Before({LoginInterceptor.class,CreatorInterceptor.class})
    @CreatorInterceptor.Param(model = Sensor.class)
    public void write()  {
        String v = getPara("v");

        Sensor sensor = getData();


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
}
