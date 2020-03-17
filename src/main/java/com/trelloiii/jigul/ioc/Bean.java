package com.trelloiii.jigul.ioc;

import com.trelloiii.jigul.ioc.instance.InstanceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bean {
    String instanceType() default InstanceType.SINGLE;
}
