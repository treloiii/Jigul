package com.trelloiii.jigul.web.mvc;
import com.trelloiii.jigul.web.httpcodes.HttpCode;
import com.trelloiii.jigul.web.httpcodes.Ok;

import java.util.Map;

public interface MvcController {
    default HttpCode handle(Map<String,Object> requestParams){
        return new Ok(null);
    }
}
