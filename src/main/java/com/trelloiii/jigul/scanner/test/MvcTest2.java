package com.trelloiii.jigul.scanner.test;

import com.trelloiii.jigul.web.httpcodes.HttpCode;
import com.trelloiii.jigul.web.httpcodes.Ok;
import com.trelloiii.jigul.web.mvc.MVCC;
import com.trelloiii.jigul.web.mvc.MvcController;

import java.util.Map;

@MVCC(path = "/sec")
public class MvcTest2 implements MvcController {

    public String hello="hello world";
}
