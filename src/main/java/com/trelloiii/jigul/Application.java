package com.trelloiii.jigul;

import com.trelloiii.jigul.web.server.ConnectionListener;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage());
        ConnectionListener listener=new ConnectionListener(8080,configuration);
    }
}
