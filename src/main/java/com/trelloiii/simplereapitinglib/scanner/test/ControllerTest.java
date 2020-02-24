package com.trelloiii.simplereapitinglib.scanner.test;

import com.trelloiii.simplereapitinglib.Controller;
import com.trelloiii.simplereapitinglib.Get;

@Controller
public class ControllerTest {

    @Get
    public void doNothing(){
        System.out.println("just nothing");
    }
}
