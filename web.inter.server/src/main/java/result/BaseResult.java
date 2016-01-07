package result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.binding.StringFormatter;

/**
 * Created by LQ on 2016/1/6.
 */
public abstract class BaseResult implements Result {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize() {

        StringBuilder builder = new StringBuilder();

        builder.append("{ok:"+isOk());
        builder.append(",data:"+getData());
        builder.append("info:"+getInfo());
        builder.append("}");
        return null;
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
