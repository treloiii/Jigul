package com.trelloiii.jigul;

import com.trelloiii.jigul.ioc.instance.ObjectInstance;
import com.trelloiii.jigul.parser.Bean;
import com.trelloiii.jigul.scanner.AnnotationScanner;
import com.trelloiii.jigul.scanner.ClassScanner;
import com.trelloiii.jigul.web.ControllerBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private String[] IoCPackages;
    private String[] webPackages;
    private ObjectInstance objectInstance;
    private ControllerBuilder controllerBuilder;
    private static Configuration configuration;
    private Class<?> configClass;
    private List<Bean> beans;
    private Configuration(String[] IoCPackages,String[] webPackages,Class<?> configClass) {
        this.IoCPackages = IoCPackages;
        this.webPackages=webPackages;
        this.configClass=configClass;
        this.objectsPoolBuilder();
        this.controllersBuilder();
    }

    private Configuration(String[] IoCPackages,String[] webPackages,List<Bean> beans) {
        this.IoCPackages = IoCPackages;
        this.webPackages=webPackages;
//        this.configClass=configClass;
        this.beans=beans;
        this.objectsPoolBuilder();
        this.controllersBuilder();
    }
    public static Configuration build(String [] packages,String[] webPackages,Class<?> configClass){
        if(configuration==null){
            configuration=new Configuration(packages,webPackages,configClass);
        }
        return configuration;
    }
    public static Configuration build(String [] packages, String[] webPackages, List<Bean> beans){
        if(configuration==null){
            configuration=new Configuration(packages,webPackages,beans);
        }
        return configuration;
    }

    public static Configuration getConfiguration() throws InstantiationException {
        if(configuration==null)
            throw new InstantiationException("There is no setup configuration in your project, cannot instance null config");
        else
            return configuration;
    }

    public ObjectInstance getObjectInstance() {
        return objectInstance;
    }

    public ControllerBuilder getControllerBuilder() {
        return controllerBuilder;
    }

    private void objectsPoolBuilder(){
        ClassScanner scanner=ClassScanner.createScanner();
        AnnotationScanner annotationScanner=new AnnotationScanner();
        List<Class<?>> classes= new ArrayList<>();
        List<Field> fields=new ArrayList<>();
        for(String pkg: IoCPackages){
            try {
                classes.addAll(annotationScanner.scanAutoGenerated(scanner.scan(pkg)));
                //Map<List<Class<?>>,List<Field>> classAndFields=annotationScanner.scanAutoGenerated(scanner.scan(pkg));
//                for(Map.Entry<List<Class<?>>,List<Field>> entry:classAndFields.entrySet()){
//                    classes.addAll(entry.getKey());
//                    fields.addAll(entry.getValue());
//                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            if(configClass!=null) {
                this.objectInstance = ObjectInstance.builder(classes, annotationScanner.scanBean(configClass), configClass.newInstance());
            }
            else{
                this.objectInstance = ObjectInstance.builder(classes,beans);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    private void controllersBuilder(){
        ClassScanner scanner=ClassScanner.createScanner();
        AnnotationScanner annotationScanner=new AnnotationScanner();
        List<Class<?>> classes=new ArrayList<>();
        List<Method> methods=new ArrayList<>();
        for(String pkg:webPackages){
            try{
                classes.addAll(annotationScanner.scanControllers(scanner.scan(pkg)));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        this.controllerBuilder=ControllerBuilder.builder(classes,objectInstance);
    }

    @Deprecated
    private Object getPooledObject(Class clazz){
        return objectInstance.getInstance(clazz);
    }
}
