package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.AutoGenerated;
import com.trelloiii.simplereapitinglib.instance.InstanceType;

@AutoGenerated(instanceType = InstanceType.SINGLE)
public class Person {
    private String name="DEFAULT";
    private String surname="DEFAULT";


    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Person() {
        System.out.println("INSTANCE OF PERSON");
    }

    public String getName() {
        return name;
    }

    public void voice(){
        System.out.println("my name is: "+this.name);
    }
}
