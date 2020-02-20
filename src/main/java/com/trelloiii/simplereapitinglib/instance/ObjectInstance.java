package com.trelloiii.simplereapitinglib.instance;

import com.trelloiii.simplereapitinglib.AutoGenerated;
import com.trelloiii.simplereapitinglib.scanner.test.Person;

import java.util.HashMap;
import java.util.List;

public class ObjectInstance {

    private SingleInstance singleInstance;
    private HashMap<Class,Object> singleInstances;
    private static ObjectInstance objectInstance;
    private ObjectInstance(List<Class<?>> classes) {
        singleInstance=SingleInstance.build();
        checkInstanceType(classes);
        singleInstances=singleInstance.getSingleObjects();
    }
    public static ObjectInstance builder(List<Class<?>> classes){
        if(objectInstance==null){
            objectInstance=new ObjectInstance(classes);
        }
        return objectInstance;
    }

    public Object getInstance(Class clazz){
        if(singleInstances.containsKey(clazz)){
            return singleInstances.get(clazz);
        }
        else{
            if(checkMultiInstance(clazz)){
                //new instance
                return multiInstance(clazz);
            }
            else{
                throw new IllegalArgumentException("Class "+clazz.getName()+" is not marked @AutoGenerated");
            }
        }
    }
    public HashMap<Class, Object> getSingleInstances() {
        return singleInstances;
    }

    private boolean checkMultiInstance(Class clazz){
        AutoGenerated autoGenerated=(AutoGenerated) clazz.getAnnotation(AutoGenerated.class);
        return !(autoGenerated ==null);
    }
    private void checkInstanceType(List<Class<?>> classes){
        for(Class clazz:classes){
            AutoGenerated autoGenerated=(AutoGenerated) clazz.getAnnotation(AutoGenerated.class);
            if(autoGenerated.instanceType().equals(InstanceType.SINGLE)){
                singleInstance(clazz);
            }
            else if(autoGenerated.instanceType().equals(InstanceType.MULTI)){
              //  multiInstance(clazz);
            }
            else{
                throw new IllegalArgumentException("Wrong InstanceTypeValue in class: "+clazz.getName());
            }
        }
    }

    private Object multiInstance(Class clazz) {
        System.out.println("class "+clazz.getName()+" is multi instance");
        try {
            return clazz.newInstance();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void singleInstance(Class clazz) {
        System.out.println("class "+clazz.getName()+" is single instance");
        singleInstance.instance(clazz);
    }
}
