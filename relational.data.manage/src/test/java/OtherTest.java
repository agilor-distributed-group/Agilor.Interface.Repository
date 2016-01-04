import agilor.distributed.relational.data.db.DB;
import org.junit.Test;

/**
 * Created by LQ on 2016/1/4.
 */
public class OtherTest {

    @Test
    public void class_name_test()
    {
        new DB.Device().findById(1);
    }


}
