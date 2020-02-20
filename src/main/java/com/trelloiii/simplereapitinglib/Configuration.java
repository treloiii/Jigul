package com.trelloiii.simplereapitinglib;

import com.trelloiii.simplereapitinglib.instance.FieldInstance;
import com.trelloiii.simplereapitinglib.instance.ObjectInstance;
import com.trelloiii.simplereapitinglib.scanner.AnnotationScanner;
import com.trelloiii.simplereapitinglib.scanner.ClassScanner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Configuration {

    private String[] packages;
    private ObjectInstance objectInstance;
    private static Configuration configuration;
    private Configuration(String[] packages) {
        this.packages = packages;
        this.objectsPoolBuilder();
    }
    public static Configuration build(String [] packages){
        if(configuration==null){
            configuration=new Configuration(packages);
        }
        return configuration;
    }
    public static Configuration getConfiguration() throws InstantiationException {
        if(configuration==null)
            throw new InstantiationException("There is no setup configuration in your project, cannot instance null config");
        else
            return configuration;
    }

    private void objectsPoolBuilder(){
        ClassScanner scanner=ClassScanner.createScanner();
        AnnotationScanner annotationScanner=new AnnotationScanner();
        List<Class<?>> classes= new ArrayList<>();
        List<Field> fields=new ArrayList<>();
        for(String pkg:packages){
            try {
                Map<List<Class<?>>,List<Field>> classAndFields=annotationScanner.scanAutoGenerated(scanner.scan(pkg));
                for(Map.Entry<List<Class<?>>,List<Field>> entry:classAndFields.entrySet()){
                    classes.addAll(entry.getKey());
                    fields.addAll(entry.getValue());
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        this.objectInstance=ObjectInstance.builder(classes);
        FieldInstance fieldInstance=new FieldInstance(objectInstance);
        fieldInstance.injectToSingleInstances();
    }

    public Object getPooledObject(Class clazz){
        return objectInstance.getInstance(clazz);
    }
}
