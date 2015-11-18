package agilor.distributed.communication.protocol;

import agilor.distributed.communication.utils.ConvertUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LQ on 2015/10/21.
 */
public class SimpleProtocolTest {

    Protocol protocol = new SimpleProtocol();

    /**
     * BOOL
     * @throws Exception
     */
    @Test
    public void testResolve() throws Exception {
        boolean data=true;
        byte[] result = new byte[]{ProtocolDataTypes.BOOL.value(),0x01};
        Assert.assertArrayEquals(protocol.resolve(data),result);

        data=false;
        result[1]=0x00;
        Assert.assertArrayEquals(protocol.resolve(data),result);


    }

    /**
     * INT
     * @throws Exception
     */
    @Test
    public void testResolve1() throws Exception {
        int data =100;
        byte[] result = new byte[]{ProtocolDataTypes.INT.value(),(byte)0x64,0,0,0};
        Assert.assertArrayEquals(protocol.resolve(data),result);

        data=0;
        result = new byte[]{ProtocolDataTypes.INT.value(),(byte)0,0,0,0};
        Assert.assertArrayEquals(protocol.resolve(data),result);

        data=65535;
        result = new byte[]{ProtocolDataTypes.INT.value(),(byte)0xff,(byte)0xff,0,0};
        Assert.assertArrayEquals(protocol.resolve(data),result);

        data=Integer.MAX_VALUE;
        result = new byte[]{ProtocolDataTypes.INT.value(),(byte)0xff,(byte)0xff,(byte)0xff,(byte)0x7f};
        Assert.assertArrayEquals(protocol.resolve(data),result);


        data = -8;
        result = new byte[]{ProtocolDataTypes.INT.value(),(byte)-0x08,(byte)-0x01,(byte)-0x01,(byte)-0x01};
        Assert.assertArrayEquals(protocol.resolve(data),result);
    }

    /**
     * STRING
     * @throws Exception
     */
    @Test
    public void testResolve2() throws Exception {

        String data = "ABC";
        byte[] result = new byte[]{ProtocolDataTypes.STRING.value(),3,0,1,65,66,67};
        Assert.assertArrayEquals(protocol.resolve(data),result);

        data="dgdfhgfhsfgvkdsjglfetjubgg";
        result = new byte[data.length()+4];
        result[0]=ProtocolDataTypes.STRING.value();
        result[1] = (byte)(data.length()&0xff);
        result[2] = (byte)((data.length()>>8)&0xff);
        result[3] = 1;
        System.arraycopy(data.getBytes(), 0, result, 4, data.length());


        Assert.assertArrayEquals(protocol.resolve(data), result);

    }

    @Test
    public void testResolve3() throws Exception {

        float val = 13.56f;

        byte[] data = ConvertUtils.toBytes(val);

        for(int i=0;i<4;i++)
            System.out.print(Integer.toHexString(data[i]) + " ");


        System.out.println(ConvertUtils.toFloat(data[0], data[1], data[2], data[3]));




    }

    @Test
    public void testResolve4() throws Exception {

    }

    @Test
    public void testResolve5() throws Exception {

    }

    @Test
    public void testResolve6() throws Exception {

    }

    @Test
    public void testResolve7() throws Exception {

    }

    @Test
    public void testResolve8() throws Exception {

    }

    @Test
    public void testResolve9() throws Exception {

    }

    @Test
    public void testResolve10() throws Exception {

    }

    @Test
    public void testAssembleToInt() throws Exception {

    }
}