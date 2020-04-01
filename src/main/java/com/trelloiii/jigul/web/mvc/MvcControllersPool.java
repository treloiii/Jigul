package com.trelloiii.jigul.web.mvc;

import com.google.gson.Gson;
import com.trelloiii.jigul.Configuration;
import com.trelloiii.jigul.web.httpcodes.*;

import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

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
            Method[] methods=controllerClass.getMethods();
            Method handler=null;
            for(Method m:methods){
                if(m.getName().equals("handle")){
                    handler=m;
                    break;
                }
            }
            Map<RequestParam,Class<?>> paramTypes=new LinkedHashMap<>();
            Parameter[] reqParams=handler.getParameters();
            for(Parameter parameter:reqParams){
                Annotation[] annotations=parameter.getAnnotations();
                for(Annotation annotation:annotations) {
                    if (annotation instanceof MvcRequestParam) {
                        MvcRequestParam param = (MvcRequestParam) annotation;
                        paramTypes.put(new RequestParam(param.paramName(),param.defaultVal(),param.required()),parameter.getType());
                    }
                    else if(annotation instanceof MvcRequestBody){
                        paramTypes.put(new RequestParam("BODY",null,true),parameter.getType());
                    }
                }
            }
            try {
                Object[] invokeParams=retypeParams(params,paramTypes);
                code=(HttpCode) handler.invoke(controller,invokeParams);
                System.out.println(code);
            }
            catch (BadRequestException | IllegalArgumentException e){
                e.printStackTrace();
                return new BadRequest(e.getMessage());
            } catch (Exception e){
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

    private Object[] retypeParams(Map<String, String> params, Map<RequestParam, Class<?>> paramTypes) throws BadRequestException {
        Gson gson=new Gson();
        try {
            List<Object> result=new ArrayList<>();
            for (Map.Entry<RequestParam, Class<?>> entry : paramTypes.entrySet()) {
                String paramName = entry.getKey().getParamName();
                String paramValue=params.get(paramName);
                if(paramValue==null){
                    if(entry.getKey().isRequired()) {
                        throw new BadRequestException("param " + paramName + " is required, but not provided in request");
                    }
                    else{
                        Object o = gson.fromJson(entry.getKey().getDefaultValue(), entry.getValue());
                        result.add(o);
                    }
                }
                else {
                    Object o = gson.fromJson(paramValue, entry.getValue());
                    result.add(o);
                }
            }
            return result.toArray(new Object[0]);
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
class RequestParam{
    private String paramName;
    private String defaultValue;
    private boolean required;

    public RequestParam(String paramName, String defaultValue, boolean required) {
        this.paramName = paramName;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getParamName() {
        return paramName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }
}
