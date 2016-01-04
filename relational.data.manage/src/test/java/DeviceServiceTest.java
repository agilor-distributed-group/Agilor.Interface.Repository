import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.entities.Device;
import agilor.distributed.relational.data.entities.DeviceType;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.services.DeviceService;
import agilor.distributed.relational.data.services.DeviceTypeService;
import jdk.nashorn.internal.objects.NativeUint16Array;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LQ on 2016/1/2.
 */
public class DeviceServiceTest {

    DeviceService service = null;
    DeviceTypeService typeService = null;

    @Before
    public void before()
    {
        Config c = new Config();
        service = new DeviceService();
        typeService= new DeviceTypeService();
    }


    @Test
    public void insert() throws IllegalAccessException, InstantiationException, NullParameterException {
        DeviceType type = typeService.getById(13);

        Device device = type.build();
        device.setCreatorId(20005);
        device.setName("DEVICE_TEST");

        service.insert(device);
    }


    @Test
    public void update()
    {
        service.update(7, "TEST_DEVICE_UPDATE");
    }


    @Test
    public void delete()
    {
         service.delete(7);
    }

}
