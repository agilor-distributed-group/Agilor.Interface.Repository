package agilor.distributed.storage.inter.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by LQ on 2015/8/31.
 */
public class TAddress {
    private String address;
    private int port;
    private int timeOut;


    private final static Logger logger = LoggerFactory.getLogger(TAddress.class);


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;


    }


//    @Override
//    public boolean equals(Object obj)
//    {
//
//        logger.info("trigger class of TAddress's equeals");
//
//        if(obj instanceof TAddress)
//        {
//            return this.address==((TAddress) obj).getAddress() && this.port==((TAddress) obj).getPort();
//        }
//        return false;
//
//    }




}
