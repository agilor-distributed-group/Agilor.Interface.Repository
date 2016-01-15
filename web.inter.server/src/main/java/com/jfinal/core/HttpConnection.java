package com.jfinal.core;

import agilor.distributed.relational.data.context.Config;
import agilor.distributed.relational.data.context.IConnection;
import agilor.distributed.relational.data.context.RequestContext;

import javax.servlet.http.Cookie;
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
        //this.response.addHeader(key,value.toString());
        this.response.addCookie(new Cookie(key,value.toString()));
    }

    @Override
    public String getHost() {

        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
        //return this.request.getRemoteHost();
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
