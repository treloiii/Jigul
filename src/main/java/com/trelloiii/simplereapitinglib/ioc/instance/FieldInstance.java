package com.trelloiii.simplereapitinglib.ioc.instance;

import com.trelloiii.simplereapitinglib.ioc.Injectable;
import com.trelloiii.simplereapitinglib.web.ControllerBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
public class FieldInstance {
    private ObjectInstance objectInstance;
    private ControllerBuilder controllerBuilder;

    public FieldInstance(ObjectInstance objectInstance) {
        this.objectInstance = objectInstance;
    }
    public FieldInstance(ControllerBuilder controllerBuilder) {
        this.controllerBuilder = controllerBuilder;
    }

    public void injectToSingleInstances(){
        HashMap<Class,Object> singleInstances=objectInstance.getSingleInstances();
        for(Map.Entry<Class,Object> entry:singleInstances.entrySet()){
            Object target=entry.getValue();
            Class targetClass=entry.getKey();
            Field[] fields=targetClass.getDeclaredFields();
            for(Field field:fields){
                Annotation[] annotations=field.getDeclaredAnnotations();
                for(Annotation annotation:annotations){
                    if(annotation instanceof Injectable){
                        try {
                            field.setAccessible(true);
                            field.set(target,objectInstance.getInstance(field.getType()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    public void fieldInstance(Object o,Class clazz){
        Field[] fields=clazz.getDeclaredFields();
        for(Field field:fields){
            Annotation[] annotations=field.getDeclaredAnnotations();
            for(Annotation annotation:annotations){
                if(annotation instanceof Injectable){
                    try {
                        field.setAccessible(true);
                            field.set(o,objectInstance.getInstance(field.getType()));//o1 - which is injectable, o- who need instance of field
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
