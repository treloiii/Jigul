package com.trelloiii.simplereapitinglib.instance;

import java.util.HashMap;

public class SingleInstance implements Instance {

    private HashMap<Class,Object> singleObjects;
    private static SingleInstance singleInstance;
    private SingleInstance() {
        this.singleObjects = new HashMap<>();
    }
    public static SingleInstance build(){
        if(singleInstance==null){
            singleInstance=new SingleInstance();
        }
        return singleInstance;
    }

    public HashMap<Class, Object> getSingleObjects() {
        return singleObjects;
    }

    @Override
    public Object instance(Class clazz)  {
        if(!singleObjects.containsKey(clazz)){
            Object object= null;
            try {
                object = clazz.newInstance();
                singleObjects.put(clazz,object);
                return object;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        else{
            return singleObjects.get(clazz);
        }
    }
}
