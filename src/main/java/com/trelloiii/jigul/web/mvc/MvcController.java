package com.trelloiii.jigul.web.mvc;
import java.util.Map;

public interface MvcController {
    void handle(Map<String,Object> requestParams);
}
