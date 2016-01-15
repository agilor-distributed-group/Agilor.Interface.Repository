package result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.binding.StringFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LQ on 2016/1/6.
 */
public abstract class BaseResult implements Result {

    private final static Logger logger = LoggerFactory.getLogger(Result.class);


    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize() {

        StringBuilder builder = new StringBuilder();

        builder.append("{\"ok\":"+isOk());
        builder.append(",\"data\":"+getData());
        builder.append(",\"info\":"+getInfo());
        builder.append("}");

        return builder.toString();
    }



    protected boolean isOk(){
        return false;
    }

    protected Object getData(){
        return null;
    }

    protected String getInfo(){
        return null;
    }
}
