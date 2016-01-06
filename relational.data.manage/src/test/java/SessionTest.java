import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.context.RequestContext;
import agilor.distributed.relational.data.context.TestConnection;
import agilor.distributed.relational.data.entities.User;
import org.junit.Test;

/**
 * Created by LQ on 2016/1/5.
 */
public class SessionTest {

    @Test
    public void test() throws Exception {

        Config config = new Config();

        RequestContext context = new RequestContext(new TestConnection());


        User model = new User();
        model.setId(10);
        model.setUserName("123");
        model.setPassword("123");

        context.setSession("user2", model);


        User dd =(User)context.getSession("user2");

        System.out.println(dd.getUserName());


    }


}
