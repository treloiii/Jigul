package com.trelloiii.jigul.web.mvc;
import com.trelloiii.jigul.web.httpcodes.HttpCode;

import java.util.Map;

public interface MvcController {
    HttpCode handle(Map<String,Object> requestParams);
}
