package com.trelloiii.simplereapitinglib.web.pool;

import com.google.gson.Gson;
import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.web.Get;
import com.trelloiii.simplereapitinglib.web.ControllerBuilder;
import com.trelloiii.simplereapitinglib.web.httpcodes.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllersPool {
    private ControllerBuilder controllerBuilder;
    private Map<Object, List<Method>> controllers;
    public ControllersPool(Configuration configuration) {
        this.controllerBuilder = configuration.getControllerBuilder();
        this.controllers=controllerBuilder.getControllers();
    }

    public HttpCode invokeMethod(String methodName, String... params){
            try{
                Map<Object,Method> pair=searchMethod(methodName);
                for(Map.Entry<Object,Method> entry:pair.entrySet()){
                    Method invokable=entry.getValue();
                    Object instance=entry.getKey();
//                    System.out.println(Arrays.toString(invokable.getParameterTypes()));
                    if(params!=null) {
                        Class<?>[] types = invokable.getParameterTypes();
                        Object[] invokableParams = convertingTypes(params, types);
                        return new Ok(invokable.invoke(instance, invokableParams));
                    }
                    else{
                        return new Ok(invokable.invoke(instance));
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
//            if(types[i]==String.class)
//                returned[i]= String.valueOf(params[i]);
//            else if(types[i]==Integer.class)
//                returned[i]= Integer.valueOf(params[i]);
//            else if(types[i]==Short.class)
//                returned[i]= Short.valueOf(params[i]);
//            else if(types[i]==Long.class)
//                returned[i]= Long.valueOf(params[i]);
//            else if(types[i]==Byte.class)
//                returned[i]= Byte.valueOf(params[i]);
//            else if(types[i]==Float.class)
//                returned[i]= Float.valueOf(params[i]);
//            else if(types[i]==Double.class)
//                returned[i]= Double.valueOf(params[i]);
//            else if(types[i]==Character.class) {
//                if(params[i].length()>1)
//                    throw new Exception("Cannot convert string to char");
//                else
//                    returned[i] = params[i].charAt(0);
//            }
//            else if(types[i]==Boolean.class)
//                returned[i]= Boolean.valueOf(params[i]);
            System.out.println(params[i]);
            returned[i]=gson.fromJson(params[i],types[i]);
        }
        return returned;
    }

    private Map<Object,Method> searchMethod(String methodName) throws Exception {
        for(Map.Entry<Object,List<Method>> entry:controllers.entrySet()){
            List<Method> methods=entry.getValue();
            for(Method method:methods){
                Get annotation=method.getAnnotation(Get.class);
                System.out.println(annotation.path());
                if(annotation.path().equals(methodName)) {
                    Map<Object,Method> result=new HashMap<>();
                    result.put(entry.getKey(),method);
                    return result;
                }
            }
        }
        throw new NotFoundException("Method not found");
    }
}
