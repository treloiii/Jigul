package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.web.Controller;
import com.trelloiii.simplereapitinglib.web.Get;

@Controller
public class ControllerTest {

    @Get(path="/doNothing")
    public String doNothing(String s,String returnable){
        System.out.println(s);
        return returnable;
    }
}
