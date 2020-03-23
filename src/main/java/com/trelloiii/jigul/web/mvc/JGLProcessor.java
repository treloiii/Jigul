package com.trelloiii.jigul.web.mvc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JGLProcessor {
    private Object controller;
    private Class<?> controllerClass;
    private String text;
    public JGLProcessor(Object controller, Class<?> controllerClass,String text) {
        this.controller = controller;
        this.controllerClass = controllerClass;
        this.text=text;
    }

    public String compileJGL() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        return processLoop(processValues(processCondition(text,controller,controllerClass),controller,controllerClass),controller,controllerClass);
    }

    private String processValues(String text,Object controller,Class<?> controllerClass) throws IllegalAccessException {
        Map<String,Object> vals=new HashMap<>();
        Field[] fields=controllerClass.getFields();
        for(Field field:fields){
            vals.put(field.getName(),field.get(controller));
        }
        try {
            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            Matcher m=pattern.matcher(text);
            List<String> names=new ArrayList<>();
            while (m.find()){
                names.add(m.group(1));
            }

            List<Object> arr=new ArrayList<>();
            for(String name:names){
                Object val=vals.get(name);
                arr.add(val);
            }

            String[] buf = pattern.split(text);
            StringBuilder builder = new StringBuilder();

            for (int i = 0, j = 0; i < buf.length; i++) {
                if (i != buf.length - 1)
                    builder.append(buf[i]).append(arr.get(j));
                else
                    builder.append(buf[i]);
                if (j < buf.length - 2)
                    j++;
            }
            String html = builder.toString();
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String processLoop(String text,Object controller,Class<?> controllerClass) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
//        Class<?> controllerClass = Controller.class;
//        Object controller = controllerClass.newInstance();

        Pattern pattern = Pattern.compile("#loop# (.*?)#(.*?)#loop#",Pattern.DOTALL);
        Matcher m=pattern.matcher(text);
//        System.out.println(Arrays.toString(pattern.split(":")));
        List<String> loops=new ArrayList<>();
        List<String> loopBodys=new ArrayList<>();
        while (m.find()){
            loops.add(m.group(1));// объявление цикла
            loopBodys.add(m.group(2));// тело цикла , то что должно повториться
        }
        List<String> result=new ArrayList<>();
        for(int li=0;li<loops.size();li++) {
            String loopBody=loopBodys.get(li);
            String loop=loops.get(li);
            System.out.println(loopBody);
            String[] arrLoop = loop.split(" ");// объявление цикла разбитое по словам
            String iteratorName = arrLoop[1];// название переменной которая используется для прохода по циклу
            String arrName = arrLoop[3].split(";")[0];//получить имя массива по которому итерируемся
            int step = 1;// шаг итерации если не указан то по умолчанию =1
            String indexName = null;
            try {
                indexName = arrLoop[3].split(";")[1];
                step = Integer.parseInt(indexName.split("=")[1]);
                indexName = indexName.split("=")[0];// имя индекса
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("no step expected");
            }


            Field arr = controllerClass.getField(arrName);
            Object[] newArr = (Object[]) arr.get(controller);
            //----процессинг доступа по индексу
            Pattern pattern1 = Pattern.compile("\\[(.*?)]");
            Matcher m1 = pattern1.matcher(loopBody);
            List<String> iter = new ArrayList<>();
            while (m1.find()) {

                iter.add(m1.group(1));
            }

            //----

            StringBuilder sb = new StringBuilder();
            String[] buf = pattern1.split(loopBody);
            if(step<=0){
                throw new IllegalStateException("loop iteration must be greater than zero");
            }
            for (int index = 0; index < newArr.length; index += step) {
                for (int i = 0; i < buf.length; i++) {
                    if (i != buf.length - 1) {
                        try {
                            LoopValue newArrIndex = parseIterator(iter.get(i), arrName, iteratorName, indexName, index);
                            String temp;
                            if (newArrIndex.isSingleIndex()) {
                                temp = String.valueOf(newArrIndex.getValue());
                            } else {
                                temp = newArr[newArrIndex.getValue()].toString();
                            }
                            sb.append(buf[i]).append(temp);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            sb.append(buf[i]);
                            System.out.println("index out");
                        }


                    } else {
                        sb.append(buf[i]);
                    }
                }
            }
            result.add(sb.toString());
        }
        result.add("");
        System.out.println("___");
        String[] beforeLoops=m.replaceAll(":").split(":");
        System.out.println(result);
        StringBuilder res=new StringBuilder();
        int i=0;
        for(String before:beforeLoops){
            res.append(before).append(result.get(i));
            i++;
        }
        return res.toString();
    }

    private LoopValue parseIterator(String iter,String arrName,String valName,String indexName,int index){
        String[] sings={"\\+","-","\\*","/"};
        Pattern pattern=Pattern.compile("\\((.*?)\\)");
        Matcher matcher=pattern.matcher(iter);

        String[] temp=pattern.split(iter);

        if(temp[0].equals(arrName)){// значит есть индекс
            if (matcher.find()){
                String body=matcher.group(1);


                if(body.contains("+")){
                    String[] bodySplitted=body.split("\\+");
                    if(bodySplitted[0].matches("\\d+")){
                        return new LoopValue(Integer.parseInt(bodySplitted[0])
                                +Integer.parseInt(bodySplitted[1]),false);
                    }
                    else{
                        if(indexName.equals(bodySplitted[0])){
                            return new LoopValue(index+Integer.parseInt(bodySplitted[1]),false);
                        }
                        else {
                            throw  new IllegalStateException("unknown state: "+bodySplitted[0]);
                        }
                    }
                }
                else if(body.contains("-")){
                    String[] bodySplitted=body.split("-");
                    if(bodySplitted[0].matches("\\d+")){
                        return new LoopValue(Integer.parseInt(bodySplitted[0])
                                -Integer.parseInt(bodySplitted[1]),false);
                    }
                    else{
                        try {
                            if (indexName.equals(bodySplitted[0])) {
                                return new LoopValue(index - Integer.parseInt(bodySplitted[1]), false);
                            }
                            else if(bodySplitted[0].equals("")){
                                throw new IllegalStateException("array index must be positive or zero");
                            }
                            else {
                                throw new IllegalStateException("unknown state: " + bodySplitted[0]);
                            }
                        }
                        catch (NullPointerException e){
                            throw new IllegalStateException("array index must be positive or zero");
                        }
                    }
                }
                else if(body.contains("*")){
                    String[] bodySplitted=body.split("\\*");
                    if(bodySplitted[0].matches("\\d+")){
                        return new LoopValue(Integer.parseInt(bodySplitted[0])
                                *Integer.parseInt(bodySplitted[1]),false);
                    }
                    else{
                        if (indexName==null){
                            throw new IllegalStateException("no index variable initialized");
                        }
                        if(indexName.equals(bodySplitted[0])){
                            return new LoopValue(index*Integer.parseInt(bodySplitted[1]),false);
                        }
                        else {
                            throw  new IllegalStateException("unknown state: "+bodySplitted[0]);
                        }
                    }
                }
                else if(body.contains("/")){
                    String[] bodySplitted=body.split("/");
                    if(bodySplitted[0].matches("\\d+")){
                        return new LoopValue(Integer.parseInt(bodySplitted[0])
                                /Integer.parseInt(bodySplitted[1]),false);
                    }
                    else{
                        if(indexName.equals(bodySplitted[0])){
                            return new LoopValue(index/Integer.parseInt(bodySplitted[1]),false);
                        }
                        else {
                            throw  new IllegalStateException("unknown state: "+bodySplitted[0]);
                        }
                    }
                }
                else if(body.equals(indexName)) {
                    return new LoopValue(index, false);
                }
                else {
                    try{
                        Integer a=new Integer(body);
                        return new LoopValue(a,false);
                    }
                    catch (Exception e){
                        throw new IllegalArgumentException("illegal loop value: "+temp[0]);
                    }
                }

            }
            else {
                throw new IllegalArgumentException("illegal loop value: "+temp[0]);
            }
        }
        else if(temp[0].equals(valName)){//значит индекса нет
            return new LoopValue(index,false);
        }
        else{
            if(temp[0].contains(indexName)) {
                if(temp[0].contains("+")){
                    return new LoopValue(index+Integer.parseInt(temp[0].split("\\+")[1]),true);
                }
                else if(temp[0].contains("-")){
                    return new LoopValue(index-Integer.parseInt(temp[0].split("-")[1]),true);
                }
                else if(temp[0].contains("*")){
                    return new LoopValue(index*Integer.parseInt(temp[0].split("\\*")[1]),true);
                }
                else if(temp[0].contains("/")){
                    return new LoopValue(index/Integer.parseInt(temp[0].split("/")[1]),true);
                }
                else if(temp[0].equals(indexName)){
                    return new LoopValue(index,true);
                }
                else {
                    throw new IllegalArgumentException("illegal loop value: " + temp[0]);
                }
            }
            else {
                throw new IllegalArgumentException("illegal loop value: " + temp[0]);
            }
        }
    }

    private String processCondition(String text,Object controller,Class<?> controllerClass) throws NoSuchFieldException, IllegalAccessException {
        Pattern pattern = Pattern.compile("#condition# (.*?)#(.*?)#condition#",Pattern.DOTALL);
        Matcher m=pattern.matcher(text);
        List<String> conditions=new ArrayList<>();
        List<String> bodys=new ArrayList<>();
        List<String> result=new ArrayList<>();
        while(m.find()){
            conditions.add(m.group(2));
            bodys.add(m.group(1));
        }

        for(int i=0;i<conditions.size();i++){
            boolean condition=processConditionInit(bodys.get(i),controller,controllerClass);
            System.out.println(condition);
            if(condition)
                result.add(conditions.get(i));
        }
        result.add("");
        String[] beforeCondition=m.replaceAll(":").split(":");
        StringBuilder res=new StringBuilder();
        int i=0;
        for(String before:beforeCondition){
            res.append(before).append(result.get(i));
            i++;
        }
        return res.toString();
    }

    private boolean processConditionInit(String body,Object controller,Class<?> controllerClass) throws NoSuchFieldException, IllegalAccessException {
        String left = body.split(" ")[0];
        String right = body.split(" ")[1];
        if (left.equals("if")) {//Если написано if
            if (right.contains("!")){ //Отрицание условия
                return antiRight(right,controller,controllerClass);
            }
            else{ // Обычное условие
                return !antiRight("!"+right,controller,controllerClass);
            }
        } else {

            throw new IllegalStateException("unknown state: "+left);
        }
    }
    private boolean antiRight(String right,Object controller,Class<?> controllerClass) throws NoSuchFieldException, IllegalAccessException {
        String condition=right.substring(1);
        if(condition.equals("true")||condition.equals("false")){
            return !Boolean.parseBoolean(condition);
        }
        else if(condition.contains("==")){
            String subLeft=condition.split("==")[0];
            String subRight=condition.split("==")[1];
            Field l=controllerClass.getField(subLeft);
            //Сравниваем с тем что справа, слева всегда переменная из контроллера
            try{
                Double d=new Double(subRight);
                Double d1=(Double)l.get(controller);
                return !d1.equals(d);
            }
            catch (Exception e){
                System.err.println("not a double");
            }
            try{
                Integer d=new Integer(subRight);
                Integer d1=(Integer) l.get(controller);
                return !d1.equals(d);
            }
            catch (Exception e){
                System.err.println("not a int");
            }
            try{
                Boolean d=new Boolean(subRight);
                Boolean d1=(Boolean) l.get(controller);
                return !d1.equals(d);
            }
            catch (Exception e){
                System.err.println("not a int");
            }
            try{
                Object d=controllerClass.getField(subRight).get(controller);
                Object d1=l.get(controller);
                return !d1.equals(d);
            }
            catch (Exception e){
                System.err.println("not a object");
            }
            String d1=(String) l.get(controller);
            return !d1.equals(subRight);
        }
        else if(condition.contains(">=")){
            return greaterOrNot(">=",condition,controller,controllerClass);
        }
        else if(condition.contains("<=")){
            return greaterOrNot("<=",condition,controller,controllerClass);
        }
        else if(condition.contains(">")){
            return greaterOrNot(">",condition,controller,controllerClass);
        }
        else if(condition.contains("<")){
            return greaterOrNot("<",condition,controller,controllerClass);
        }
        else{
            System.out.println(condition);
            Field l=controllerClass.getField(condition);
            return !(Boolean)l.get(controller);
        }
    }

    private Boolean greaterOrNot(String sign,String condition,Object controller,Class<?> controllerClass) throws NoSuchFieldException, IllegalAccessException {
        String subLeft=condition.split(sign)[0];
        String subRight=condition.split(sign)[1];
        Field l=controllerClass.getField(subLeft);
        switch (sign) {
            case ">":
                //Сравниваем с тем что справа, слева всегда переменная из контроллера
                try {
                    try {
                        Double d = new Double(subRight);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) > 0);
                    } catch (NumberFormatException e) {
                        Double d = (Double) controllerClass.getField(subRight).get(controller);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) > 0);
                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a double");
                }

                try{
                    try {
                        Integer d=new Integer(subRight);
                        Integer d1=(Integer) l.get(controller);
                        return !(d1.compareTo(d)>0);
                    }
                    catch (NumberFormatException e) {
                        Integer d = (Integer) controllerClass.getField(subRight).get(controller);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) > 0);
                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a int");
                }
                throw new IllegalStateException("cant compare with '>' ");
            case "<":
                //Сравниваем с тем что справа, слева всегда переменная из контроллера
                try {
                    try {
                        Double d = new Double(subRight);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) < 0);
                    } catch (NumberFormatException e) {
                        Double d = (Double) controllerClass.getField(subRight).get(controller);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) < 0);

                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a double");
                }
                try {
                    try {
                        Integer d = new Integer(subRight);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) < 0);
                    } catch (NumberFormatException e) {
                        Integer d = (Integer) controllerClass.getField(subRight).get(controller);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) < 0);
                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a int");
                }
                throw new IllegalStateException("cant compare with '<' ");
            case ">=":
                //Сравниваем с тем что справа, слева всегда переменная из контроллера
                try {
                    try {
                        Double d = new Double(subRight);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) >= 0);
                    } catch (NumberFormatException e) {
                        Double d = (Double) controllerClass.getField(subRight).get(controller);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) >= 0);
                    }
                }
                catch (ClassCastException e1){
                    System.err.println("not a double");
                }
                try{
                    try {
                        Integer d = new Integer(subRight);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) >= 0);
                    }
                    catch (NumberFormatException e) {
                        Integer d = (Integer) controllerClass.getField(subRight).get(controller);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) >= 0);
                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a int");
                }
                throw new IllegalStateException("cant compare with '>=' ");
            case "<=":
                //Сравниваем с тем что справа, слева всегда переменная из контроллера
                try{
                    try {
                        Double d = new Double(subRight);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) <= 0);
                    }
                    catch (NumberFormatException e) {
                        Double d = (Double) controllerClass.getField(subRight).get(controller);
                        Double d1 = (Double) l.get(controller);
                        return !(d1.compareTo(d) <= 0);
                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a double");
                }
                try{
                    try {
                        Integer d = new Integer(subRight);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) <= 0);
                    }
                    catch (NumberFormatException e) {
                        Integer d = (Integer) controllerClass.getField(subRight).get(controller);
                        Integer d1 = (Integer) l.get(controller);
                        return !(d1.compareTo(d) <= 0);
                    }
                }
                catch (ClassCastException e){
                    System.err.println("not a int");
                }
                throw new IllegalStateException("cant compare with '<=' ");
            default:
                throw new IllegalStateException("unknown operator: "+sign);
        }
    }
}

class LoopValue{
    private boolean isSingleIndex;
    private int value;
    LoopValue(int value,boolean isSingleIndex){
        this.value=value;
        this.isSingleIndex=isSingleIndex;
    }

    public int getValue() {
        return value;
    }

    public boolean isSingleIndex() {
        return isSingleIndex;
    }
}

