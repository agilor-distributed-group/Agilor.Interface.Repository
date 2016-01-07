package controller;

import agilor.distributed.relational.data.context.IConnection;
import agilor.distributed.relational.data.context.RequestContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.core.Controller;
import com.jfinal.render.ContentType;
import result.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2015/12/28.
 */
public class DistrController extends Controller {

    private RequestContext context =null;

    protected static ObjectMapper mapper = new ObjectMapper();


    public DistrController() throws Exception {
        super();
        context = new RequestContext(new HttpConnection(getRequest(), getResponse()));

    }


    public RequestContext getContext()
    {
        return context;
    }


    public <T> T toClass(String connect, Class<T> cls) throws IOException {
        return mapper.readValue(connect, cls);
    }


    public <T> List<T> toList(String connect,Class<T> cls) throws IOException {
        return mapper.readValue(connect, new TypeReference<List<T>>() {
        });
    }


    public int userId()
    {
        Integer id = getSessionAttr("userId");
        return id==null?0:id.intValue();
    }


    public void renderResult(Result result)
    {
        renderText(result.serialize());
    }

}
