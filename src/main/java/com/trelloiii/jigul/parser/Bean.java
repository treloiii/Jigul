package com.trelloiii.jigul.parser;

import java.util.Arrays;

public class Bean {
    private String classPath;
    private Object[] constructorArgs;
    private String[] constructorTypes;
    private String instanceType;


    public void setConstructorTypes(String[] constructorTypes) {
        this.constructorTypes = constructorTypes;
    }

    public String[] getConstructorTypes() {
        return constructorTypes;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public void setConstructorArgs(Object[] constructorArgs) {
        this.constructorArgs = constructorArgs;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getClassPath() {
        return classPath;
    }

    public Object[] getConstructorArgs() {
        return constructorArgs;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public Bean(String classPath, Object[] constructorArgs, String[] constructorTypes, String instanceType) {
        this.classPath = classPath;
        this.constructorArgs = constructorArgs;
        this.constructorTypes = constructorTypes;
        this.instanceType = instanceType;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "classPath='" + classPath + '\'' +
                ", constructorArgs=" + Arrays.toString(constructorArgs) +
                ", instanceType='" + instanceType + '\'' +
                '}';
    }
}
