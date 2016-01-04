import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.entities.User;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.UserService;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by LQ on 2015/12/31.
 */
public class UserServiceTest {



    @Before
    public void before()
    {
        Config config = new Config();
    }



    @Test
    public void register() {
        UserService service = new UserService();
        try {
            User u = service.register("admin3", "123456");
        } catch (NullParameterException e) {
            System.out.println(e.getName());

        } catch (ValidateParameterException e) {
            System.out.println(e.getName()+":"+e.getError());
        } catch (SqlHandlerException e) {
            System.out.println(e.getType().getCode());
        }
    }


    @Test
    public void check()
    {
        UserService service = new UserService();
        try {
            User u = service.check("admin", "12345611");
        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void all() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        UserService service = new UserService();
        List<User> list = service.all();

        for (User i : list) {
            System.out.println("user:" + i.getUserName() + ",password:" + i.getPassword() + ",dateCreated:" + i.getDateCreated().toString());
        }
    }







}
