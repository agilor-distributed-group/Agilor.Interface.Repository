package interceptor;

import agilor.distributed.relational.data.entities.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.DistrController;
import result.Action;

/**
 * Created by LQ on 2016/1/6.
 */
public class LoginInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        DistrController controller = (DistrController) invocation.getController();
        try {
            User user = (User) controller.getContext().getSession("user");
            if (user == null)
                controller.renderText(Action.failed().serialize());
            else {

                invocation.getController().setSessionAttr("userId", user.getId());

                invocation.invoke();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
