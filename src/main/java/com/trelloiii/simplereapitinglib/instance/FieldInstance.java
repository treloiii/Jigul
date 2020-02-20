package com.trelloiii.simplereapitinglib.instance;

import com.trelloiii.simplereapitinglib.Injectable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldInstance {
    private ObjectInstance objectInstance;

    public FieldInstance(ObjectInstance objectInstance) {
        this.objectInstance = objectInstance;
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
                        field.set(o,objectInstance.getInstance(field.getType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
