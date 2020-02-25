package com.trelloiii.simplereapitinglib;

import com.trelloiii.simplereapitinglib.pool.ControllersPool;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage());
        ControllersPool pool=new ControllersPool(configuration);
        String a=(String)pool.invokeMethod("/doNothing","SUKA","returned");
    }
}
