package agilor.distributed.web.inter.server.interceptor;

import agilor.distributed.relational.data.db.DB;
import agilor.distributed.relational.data.db.ExtendModel;
import agilor.distributed.relational.data.entities.*;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.DistrController;
import agilor.distributed.web.inter.server.result.Action;

import java.lang.annotation.*;

/**
 * Created by LQ on 2016/1/7.
 */
public class CreatorInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        Annotation[] annotations = invocation.getMethod().getDeclaredAnnotations();



        DistrController controller = (DistrController) invocation.getController();


        for (Annotation annotation : annotations) {

            if (annotation.annotationType() == Param.class) {
                Param param = (Param) annotation;
                ExtendModel model = null;
                if (param.model() == User.class)
                    model = DB.User.instance();
                else if (param.model() == Device.class)
                    model = DB.Device.instance();
                else if (param.model() == DeviceType.class)
                    model = DB.DeviceType.instance();
                else if (param.model() == SensorOfType.class)
                    model = DB.SensorOfType.instance();
                else if (param.model() == Sensor.class)
                    model = DB.Sensor.instance();

                int id = controller.getParaToInt(param.id(), 0);
                if (id == 0) {
                    if (param.nullAble()) continue;
                    else {
                        controller.renderResult(Action.validate(param.id()));
                        return;
                    }
                }
                model = (ExtendModel) model.findById(id);
                if (model == null) {
                    if (param.nullAble()) {
                        controller.setAttr(param.context(), null);
                        continue;
                    } else {
                        controller.renderResult(Action.notFound());
                        return;
                    }
                }

                try {
                    Creator data = (Creator) model.build(param.model());
                    if (data.getCreatorId() != controller.userId()) {
                        controller.renderResult(Action.error());
                        return;
                    }
                    else controller.setAttr(param.context(),data);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        invocation.invoke();
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Param {
        String id() default "i";

        Class<? extends Creator> model();

        String context() default "data";

        boolean nullAble() default false;
    }

}
