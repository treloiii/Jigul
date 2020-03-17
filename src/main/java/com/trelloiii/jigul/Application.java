package com.trelloiii.jigul;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.trelloiii.jigul.json.Json;
import com.trelloiii.jigul.web.server.ConnectionListener;

import java.io.FileReader;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage(),configurationClass);
        ConnectionListener listener=new ConnectionListener(config.serverPort(),configuration);
    }
    public static void start(){
        Json config= readConfig();
        Configuration configuration=Configuration.build(config.getConfiguration().getIocPackages(),config.getConfiguration().getWebPackages(),config.getBeans());
        ConnectionListener listener=new ConnectionListener(config.getConfiguration().getServerPort(),configuration);
    }

    private static Json readConfig(){
        try {
            Gson gson = new Gson();
            String json = "/Users/trelloiii/Desktop/JavaProjects/Jigul/src/main/resources/jigul-config.json";
            Json config = gson.fromJson(new JsonReader(new FileReader(json)), Json.class);
            return config;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
