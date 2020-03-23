package com.trelloiii.jigul.scanner.test;

import com.trelloiii.jigul.web.RequestParam;
import com.trelloiii.jigul.web.httpcodes.HttpCode;
import com.trelloiii.jigul.web.httpcodes.Ok;
import com.trelloiii.jigul.web.mvc.MVCC;
import com.trelloiii.jigul.web.mvc.MvcController;
import com.trelloiii.jigul.web.mvc.MvcRequestParam;

import java.util.Map;

@MVCC(path = "/index")
public class MvcTest implements MvcController {

    public String name="DEFAULT";
    public String[] twos={"1","2","3"};
    @Override
    @MvcRequestParam(paramName = "name",paramType = String.class)
    public HttpCode handle(Map<String, Object> requestParams) {
        this.name=(String) requestParams.get("name");
        return new Ok("OK");
    }
}
