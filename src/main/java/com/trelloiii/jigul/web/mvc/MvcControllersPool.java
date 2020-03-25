package com.trelloiii.jigul.web.mvc;

import com.google.gson.Gson;
import com.trelloiii.jigul.Configuration;
import com.trelloiii.jigul.web.httpcodes.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MvcControllersPool {
    private MvcControllerBuilder mvcControllerBuilder;
    private Map<String,MvcController> mvcControllerMap;

    public MvcControllersPool(Configuration configuration) {
        this.mvcControllerBuilder=configuration.getMvcControllerBuilder();
        this.mvcControllerMap=mvcControllerBuilder.getMvcControllerMap();
    }

    public HttpCode invokeMethod(String path,Map<String,String> params) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException, NoSuchMethodException {
        MvcController controller=mvcControllerMap.get(path);
        if(controller!=null) {
            HttpCode code;
            Class<?> controllerClass = controller.getClass();
            Method handler=controllerClass.getMethod("handle", Map.class);
            Map<String,Class<?>> paramTypes=new HashMap<>();
            Annotation[] reqParams=handler.getAnnotations();
            for(Annotation annotation:reqParams){
                if(annotation instanceof MvcRequestParam){
                    MvcRequestParam param=(MvcRequestParam) annotation;
                    paramTypes.put(param.paramName(),param.paramType());
                }
            }
            try {
                code=controller.handle(retypeParams(params,paramTypes));
            }
            catch (Exception e){
                e.printStackTrace();
                return new InternalServerError("Internal server error: "+e.getMessage());
            }

            JGLProcessor processor = new JGLProcessor(controller, controllerClass, getControllerView(path));
            String body = processor.compileJGL();
            if(code.getCODE()==200)
                return new Ok(body);
            else if(code.getCODE()==400){
                return new BadRequest(code.getBody());
            }
        }
        return new NotFound("Page not found");
        //TODO доделать инвок
    }

    private Map<String, Object> retypeParams(Map<String, String> params, Map<String, Class<?>> paramTypes) {
        Map<String,Object> resultMap=new HashMap<>();
        Gson gson=new Gson();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String paramName = entry.getKey();
                Object o = gson.fromJson(entry.getValue(), paramTypes.get(paramName));
                resultMap.put(paramName, o);
            }
            return resultMap;
        }
        catch (NullPointerException e){
            return null;
        }
    }

    private String getControllerView(String path) throws IOException {
        StringBuilder a = new StringBuilder();
        FileReader fileReader;
        if(!path.equals("/")) {
            fileReader = new FileReader("src/main/resources/" + path + ".jgl");
        }
        else {
            fileReader = new FileReader("src/main/resources/index.jgl");
        }
        int c;
        while ((c = fileReader.read()) != -1) {
            a.append((char) c);
        }
        fileReader.close();
        return a.toString();
    }
}
