package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.web.Controller;
import com.trelloiii.simplereapitinglib.web.Get;

@Controller
public class ControllerTest {

    @Get(path="/test")
    public Car doNothing(String color,String mark){
        return new Car(mark,color);
    }
    @Get(path = "/")
    public Car gavno(Car a){
        return a;
    }

    @Get(path="/marsel")
    public Integer getMarsel(){
        return 5/0;
    }
}
