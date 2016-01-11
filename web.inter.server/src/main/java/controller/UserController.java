package controller;

import agilor.distributed.relational.data.entities.User;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.UserService;
import com.jfinal.aop.Before;
import interceptor.LoginInterceptor;
import result.Action;


/**
 * Created by LQ on 2015/12/24.
 */
public class UserController extends DistrController {

    UserService service = null;



    public UserController() throws Exception {
        super();
        service = new UserService();
    }

    public void login() {
        String u = getPara("u");
        String p = getPara("p");



        try {
            User model = service.check(u, p);

            if(model!=null) {
                getContext().setSession("user", model.getId());
                renderResult(Action.success());
            }
            else
                renderResult(Action.failed());


        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before(LoginInterceptor.class)
    public void logout() {
        try {
            getContext().removeSession("user");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void register() {

        try {
            User user = service.register(getPara("u"), getPara("p"));
            if (user != null)
                login();
            else
                renderResult(Action.failed());

        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        } catch (SqlHandlerException e) {
            e.printStackTrace();
        }
    }








}
