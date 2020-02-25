package com.trelloiii.simplereapitinglib;

import com.trelloiii.simplereapitinglib.web.pool.ControllersPool;
import com.trelloiii.simplereapitinglib.web.server.ConnectionListener;

import java.util.Arrays;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage());

       // pool.invokeMethod("/doNothin","SUKA","returned");
        Object[] obj=new Object[3];
        obj[0]=3;
        obj[1]="s";
        obj[2]=true;
        System.out.println(Arrays.toString(obj));
        ConnectionListener listener=new ConnectionListener(8080,configuration);
    }
}
