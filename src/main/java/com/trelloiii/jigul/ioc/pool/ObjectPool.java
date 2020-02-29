package com.trelloiii.jigul.ioc.pool;

import com.trelloiii.jigul.Configuration;
import com.trelloiii.jigul.ioc.instance.ObjectInstance;

public class ObjectPool {
    private ObjectInstance objectInstance;
    public ObjectPool(Configuration configuration) {
        this.objectInstance=configuration.getObjectInstance();
    }

    public <T> T getPooledObject(Class<T> clazz){

        return clazz.cast(objectInstance.getInstance(clazz));
    }
}
