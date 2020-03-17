package com.trelloiii.jigul.scanner;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.trelloiii.jigul.Application;
import com.trelloiii.jigul.json.Json;
import com.trelloiii.jigul.json.JsonConfiguration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
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
            Application.start();
    }
}
