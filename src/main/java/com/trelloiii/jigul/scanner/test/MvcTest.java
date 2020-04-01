package com.trelloiii.jigul.scanner.test;

import com.trelloiii.jigul.web.RequestParam;
import com.trelloiii.jigul.web.httpcodes.HttpCode;
import com.trelloiii.jigul.web.httpcodes.Ok;
import com.trelloiii.jigul.web.mvc.MVCC;
import com.trelloiii.jigul.web.mvc.MvcController;
import com.trelloiii.jigul.web.mvc.MvcRequestBody;
import com.trelloiii.jigul.web.mvc.MvcRequestParam;

import java.util.Map;

@MVCC(path = "/")
public class MvcTest implements MvcController {

    public String name="DEFAULT";
    public  String surname="DEFAULT";
    public final Person person=new Person(name,surname);
    public String[] twos={"1","2","3"};

    public HttpCode handle(@MvcRequestBody Person p) {
        System.out.println(p);
        this.name=p.getName();
        this.surname=p.getSurname();
        return new Ok("OK");
    }
    public void suka(){

    }
}
