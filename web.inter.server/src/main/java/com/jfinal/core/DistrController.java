package com.jfinal.core;

import agilor.distributed.relational.data.context.RequestContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import agilor.distributed.web.inter.server.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQ on 2016/1/12.
 */
public class DistrController extends Controller {

    private final static Logger logger = LoggerFactory.getLogger(DistrController.class);


    private RequestContext context =null;

    protected static ObjectMapper mapper = new ObjectMapper();



    public RequestContext getContext() throws Exception {
        if (context == null) {
            logger.info("excaute getContext and create requestContext ");
            context = new RequestContext(new HttpConnection(getRequest(), getResponse()));



        }
        return context;
    }


    public <T> T toClass(String connect, Class<T> cls) throws IOException {
        return mapper.readValue(connect, cls);
    }


    public <T> List<T> toList(String connect,Class<T> cls) throws IOException {
        if(StringUtils.isEmpty(connect))
            return null;

        JavaType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class,cls);
        return (List<T>)mapper.readValue(connect,type);
    }


    public String toJson(Object data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }


    public int userId() {
        Integer id = getAttr("userId");
        return id == null ? 0 : id.intValue();
    }


    public void renderResult(Result result)
    {
        renderText(result.serialize());
    }

    void init(HttpServletRequest request, HttpServletResponse response, String urlPara) {

        super.init(request, response, urlPara);
        try {

            context = new RequestContext(new HttpConnection(getRequest(), getResponse()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> T getData()
    {
        return getData("data");
    }

    public <T> T getData(String key)
    {
        return getAttr(key);
    }

}
