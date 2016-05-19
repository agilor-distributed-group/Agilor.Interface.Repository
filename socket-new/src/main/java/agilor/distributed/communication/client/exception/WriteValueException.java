package agilor.distributed.communication.client.exception;

/**
 * Created by LQ on 2015/11/18.
 */
public class WriteValueException extends Exception {
    private String device;
    private String target;
    public WriteValueException(String device,String target) {
        this.device = device;
        this.target = target;
    }


    @Override
    public String getMessage() {
        if (device != null && target != null)
            return "the " + device + "." + target + " write value error";
        else
            return "device or target is null";


    }
}
