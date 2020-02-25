package com.trelloiii.simplereapitinglib.web.pool;

import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.web.Get;
import com.trelloiii.simplereapitinglib.web.ControllerBuilder;
import com.trelloiii.simplereapitinglib.web.httpcodes.NotFound;

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

    public Object invokeMethod(String methodName,String... params){
            try{
                Map<Object,Method> pair=searchMethod(methodName);
                for(Map.Entry<Object,Method> entry:pair.entrySet()){
                    Method invokable=entry.getValue();
                    Object instance=entry.getKey();
                    System.out.println(Arrays.toString(invokable.getParameterTypes()));
                    Class<?>[] types=invokable.getParameterTypes();
                    Object[] invokableParams=convertingTypes(params,types);
                    return invokable.invoke(instance,invokableParams);
                }
                return new NotFound();
            }
            catch (Exception e){
                e.printStackTrace();
                return new NotFound();
            }

    }

    private Object[] convertingTypes(String[] params,Class<?>[] types) throws Exception {
        Object[] returned=new Object[params.length];
        for(int i=0;i<params.length;i++){
            if(types[i]==String.class)
                returned[i]= String.valueOf(params[i]);
            else if(types[i]==Integer.class)
                returned[i]= Integer.valueOf(params[i]);
            else if(types[i]==Short.class)
                returned[i]= Short.valueOf(params[i]);
            else if(types[i]==Long.class)
                returned[i]= Long.valueOf(params[i]);
            else if(types[i]==Byte.class)
                returned[i]= Byte.valueOf(params[i]);
            else if(types[i]==Float.class)
                returned[i]= Float.valueOf(params[i]);
            else if(types[i]==Double.class)
                returned[i]= Double.valueOf(params[i]);
            else if(types[i]==Character.class) {
                if(params[i].length()>1)
                    throw new Exception("Cannot convert string to char");
                else
                    returned[i] = params[i].charAt(0);
            }
            else if(types[i]==Boolean.class)
                returned[i]= Boolean.valueOf(params[i]);
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
        throw new Exception("Method not found");
    }
}
