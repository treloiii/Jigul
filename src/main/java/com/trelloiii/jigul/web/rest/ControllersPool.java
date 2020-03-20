package com.trelloiii.jigul.web.rest;

import com.google.gson.Gson;
import com.trelloiii.jigul.Configuration;
import com.trelloiii.jigul.web.*;
import com.trelloiii.jigul.web.httpcodes.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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

    public HttpCode invokeMethod(String methodName,Class<? extends Annotation> reqType, Map<String,String> params){
            try{
                Method invokable=methods.get(methodName);
                if(invokable!=null) {
                    Object controller=controllers.get(invokable.getDeclaringClass());
                    System.out.println("SSSSSSSSSSSSSSS");
                    if (invokable.getAnnotation(reqType) != null) {
                        if (params != null) {
                            Parameter[] parameters=invokable.getParameters();
                            String[] invokingParams=sotParams(parameters,params);
                            if(invokingParams==null)
                                return new BadRequest("Bad request. Wrong parameters name are provided");
                            Class<?>[] types = invokable.getParameterTypes();
                            Object[] invokableParams = convertingTypes(invokingParams, types);
                            return new Ok(invokable.invoke(controller, invokableParams));
                        } else {
                            return new Ok(invokable.invoke(controller));
                        }
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

    public String getCorsPolicy(String methodName){
        Method checked=methods.get(methodName);
        CrossOrigin cors=checked.getAnnotation(CrossOrigin.class);
        if(cors==null)
            return null;
        else
            return cors.domain();
    }

    private String[] sotParams(Parameter[] parameters,Map<String,String> params){
        List<String> rightParamsOrderByName=new ArrayList<>();
        String[] valuesResult=new String[parameters.length];
        int i=0;
        for(Parameter parameter:parameters){
            RequestParam param=parameter.getAnnotation(RequestParam.class);
            if(param==null)//IF @RequestBody presented
                break;
            rightParamsOrderByName.add(param.name());
            i++;
        }
        for(Map.Entry<String,String> entry:params.entrySet()){
            if(rightParamsOrderByName.size()==0) {// IF @RequestBody presented
                valuesResult[0] = entry.getValue();
                break;
            }
            int index=rightParamsOrderByName.indexOf(entry.getKey());
            if(index==-1)//Wrong name of parameter in request
                return null;
            valuesResult[index]=entry.getValue();
        }
        return valuesResult;
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
