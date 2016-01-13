package controller;

import com.jfinal.core.Controller;
import com.jfinal.core.DistrController;

/**
 * Created by LQ on 2016/1/12.
 */
public class TestController extends DistrController {

    public TestController() throws Exception {
    }

    public void ping(){
        renderText("ping finish");
    }
}
