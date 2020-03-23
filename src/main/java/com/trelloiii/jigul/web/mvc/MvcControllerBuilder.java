package com.trelloiii.jigul.web.mvc;

import com.sun.tools.javac.processing.AnnotationProcessingError;
import com.trelloiii.jigul.ioc.instance.FieldInstance;
import com.trelloiii.jigul.ioc.instance.ObjectInstance;
import com.trelloiii.jigul.scanner.AnnotationNotFoundException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MvcControllerBuilder {

    private static MvcControllerBuilder mvcControllerBuilder;
    private Map<String,MvcController> mvcControllerMap;

    private MvcControllerBuilder(List<Class<? extends MvcController>> classes, ObjectInstance objectInstance){
        mvcControllerMap=new HashMap<>();
        for(Class<? extends MvcController> clazz:classes){
            try{
                MvcController controller=clazz.newInstance();
                mvcControllerMap.put(scanPath(clazz),controller);
                FieldInstance fieldInstance=new FieldInstance(objectInstance);
                fieldInstance.fieldInstance(controller,clazz);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Map<String, MvcController> getMvcControllerMap() {
        return mvcControllerMap;
    }

    public static MvcControllerBuilder builder(List<Class<? extends MvcController>> classes, ObjectInstance objectInstance){
        if(mvcControllerBuilder==null){
            mvcControllerBuilder=new MvcControllerBuilder(classes,objectInstance);
        }
        return mvcControllerBuilder;
    }

    private String scanPath(Class<? extends MvcController> clazz) throws AnnotationNotFoundException {
        Annotation[] annotations=clazz.getAnnotations();
        for(Annotation annotation:annotations){
            if(annotation instanceof MVCC){
                MVCC path=(MVCC) annotation;
                return path.path();
            }
        }
        throw new AnnotationNotFoundException("MVCC annotation not found in class: "+clazz.getName());
    }
}
