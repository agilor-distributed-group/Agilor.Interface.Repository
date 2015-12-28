package controller;

import agilor.distributed.relational.data.entities.User;
import agilor.distributed.relational.data.exceptions.NullParameterException;
import agilor.distributed.relational.data.exceptions.ValidateParameterException;
import agilor.distributed.relational.data.services.UserService;



/**
 * Created by LQ on 2015/12/24.
 */
public class UserController extends DistrController {

    UserService service = null;



    public UserController() throws Exception {
        super();
        service = new UserService(getContext());
    }

    public void login() {
        String u = getPara("u");
        String p = getPara("p");


        User model = null;
        try {
            model = service.check(u, p);
            if (model != null)
                service.login(model);
        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        service.logout(null);
    }



    public void register() {

        try {
            User user = service.register(getPara("u"), getPara("p"));
            if(user!=null)
                service.login(user);


        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (ValidateParameterException e) {
            e.printStackTrace();
        }

        renderJson("r","ture");
    }








}
