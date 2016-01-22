package agilor.distributed.web.inter.server.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.DistrController;
import agilor.distributed.web.inter.server.result.Action;

/**
 * Created by LQ on 2016/1/6.
 */
public class LoginInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {

        DistrController controller = (DistrController) invocation.getController();
        try {
            Integer id = (Integer) controller.getContext().getSession("user");
            if (id == null)
                controller.renderText(Action.failed().serialize());
            else {

                //invocation.getController().setSessionAttr("userId", id);
                invocation.getController().getRequest().setAttribute("userId", id);

                invocation.invoke();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
