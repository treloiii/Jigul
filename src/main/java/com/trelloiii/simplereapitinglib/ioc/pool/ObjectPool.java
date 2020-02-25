package com.trelloiii.simplereapitinglib.ioc.pool;

import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.ioc.instance.ObjectInstance;

public class ObjectPool {
    private ObjectInstance objectInstance;
    public ObjectPool(Configuration configuration) {
        this.objectInstance=configuration.getObjectInstance();
    }

    public <T> T getPooledObject(Class<T> clazz){

        return clazz.cast(objectInstance.getInstance(clazz));
    }
}
