package com.trelloiii.simplereapitinglib;

import com.google.gson.Gson;
import com.trelloiii.simplereapitinglib.web.pool.ControllersPool;
import com.trelloiii.simplereapitinglib.web.server.ConnectionListener;

import java.util.Arrays;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage());

        Gson gson=new Gson();
        Integer a=gson.fromJson("2323",Integer.class);
        System.out.println(a);
        ConnectionListener listener=new ConnectionListener(8080,configuration);
    }
}
