package com.trelloiii.simplereapitinglib.scanner;
import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.instance.ObjectInstance;
import com.trelloiii.simplereapitinglib.scanner.test.Car;
import com.trelloiii.simplereapitinglib.scanner.test.Person;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException, IllegalAccessException, InstantiationException {
        String pkg="com.trelloiii.simplereapitinglib.scanner.test";
        Configuration configuration=Configuration.build(new String[]{pkg});

        Car car=(Car) configuration.getPooledObject(Car.class);
        car.beep();
    }
}
