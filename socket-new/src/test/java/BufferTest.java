import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/11/12.
 */
public class BufferTest {


    public byte[]  createbytes(int size) {
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = 100;
        }
        return data;
    }



    @Test
    public void bufferSizeTest()
    {

        List<byte[]> list = new ArrayList<>();

        int cs_len = 200000;
        int array_len = 5000;

        for(int i=0;i<cs_len;i++) {
            list.add(createbytes(array_len));
        }

        for(int i=0;i<list.size();i++) {
            byte[] buffer = list.get(i);
            for (int j = 0; j < buffer.length; j++) {
                byte a = buffer[j];
            }
        }
    }


    @Test
    public void bufferArray()
    {



        int cs_len = 200000;
        int array_len =5000;

        byte[] result = new byte[cs_len*array_len];

        for(int i=0;i<cs_len;i++) {
            System.arraycopy(createbytes(array_len), 0, result, i * array_len, array_len);
        }

        for(int i=0;i<result.length;i++) {
            byte a = result[i];
        }
    }


    @Test
    public void changearraytest()
    {
        byte[] data = new byte[5];
        data[0]=100;
        changearray(data);
        System.out.println(data[0]);
    }


    public void changearray(byte[] ar)
    {
        ar[0]=1;
    }
}
