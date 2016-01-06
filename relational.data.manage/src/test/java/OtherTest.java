import agilor.distributed.relational.data.context.SessionMetaData;
import agilor.distributed.relational.data.db.DB;
import org.junit.Test;

import java.io.*;

/**
 * Created by LQ on 2016/1/4.
 */
public class OtherTest {

    @Test
    public void class_name_test()
    {
        new DB.Device().findById(1);
    }



    @Test
    public void serializable_test() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutputStream os = new ObjectOutputStream(bos);

        SessionMetaData data = new SessionMetaData(new byte[]{1,2,3});

        os.writeObject(data);

        byte[] result = bos.toByteArray();





        ByteArrayInputStream bin = new ByteArrayInputStream(result);
        ObjectInputStream in = new ObjectInputStream(bin);

        SessionMetaData ddd = (SessionMetaData)in.readObject();

        System.out.println( ddd.getClass().getCanonicalName());
        System.out.println("first:"+String.valueOf(((byte[])ddd.value)[0]));
        System.out.println("second:"+String.valueOf(((byte[])ddd.value)[1]));
        System.out.println("thirt:"+String.valueOf(((byte[])ddd.value)[2]));

    }


    @Test
    public void str_append_null()
    {
        System.out.println("abc"+null);
    }


}
