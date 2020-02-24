package com.trelloiii.simplereapitinglib.scanner;
import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.instance.ObjectInstance;
import com.trelloiii.simplereapitinglib.pool.ControllersPool;
import com.trelloiii.simplereapitinglib.pool.ObjectPool;
import com.trelloiii.simplereapitinglib.scanner.test.Car;
import com.trelloiii.simplereapitinglib.scanner.test.Person;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String pkg="com.trelloiii.simplereapitinglib.scanner.test";
        Configuration configuration=Configuration.build(new String[]{pkg},new String[]{pkg});

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
        ControllersPool pool=new ControllersPool(configuration);
        pool.invokeMethod("doNothing");
    }
}
