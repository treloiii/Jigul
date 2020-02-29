package com.trelloiii.jigul.scanner;
import com.trelloiii.jigul.Application;
import com.trelloiii.jigul.scanner.test.TestConfig;

public class Main {
    public static void main(String[] args){
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
        Application.start(TestConfig.class);
    }
}
