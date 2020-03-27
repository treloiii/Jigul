package com.trelloiii.jigul;

import com.trelloiii.jigul.web.server.ConnectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config {
    String[] iocPackage();
    String[] webPackage();
    ConnectionType applicationType() default ConnectionType.REST;
    int serverPort() default 8080;
}
