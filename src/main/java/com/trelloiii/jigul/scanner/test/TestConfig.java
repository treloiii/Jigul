package com.trelloiii.jigul.scanner.test;

import com.trelloiii.jigul.Config;
import com.trelloiii.jigul.ioc.Bean;
import com.trelloiii.jigul.ioc.instance.InstanceType;

@Config(iocPackage = {"com.trelloiii.jigul.scanner.test"},webPackage = {"com.trelloiii.jigul.scanner.test"})
public class TestConfig {

    @Bean(instanceType = InstanceType.SINGLE)
    public Person getCar(){
        return new Person("bean person","new bean person");
    }
}
