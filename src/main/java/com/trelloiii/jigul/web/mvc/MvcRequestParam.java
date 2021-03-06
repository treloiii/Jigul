package com.trelloiii.jigul.web.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MvcRequestParam {
    String paramName();
    boolean required() default true;
    String defaultVal() default "";
    Class<?> defaultClass() default String.class;
}
