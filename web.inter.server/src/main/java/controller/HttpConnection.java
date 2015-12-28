package controller;

import agilor.distributed.relational.data.context.IConnection;
import agilor.distributed.relational.data.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by LQ on 2015/12/27.
 */
public class HttpConnection implements IConnection {

    private HttpServletRequest request=null;
    private HttpServletResponse response=null;


    private RequestContext context = null;

    public HttpConnection(HttpServletRequest request,HttpServletResponse response) throws Exception {
        this.request=request;
        this.response=response;

    }



    @Override
    public void addResponseData(String key, Object value) {
        this.response.addHeader(key,value.toString());
    }

    @Override
    public String getHost() {
        return this.request.getRemoteHost();
    }

    @Override
    public int getPort() {
        return this.request.getRemotePort();
    }

    @Override
    public String attr(String key) {
        return this.request.getParameter(key);
    }

}
