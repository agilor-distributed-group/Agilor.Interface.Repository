package controller;

import agilor.distributed.relational.data.context.IConnection;
import agilor.distributed.relational.data.context.RequestContext;
import com.jfinal.core.Controller;
import com.jfinal.render.ContentType;

/**
 * Created by LQ on 2015/12/28.
 */
public class DistrController extends Controller {

    private RequestContext context =null;


    public DistrController() throws Exception {
        super();
        context = new RequestContext(new HttpConnection(getRequest(), getResponse()));

    }


    protected RequestContext getContext()
    {
        return context;
    }
}
