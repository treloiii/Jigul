package com.trelloiii.jigul;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.trelloiii.jigul.parser.ConfigType;
import com.trelloiii.jigul.parser.Json;
import com.trelloiii.jigul.parser.XmlConfigParser;
import com.trelloiii.jigul.web.server.ConnectionListener;
import com.trelloiii.jigul.web.server.ConnectionType;

import java.io.FileReader;

public class Application {
    public static void start(Class<?> configurationClass){
        Config config=configurationClass.getAnnotation(Config.class);
        Configuration configuration=Configuration.build(config.iocPackage(),config.webPackage(),configurationClass);
        ConnectionListener listener=new ConnectionListener(config.serverPort(),configuration, ConnectionType.REST);
    }
    public static void start(ConfigType type){
        Json config= readConfig(type);
        Configuration configuration=Configuration.build(config.getConfiguration().getIocPackages(),config.getConfiguration().getWebPackages(),config.getBeans());
        ConnectionListener listener=new ConnectionListener(config.getConfiguration().getServerPort(),configuration,config.getConfiguration().getConnectionType());
    }

    private static Json readConfig(ConfigType type){
        try {
            if(type.equals(ConfigType.JSON)) {
                Gson gson = new Gson();
                String json = "src/main/resources/jigul-config.json";
                Json config = gson.fromJson(new JsonReader(new FileReader(json)), Json.class);
                return config;
            }
            else {
                XmlConfigParser parser=new XmlConfigParser();
                Json config=parser.parseConfig();
                return config;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
