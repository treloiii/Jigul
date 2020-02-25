package com.trelloiii.simplereapitinglib.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerBuilder {
    private static ControllerBuilder builder;
    //private List<Object> controllers;
    private Map<Object, List<Method>> controllers;
    private ControllerBuilder(List<Class<?>> classes){
        //controllers=new ArrayList<>();
        controllers =new HashMap<>();
        for(Class<?> clazz:classes){
            try {
                Object controller=clazz.newInstance();
                controllers.put(controller,scanGet(clazz));
                //controllers.add(controller);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Map<Object, List<Method>> getControllers() {
        return controllers;
    }

    public static ControllerBuilder builder(List<Class<?>> classes){
        if(builder==null){
            builder=new ControllerBuilder(classes);
        }
        return builder;
    }
    private List<Method> scanGet(Class<?> clazz){
        Method [] methods=clazz.getMethods();
        List<Method> result=new ArrayList<>();
        for(Method method:methods){
            Annotation[] annotations=method.getAnnotations();
            for(Annotation annotation:annotations){
                if(annotation instanceof Get){
                    result.add(method);
                }
            }
        }
        return result;
    }
}
