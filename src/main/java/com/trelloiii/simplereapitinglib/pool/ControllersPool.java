package com.trelloiii.simplereapitinglib.pool;

import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.web.ControllerBuilder;

import java.lang.reflect.Method;
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

    public void invokeMethod(String methodName,Object... params){
            try{
                Map<Object,Method> pair=searchMethod(methodName);
                for(Map.Entry<Object,Method> entry:pair.entrySet()){
                    Method invokable=entry.getValue();
                    Object instance=entry.getKey();
                    invokable.invoke(instance,params);//TODO parse params
                }

            }
            catch (Exception e){
                e.printStackTrace();
                //TODO return 404 not found
            }
    }

    private Map<Object,Method> searchMethod(String methodName) throws Exception {
        for(Map.Entry<Object,List<Method>> entry:controllers.entrySet()){
            List<Method> methods=entry.getValue();
            for(Method method:methods){
                if(method.getName().equals(methodName)) {
                    Map<Object,Method> result=new HashMap<>();
                    result.put(entry.getKey(),method);
                    return result;
                }
            }
        }
        throw new Exception("Method not found");
    }
}
