package com.trelloiii.simplereapitinglib;

import com.google.gson.Gson;
import com.trelloiii.simplereapitinglib.ioc.pool.ObjectPool;
import com.trelloiii.simplereapitinglib.scanner.test.Car;
import com.trelloiii.simplereapitinglib.web.pool.ControllersPool;
import com.trelloiii.simplereapitinglib.web.server.ConnectionListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage());
        ConnectionListener listener=new ConnectionListener(8080,configuration);
    }
}
