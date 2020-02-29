package com.trelloiii.jigul.scanner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    private final char PKG_SEPARATOR='.';
    private final char DIR_SEPARATOR='/';
    private final String CLASS_FILE_SUFFIX=".class";

    private static ClassScanner classScanner;

    private ClassScanner(){}

    public static ClassScanner createScanner(){
        if(classScanner==null){
            classScanner=new ClassScanner();
        }
        return classScanner;
    }

    public List<Class<?>> scan(String basePackage) throws UnsupportedEncodingException {
        String scannedPath=basePackage.replace(this.PKG_SEPARATOR,this.DIR_SEPARATOR);
        URL undecodedScannedURL=Thread.currentThread().getContextClassLoader().getResource(scannedPath);//URL of package in file system
        String crc=undecodedScannedURL.toString();//String maybe in cyrillic, need to decode
        String decodedPath=URLDecoder.decode(crc,"utf-8");
        URL scannedURL= null;
        try {
            scannedURL = new URL(decodedPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(scannedURL==null){//If no url throw exception
           throw new IllegalArgumentException("Cannot find package with path '"+scannedPath+"'. Are package '"+basePackage+"' exists?");
        }
        File scannedDir=new File(scannedURL.getFile());//dir of classes
        List<Class<?>> classes=new ArrayList<Class<?>>();
        System.out.println(scannedDir.isDirectory());
        for (File file:scannedDir.listFiles()){// for each file in dir
            classes.addAll(scan(file,basePackage));// deep search classes in child dirs and root dir
        }
        return classes;
    }

    private List<Class<?>> scan(File file,String basePackage){
        List<Class<?>> classes=new ArrayList<Class<?>>();
        String resource=basePackage+this.PKG_SEPARATOR+file.getName();
        if(file.isDirectory()){//if file is dir go deeper and find classes there
            for(File child:file.listFiles()){//for each file in current dir
                classes.addAll(scan(child,resource));
            }
        }
        else if(resource.endsWith(this.CLASS_FILE_SUFFIX)){
            int endIndex=resource.length()-CLASS_FILE_SUFFIX.length();// example: com.example.Main{.class}
            String className=resource.substring(0,endIndex);
            try{
                classes.add(Class.forName(className));
            }
            catch (ClassNotFoundException e){
            }
        }
        return classes;
    }

}
