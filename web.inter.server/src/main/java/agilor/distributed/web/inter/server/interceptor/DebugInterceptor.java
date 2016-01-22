package agilor.distributed.web.inter.server.interceptor;

import agilor.distributed.web.inter.server.config.Global;
import agilor.distributed.web.inter.server.result.Action;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * Created by LQ on 2016/1/22.
 */
public class DebugInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        if(Global.isDebug())
            inv.invoke();
        else
            inv.getController().renderText(Action.disable().serialize());
    }
}
