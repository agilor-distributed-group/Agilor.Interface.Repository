import agilor.distributed.communication.client.Value;
import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.entities.SensorOfType;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.services.SensorOfTypeService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by LQ on 2016/1/2.
 */
public class SensorOfTypeServiceTest {


    SensorOfTypeService service = null;

    @Before
    public void before()
    {
        Config c = new Config();
        service = new SensorOfTypeService();
    }

    @Test
    public void insert_null_typeId_exception_test() throws NullParameterException, SqlHandlerException {
        SensorOfType data = new SensorOfType();

        service.insert(data);
    }
    @Test
    public void insert_no_exist_typeId_exception_test() throws NullParameterException, SqlHandlerException {
        SensorOfType data = new SensorOfType();
        data.setTypeId(12222);

        service.insert(data);
    }


    @Test
    public void insert() throws NullParameterException, SqlHandlerException {
        SensorOfType data = new SensorOfType();
        data.setTypeId(13);
        service.insert(data);
    }


    @Test
    public void update() throws SqlHandlerException {
        SensorOfType data = new SensorOfType();
        data.setId(25);
        data.setTypeId(13);
        data.setType(Value.Types.INT);
        service.update(data);
    }



    @Test
    public void delete_not_exist()
    {
        Assert.assertEquals(service.delete(333333), false);
    }


    @Test
    public void delete()
    {
        Assert.assertTrue(service.delete(25));
    }


    @Test
    public void all() {
        List<SensorOfType> list = service.all(13);

        for(SensorOfType it:list)
            System.out.println(it.getType());
    }
}
