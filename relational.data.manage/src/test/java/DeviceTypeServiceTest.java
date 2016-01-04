import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.entities.Sensor;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.DeviceTypeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by LQ on 2015/12/31.
 */
public class DeviceTypeServiceTest {

    DeviceTypeService service = null;
    @Before
    public void before()
    {
        Config config = new Config();
        service = new DeviceTypeService();
    }


    @Test
    public void insert() {
        DeviceTypeService service = new DeviceTypeService();
        DeviceType data = new DeviceType();

        data.setName("test");
        data.setCreatorId(20005);


        SensorOfType sensor1 = new SensorOfType();
        sensor1.setType(Value.Types.FLOAT);

        SensorOfType sensor2 = new SensorOfType();
        sensor2.setType(Value.Types.INT);

        SensorOfType sensor3 = new SensorOfType();
        sensor3.setType(Value.Types.BOOL);

        SensorOfType sensor4 = new SensorOfType();
        sensor4.setType(Value.Types.STRING);

        data.addSensor(sensor1);
        data.addSensor(sensor2);
        data.addSensor(sensor3);
        data.addSensor(sensor4);

        try {
            service.insert(data);
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (SqlHandlerException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void insert_unique_exception_test(){
        DeviceTypeService service = new DeviceTypeService();
        DeviceType data = new DeviceType();

        data.setName("test");
        data.setCreatorId(20005);


        SensorOfType sensor1 = new SensorOfType();
        sensor1.setType(Value.Types.FLOAT);

        SensorOfType sensor2 = new SensorOfType();
        sensor1.setType(Value.Types.INT);

        SensorOfType sensor3 = new SensorOfType();
        sensor1.setType(Value.Types.BOOL);

        SensorOfType sensor4 = new SensorOfType();
        sensor1.setType(Value.Types.STRING);

        data.addSensor(sensor1);
        data.addSensor(sensor2);
        data.addSensor(sensor3);
        data.addSensor(sensor4);

        try {
            service.insert(data);
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (SqlHandlerException e) {
            e.printStackTrace();
        }

    }


    /**
     * 测试删除一个不存在的数据
     */
    @Test
    public void delete_error_test()
    {
        DeviceTypeService service = new DeviceTypeService();
        Assert.assertEquals(service.delete(323242),false);
    }


    @Test
    public void delete_test()
    {
        Assert.assertEquals(service.delete(12), true);

    }


    @Test
    public void update_test() throws ValidateParameterException {
        DeviceType data =new DeviceType();
        data.setId(10);
        data.setName("TEST_UPDATE");
        Assert.assertEquals(service.update(data), true);
    }








    @Test
    public void all_01()
    {
        DeviceTypeService service = new DeviceTypeService();
        List<DeviceType> list = service.all();


        for (DeviceType it:list)
            System.out.println(it.getName());
    }



    @Test
    public void getSensor() throws Exception {
        DeviceType d = service.getById(13);
        List<SensorOfType> sensors = d.getSensors();

        for(SensorOfType it:sensors)
            System.out.println(it.getType().toString());
    }


    @Test
    public void getCount() throws IllegalAccessException, InstantiationException {
        DeviceType d = service.getById(13);
        System.out.println(d.getSensorCount());
    }


    @Test
    public void build() throws IllegalAccessException, InstantiationException {
        DeviceType type = service.getById(13);


        Device device = type.build();


        System.out.println("name:" + device.getName());

        for (Sensor sensor : device.getSensors())
            System.out.println("base:" + sensor.getBaseName() + ",name:" + sensor.getName() + ",type:" + sensor.getDataType());
    }





}
