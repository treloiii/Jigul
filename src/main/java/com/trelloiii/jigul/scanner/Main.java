package com.trelloiii.jigul.scanner;
import com.trelloiii.jigul.Application;
import com.trelloiii.jigul.parser.ConfigType;
import com.trelloiii.jigul.parser.Json;
import com.trelloiii.jigul.parser.XmlConfigParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        String pkg="com.trelloiii.simplereapitinglib.scanner.test";
       // Configuration configuration=Configuration.build(new String[]{pkg},new String[]{pkg});

//        ObjectPool pool=new ObjectPool(configuration);
//        Car car=pool.getPooledObject(Car.class);
//        Class<Car> clazz=Car.class;
//        try {
//            Method method=clazz.getMethod("getMark");
//            method.invoke(car);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        //car.beep();
//        car.getDriver().voice();
//        car.pickUpPassenger();
//        car.beep();
        //Application.start(TestConfig.class);
//        Gson gson=new Gson();
//        String json= "/Users/trelloiii/Desktop/JavaProjects/Jigul/src/main/resources/jigul-config.json";
//        Json config=gson.fromJson(new JsonReader(new FileReader(json)), Json.class);
//        System.out.println(Arrays.toString(config.getBeans().toArray()));
//            Application.start();
//        XmlConfigParser parser=new XmlConfigParser();
////        Json json=parser.parseConfig();
////        System.out.println(json.toString());
        Application.start(ConfigType.XML);
    }
}
