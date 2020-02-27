package com.trelloiii.simplereapitinglib.web.pool;

import com.google.gson.Gson;
import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.web.Get;
import com.trelloiii.simplereapitinglib.web.ControllerBuilder;
import com.trelloiii.simplereapitinglib.web.Post;
import com.trelloiii.simplereapitinglib.web.httpcodes.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllersPool {
    private ControllerBuilder controllerBuilder;
    private Map<Object, List<Method>> controllers1;
    private Map<Class<?>,Object> controllers;
    private Map<String,Method> methods;
    public ControllersPool(Configuration configuration) {
        this.controllerBuilder = configuration.getControllerBuilder();
        this.controllers=controllerBuilder.getControllers();
        this.methods=controllerBuilder.getMethods();
    }

    public HttpCode invokeMethod(String methodName,Class<? extends Annotation> reqType, String... params){
            try{
                Method invokable=methods.get(methodName);
                Object controller=controllers.get(invokable.getDeclaringClass());
                if(invokable.getAnnotation(reqType)!=null) {
                    if (params != null) {
                        Class<?>[] types = invokable.getParameterTypes();
                        Object[] invokableParams = convertingTypes(params, types);
                        return new Ok(invokable.invoke(controller, invokableParams));
                    } else {
                        return new Ok(invokable.invoke(controller));
                    }
                }
                return new NotFound("Page not found");//BAD BAD BAD IDK
            }
            catch (IllegalArgumentException e){
                e.printStackTrace();
                return new BadRequest("Bad request. "+ e.getMessage());
            }
            catch (NotFoundException e1){
                e1.printStackTrace();
                return new NotFound("Page not found");
            }
            catch (Exception e2){
                e2.printStackTrace();
                return new InternalServerError("Internal server error. "+e2.getCause().toString());
            }

    }

    private Object[] convertingTypes(String[] params,Class<?>[] types) throws Exception {
        Object[] returned=new Object[params.length];
        Gson gson=new Gson();
        for(int i=0;i<params.length;i++){
            System.out.println(params[i]);
            returned[i]=gson.fromJson(params[i],types[i]);
        }
        return returned;
    }

    @Deprecated
    private Map<Object,Method> searchMethod(String methodName,Class<? extends Annotation> reqType) throws Exception {
        for(Map.Entry<Object,List<Method>> entry:controllers1.entrySet()){
            List<Method> methods=entry.getValue();
            for(Method method:methods) {
                try {
                    if (reqType == Get.class) {
                        Get a = method.getAnnotation(Get.class);
                        if (a.path().equals(methodName)) {
                            Map<Object, Method> result = new HashMap<>();
                            result.put(entry.getKey(), method);
                            return result;
                        }
                    } else {
                        Post a = method.getAnnotation(Post.class);
                        if (a.path().equals(methodName)) {
                            Map<Object, Method> result = new HashMap<>();
                            result.put(entry.getKey(), method);
                            return result;
                        }
                    }
                }
                catch (NullPointerException e) {
                    continue;
                }
            }
        }
        throw new NotFoundException("Method not found");
    }
}
