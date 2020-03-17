package com.trelloiii.jigul.parser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.tools.jstat.Jstat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlConfigParser {

    private Document document;
    public XmlConfigParser(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File f = new File("/Users/trelloiii/Desktop/JavaProjects/Jigul/src/main/resources/jigul-config.xml");
            this.document = builder.parse(f);
            document.getDocumentElement().normalize();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public Json parseConfig(){
        String [] webPkgs=getArrayTextContent(parseMultiTag("webPackage",document.getDocumentElement()));
        String [] iocPkgs=getArrayTextContent(parseMultiTag("iocPackage",document.getDocumentElement()));
        int serverPort=Integer.parseInt(parseSingleTag("serverPort",document.getDocumentElement()).getTextContent());
        JsonConfiguration jsonConfiguration=new JsonConfiguration(webPkgs,iocPkgs,serverPort);
        List<Bean> beans=parseBeans();
        return new Json(jsonConfiguration,beans);
    }

    private List<Bean> parseBeans(){
        Gson gson=new Gson();
        List<Bean> beans=new ArrayList<>();
        Element[] beanElements=parseMultiTag("bean",document.getDocumentElement());
        for(Element beanElement:beanElements){
            String classPath=parseSingleTag("classPath",beanElement).getTextContent();
            String[] constructorArgs=getArrayTextContent(parseMultiTag("constructorArg",beanElement));
            String[] constructorTypes=getArrayTextContent(parseMultiTag("constructorType",beanElement));
            Object[] constructorArgsNormalized=new Object[constructorArgs.length];
            for(int i=0;i<constructorArgsNormalized.length;i++){
                try {
                    JsonReader reader=new JsonReader(new StringReader(constructorArgs[i]));
                    reader.setLenient(true);
                    constructorArgsNormalized[i]=gson.fromJson(reader,Class.forName(constructorTypes[i]));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String instanceType=parseSingleTag("instanceType",beanElement).getTextContent();
            beans.add(new Bean(classPath,constructorArgsNormalized,constructorTypes,instanceType));
        }
        return beans;
    }
    private Element[] parseMultiTag(String tagName,Element root){
        NodeList webPackages=root.getElementsByTagName(tagName);
        Element[] tagContent=new Element[webPackages.getLength()];
        for(int i=0;i<webPackages.getLength();i++){
            Node pckg=webPackages.item(i);
            if(pckg.getNodeType()==Node.ELEMENT_NODE) {
                Element element=(Element) pckg;
                tagContent[i]=element;
            }
        }
        return tagContent;
    }
    private Element parseSingleTag(String tagName,Element root){
        NodeList webPackages=root.getElementsByTagName(tagName);
        Node pckg=webPackages.item(0);
        if(pckg.getNodeType()==Node.ELEMENT_NODE) {
            Element element=(Element) pckg;
            return element;
        }
        else {
            return null;
        }
    }

    private String[] getArrayTextContent(Element[] elements){
        String[] res=new String[elements.length];
        for(int i=0;i<res.length;i++){
            res[i]=elements[i].getTextContent();
        }
        return res;
    }
}
