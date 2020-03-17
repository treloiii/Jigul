package com.trelloiii.jigul.ioc.instance;

import com.trelloiii.jigul.ioc.AutoGenerated;

import com.trelloiii.jigul.json.Bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ObjectInstance {

    private SingleInstance singleInstance;
    private static FieldInstance fieldInstance;
    private HashMap<Class,Object> singleInstances;
    private static ObjectInstance objectInstance;
    private Object userConfiguration;
    private HashMap<Class<?>,Method> methodClasses;
    private HashMap<Class<?>,Bean> beanClasses;
    private ObjectInstance(List<Class<?>> classes,List<Method> methods,Object userConfiguration) {
        singleInstance=SingleInstance.build();
        methodClasses =new HashMap<>();
        beanClasses =new HashMap<>();
        this.userConfiguration=userConfiguration;
        checkInstanceType(classes);
        checkBean(methods);
        singleInstances=singleInstance.getSingleObjects();
    }
    private ObjectInstance(List<Class<?>> classes,List<Bean> beans) {
        singleInstance=SingleInstance.build();
        beanClasses =new HashMap<>();
        methodClasses =new HashMap<>();
        checkInstanceType(classes);
        checkBean(beans.toArray(new Bean[0]));
        singleInstances=singleInstance.getSingleObjects();
    }
    public static ObjectInstance builder(List<Class<?>> classes,List<Method> methods,Object userConfiguration){
        if(objectInstance==null){
            objectInstance=new ObjectInstance(classes,methods,userConfiguration);
            fieldInstance=new FieldInstance(objectInstance);
            fieldInstance.injectToSingleInstances();
        }

        return objectInstance;
    }
    public static ObjectInstance builder(List<Class<?>> classes, List<Bean> beans){
        if(objectInstance==null){
            objectInstance=new ObjectInstance(classes,beans);
            fieldInstance=new FieldInstance(objectInstance);
            fieldInstance.injectToSingleInstances();
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
            else if(methodClasses.containsKey(clazz)){
                return multiInstance(methodClasses.get(clazz));
            }
            else if(beanClasses.containsKey(clazz)){
                return multiInstance(beanClasses.get(clazz));
            }
            else{
                throw new IllegalArgumentException("Class "+clazz.getName()+" is not marked @AutoGenerated or marked as bean");
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
            Object returned=clazz.newInstance();
            fieldInstance.fieldInstance(returned,clazz);
            return returned;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private Object multiInstance(Method m){
        try {
            Object returned=m.invoke(this.userConfiguration);
            fieldInstance.fieldInstance(returned,m.getReturnType());
            return returned;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Object multiInstance(Bean bean){
        try{
            Class<?> clazz = Class.forName(bean.getClassPath());
            Class<?>[] constructorTypes=new Class<?>[bean.getConstructorTypes().length];
            for(int i=0;i<bean.getConstructorTypes().length;i++){
                constructorTypes[i]=Class.forName(bean.getConstructorTypes()[i]);
            }
            Constructor constructor=clazz.getConstructor(constructorTypes);
            Object buildedBean=constructor.newInstance(bean.getConstructorArgs());
            fieldInstance.fieldInstance(buildedBean,clazz);
            return buildedBean;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void singleInstance(Class clazz) {
        System.out.println("class "+clazz.getName()+" is single instance");
        singleInstance.instance(clazz);
    }

    private void checkBean(List<Method> methods){
        for(Method method:methods){
            com.trelloiii.jigul.ioc.Bean bean=method.getAnnotation(com.trelloiii.jigul.ioc.Bean.class);
            methodClasses.put(method.getReturnType(),method);
            if(bean.instanceType().equals(InstanceType.SINGLE)){
                try {
                    Object buildedBean=method.invoke(this.userConfiguration);
                    singleInstance.instance(buildedBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            else if(bean.instanceType().equals(InstanceType.MULTI)){
                //TODO bean multi instance????????
            }
            else {
                throw new IllegalArgumentException("Wrong InstanceTypeValue in bean "+method.getName());
            }

        }
    }

    private void checkBean(Bean[] beans){
        for(Bean bean:beans){
            try {
                Class<?> clazz = Class.forName(bean.getClassPath());
                beanClasses.put(clazz,bean);
                if(bean.getInstanceType().equals(InstanceType.SINGLE)){
                    Class<?>[] constructorTypes=new Class<?>[bean.getConstructorTypes().length];
                    for(int i=0;i<bean.getConstructorTypes().length;i++){
                        constructorTypes[i]=Class.forName(bean.getConstructorTypes()[i]);
                    }
                    Constructor constructor=clazz.getConstructor(constructorTypes);
                    Object buildedBean=constructor.newInstance(bean.getConstructorArgs());
                    singleInstance.instance(buildedBean);
                }
                else if(bean.getInstanceType().equals(InstanceType.MULTI)){

                }
                else{
                    throw new IllegalArgumentException("Wrong InstanceTypeValue in bean "+bean.getClassPath());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
