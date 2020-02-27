package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.web.*;

@Controller
public class ControllerTest {

    @Get(path="/test")
    public Car doNothing(@RequestParam(name="color") String color, @RequestParam(name="mark") String mark){
        return new Car(mark,color);
    }
    @Post(path = "/")
    public Car gavno(@RequestBody Car a){
        return a;
    }

    @Post(path="/query")
    public String q(@RequestParam(name="gavno") String a1,@RequestParam(name="jopa")String a2){
        return a1+":"+a2;
    }
    @Get(path="/marsel")
    public Integer getMarsel(){
        return 5/0;
    }
}
