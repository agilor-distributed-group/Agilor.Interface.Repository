package agilor.distributed.communication.utils;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by LQ on 2015/11/9.
 */
public class ConvertUtils {
    public static int toInt(byte[] data) {
        return toInt(data[0], data[1], data[2], data[3]);
    }

    public static int toInt(byte l1,byte l2,byte h1, byte h2)
    {
        int result = 0;
        result += (l1 & 0xff);
        result += (l2 & 0xff) << 8;
        result += (h1 & 0xff) << 16;
        result += (h2 & 0xff) << 24;
        return result;
    }

    public static float toFloat(byte l1,byte l2,byte h1, byte h2) {
        return Float.intBitsToFloat(ConvertUtils.toInt(l1, l2, h1, h2));
    }


    public static byte[] toBytes(int data) {
        byte[] result = new byte[4];
        result[0] = (byte) (data & 0xff);
        result[1] = (byte) ((data >> 8) & 0xff);
        result[2] = (byte) ((data >> 16) & 0xff);
        result[3] = (byte) ((data >> 24) & 0xff);
        return result;
    }

    public static byte[] toBytes(float data) {
        int result = Float.floatToIntBits(data);
        return toBytes(result);
    }
}
