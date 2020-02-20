package com.trelloiii.simplereapitinglib.pool;

import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.instance.ObjectInstance;

public class ObjectPool {
    private ObjectInstance objectInstance;
    public ObjectPool(Configuration configuration) {
        this.objectInstance=configuration.getObjectInstance();
    }

    public <T> T getPooledObject(Class<T> clazz){

        return clazz.cast(objectInstance.getInstance(clazz));
    }
}
