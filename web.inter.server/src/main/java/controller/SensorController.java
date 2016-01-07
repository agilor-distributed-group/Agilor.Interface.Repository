package controller;

import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.services.DeviceService;
import agilor.distributed.relational.data.services.DeviceTypeService;
import agilor.distributed.relational.data.services.SensorOfTypeService;
import agilor.distributed.relational.data.services.SensorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.aop.Before;
import interceptor.CreatorInterceptor;
import interceptor.LoginInterceptor;
import result.Data;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by LQ on 2015/12/30.
 */
public class SensorController extends DistrController {

    private SensorService service = null;


    public SensorController() throws Exception {
        service = new SensorService();
    }


    @Before(LoginInterceptor.class)
    public void insert() {

        int deviceId = getParaToInt("di");
        DeviceService ds = new DeviceService();
        try {
            Device device = ds.getById(deviceId);
            if(device==null)
                renderResult(Data.notFound());
            else if(device.getCreatorId()==userId())
                renderResult(Data.error());
            else {
                Sensor sensor = new Sensor();
                sensor.setDeviceId(device.getId());
                sensor.setDataType(Value.Types.value(getParaToInt("t").byteValue()));
                sensor.setCreatorId(device.getCreatorId());
                sensor.setDateCreated(new Date());

                service.insert(sensor);

                renderResult(Data.success());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Before(LoginInterceptor.class)
    public void insertOfType()
    {
        int typeId = getParaToInt("ti");

        DeviceTypeService dts = new DeviceTypeService();
        try {
            DeviceType dt = dts.getById(typeId);
            if(dt==null)
                renderResult(Data.notFound());
            else if(dt.getCreatorId()!=userId())
                renderResult(Data.error());
            else {
                SensorOfTypeService sts = new SensorOfTypeService();
                SensorOfType data = new SensorOfType();
                data.setType(Value.Types.value(getParaToInt("t").byteValue()));
                data.setTypeId(typeId);
                sts.insert(data);
                renderResult(Data.success());
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (SqlHandlerException e) {
            e.printStackTrace();
        }
    }


    @Before(LoginInterceptor.class)
    public void write()  {
        int type = getParaToInt("t");
        String v = getPara("v");
        Value val = new Value(Value.Types.value((byte) type));
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
        }


        int id = getParaToInt("i");
        try {
            Sensor sensor = service.findById(id);
            sensor.write(val);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
