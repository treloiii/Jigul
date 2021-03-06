package com.trelloiii.jigul.web.rest;

import com.trelloiii.jigul.ioc.instance.FieldInstance;
import com.trelloiii.jigul.ioc.instance.ObjectInstance;
import com.trelloiii.jigul.web.Get;
import com.trelloiii.jigul.web.Post;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerBuilder {
    private static ControllerBuilder builder;
    private Map<Class<?>,Object> controllers;
    private Map<String,Method> methods;
    private ControllerBuilder(List<Class<?>> classes,ObjectInstance objectInstance){
        controllers=new HashMap<>();
        methods=new HashMap<>();
        for(Class<?> clazz:classes){
            try {
                Object controller=clazz.newInstance();
                controllers.put(clazz,controller);
                FieldInstance fieldInstance=new FieldInstance(objectInstance);
                fieldInstance.fieldInstance(controller,clazz);
                methods.putAll(scanGetPost(clazz));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public static ControllerBuilder builder(List<Class<?>> classes,ObjectInstance objectInstance){
        if(builder==null){
            builder=new ControllerBuilder(classes,objectInstance);
        }
        return builder;
    }
    @Deprecated
    private List<Method> scanGetPostOld(Class<?> clazz){
        Method [] methods=clazz.getMethods();
        List<Method> result=new ArrayList<>();
        for(Method method:methods){
            Annotation[] annotations=method.getAnnotations();
            for(Annotation annotation:annotations){
                if(annotation instanceof Get){
                    result.add(method);
                }
                else if(annotation instanceof Post)
                    result.add(method);
            }
        }
        return result;
    }
    private Map<String,Method> scanGetPost(Class<?> clazz){
        Method [] methods=clazz.getMethods();
        Map<String,Method> result=new HashMap<>();
        for(Method method:methods){
            Annotation[] annotations=method.getAnnotations();
            for(Annotation annotation:annotations){
                if(annotation instanceof Get){
                    Get get=(Get) annotation;
                    result.put(get.path(),method);
                }
                else if(annotation instanceof Post) {
                    Post post = (Post) annotation;
                    result.put(post.path(), method);
                }
            }
        }
        return result;
    }
}
