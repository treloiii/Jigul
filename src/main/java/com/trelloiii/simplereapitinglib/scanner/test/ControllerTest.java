package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.web.Controller;
import com.trelloiii.simplereapitinglib.web.Get;
import com.trelloiii.simplereapitinglib.web.Post;

@Controller
public class ControllerTest {

    @Get(path="/test")
    public Car doNothing(String color,String mark){
        return new Car(mark,color);
    }
    @Post(path = "/")
    public Car gavno(Car a){
        return a;
    }

    @Post(path="/query")
    public String q(String a1,String a2){
        return a1+":"+a2;
    }
    @Get(path="/marsel")
    public Integer getMarsel(){
        return 5/0;
    }
}
