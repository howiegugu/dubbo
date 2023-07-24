package org.apache.dubbo.demo.provider.wrapperdemo;

import org.apache.dubbo.common.bytecode.Wrapper;
import org.apache.dubbo.demo.provider.DemoServiceImpl;

import java.lang.reflect.InvocationTargetException;

public class Demo {
    public static void main(String[] args) throws InvocationTargetException {
        DemoServiceImpl demoService = new DemoServiceImpl();
        Wrapper wrapper = Wrapper.getWrapper(demoService.getClass());
        Object sayHello = wrapper.invokeMethod(demoService, "sayHello",
            new Class[]{String.class},
            new Object[]{"Geek"});
         System.out.println(sayHello);
    }
}
