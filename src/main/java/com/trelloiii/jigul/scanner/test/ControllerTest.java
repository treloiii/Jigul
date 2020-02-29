package com.trelloiii.jigul.scanner.test;

import com.trelloiii.jigul.ioc.Injectable;
import com.trelloiii.jigul.web.*;

@Controller
public class ControllerTest {
    @Injectable
    Car car;
    @Get(path="/test")
    public Car doNothing(@RequestParam(name="color") String color, @RequestParam(name="mark") String mark){
        return new Car(mark,color);
    }
    @Get(path = "/")
    @CrossOrigin
    public Car gavno() throws InstantiationException {
        return car;
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
