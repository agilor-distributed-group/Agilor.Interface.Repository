package agilor.distributed.communication.utils;

/**
 * Created by xinlongli on 16/3/30.
 */
public class Constant {
    public static final byte COMM_ADD_TAG=1;
    public static final byte COMM_ADD_VAL=2;
    public static final byte COMM_GET_VALUE =3;
    public static final byte COMM_GET_ALL_TAG=4;


    public static final int COMM_HEAD_LEN=5;

    public static final int HEADLEN=4;

    public static final byte RES_OK=0;
    public static final byte RES_FAIL_COMMON=1;
    public static final byte RES_FAIL_NO_TAG=2;
    public static final byte RES_FAIL_RETURN_EMPTY=3;

    public static final int DQBUFFER_ELEMENT_LEN=1024;
    public static final int DQBUFFER_QUEUE_LEN=10;
    public static final int RECV_BUFFER_LEN=1024;//need be longer than max constant receive length

    public static final int SEND_BUFFER_SIZE=1024*1024;
    public static final int RECV_BUFFER_SIZE=1024*1024;

}
