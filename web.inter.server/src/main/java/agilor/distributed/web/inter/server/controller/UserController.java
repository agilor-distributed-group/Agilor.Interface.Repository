package agilor.distributed.web.inter.server.controller;

import agilor.distributed.relational.data.entities.User;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.SqlHandlerException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.UserService;
import com.jfinal.aop.Before;
import com.jfinal.core.DistrController;
import agilor.distributed.web.inter.server.interceptor.LoginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import agilor.distributed.web.inter.server.result.Action;


/**
 * Created by LQ on 2015/12/24.
 */
public class UserController extends DistrController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);



    UserService service = null;




    public UserController() throws Exception {
        super();
        service = new UserService();
    }

    //public


    public void index()
    {
        render("/index.jsp");
    }


    public void isLogin() throws Exception {
        Integer id = (Integer) getContext().getSession("user");

        renderText(String.valueOf((id==null?false:true)));
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
            else {

                renderResult(Action.failed());
            }


        } catch (NullParameterException e) {
            renderResult(Action.validate(e.getName()));
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
            renderResult(Action.success());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void register() {
        try {

            User user = service.register(getPara("u"), getPara("p"));
            if (user != null)
                login();
            else {
                logger.info("register failed");
                logger.info("result {}",Action.failed().serialize());
                renderResult(Action.failed());
            }

        } catch (NullParameterException e) {
            renderResult(Action.validate(e.getName()));
        } catch (ValidateParameterException e) {
            renderResult(Action.validate(e.getName()));
        } catch (SqlHandlerException e) {

            switch (e.getType())
            {
                case SQL_ONLY_UNIQUE:renderResult(Action.exist());break;
                default:renderResult(Action.failed());break;
            }
        }
//        catch (SqlHandlerException e) {
//            e.printStackTrace();
//        }
    }


    public void ping()
    {
        renderNull();
        //renderText(getRequest().getContextPath());


    }










}
