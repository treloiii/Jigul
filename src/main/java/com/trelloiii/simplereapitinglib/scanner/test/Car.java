package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.AutoGenerated;
import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.Injectable;
import com.trelloiii.simplereapitinglib.instance.InstanceType;

@AutoGenerated(instanceType = InstanceType.SINGLE)
public class Car {
    private String mark;
    private String color;
    @Injectable
    private Person driver;

    public Car(String mark, String color) {
        this.mark = mark;
        this.color = color;


    }

    public Car() {
        this.mark="Mercedes";
        this.color="Black";
//        Configuration configuration= null;
//        try {
//            configuration = Configuration.getConfiguration();
//            this.driver=(Person) configuration.getPooledObject(Person.class);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
    }

    public Person getDriver() {
        return driver;
    }

    public void beep(){
        System.out.println(this.mark+" : "+this.color+", driver is:"+this.driver.getName());
    }
}
